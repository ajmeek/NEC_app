package com.example.neuroscience;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class aboutAppPage1 extends AppCompatActivity implements View.OnClickListener {

    TextView exit1Button, next1Button, back1Button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app_page1);

        //Get rid of action bar in this Class/Activity
        getSupportActionBar().hide();

        exit1Button = findViewById(R.id.exit1Btn);
        next1Button = findViewById(R.id.next1Btn);
        back1Button = findViewById(R.id.back1Btn);

        exit1Button.setOnClickListener(this);
        next1Button.setOnClickListener(this);
        back1Button.setOnClickListener(this);

    }//End of onCreate

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.exit1Btn:
            case R.id.back1Btn:
                Intent backIntent = new Intent(aboutAppPage1.this, homepage.class);
                startActivity(backIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.next1Btn:
                Intent page2Intent = new Intent(aboutAppPage1.this, aboutAppPage2.class);
                startActivity(page2Intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;


        }//end of switch

    }//End of onClick
}//End of aboutAppPage1