package appstopper.mobile.cs.fsu.edu.appstopper;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity{
    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth; // Firebase authentication handler
    private LoginFragment logFrag;
    private RegisterFragment regFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* change to login fragment as default on creation */
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        logFrag = new LoginFragment();
        trans.add(R.id.main_fragment_container, logFrag, "LoginFragment");
        trans.commit();

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(TAG, "User already signed in");
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // If a user is already signed in, we'll send them to dashboard
        if(currentUser != null) {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
        }
    }




}




