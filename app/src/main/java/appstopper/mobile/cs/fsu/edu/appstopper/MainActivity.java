package appstopper.mobile.cs.fsu.edu.appstopper;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginListener{
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

        FirebaseUser currentUser = mAuth.getCurrentUser();

        // If a user is already signed in, we'll send them to dashboard
        if(currentUser != null) {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
        }
    }

    /**
     * Signs user in using email and password, returns a
     * boolean value signifying whether or not authentication
     * was successful
     * @param email     the inputted email
     * @param password  the inputted password
     * @return true or false whether successful or not
     */
    public void emailLogIn(String email, final String password) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "emailLogIn:success");

                        // !!Change to home screen activity here!!
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(intent);

                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(getApplicationContext(), "Sign in failed, try again",
                                Toast.LENGTH_SHORT).show();
                        logFrag.loginEmail.setText("");
                        logFrag.loginPassword.setText("");
                        Log.w(TAG, "emailLogIn:failure", task.getException());
                    }
                }
            });
    }
    public void displayRegister() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        regFrag = new RegisterFragment();
        trans.add(R.id.main_fragment_container, regFrag, "RegisterFragment");
        trans.commit();
    }
}




