package com.school.eventrra.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.school.eventrra.R;
import com.school.eventrra.util.BitmapUtil;
import com.school.eventrra.util.Constants;
import com.school.eventrra.util.DataSet;
import com.school.eventrra.util.DateUtil;

public class EventActivity extends AppCompatActivity {
    private static boolean isFavorite;

    private FirebaseUser currentUser;
    private DatabaseReference myRef;

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_editor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(DataSet.selectedEvent.getTitle());
        setSupportActionBar(toolbar);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference(Constants.TABLE_WISHLIST);

        findViewById(R.id.viewer).setVisibility(View.VISIBLE);

        ImageView img = findViewById(R.id.img_view);
        img.setImageBitmap(BitmapUtil.base64StringToBitmap(DataSet.selectedEvent.getImageBase64()));

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                triggerFav();
            }
        });

        isFavorite = DataSet.isWishlist(DataSet.selectedEvent.getId());
        triggerFavIcon();

        setText(R.id.tv_date, DateUtil.stdDateFormat(DataSet.selectedEvent.getDate()));
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
        startActivity(new Intent(this, RegisterActivity.class));
    }

    private void triggerFav() {
        if (currentUser == null) {
            showFailToAddToast();
            return;
        }

        if (isFavorite) {
            myRef.child(currentUser.getUid())
                    .child(DataSet.selectedEvent.getId())
                    .removeValue()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                isFavorite = !isFavorite;
                                triggerFavIcon();
                            }
                        }
                    });
        } else {
            myRef.child(currentUser.getUid())
                    .child(DataSet.selectedEvent.getId())
                    .setValue(DataSet.selectedEvent.getId())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                isFavorite = !isFavorite;
                                triggerFavIcon();
                            } else {
                                showFailToAddToast();
                            }
                        }
                    });
        }
    }

    private void triggerFavIcon() {
        fab.setImageDrawable(getResources().getDrawable(isFavorite
                ? R.drawable.ic_favorite_black_24dp
                : R.drawable.ic_favorite_border_black_24dp));
    }

    private void showFailToAddToast() {
        Toast.makeText(EventActivity.this, "Fail to add wishlist", Toast.LENGTH_SHORT).show();
    }
}
