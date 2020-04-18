package com.school.eventrra.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.school.eventrra.R;
import com.school.eventrra.util.UiUtil;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private TextInputEditText edtEmail, edtPassword, edtConfirmPassword;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        initUI();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        mAuth = FirebaseAuth.getInstance();
    }

    private void initUI() {
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        edtConfirmPassword = findViewById(R.id.edt_confirm_password);
    }

    public void signUp(View v) {
        final String email = UiUtil.getString(edtEmail);
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(SignUpActivity.this,
                    "Fill Email",
                    Toast.LENGTH_SHORT).show();
            edtEmail.requestFocus();
            return;
        }

        final String password = UiUtil.getString(edtPassword);
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(SignUpActivity.this,
                    "Fill password",
                    Toast.LENGTH_SHORT).show();
            edtPassword.requestFocus();
            return;
        }

        final String confirmPassword = UiUtil.getString(edtConfirmPassword);
        if (TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(SignUpActivity.this,
                    "Fill confirm password",
                    Toast.LENGTH_SHORT).show();
            edtConfirmPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(SignUpActivity.this,
                    "Password and confirm password do not match!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignUpActivity.this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    setResult(RESULT_OK);
                                    finish();
                                } else {
                                    Toast.makeText(SignUpActivity.this,
                                            "Fail to sign up!",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
    }

    public void close(View v) {
        finish();
    }
}
