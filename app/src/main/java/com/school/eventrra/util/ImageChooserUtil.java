package com.school.eventrra.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.IOException;

public class ImageChooserUtil {

    public static final int REQUEST_CODE_IMAGE_PICKER = 100;

    public static void showImageChooser(Context context, int requestCode) {
        Intent chooseIntent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        ((Activity) context).startActivityForResult(chooseIntent, requestCode);
    }

    public static Bitmap getBitmapFromIntent(Context context, Intent data) {
        if (data == null) {
            return null;
        }

        Uri uri = data.getData();
        try {
            return MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
