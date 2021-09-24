package com.example.neuroscience;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class aboutAppPage4 extends AppCompatActivity implements View.OnClickListener{

    TextView gotItButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app_page4);
        getSupportActionBar().hide();

        gotItButton = findViewById(R.id.gotItBtn);

        gotItButton.setOnClickListener(this);


    }//End of onCreate

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.gotItBtn:
                Intent homepageIntent = new Intent(aboutAppPage4.this, homepage.class);
                startActivity(homepageIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;

        }//End of switch

    }//End of onClick
}//End of aboutAppPage4