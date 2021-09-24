package com.example.neuroscience;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class appInfo extends AppCompatActivity implements View.OnClickListener{

    ImageButton appInfoBackButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);

        //Get rid of action bar
        getSupportActionBar().hide();

        appInfoBackButton = findViewById(R.id.appInfo_backBtn);

        appInfoBackButton.setOnClickListener(this);

    }//End of onCreate

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.appInfo_backBtn:
                Intent homepageIntent = new Intent(appInfo.this, homepage.class);
                startActivity(homepageIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;


        }//End of switch

    }//ENd of onClick
}//End of appInfo