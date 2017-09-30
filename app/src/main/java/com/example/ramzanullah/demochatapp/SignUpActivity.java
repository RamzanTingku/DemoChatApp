package com.example.ramzanullah.demochatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {


    private EditText nameET, emailET, passET;
    private Button singupBTN;
    private FirebaseAuth mAuth;
    private Toolbar toolbar;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();

        toolbar = (Toolbar) findViewById(R.id.sign_up_page_appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Demo Chat App");
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressDialog = new ProgressDialog(this);

        nameET = (EditText) findViewById(R.id.name);
        emailET = (EditText) findViewById(R.id.email);
        passET = (EditText) findViewById(R.id.password);

        singupBTN = (Button) findViewById(R.id.sign_up);

        singupBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameET.getText().toString();
                String email = emailET.getText().toString();
                String pass = passET.getText().toString();

                if (name.isEmpty()){
                    nameET.setError("This field must not be emplty");
                }
                else if (email.isEmpty()){
                    emailET.setError("This field must not be emplty");
                }
                else if (pass.isEmpty()){
                    passET.setError("This field must not be emplty");
                }
                else if (!TextUtils.isEmpty(name) || !TextUtils.isEmpty(email) || !TextUtils.isEmpty(pass)){
                    progressDialog.setTitle("Registering User");
                    progressDialog.setMessage("Please wait. "+name+" is being Registered");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    signup(name,email,pass);
                }

            }
        });

    }

    private void signup(final String name, final String email, final String pass) {

        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(SignUpActivity.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    progressDialog.hide();
                    Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
