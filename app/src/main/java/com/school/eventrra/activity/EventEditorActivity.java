package com.school.eventrra.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.school.eventrra.R;
import com.school.eventrra.model.Event;
import com.school.eventrra.util.BitmapUtil;
import com.school.eventrra.util.DataSet;
import com.school.eventrra.util.DateUtil;
import com.school.eventrra.util.ImageChooserUtil;

import java.util.Date;
import java.util.List;

public class EventEditorActivity extends AppCompatActivity {
    private List<String> countries;

    private ImageView imageView;
    private EditText edtTitle, edtAbout, edtPrice;
    private Button btnDate, btnFrom, btnTo;
    private Spinner spnLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_editor);

        if (DataSet.selectedEvent == null) {
            DataSet.selectedEvent = new Event();
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(DataSet.selectedEvent.getTitle());
        setSupportActionBar(toolbar);

        findViewById(R.id.editor).setVisibility(View.VISIBLE);

        initUI();

        initListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case ImageChooserUtil.REQUEST_CODE_IMAGE_PICKER: {
                    Bitmap bm = BitmapUtil.resize(ImageChooserUtil.getBitmapFromIntent(
                            this,
                            data));
                    imageView.setImageBitmap(bm);
                    break;
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            save();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initUI() {
        imageView = findViewById(R.id.img_view);
        edtTitle = findViewById(R.id.edt_title);
        btnDate = findViewById(R.id.btn_date);
        btnFrom = findViewById(R.id.btn_from);
        btnTo = findViewById(R.id.btn_to);
        spnLocation = findViewById(R.id.spn_location);
        edtAbout = findViewById(R.id.edt_about);
        edtPrice = findViewById(R.id.edt_price);
    }

    private void initListener() {
        imageView.setImageBitmap(BitmapUtil.base64StringToBitmap(DataSet.selectedEvent.getImageBase64()));

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(DataSet.selectedEvent.getDate(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                DataSet.selectedEvent.setDate(new Date(year, month, dayOfMonth));

                                btnDate.setText(DateUtil.stdDateFormat(DataSet.selectedEvent.getDate()));
                            }
                        });
            }
        });

        btnFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(DataSet.selectedEvent.getFromDate(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                DataSet.selectedEvent.setFromDate(
                                        new Date(0, 0, 0, hourOfDay, minute, 0));

                                btnFrom.setText(DateUtil.hourMinuteAmPm(DataSet.selectedEvent.getFromDate()));
                            }
                        });
            }
        });

        btnTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(DataSet.selectedEvent.getToDate(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                DataSet.selectedEvent.setToDate(
                                        new Date(0, 0, 0, hourOfDay, minute, 0));

                                btnTo.setText(DateUtil.hourMinuteAmPm(DataSet.selectedEvent.getToDate()));
                            }
                        });
            }
        });

        countries = DataSet.getCountries();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                countries);
        spnLocation.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_a_photo_black_24dp));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageChooserUtil.showImageChooser(EventEditorActivity.this,
                        ImageChooserUtil.REQUEST_CODE_IMAGE_PICKER);
            }
        });
    }

    private void showDatePickerDialog(Date selectedDate,
                                      final DatePickerDialog.OnDateSetListener onDateSetListener) {
        if (selectedDate == null) {
            selectedDate = new Date();
        }
        DatePickerDialog dialog = new DatePickerDialog(this,
                onDateSetListener,
                selectedDate.getYear() + 1900,
                selectedDate.getMonth(),
                selectedDate.getDate());
        dialog.show();
    }

    private void showTimePickerDialog(Date selectedDate,
                                      TimePickerDialog.OnTimeSetListener onTimeSetListener) {
        if (selectedDate == null) {
            selectedDate = new Date();
        }
        TimePickerDialog dialog = new TimePickerDialog(this,
                onTimeSetListener,
                selectedDate.getHours(),
                selectedDate.getMinutes(),
                false);
        dialog.show();
    }

    private void save() {
        // TODO: 4/16/2020 call api
    }
}
