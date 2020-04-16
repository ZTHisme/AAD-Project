package com.school.eventrra.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.school.eventrra.R;

import java.util.Arrays;
import java.util.List;

public class SignInActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_SIGN_UP = 1324;
    private static final int REQUEST_CODE_PERMISSIONS = 34434;
    private static final List<String> permissions = Arrays.asList(
            Manifest.permission.INTERNET
    );

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
        // TODO: 4/16/2020 call api

        goToBottomNavigationActivity();
    }

    public void signUp(View v) {
        startActivityForResult(new Intent(this, SignUpActivity.class),
                REQUEST_CODE_SIGN_UP);
    }
}
