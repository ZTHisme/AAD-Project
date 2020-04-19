package com.school.eventrra.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;

import com.school.eventrra.R;
import com.school.eventrra.util.BitmapUtil;
import com.school.eventrra.util.DataSet;

public class RegisterActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 444;

    private NumberPicker np;
    public static int noOfTicket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_editor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(DataSet.selectedEvent.getTitle());
        setSupportActionBar(toolbar);

        findViewById(R.id.fab).setVisibility(View.GONE);

        findViewById(R.id.register).setVisibility(View.VISIBLE);

        ImageView img = findViewById(R.id.img_view);
        img.setImageBitmap(BitmapUtil.base64StringToBitmap(DataSet.selectedEvent.getImageBase64()));

        np = findViewById(R.id.np);
        np.setMinValue(1);
        np.setMaxValue(100);

        noOfTicket = 0;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            setResult(RESULT_OK);
            finish();
        }
    }

    public void next(View v) {
        noOfTicket = np.getValue();
        startActivityForResult(new Intent(this, BookingActivity.class), REQUEST_CODE);
    }
}
