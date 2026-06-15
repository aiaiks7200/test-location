package com.test.mocklocation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.ActivityNotFoundException;

public class PaymentActivity extends Activity {
    private TrialManager trialManager;
    private Handler handler;
    private String currentOutTradeNo;
    private String currentOrderId;
    private String selectedPlan = "monthly";
    private String selectedPayType = "alipay";
    private TextView tvStatus;
    private Button btnMonthly;
    private Button btnYearly;
    private String monthlyPrice = "19.80";
    private String yearlyPrice = "168.00";
    private boolean activatedThisOrder = false;
    private int pollAttempts = 0;
    private String currentPayUrl;
    private String currentH5Url;
    private String currentWechatPayUrl;
    private boolean wxpayEnabled = true; // 默认开启，从服务器读取

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        trialManager = new TrialManager(this);
        handler = new Handler(Looper.getMainLooper());

        btnMonthly = (Button) findViewById(R.id.btn_monthly);
        btnYearly = (Button) findViewById(R.id.btn_yearly);
        tvStatus = (TextView) findViewById(R.id.tv_payment_status);

        updateInitialStatus();
        refreshPlanPrices();

        btnMonthly.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                selectedPlan = "monthly";
                showPayMethodDialog(monthlyPrice, getString(R.string.app_name) + "-" + getString(R.string.monthly_plan));
            }
        });

        btnYearly.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                selectedPlan = "yearly";
                showPayMethodDialog(yearlyPrice, getString(R.string.app_name) + "-" + getString(R.string.yearly_plan));
            }
        });
    }

    private void refreshPlanPrices() {
        PlanApiHelper.fetchPlans((monthly, yearly, wxpay) -> runOnUiThread(() -> {
            monthlyPrice = monthly;
            yearlyPrice = yearly;
            wxpayEnabled = wxpay;
            btnMonthly.setText(getString(R.string.subscribe_now));
            btnYearly.setText(getString(R.string.subscribe_now));
        }));
    }

    private void updateInitialStatus() {
        String msg = getString(R.string.device_code_label) + trialManager.getDeviceCode()
                + "\n" + getString(R.string.membership_valid_until) + trialManager.getSubscriptionEndText();
        if (trialManager.hasActiveSubscription()) {
            msg += "\n" + getString(R.string.active_member_order_notice);
        }
        tvStatus.setText(msg);
    }

    private void showPayMethodDialog(final String amount, final String subject) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.select_payment_method))
               .setMessage(getString(R.string.payment_device_bind_message, trialManager.getDeviceCode()))
               .setPositiveButton(getString(R.string.alipay), (dialog, which) -> {
                   selectedPayType = "alipay";
                   createOrder(amount, subject + "-" + trialManager.getDeviceCode());
               })
               .setNegativeButton(getString(R.string.cancel), null);

        // 根据服务器设置决定是否显示微信支付
        if (wxpayEnabled) {
            builder.setNeutralButton(getString(R.string.wechat_pay), (dialog, which) -> {
                selectedPayType = "wxpay";
                createOrder(amount, subject + "-" + trialManager.getDeviceCode());
            });
        }
        builder.show();
    }

    private void createOrder(final String amount, final String subject) {
        currentOrderId = PaymentApiHelper.generateOrderId();
        currentOutTradeNo = null;
        activatedThisOrder = false;
        pollAttempts = 0;
        currentPayUrl = null;
        currentH5Url = null;
        currentWechatPayUrl = null;
        tvStatus.setText(getString(R.string.creating_order));

        PaymentApiHelper.createOrder(amount, subject, selectedPayType, currentOrderId, trialManager.getDeviceCode(), selectedPlan,
                new PaymentApiHelper.PaymentCallback() {
                    @Override public void onSuccess(final String payUrl, final String h5Url, final String wechatPayUrl, final String tradeNo) {
                        runOnUiThread(() -> {
                            currentOutTradeNo = tradeNo;
                            currentPayUrl = payUrl;
                            currentH5Url = h5Url;
                            currentWechatPayUrl = wechatPayUrl;
                            tvStatus.setText(getString(R.string.order_created) + "\n" + getString(R.string.order_no_label) + currentOrderId);
                            openPayUrl();
                            handler.removeCallbacks(pollRunnable);
                            handler.postDelayed(pollRunnable, 3000);
                        });
                    }

                    @Override public void onError(final String error) {
                        runOnUiThread(() -> {
                            tvStatus.setText(getString(R.string.order_failed) + error);
                            new AlertDialog.Builder(PaymentActivity.this)
                                    .setTitle(getString(R.string.create_order_failed_title))
                                    .setMessage(getString(R.string.create_order_failed_message, error))
                                    .setPositiveButton(getString(R.string.ok), null)
                                    .show();
                        });
                    }
                });
    }

    private void openPayUrl() {
        if ("wxpay".equals(selectedPayType)) {
            openWechatPay();
            return;
        }
        String url = currentPayUrl;
        if (url == null || url.isEmpty()) url = currentH5Url;
        if (url == null || url.isEmpty()) {
            tvStatus.setText(getString(R.string.empty_payment_link));
            return;
        }
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.open_payment_link_title))
                    .setMessage(getString(R.string.open_payment_link_message, url))
                    .setPositiveButton(getString(R.string.ok), null)
                    .show();
        } catch (Exception e) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.payment_redirect_failed))
                    .setMessage(getString(R.string.payment_redirect_failed_message, url, e.getMessage()))
                    .setPositiveButton(getString(R.string.ok), null)
                    .show();
        }
    }

    private void openWechatPay() {
        // 优先用 Spay H5 页面（展示微信扫码二维码 + 手机端可复制链接到微信）
        // 不再使用 weixin://dl/business/?t= 协议（需要域名在微信注册，Spay 域名未注册会报"页面无法访问"）
        String payLink = currentH5Url;
        if (payLink == null || payLink.isEmpty()) payLink = currentPayUrl;
        if (payLink == null || payLink.isEmpty()) {
            tvStatus.setText(getString(R.string.empty_payment_link));
            return;
        }
        final String url = payLink;
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.open_payment_link_title))
                    .setMessage(getString(R.string.open_payment_link_message, url))
                    .setPositiveButton(getString(R.string.ok), null)
                    .show();
        } catch (Exception e) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.payment_redirect_failed))
                    .setMessage(getString(R.string.payment_redirect_failed_message, url, e.getMessage()))
                    .setPositiveButton(getString(R.string.ok), null)
                    .show();
        }
    }

    private final Runnable pollRunnable = new Runnable() {
        @Override public void run() {
            if (currentOrderId == null || pollAttempts >= 60 || activatedThisOrder) {
                if (!activatedThisOrder) tvStatus.setText(getString(R.string.payment_timeout));
                return;
            }
            pollAttempts++;
            final int attempt = pollAttempts;

            PaymentApiHelper.queryOrder(currentOrderId, new PaymentApiHelper.QueryCallback() {
                @Override public void onResult(final boolean paid, final String status, final long expireAtSeconds) {
                    runOnUiThread(() -> {
                        if (activatedThisOrder) return;
                        if (paid) {
                            if (expireAtSeconds > 0) {
                                trialManager.markOrderActivated(currentOrderId);
                                trialManager.syncSubscriptionEndFromServer(expireAtSeconds);
                                activatedThisOrder = true;
                                handler.removeCallbacks(pollRunnable);
                                showPaymentSuccessDialog();
                            } else if (trialManager.activateSubscriptionOnce("monthly".equals(selectedPlan) ? 1 : 12, currentOrderId)) {
                                activatedThisOrder = true;
                                handler.removeCallbacks(pollRunnable);
                                showPaymentSuccessDialog();
                            } else {
                                activatedThisOrder = true;
                                handler.removeCallbacks(pollRunnable);
                                tvStatus.setText(getString(R.string.order_already_processed) + "\n" + getString(R.string.membership_valid_until) + trialManager.getSubscriptionEndText());
                                Toast.makeText(PaymentActivity.this, getString(R.string.order_already_processed_toast), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            tvStatus.setText(getString(R.string.waiting_payment) + attempt + "\n" + getString(R.string.status_label) + status);
                            handler.postDelayed(pollRunnable, 3000);
                        }
                    });
                }

                @Override public void onError(final String error) {
                    runOnUiThread(() -> {
                        tvStatus.setText(getString(R.string.query_error) + "\n" + error);
                        handler.postDelayed(pollRunnable, 3000);
                    });
                }
            });
        }
    };

    private void showPaymentSuccessDialog() {
        tvStatus.setText(getString(R.string.payment_success) + "\n" + getString(R.string.membership_valid_until) + trialManager.getSubscriptionEndText());
        new AlertDialog.Builder(PaymentActivity.this)
                .setTitle(getString(R.string.payment_success))
                .setMessage(getString(R.string.payment_success_message) + "\n" + getString(R.string.membership_valid_until) + trialManager.getSubscriptionEndText())
                .setPositiveButton(getString(R.string.ok), (d, w) -> finish())
                .setCancelable(false)
                .show();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(pollRunnable);
    }
}
