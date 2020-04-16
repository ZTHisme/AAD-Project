package com.school.eventrra.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.school.eventrra.R;
import com.school.eventrra.util.BitmapUtil;
import com.school.eventrra.util.DataSet;
import com.school.eventrra.util.ImageChooserUtil;

import java.util.List;

public class EventEditorActivity extends AppCompatActivity {
    private List<String> countries;

    private ImageView imageView;
    private EditText edtTitle, edtAbout, edtPrice;
    private TextView tvDatetime;
    private Spinner spnLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_editor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        findViewById(R.id.editor).setVisibility(View.VISIBLE);

        imageView = findViewById(R.id.img_view);

        spnLocation = findViewById(R.id.spn_location);

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

    private void save() {
        // TODO: 4/16/2020 call api
    }
}
