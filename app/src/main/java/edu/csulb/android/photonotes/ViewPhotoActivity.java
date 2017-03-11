package edu.csulb.android.photonotes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

/**
 * Created by Hemant on 3/8/2017.
 */

public class ViewPhotoActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_photo);

        // Bind the components with the xml
        ImageView imageViewPhoto = (ImageView) findViewById(R.id.view_photo_image);
        TextView textViewPhotoCaption = (TextView) findViewById(R.id.view_photo_image_caption);

        String photoCaption;
        String photoFilePath;

        Intent intent = getIntent();

        photoCaption = intent.getStringExtra("caption");
        textViewPhotoCaption.setText(photoCaption);

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        photoFilePath = db.getPhotoFilePath(photoCaption);
        //Toast.makeText(this, "" + photoFilePath, Toast.LENGTH_SHORT).show();

        Bitmap imageBitmap = BitmapFactory.decodeFile(photoFilePath);
        imageViewPhoto.setImageBitmap(imageBitmap);

    }
}
