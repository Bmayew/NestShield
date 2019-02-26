package appstopper.mobile.cs.fsu.edu.appstopper;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginFragment extends Fragment {

    /* Interface for login authorization in main activity */
    public interface LoginListener{
        void emailLogIn(String email, final String password);
        // To be added: google sign in, facebook sign in
    }
    private LoginListener loginListener;    // local reference to interface

    private static final String TAG = "LoginFragment";
    private Button loginButton;
    private TextView registerTextButton;
    protected EditText loginEmail, loginPassword;
    private FirebaseAuth mAuth;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login, container, false);

        loginButton = root.findViewById(R.id.login_button);
        registerTextButton = root.findViewById(R.id.register_text_button);
        loginEmail = root.findViewById(R.id.login_email);
        loginPassword = root.findViewById(R.id.login_password);

        // Listeners for register and login
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginListener.emailLogIn(loginEmail.getText().toString(), loginPassword.getText().toString());
            }
        });
        registerTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        // Inflate the layout for this fragment
        return root;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginFragment.LoginListener) {
            loginListener = (LoginFragment.LoginListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement LoginListener");
        }
    }

}
