package com.eticket.liamu.eticket2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private TextInputLayout mLoginEmail;
    private TextInputLayout mPassword;

    private Button mLogin_btn;

    private ProgressDialog mLoginProcess;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //Setup Toolbar
        mToolbar = (Toolbar) findViewById(R.id.login_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Login To Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Setup Progress Dialog
        mLoginProcess = new ProgressDialog(this);

        //Init fields
        mLoginEmail = (TextInputLayout) findViewById(R.id.textInputLayout2);
        mPassword = (TextInputLayout) findViewById(R.id.textInputLayout3);
        mLogin_btn = (Button) findViewById(R.id.login_button);

        //Firebase Init
        mAuth = FirebaseAuth.getInstance();


        //Submit Login Details
        mLogin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = mLoginEmail.getEditText().getText().toString();
                String password = mPassword.getEditText().getText().toString();

                if(!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)){

                    mLoginProcess.setTitle("Logging In...");
                    mLoginProcess.setMessage("Please wait will we log you in");
                    mLoginProcess.setCanceledOnTouchOutside(false);
                    mLoginProcess.show();

                    loginUser(email, password);
                }
            }
        });
    }

    //Attempt login with supplied data
    private void loginUser(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                //If logged in
                if(task.isSuccessful()){
                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                    finish();
                    mLoginProcess.dismiss();
                } else{
                    Toast.makeText(LoginActivity.this, "Cannot login user. Please try again.", Toast.LENGTH_LONG).show();
                    mLoginProcess.hide();
                }

            }
        });

    }

}
