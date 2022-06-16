package com.example.neuroscience;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class aboutAppPage2 extends AppCompatActivity implements View.OnClickListener{

    TextView exit2Button, next2Button, back2Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app_page2);
        getSupportActionBar().hide();

        exit2Button = findViewById(R.id.exit2Btn);
        next2Button = findViewById(R.id.next2Btn);
        back2Button = findViewById(R.id.back2Btn);

        exit2Button.setOnClickListener(this);
        next2Button.setOnClickListener(this);
        back2Button.setOnClickListener(this);


    }//End of onCreate

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.exit2Btn:
                Intent homepageIntent = new Intent(aboutAppPage2.this, homepage.class);
                startActivity(homepageIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.next2Btn:
                Intent page3Intent = new Intent(aboutAppPage2.this, aboutAppPage3.class);
                startActivity(page3Intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.back2Btn:
                Intent page1Intent = new Intent(aboutAppPage2.this, aboutAppPage1.class);
                startActivity(page1Intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;

        }//End os switch
    }//End of onClick
}//End of aboutAppPage2