package com.example.ramzanullah.demochatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.LoginFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LogInActivity extends AppCompatActivity {
    private static final String TAG = "LOG_IN_ACTIVITY";
    private Button goSignupBTN, singninBTN, googleBTN;
    private TextInputLayout emailET;
    private TextInputLayout passET;
    private FirebaseAuth mAuth;
    private Toolbar toolbar;
    private ProgressDialog progressDialog;
    private static final int RC_SIGN_IN = 1;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        mAuth = FirebaseAuth.getInstance();

        toolbar = (Toolbar) findViewById(R.id.sign_in_page_appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Demo Chat App");

        progressDialog = new ProgressDialog(this);

        emailET = (TextInputLayout) findViewById(R.id.et_login_email);
        passET = (TextInputLayout) findViewById(R.id.et_login_pass);
        singninBTN = (Button) findViewById(R.id.sign_in);
        goSignupBTN = (Button) findViewById(R.id.go_signup_page);

        googleBTN = (Button) findViewById(R.id.google);



        singninBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = emailET.getEditText().getText().toString();
                String pass = passET.getEditText().getText().toString();

                if (!TextUtils.isEmpty(email) || !TextUtils.isEmpty(pass)){
                    progressDialog.setTitle("Loing in User");
                    progressDialog.setMessage("Please wait. "+"user"+" is being Logged in");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    signin(email,pass);
                }else {
                    Toast.makeText(LogInActivity.this, "somethinhg wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });


        goSignupBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }



        });


        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();

        googleBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setTitle("Loing in User");
                progressDialog.setMessage("Please wait. "+"user"+" is being Logged in");
                progressDialog.setCancelable(false);
                progressDialog.show();
                signInGoogle();
            }
        });

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() != null){
                    startActivity(new Intent(LogInActivity.this, MainActivity.class));
                }
            }
        };


        }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    private void signin(final String email, String pass) {
        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(LogInActivity.this, "Log in with "+email, Toast.LENGTH_SHORT).show();
                    //Intent intent = new Intent(LogInActivity.this,MainActivity.class);
                    //startActivity(intent);
                    finish();
                }else {
                    progressDialog.hide();
                    Toast.makeText(LogInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void signInGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            // Google sign out
                            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                            Toast.makeText(LogInActivity.this, "Log in Successful", Toast.LENGTH_SHORT).show();
                            // Sign in success, update UI with the signed-in user's information
                           /* Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);*/
                        } else {
                            progressDialog.hide();
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LogInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // ...
                    }
                });
    }


}
