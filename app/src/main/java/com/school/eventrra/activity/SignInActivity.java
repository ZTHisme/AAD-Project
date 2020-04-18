package com.school.eventrra.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.school.eventrra.R;
import com.school.eventrra.util.DataSet;
import com.school.eventrra.util.UiUtil;

import java.util.Arrays;
import java.util.List;

public class SignInActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_SIGN_UP = 1324;
    private static final int REQUEST_CODE_PERMISSIONS = 34434;
    private static final List<String> permissions = Arrays.asList(
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    );

    private FirebaseAuth mAuth;

    private TextInputEditText edtEmail, edtPassword;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        initUI();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        requestPermissionsIfNecessary();

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK &&
                requestCode == REQUEST_CODE_SIGN_UP) {
            goToBottomNavigationActivity();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            requestPermissionsIfNecessary();
        }
    }

    private void initUI() {
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
    }

    private void goToBottomNavigationActivity() {
        startActivity(new Intent(this, BottomNavigationActivity.class));
        finish();
    }

    private boolean checkAllPermissions() {
        boolean hasPermissions = true;
        for (String permission : permissions) {
            hasPermissions &= ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
        }
        return hasPermissions;
    }

    private void requestPermissionsIfNecessary() {
        if (!checkAllPermissions()) {
            ActivityCompat.requestPermissions(
                    this,
                    permissions.toArray(new String[0]),
                    REQUEST_CODE_PERMISSIONS);
        }
    }

    public void signIn(View v) {
        final String email = UiUtil.getString(edtEmail);
        final String password = UiUtil.getString(edtPassword);

        if (TextUtils.isEmpty(email)) {
            edtEmail.requestFocus();
            edtEmail.setError("Enter email");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            edtPassword.requestFocus();
            edtPassword.setError("Enter password");
            return;
        }

        DataSet.isAdmin = email.equals("admin@gmail.com");

        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            goToBottomNavigationActivity();
                        } else {
                            Toast.makeText(SignInActivity.this, "Sign in failed!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void signUp(View v) {
        startActivityForResult(new Intent(this, SignUpActivity.class),
                REQUEST_CODE_SIGN_UP);
    }
}
