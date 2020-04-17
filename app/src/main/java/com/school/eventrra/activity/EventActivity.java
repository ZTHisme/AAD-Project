package com.school.eventrra.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.school.eventrra.R;
import com.school.eventrra.util.BitmapUtil;
import com.school.eventrra.util.DataSet;
import com.school.eventrra.util.DateUtil;

import java.util.Date;

public class EventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_editor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(DataSet.selectedEvent.getTitle());
        setSupportActionBar(toolbar);

        findViewById(R.id.viewer).setVisibility(View.VISIBLE);

        ImageView img = findViewById(R.id.img_view);
        img.setImageBitmap(BitmapUtil.base64StringToBitmap(DataSet.selectedEvent.getImageBase64()));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 4/16/2020 set fav or not
            }
        });

        setText(R.id.tv_datetime_value,
                DateUtil.hourMinuteAmPm(DataSet.selectedEvent.getFromDate()) + " - " +
                        DateUtil.hourMinuteAmPm(DataSet.selectedEvent.getToDate()));
        setText(R.id.tv_location_value, String.valueOf(DataSet.selectedEvent.getLocation()));
        setText(R.id.tv_about_value, String.valueOf(DataSet.selectedEvent.getAbout()));
        setText(R.id.tv_price_value, String.valueOf(DataSet.selectedEvent.getPrice()));
    }

    private void setText(int tvId, String str) {
        if (str == null || TextUtils.isEmpty(str)) {
            return;
        }
        ((TextView) findViewById(tvId)).setText(str);
    }

    public void register(View v) {
        // TODO: 4/17/2020 call api
    }
}
