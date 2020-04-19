package com.school.eventrra.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.school.eventrra.R;
import com.school.eventrra.model.Event;
import com.school.eventrra.util.BitmapUtil;
import com.school.eventrra.util.Constants;
import com.school.eventrra.util.DataSet;
import com.school.eventrra.util.DateUtil;
import com.school.eventrra.util.ImageChooserUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EventEditorActivity extends AppCompatActivity {
    public static final int REQUEST_CODE = 1;

    private List<String> countries;

    private DatabaseReference myRef;

    private ImageView imageView;
    private EditText edtTitle, edtPlace, edtAbout, edtPrice;
    private Button btnDate, btnFrom, btnTo;
    private Spinner spnLocation;
    private ProgressDialog progressDialog;
    private TextView tvLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_editor);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        findViewById(R.id.editor).setVisibility(View.VISIBLE);

        initUI();

        initListener();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference(Constants.TABLE_EVENT);

        if (DataSet.selectedEvent == null) {
            DataSet.selectedEvent = new Event();
        } else {
            fillData();
        }
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
                    DataSet.selectedEvent.setImageBase64(BitmapUtil.bitmapToBase64String(bm));
                    imageView.setImageBitmap(bm);
                    break;
                }
                case REQUEST_CODE: {
                    if (data.getExtras() == null) {
                        showFailToGetLocationToast();
                        return;
                    }
                    LatLng latLng = (LatLng) data.getExtras().get(MapsActivity.RESULT_LAT_LNG);
                    if (latLng == null) {
                        showFailToGetLocationToast();
                        return;
                    }
                    checkLocationPicker();
                    DataSet.selectedEvent.setLatitude(latLng.latitude);
                    DataSet.selectedEvent.setLongitude(latLng.longitude);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DataSet.selectedEvent = null;
    }

    private void initUI() {
        imageView = findViewById(R.id.img_view);
        edtTitle = findViewById(R.id.edt_title);
        btnDate = findViewById(R.id.btn_date);
        btnFrom = findViewById(R.id.btn_from);
        btnTo = findViewById(R.id.btn_to);
        spnLocation = findViewById(R.id.spn_location);
        tvLocation = findViewById(R.id.location);
        edtPlace = findViewById(R.id.edt_place);
        edtAbout = findViewById(R.id.edt_about);
        edtPrice = findViewById(R.id.edt_price);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing...");
        progressDialog.setCancelable(false);
    }

    private void initListener() {
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(DataSet.selectedEvent.getDate(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(year, month, dayOfMonth);
                                DataSet.selectedEvent.setDate(calendar.getTime());

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
                                Date date = new Date(0, 0, 0, hourOfDay, minute, 0);
                                DataSet.selectedEvent.setFromDate(date);
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
                                Date date = new Date(0, 0, 0, hourOfDay, minute, 0);
                                DataSet.selectedEvent.setToDate(date);
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

        tvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EventEditorActivity.this, MapsActivity.class);
                i.putExtra(MapsActivity.PARAM_LAT, DataSet.selectedEvent.getLatitude());
                i.putExtra(MapsActivity.PARAM_LNG, DataSet.selectedEvent.getLongitude());
                i.putExtra(MapsActivity.PARAM_SET_LONG_CLICK_LISTENER, true);
                startActivityForResult(i, REQUEST_CODE);
            }
        });

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
        Calendar calendar = Calendar.getInstance();

        if (selectedDate != null) {
            calendar.set(Calendar.YEAR, selectedDate.getYear() + 1900);
            calendar.set(Calendar.MONTH, selectedDate.getMonth());
            calendar.set(Calendar.DAY_OF_MONTH, selectedDate.getDate());
        }

        DatePickerDialog dialog = new DatePickerDialog(this,
                onDateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

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
        String title = edtTitle.getText().toString().trim();
        String location = (String) spnLocation.getSelectedItem();
        String place = edtPlace.getText().toString().trim();
        String about = edtAbout.getText().toString().trim();
        String price = edtPrice.getText().toString().trim();

        if (TextUtils.isEmpty(DataSet.selectedEvent.getImageBase64())) {
            Toast.makeText(this, "Add photo", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(title)) {
            edtTitle.setError("Enter title");
            return;
        }

        if (TextUtils.isEmpty(place)) {
            edtPlace.setError("Enter place");
            return;
        }

        if (TextUtils.isEmpty(about)) {
            edtAbout.setError("Enter about");
            return;
        }

        if (TextUtils.isEmpty(price)) {
            edtPrice.setError("Enter price");
            return;
        }

        if (DataSet.selectedEvent.getDate() == null) {
            Toast.makeText(this, "Set date", Toast.LENGTH_SHORT).show();
            return;
        }

        if (DataSet.selectedEvent.getFromDate() == null) {
            Toast.makeText(this, "Set start time", Toast.LENGTH_SHORT).show();
            return;
        }

        if (DataSet.selectedEvent.getToDate() == null) {
            Toast.makeText(this, "Set end time", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(DataSet.selectedEvent.getId())) {
            final String id = myRef.push().getKey();
            if (TextUtils.isEmpty(id)) {
                showFailToSaveToast();
                return;
            }
            DataSet.selectedEvent.setId(id);
        }
        DataSet.selectedEvent.setTitle(title);
        DataSet.selectedEvent.setLocation(location);
        DataSet.selectedEvent.setPlace(place);
        DataSet.selectedEvent.setAbout(about);
        DataSet.selectedEvent.setPrice(price);

        progressDialog.show();
        myRef.child(DataSet.selectedEvent.getId())
                .setValue(DataSet.selectedEvent)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            finish();
                        } else {
                            showFailToSaveToast();
                        }
                    }
                });
    }

    private void showFailToSaveToast() {
        Toast.makeText(this, "Fail to save", Toast.LENGTH_SHORT).show();
    }

    private void fillData() {
        imageView.setImageBitmap(BitmapUtil.base64StringToBitmap(DataSet.selectedEvent.getImageBase64()));
        edtTitle.setText(DataSet.selectedEvent.getTitle());
        btnDate.setText(DateUtil.stdDateFormat(DataSet.selectedEvent.getDate()));
        btnFrom.setText(DateUtil.hourMinuteAmPm(DataSet.selectedEvent.getFromDate()));
        btnTo.setText(DateUtil.hourMinuteAmPm(DataSet.selectedEvent.getToDate()));
        spnLocation.setSelection(countries.indexOf(DataSet.selectedEvent.getLocation()));
        edtAbout.setText(DataSet.selectedEvent.getAbout());
        edtPrice.setText(DataSet.selectedEvent.getPrice());

        if (DataSet.selectedEvent.getLatitude() != 0 ||
                DataSet.selectedEvent.getLongitude() != 0) {
            checkLocationPicker();
        }
    }

    private void checkLocationPicker() {
        tvLocation.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_location_yellow_24dp,
                0,
                R.drawable.ic_check_green_24dp,
                0);
    }

    private void showFailToGetLocationToast() {
        Toast.makeText(this, "Fail to get location!", Toast.LENGTH_SHORT).show();
    }
}
