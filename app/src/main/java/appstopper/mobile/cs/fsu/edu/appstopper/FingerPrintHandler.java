package appstopper.mobile.cs.fsu.edu.appstopper;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.M)
public class FingerPrintHandler extends FingerprintManager.AuthenticationCallback {

    private Context context;

    public FingerPrintHandler(Context context) {
        this.context = context;
    }

    public void startAuth(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject) {
        CancellationSignal cancellationSignal = new CancellationSignal();
        fingerprintManager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        this.update("There was an authentication error. " + errString, false);
    }

    @Override
    public void onAuthenticationFailed() {
        this.update("Authentication failed. ", false);
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        this.update("Error " + helpString, false);
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        this.update("You can now access the App.", true);
    }

    private void update(String s, Boolean b) {
        TextView bodyLabel = (TextView) ((Activity) context).findViewById(R.id.textView2);
        ImageView fingerprintImage = (ImageView) ((Activity) context).findViewById(R.id.imageView);

        bodyLabel.setText(s);
        if (b == false) {
            bodyLabel.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        } else {
            bodyLabel.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
            fingerprintImage.setImageResource(R.mipmap.ic_fingerprint_authorized_round);
            fingerprintImage.invalidate();
        }

    }
}
