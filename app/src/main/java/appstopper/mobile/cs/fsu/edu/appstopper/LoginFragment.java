package appstopper.mobile.cs.fsu.edu.appstopper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";
    static final int DIALOG_CHILD_NAME_ID = 12    ;
    private SharedPreferences sPref;
    private static final String PREFS_NAME = "DevicePrefsFile"; // Name of shared preferences file
    private String childName;
    private Button loginButton;
    private TextView registerTextButton;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;                                 // Firebase authorization interface
    protected EditText loginEmail, loginPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login, container, false);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        sPref = getContext().getSharedPreferences(PREFS_NAME, 0);
        loginButton = root.findViewById(R.id.login_button);
        registerTextButton = root.findViewById(R.id.register_text_button);
        loginEmail = root.findViewById(R.id.login_email);
        loginPassword = root.findViewById(R.id.login_password);

        // Login button calls email login when clicked (Logs in a Firebase user)
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailLogIn(loginEmail.getText().toString(), loginPassword.getText().toString());
            }
        });
        registerTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayRegister();
            }
        });

        // Inflate the layout for this fragment
        return root;
    }

    /**
     * Signs user in using email and password, returns a
     * boolean value signifying whether or not authentication
     * was successful
     * @param email     the inputted email
     * @param password  the inputted password
     */
    public void emailLogIn(String email, final String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "emailLogIn:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            Log.v(TAG, mUid);

                            // Push device if shared preferences deviceID doesn't exist (first login)
                            if (!sPref.contains("deviceID")) {
                                Map<String, Object> dbMap = new HashMap<>();    // Map for adding key value pairs
                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                                String deviceID = mDatabase.child("devices").push().getKey();

                                // Add the device to devices
                                dbMap.put("deviceid", deviceID);
                                dbMap.put("type", "android");
                                dbMap.put("whitelist_entries", "none"); // none = temp val
                                mDatabase.child("devices").child(deviceID).setValue(dbMap); // Adds the device key and sets type

                                // Creating whitelist for the first time
                                dbMap.clear();
                                PackageManager pm = getActivity().getPackageManager();
                                List<PackageInfo> entries = pm.getInstalledPackages(0);
                                for (PackageInfo packageInfo : entries) {
                                    // Creating device key
                                    String key = mDatabase.child("devices").child(deviceID)
                                            .child("whitelist_entries").push().getKey();

                                    mDatabase.child("devices").child(deviceID)
                                            .child("whitelist_entries")
                                            .child(key).setValue(packageInfo.packageName);
                                    Log.v(TAG, "package: " + packageInfo.packageName);
                                }

                                // Adds key/val pair (deviceid, name) to users as a reference
                                dbMap.clear();
                                dbMap.put("/devices/" + deviceID, Build.MODEL);
                                mDatabase.child("users").child(user.getUid()).updateChildren(dbMap);

                                // Add to shared preference to remember this device
                                SharedPreferences.Editor editor = sPref.edit();
                                editor.putString("deviceID", deviceID);
                                editor.apply();
                                showMyDialog(deviceID);
                            }
                            else {

                                // !!Change to home screen activity here!!
                                Intent intent = new Intent(getActivity(), HomeActivity.class);
                                startActivity(intent);
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getActivity(), "Sign in failed, try again",
                                    Toast.LENGTH_SHORT).show();
                            loginEmail.setText("");
                            loginPassword.setText("");
                            Log.w(TAG, "emailLogIn:failure", task.getException());
                        }
                    }
                });
    }



    protected void showMyDialog(final String did) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("What child is this?:");
        builder.setCancelable(false);

        final EditText input = new EditText(getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        builder.setView(input);

        builder.setPositiveButton("Enter", new
                DialogInterface.OnClickListener() {
                    public void onClick (DialogInterface dialog, int id) {
                        childName = input.getText().toString();
                        // Adding in the child name
                        Map<String, Object> dbMap = new HashMap<>();
                        dbMap.clear();
                        dbMap.put("/child_name/", childName);
                        mDatabase.child("devices").child(did).updateChildren(dbMap);

                        // Switching our activity
                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                        startActivity(intent);
                    }
                });
        builder.show();
    }

    /**
     * Switches fragment to the RegisterFragment
     */
    public void displayRegister() {
        RegisterFragment regFrag = new RegisterFragment();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.add(R.id.main_fragment_container, regFrag, "RegisterFragment");
        trans.commit();
    }

}
