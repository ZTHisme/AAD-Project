package com.school.eventrra.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.school.eventrra.R;
import com.school.eventrra.util.BitmapUtil;
import com.school.eventrra.util.DataSet;

public class RegisterActivity extends AppCompatActivity {

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

        NumberPicker np = findViewById(R.id.np);
        np.setMinValue(1);
        np.setMaxValue(100);
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Toast.makeText(RegisterActivity.this,
                        newVal + " Tickets",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void next(View v) {
        startActivity(new Intent(this, BookingActivity.class));
    }
}
