package com.school.eventrra.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.school.eventrra.R;
import com.school.eventrra.model.Register;
import com.school.eventrra.util.Constants;
import com.school.eventrra.util.DataSet;

import java.util.Date;

public class BookingActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 666;

    private DatabaseReference myRef;

    private EditText edtName, edtPhone, edtEmail, edtAddress;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_white_24dp);
        }

        initUI();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "Sign in first", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference(Constants.TABLE_REGISTER);

        edtEmail.setText(currentUser.getEmail());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            DataSet.selectedEvent = null;
            setResult(RESULT_OK);
            finish();
        }
    }

    private void initUI() {
        edtName = findViewById(R.id.edt_name);
        edtPhone = findViewById(R.id.edt_phone);
        edtEmail = findViewById(R.id.edt_email);
        edtAddress = findViewById(R.id.edt_address);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing...");
        progressDialog.setCancelable(false);
    }

    public void register(View v) {
        String name = edtName.getText().toString();
        String phone = edtPhone.getText().toString();
        String email = edtEmail.getText().toString();
        String address = edtAddress.getText().toString();

        if (TextUtils.isEmpty(name)) {
            edtName.requestFocus();
            edtName.setError("Enter name");
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            edtPhone.requestFocus();
            edtPhone.setError("Enter phone");
            return;
        }

        if (TextUtils.isEmpty(email)) {
            edtEmail.requestFocus();
            edtEmail.setError("Enter email");
            return;
        }

        if (TextUtils.isEmpty(address)) {
            edtAddress.requestFocus();
            edtAddress.setError("Enter address");
            return;
        }

        final String id = myRef.push().getKey();
        if (TextUtils.isEmpty(id)) {
            showFailToSaveToast();
            return;
        }

        final Register register = new Register();
        register.setId(id);
        register.setDate(new Date());
        register.setEventId(DataSet.selectedEvent.getId());
        register.setEventTitle(DataSet.selectedEvent.getTitle());
        register.setNoOfTicket(RegisterActivity.noOfTicket);
        register.setPrice(DataSet.selectedEvent.getPrice());
        register.setUsername(name);
        register.setPhone(phone);
        register.setEmail(email);
        register.setAddress(address);

        progressDialog.show();
        myRef.child(register.getId())
                .setValue(register)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            DataSet.selectedRegister = register;
                            startActivityForResult(new Intent(BookingActivity.this, PurchasedDetailActivity.class), REQUEST_CODE);
                        } else {
                            showFailToSaveToast();
                        }
                    }
                });
    }

    private void showFailToSaveToast() {
        Toast.makeText(this, "Fail to save", Toast.LENGTH_SHORT).show();
    }
}
