package appstopper.mobile.cs.fsu.edu.appstopper;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
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


public class RegisterFragment extends Fragment {
    private static final String TAG = "RegisterFragment";
    private DatabaseReference mDatabase;
    private static final String PREFS_NAME = "DevicePrefsFile"; // Name of shared preferences file
    private String childName;
    private Button registerButton;
    private SharedPreferences sPref;
    EntryViewModel entryViewModel;
    protected EditText registerEmail, registerPassword, registerPasswordConfirm, registerName;
    private FirebaseAuth mAuth;
    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_register, container, false);
        entryViewModel = ViewModelProviders.of(this).get(EntryViewModel.class);

        // Firebase database and authentication
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        registerButton = root.findViewById(R.id.register_button);
        sPref = getContext().getSharedPreferences(PREFS_NAME, 0);
        registerEmail = root.findViewById(R.id.register_email);
        registerPassword = root.findViewById(R.id.register_password);
        registerPasswordConfirm = root.findViewById(R.id.register_password_confirm);
        registerName = root.findViewById(R.id.register_display_name);


        // TODO: Add better password checking (more than 6 letters, numbers and letters, etc.)
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (registerPassword.getText().toString().equals(registerPasswordConfirm.getText()
                        .toString()) && registerPassword.getText().toString().length() <= 6)
                {
                    createWithEmail(registerEmail.getText().toString(), registerPassword.getText()
                            .toString(), registerName.getText().toString());
                }
                else {  // Passwords do not match
                    Toast.makeText(getContext(), "Invalid password", Toast.LENGTH_SHORT)
                            .show();
                    registerPassword.setText("");
                    registerPasswordConfirm.setText("");
                }
            }
        });

        return root;
    }
    /**
     * Signs user in using email and password, returns a
     * boolean value signifying whether or not authentication
     * was successful
     * @param email         the inputted email
     * @param password      the inputted password
     * @param displayName   the inputted display name for a user
     */
    public void createWithEmail(String email, final String password, String displayName) {
        final String name = displayName;
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success
                        Log.d(TAG, "createUserWithEmail:success");

                        FirebaseUser user = mAuth.getCurrentUser();

                        // Database logic begins here
                        Map<String, Object> dbMap = new HashMap<>();    // Map for adding key value pairs
                        dbMap.put("name", name);
                        dbMap.put("email", user.getEmail());
                        dbMap.put("uid", user.getUid());
                        dbMap.put("devices", "none");
                        mDatabase.child("users").child(user.getUid()).setValue(dbMap);

                        // Generates a new key for the device under /devices/$deviceid
                        String deviceID = mDatabase.child("devices").push().getKey();
                        
                        dbMap.clear();

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
                            if (pm.getLaunchIntentForPackage(packageInfo.packageName) != null) {
                                WhitelistEntry entry = new WhitelistEntry();
                                entry.packageName = packageInfo.packageName;
                                entry.labelName = packageInfo.applicationInfo
                                        .loadLabel(pm).toString();
                                entry.isWhitelisted = true;
                                entryViewModel.insert(entry);
                                Log.v("Database", "First creation");
                                // Creating device key
                                String key = mDatabase.child("devices").child(deviceID)
                                        .child("whitelist_entries").push().getKey();

                                mDatabase.child("devices").child(deviceID)
                                        .child("whitelist_entries")
                                        .child(key).setValue(packageInfo.packageName);
                            }
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

                        Toast.makeText(getActivity(), "Authentication Succeeded.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        // !!Change to home screen activity here!!
                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                        startActivity(intent);

                        registerEmail.setText("");
                        registerPassword.setText("");
                        registerPasswordConfirm.setText("");
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

}
