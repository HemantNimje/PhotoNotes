package edu.csulb.android.photonotes;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by Hemant on 3/8/2017.
 */

public class AddPhotoActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    String mCurrentPhotoPath;
    Button addButton, saveButton;
    ImageView capturedImage;
    TextView textViewImageCaption;
    Bitmap imageBitmap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_photo);

        // Bind components from the xml
        addButton = (Button) findViewById(R.id.button_add);
        saveButton = (Button) findViewById(R.id.button_save);
        capturedImage = (ImageView) findViewById(R.id.captured_image);
        textViewImageCaption = (TextView) findViewById(R.id.image_caption);

        // Handle click event on ADD button
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        // Handle click event on SAVE button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImageAndPath();
            }
        });
    }

    /* Launch the camera intent to capture the image */
    public void dispatchTakePictureIntent() {

        // Intent to call the camera
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Make sure that there is a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            // Create a file where the photo should go
            File photoFile = null;

            // Check if the file is created or not
            try {
                photoFile = createImageFile();
            } catch (IOException exception) {
                // Error occurred while creating the file
                Toast.makeText(this, "Error occurred while creating the file", Toast.LENGTH_SHORT)
                        .show();
            }

            // Continue only if the File was created successfully
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    /* Set the imageView with the image that is returned by the camera */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            imageBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
            capturedImage.setImageBitmap(imageBitmap);
        }
    }

    private File createImageFile() throws IOException {

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        // Save a file: path for the use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void saveImageAndPath() {
        String imageCaption;
        String imagePath;

        imageCaption = textViewImageCaption.getText().toString();
        imagePath = mCurrentPhotoPath;


        if (imageCaption.trim().length() > 0 && imagePath != null) {
            // Database Handler
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());

            // Inserting new photo caption and path to database
            db.insertPhoto(imageCaption, imagePath);

            // Making input field text to blank
            textViewImageCaption.setText("");

            // Hiding the keyboard
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(textViewImageCaption.getWindowToken(), 0);

            Toast.makeText(this, "Data stored successfully", Toast.LENGTH_LONG).show();

            finish();

        } else {
            Toast.makeText(this, "Please enter name as well as capture image before saving",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Put caption to the bundle
        outState.putString("caption", textViewImageCaption.getText().toString());

        // Put bitmap to the bundle
        outState.putParcelable("photoBitmap", imageBitmap);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

        // Set the textview with the photo caption
        textViewImageCaption.setText(savedInstanceState.getString("caption"));

        // Set the imageview with the parcelable bitmap
        capturedImage.setImageBitmap((Bitmap) savedInstanceState.getParcelable("photoBitmap"));

        super.onRestoreInstanceState(savedInstanceState);
    }
}
