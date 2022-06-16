package com.example.neuroscience;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Welcome extends AppCompatActivity implements View.OnClickListener {

    TextView exitWButton, nextWButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //Get rid of action bar in this Class/Activity
        getSupportActionBar().hide();

        exitWButton = findViewById(R.id.exitWBtn);
        nextWButton = findViewById(R.id.nextWBtn);


        exitWButton.setOnClickListener(this);
        nextWButton.setOnClickListener(this);


    }//End of onCreate

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.exitWBtn:
                Intent backIntent = new Intent(Welcome.this, homepage.class);
                startActivity(backIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.nextWBtn:
                Intent page1Intent = new Intent(Welcome.this, aboutAppPage1.class);
                startActivity(page1Intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

        }//end of switch

    }//End of onClick
}//End of aboutAppPage1