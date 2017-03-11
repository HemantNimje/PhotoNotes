package edu.csulb.android.photonotes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Hemant on 3/8/2017.
 */

public class ViewPhotoActivity extends AppCompatActivity {
    String photoCaption;
    String photoFilePath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_photo);

        // Bind the components with the xml
        ImageView imageViewPhoto = (ImageView) findViewById(R.id.view_photo_image);
        TextView textViewPhotoCaption = (TextView) findViewById(R.id.view_photo_image_caption);

        // Get intent
        Intent intent = getIntent();

        // Get the photo caption from intent and set it to textview
        photoCaption = intent.getStringExtra("caption");
        textViewPhotoCaption.setText(photoCaption);

        // Get photo file path from the intent and set it to imageview
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        photoFilePath = db.getPhotoFilePath(photoCaption);

        Bitmap imageBitmap = BitmapFactory.decodeFile(photoFilePath);
        imageViewPhoto.setImageBitmap(imageBitmap);

    }
}
