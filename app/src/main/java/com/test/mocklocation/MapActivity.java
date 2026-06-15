package com.test.mocklocation;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MapActivity extends Activity {

    private WebView webView;
    private EditText etLatitude;
    private EditText etLongitude;
    private Button btnStartMock;
    private Button btnStopMock;
    private Button btnSettings;
    private SeekBar seekBarSpeed;
    private TextView tvSpeedValue;
    private TextView tvStatus;

    private double selectedLat = 39.9042;
    private double selectedLng = 116.4074;
    private float speed = 5.0f;
    private Handler handler;
    private TrialManager trialManager;
    private boolean expiredDialogShown = false;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        handler = new Handler(Looper.getMainLooper());
        trialManager = new TrialManager(this);

        webView = (WebView) findViewById(R.id.webview_map);
        etLatitude = (EditText) findViewById(R.id.et_latitude);
        etLongitude = (EditText) findViewById(R.id.et_longitude);
        btnStartMock = (Button) findViewById(R.id.btn_start_mock);
        btnStopMock = (Button) findViewById(R.id.btn_stop_mock);
        btnSettings = (Button) findViewById(R.id.btn_settings);
        seekBarSpeed = (SeekBar) findViewById(R.id.seekbar_speed);
        tvSpeedValue = (TextView) findViewById(R.id.tv_speed_value);
        tvStatus = (TextView) findViewById(R.id.tv_status);

        etLatitude.setText("39.9042");
        etLongitude.setText("116.4074");

        setupWebView();
        setupControls();
        checkMockLocationApp();
        handler.post(statusRunnable);
        checkServerLicense(false);
        if (!canUseByServerOrLocal()) {
            disableMockForExpired();
            showExpiredDialog();
        }
    }

    private boolean isMockLocationAppSet() {
        try {
            android.location.LocationManager lm = (android.location.LocationManager) getSystemService(Context.LOCATION_SERVICE);
            lm.addTestProvider(android.location.LocationManager.GPS_PROVIDER,
                false, false, false, false, true, true, true,
                android.location.Criteria.POWER_LOW, android.location.Criteria.ACCURACY_FINE);
            lm.removeTestProvider(android.location.LocationManager.GPS_PROVIDER);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void checkMockLocationApp() {
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        int guideCount = prefs.getInt("guide_count", 0);
        if (!isMockLocationAppSet() && guideCount < 2) {
            prefs.edit().putInt("guide_count", guideCount + 1).apply();
            showMockLocationGuide();
        }
    }

    private void showMockLocationGuide() {
        new AlertDialog.Builder(this)
            .setTitle(getString(R.string.mock_location_guide_title))
            .setMessage(getString(R.string.mock_location_guide_message))
            .setPositiveButton(getString(R.string.go_settings), (d, w) -> {
                try { startActivity(new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS)); }
                catch (Exception e) { startActivity(new Intent(Settings.ACTION_SETTINGS)); }
            })
            .setNegativeButton(getString(R.string.later), null)
            .show();
    }

    private void setupWebView() {
        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setAllowFileAccess(true);
        s.setAllowContentAccess(true);
        s.setAllowFileAccessFromFileURLs(true);
        s.setAllowUniversalAccessFromFileURLs(true);
        s.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.addJavascriptInterface(new WebAppInterface(), "Android");

        // Load map HTML directly from assets
        webView.loadUrl("file:///android_asset/map.html");
    }

    private void setupControls() {
        seekBarSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar sb, int p, boolean f) { speed = p + 1; tvSpeedValue.setText(speed + " m/s"); }
            @Override public void onStartTrackingTouch(SeekBar sb) {}
            @Override public void onStopTrackingTouch(SeekBar sb) {}
        });
        btnStartMock.setOnClickListener(v -> onStartMockClicked());
        btnStopMock.setOnClickListener(v -> onStopMockClicked());
        btnSettings.setOnClickListener(v -> {
            startActivity(new Intent(MapActivity.this, SettingsActivity.class));
        });
    }

    private void onStartMockClicked() {
        if (!canUseByServerOrLocal()) {
            disableMockForExpired();
            showExpiredDialog();
            return;
        }
        checkServerLicense(false);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1002);
            return;
        }
        if (!isMockLocationAppSet()) { showMockLocationGuide(); return; }

        String latStr = etLatitude.getText().toString().trim();
        String lngStr = etLongitude.getText().toString().trim();
        if (latStr.isEmpty() || lngStr.isEmpty()) {
            Toast.makeText(this, getString(R.string.enter_coordinates), Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            double lat = Double.parseDouble(latStr);
            double lng = Double.parseDouble(lngStr);
            if (lat < -90 || lat > 90 || lng < -180 || lng > 180) {
                Toast.makeText(this, getString(R.string.invalid_coordinates), Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(this, MockLocationService.class);
            intent.setAction(MockLocationService.ACTION_SET_LOCATION);
            intent.putExtra("lat", lat);
            intent.putExtra("lng", lng);
            startService(intent);
            new RecentLocationManager(this).saveRecent(lat, lng);
            Toast.makeText(this, getString(R.string.mock_started), Toast.LENGTH_SHORT).show();
            webView.loadUrl("javascript:setMM(" + lat + "," + lng + ")");
        } catch (NumberFormatException e) {
            Toast.makeText(this, getString(R.string.invalid_format), Toast.LENGTH_SHORT).show();
        }
    }

    private void onStopMockClicked() {
        Intent intent = new Intent(this, MockLocationService.class);
        intent.setAction(MockLocationService.ACTION_STOP);
        startService(intent);
        webView.loadUrl("javascript:clrMM()");
        Toast.makeText(this, getString(R.string.mock_stopped_toast), Toast.LENGTH_SHORT).show();
    }

    private final Runnable statusRunnable = new Runnable() {
        @Override
        public void run() {
            if (MockLocationService.isServiceRunning) {
                tvStatus.setText(getString(R.string.mock_running_label) + " " + String.format("%.6f, %.6f",
                        MockLocationService.mockLatitude, MockLocationService.mockLongitude));
                tvStatus.setTextColor(0xFF4CAF50);
                webView.loadUrl("javascript:setMM(" +
                        MockLocationService.mockLatitude + "," + MockLocationService.mockLongitude + ")");
            } else {
                tvStatus.setText(getString(R.string.mock_stopped));
                tvStatus.setTextColor(0xFFF44336);
            }
            handler.postDelayed(this, 1500);
        }
    };

    @Override protected void onResume() {
        super.onResume();
        handler.post(statusRunnable);
        checkServerLicense(false);
        if (!canUseByServerOrLocal()) {
            disableMockForExpired();
            if (!expiredDialogShown) showExpiredDialog();
        }
    }
    @Override protected void onPause() { super.onPause(); handler.removeCallbacks(statusRunnable); }

    @Override
    public void onRequestPermissionsResult(int rc, String[] p, int[] gr) {
        super.onRequestPermissionsResult(rc, p, gr);
        if (gr.length > 0 && gr[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, getString(R.string.permission_granted), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.permission_required), Toast.LENGTH_LONG).show();
        }
    }



    private boolean canUseByServerOrLocal() {
        return trialManager.canUseApp() || LicenseApiHelper.canUseCached(this);
    }

    private void checkServerLicense(final boolean unused) {
        LicenseApiHelper.checkLicense(this, trialManager.getDeviceCode(), (canUse, mustUpgrade, message) -> runOnUiThread(() -> {
            if (mustUpgrade) {
                showUpgradeDialog();
            } else if (!canUseByServerOrLocal()) {
                disableMockForExpired();
            }
        }));
    }

    private void showUpgradeDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.upgrade_required))
                .setMessage(getString(R.string.upgrade_message, LicenseApiHelper.getLatestVersion(this), LicenseApiHelper.getChangelog(this)))
                .setPositiveButton(getString(R.string.download_now), (d, w) -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(LicenseApiHelper.getDownloadUrl(this)))))
                .setCancelable(false)
                .show();
    }

    private void disableMockForExpired() {
        try {
            Intent intent = new Intent(this, MockLocationService.class);
            intent.setAction(MockLocationService.ACTION_STOP);
            startService(intent);
        } catch (Exception ignored) {}
        btnStartMock.setEnabled(false);
        btnStartMock.setAlpha(0.5f);
        tvStatus.setText(getString(R.string.expired_cannot_start_mock));
        tvStatus.setTextColor(0xFFF44336);
    }

    private void showExpiredDialog() {
        expiredDialogShown = true;
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.add_usage_time_required))
                .setMessage(getString(R.string.map_expired_dialog_message))
                .setPositiveButton(getString(R.string.add_usage_time), (d, w) -> startActivity(new Intent(this, PaymentActivity.class)))
                .setNegativeButton(getString(R.string.close), null)
                .setCancelable(true)
                .show();
    }

    public class WebAppInterface {
        @JavascriptInterface
        public void onLoc(final double lat, final double lng) {
            selectedLat = lat; selectedLng = lng;
            runOnUiThread(() -> {
                etLatitude.setText(String.valueOf(lat));
                etLongitude.setText(String.valueOf(lng));
            });
        }
    }
}
