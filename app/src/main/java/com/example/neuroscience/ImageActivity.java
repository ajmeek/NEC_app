package com.example.neuroscience;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
//import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.fastICA.FastICA;
import com.example.imagePatches.imagePatches;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ImageActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView photoPreview;
    ImageButton galleryButton, backButton, homeButton, cameraButton, processBlackAndWhiteImage;
    TextView process, viewButton;

    private String currentPhotoPath;
    private Bitmap mResultsBitmap;
    private AppExecutor mAppExecutor;

    private static int RESULT_LOAD_IMAGE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private static final int REQUEST_STORAGE_PERMISSION = 1;

    private static final String FILE_PROVIDER_AUTHORITY = "com.example.neuroscience.fileprovider";

    double[][] patch_data;
    double[][] mixing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        //Get rid of action bar in this Class/Activity
        getSupportActionBar().hide();

        mAppExecutor = new AppExecutor();

        Intent intent = getIntent();
        String value = intent.getStringExtra("key"); //if it's a string you stored.


        photoPreview = findViewById(R.id.picPreview);
        galleryButton = findViewById(R.id.photoGallery_Btn);
        cameraButton = findViewById(R.id.camera_Btn);
        backButton = findViewById(R.id.imageActivity_backBtn);
        homeButton = findViewById(R.id.imageActivity_homeBtn);

        viewButton = findViewById(R.id.textView8);
        processBlackAndWhiteImage = findViewById(R.id.processBWImage_Btn);
        process = findViewById(R.id.textView2);

        //Hide image process button/text until photo is pushed to imageview
        processBlackAndWhiteImage.setVisibility(View.GONE);
        process.setVisibility(View.GONE);
        viewButton.setVisibility(View.GONE);

        backButton.setOnClickListener(this);
        homeButton.setOnClickListener(this);
        galleryButton.setOnClickListener(this);
        cameraButton.setOnClickListener(this);
        processBlackAndWhiteImage.setOnClickListener(this);
        viewButton.setOnClickListener(this);

    }//End of onCreate method

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.imageActivity_backBtn:
            case R.id.imageActivity_homeBtn:
                Intent homePageIntent = new Intent(ImageActivity.this, homepage.class);
                startActivity(homePageIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            break;
            case R.id.camera_Btn:
                // Check for the external storage permission
                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // If you do not have permission, request it
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
                } else {
                    // Launch the camera if the permission exists
                    launchCamera();
                }

            break;
            case R.id.photoGallery_Btn:
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            break;
            case R.id.processBWImage_Btn:

                new MyAsyncTask().execute();

                //Toast.makeText(ImageActivity.this, "Finished processing Image!!!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.textView8:
                Intent ImageProcessedIntent = new Intent(ImageActivity.this, ImageProcessed.class);
                Bitmap test = imagePatches.showPatches(imagePatches.transpose(mixing), 25);
                Uri imageURI = getImageUri(getBaseContext(), test);
                String str = imageURI.toString();
                ImageProcessedIntent.putExtra("str",str);
                startActivity(ImageProcessedIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
        }//End of switch

    }//End of onCLick method

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // If the Gallery capture activity was called and was successful
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            android.net.Uri imageData = data.getData();
            photoPreview.setImageURI(imageData);
        }
        // If the image capture activity was called and was successful
        else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Process the image and set it to the TextView
            processAndSetImage();

        } else {
            // Otherwise, delete the temporary image file
            BitmapUtils.deleteImageFile(this, currentPhotoPath);
        }

        processBlackAndWhiteImage.setVisibility(View.VISIBLE);
        process.setVisibility(View.VISIBLE );
        viewButton.setVisibility(View.VISIBLE);
    }//End of onActivityResult


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Called when you request permission to read and write to external storage
        switch (requestCode) {
            case REQUEST_STORAGE_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // If you get permission, launch the camera
                    launchCamera();
                } else {
                    // If you do not get permission, show a Toast
                    Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }//End of switch-case
    }//End of onRequestPermissionResult method


    /**
     * Creates a temporary image file and captures a picture to store in it.
     */
    private void launchCamera() {
        // Create the capture image intent
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the temporary File where the photo should go
            File photoFile = null;
            try {
                photoFile = BitmapUtils.createTempImageFile(this);
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                // Get the path of the temporary file
                currentPhotoPath = photoFile.getAbsolutePath();

                // Get the content URI for the image file
                Uri photoURI = FileProvider.getUriForFile(this, FILE_PROVIDER_AUTHORITY, photoFile);

                // Add the URI so the camera can store the image
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                // Launch the camera activity
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }//End of launchCamera method


    /**
     * Method for processing the captured image and setting it to the TextView.
     */
    private void processAndSetImage() {

        // Resample the saved image to fit the ImageView
        mResultsBitmap = BitmapUtils.resamplePic(this, currentPhotoPath);

        // Set the new bitmap to the ImageView
        photoPreview.setImageBitmap(mResultsBitmap);

        //Save Image!!!
        mAppExecutor.diskIO().execute(() -> {
            // Delete the temporary image file
            BitmapUtils.deleteImageFile(this, currentPhotoPath);

            // Save the image
            BitmapUtils.saveImage(this, mResultsBitmap);

        });
        //Toast.makeText(this, "In ProcessAndSetImage", Toast.LENGTH_SHORT).show();
    }//End of processAndSetImage method

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }//End of getImageUri method

    public class MyAsyncTask extends AsyncTask<String, Integer, Boolean>{
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
            progressDialog = new ProgressDialog(ImageActivity.this);

            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_dialog);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            progressDialog.setCancelable(false);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Boolean doInBackground(String... strings) {
            heavyProcessImage();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            //super.onPostExecute(aBoolean);
            progressDialog.dismiss();
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        private void heavyProcessImage(){
            BitmapDrawable drawable = (BitmapDrawable) photoPreview.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            patch_data = imagePatches.get_patches(50000, 8, bitmap);
            FastICA fastICA = new FastICA();
            try {
                fastICA.fit(imagePatches.transpose(patch_data),25);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mixing = fastICA.getEM();
            //Log.d("OUTPUT", mixing.toString());
            Bitmap test = imagePatches.showPatches(imagePatches.transpose(mixing), 25);
            //storeImage(test);
            if(test == null)
                Log.d("NULL", "Bitmap is Null");
            Bitmap testBit = ((BitmapDrawable) photoPreview.getDrawable()).getBitmap();
            Uri imageURI = getImageUri(getBaseContext(), test);
            //photoPreview.setImageURI(imageURI);


            //Since this is in a separate thread of the main thread
            //We need to run and update "ImageView" in the main thread with runOnUiThread function
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Update UI ImageView
                    photoPreview.setImageURI(imageURI);
                }
            });
        }//End of heavyProcessImage


    }//End of MyAsyncTask

}//End of ImageActivity class