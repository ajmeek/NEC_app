package com.example.neuroscience;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class aboutAppPage4 extends AppCompatActivity implements View.OnClickListener{

    TextView gotItButton, exit4Button, back4Button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app_page4);
        getSupportActionBar().hide();

        gotItButton = findViewById(R.id.gotItBtn);
        exit4Button = findViewById(R.id.exit4Btn);
        back4Button = findViewById(R.id.back4Btn);

        gotItButton.setOnClickListener(this);
        exit4Button.setOnClickListener(this);
        back4Button.setOnClickListener(this);


    }//End of onCreate

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.gotItBtn:
            case R.id.exit4Btn:
                Intent homepageIntent = new Intent(aboutAppPage4.this, homepage.class);
                startActivity(homepageIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.back4Btn:
                Intent page3Intent = new Intent(aboutAppPage4.this, aboutAppPage3.class);
                startActivity(page3Intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;

        }//End of switch

    }//End of onClick
}//End of aboutAppPage4