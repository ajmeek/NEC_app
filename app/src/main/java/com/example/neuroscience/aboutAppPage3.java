package com.example.neuroscience;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class aboutAppPage3 extends AppCompatActivity implements View.OnClickListener{

    TextView skip3Button, next3Button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app_page3);
        getSupportActionBar().hide();

        skip3Button = findViewById(R.id.skip3Btn);
        next3Button = findViewById(R.id.next3Btn);

        skip3Button.setOnClickListener(this);
        next3Button.setOnClickListener(this);

    }//End of onCreate

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.skip3Btn:
                Intent homepageIntent = new Intent(aboutAppPage3.this, homepage.class);
                startActivity(homepageIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.next3Btn:
                Intent page4Intent = new Intent(aboutAppPage3.this, aboutAppPage4.class);
                startActivity(page4Intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
        }//End of switch
    }//End of onClick
}//End of aboutAppPage3