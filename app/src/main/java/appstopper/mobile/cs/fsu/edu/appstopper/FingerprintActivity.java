package appstopper.mobile.cs.fsu.edu.appstopper;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class FingerprintActivity extends AppCompatActivity {

    private TextView title;
    private ImageView fingerPrintImage;
    private TextView body;

    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint);

        title = findViewById(R.id.textView);
        fingerPrintImage = findViewById(R.id.imageView);
        fingerPrintImage.setImageResource(R.mipmap.ic_fingerprint_round);
        body = findViewById(R.id.textView2);

        // TODO check 1: Android version should be >= marshmallow
        // TODO check 2: Determine if device has a fingerprint scanner
        // TODO check 3: Have permission to use fingerprint scanner in app
        // TODO check 4: Lock screen is secured with at least one type of lock
        // TODO check 5: At least one fingerprint is registered on the device

        // ---- Checks ---- //
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
            keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

            if (!fingerprintManager.isHardwareDetected()) {
                body.setText("Fingerprint scanner not detected on device");
            } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                // If the permission is not granted
                body.setText("Permission not granted to use fingerprint scanner");

            } else if (!keyguardManager.isKeyguardSecure()) {
                body.setText("Add lock to your device in settings");
            } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                body.setText("Add at least one fingerprint to device to use this feature");
            } else {
                body.setText("Place your finger on scanner to access the App");
                FingerprintHandler fingerPrintHandler = new FingerprintHandler(this);
                fingerPrintHandler.startAuth(fingerprintManager, null);
            }
        }
    }
}
