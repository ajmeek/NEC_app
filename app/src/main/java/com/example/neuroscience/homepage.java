package com.example.neuroscience;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class homepage extends AppCompatActivity implements View.OnClickListener{

    ImageButton imagesButton, soundsButton;
    TextView startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        this.setTitle("");

        imagesButton = findViewById(R.id.imageBtn);
        soundsButton = findViewById(R.id.soundsBtn);
        startButton = findViewById(R.id.start);

        imagesButton.setOnClickListener(this);
        soundsButton.setOnClickListener(this);
        startButton.setOnClickListener(this);


    }//End of onCreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.info_menu, menu);
        return true;
    }//End of onOptionsMenu

    //Action Bar options
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.aboutAppBtn:
                //Toast.makeText(this, "About app", Toast.LENGTH_SHORT).show();
                Intent aboutAppPage1Intent = new Intent(homepage.this, aboutAppPage1.class);
                startActivity(aboutAppPage1Intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            case R.id.appInfoBtn:
                //Toast.makeText(this, "App Info", Toast.LENGTH_SHORT).show();
                Intent appInfoIntent = new Intent(homepage.this, appInfo.class);
                startActivity(appInfoIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }//End of onOptionsItemsSelected

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.imageBtn:
                //Toast.makeText(this, "image Button clicked", Toast.LENGTH_SHORT).show();
                Intent imageActivityIntent = new Intent(homepage.this, ImageActivity.class);
                startActivity(imageActivityIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.soundsBtn:
                //Toast.makeText(this, "sound Button clicked", Toast.LENGTH_SHORT).show();
                Intent soundActivityIntent = new Intent(homepage.this, SoundActivity.class);
                startActivity(soundActivityIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.start:
                Intent start = new Intent(homepage.this, Welcome.class);
                startActivity(start);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }//End of switch

    }//End of onClick
}//End of homepage