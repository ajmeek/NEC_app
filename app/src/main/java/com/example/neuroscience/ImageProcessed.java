package com.example.neuroscience;


import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

//import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageProcessed extends AppCompatActivity implements View.OnClickListener {


    ImageView photoPreview;
    ImageButton backButton, homeButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_processed);

        //Get rid of action bar in this Class/Activity
        getSupportActionBar().hide();

        homeButton = findViewById(R.id.imageProcess_homeBtn);
        backButton = findViewById(R.id.imageProcess_backBtn);
        photoPreview = findViewById(R.id.picPreview);

        homeButton.setOnClickListener(this);
        backButton.setOnClickListener(this);



    }//End of onCreate method

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageProcess_backBtn:
                Intent imagePageIntent = new Intent(ImageProcessed.this, ImageActivity.class);
                startActivity(imagePageIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.imageProcess_homeBtn:
                Intent homePageIntent = new Intent(ImageProcessed.this, homepage.class);
                startActivity(homePageIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;

        }//End of switch
    }//End of onCLick method
}




