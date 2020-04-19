package com.school.eventrra.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.school.eventrra.R;
import com.school.eventrra.util.DataSet;
import com.school.eventrra.util.DateUtil;

public class PurchasedDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchased_detail);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_white_24dp);
        }

        setText(R.id.tv_invoice_id, DataSet.selectedRegister.getId());
        setText(R.id.tv_invoice_date, DateUtil.stdDateFormat(DataSet.selectedRegister.getDate()));
        setText(R.id.tv_event_title, DataSet.selectedRegister.getEventTitle());
        setText(R.id.tv_no_of_ticket, DataSet.selectedRegister.getNoOfTicket() + "");
        setText(R.id.tv_price, DataSet.selectedRegister.getPrice());
        setText(R.id.tv_username, DataSet.selectedRegister.getUsername());
        setText(R.id.tv_phone, DataSet.selectedRegister.getPhone());
        setText(R.id.tv_email, DataSet.selectedRegister.getEmail());
        setText(R.id.tv_address, DataSet.selectedRegister.getAddress());
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
    protected void onDestroy() {
        super.onDestroy();
        DataSet.selectedRegister = null;
    }

    @Override
    public void onBackPressed() {
        done(null);
    }

    private void setText(int tvId, String str) {
        if (str == null || TextUtils.isEmpty(str)) {
            return;
        }
        ((TextView) findViewById(tvId)).setText(str);
    }

    public void done(View v) {
        setResult(RESULT_OK);
        finish();
    }
}
