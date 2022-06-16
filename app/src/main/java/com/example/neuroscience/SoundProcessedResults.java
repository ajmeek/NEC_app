package com.example.neuroscience;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class SoundProcessedResults extends AppCompatActivity implements View.OnClickListener{
    ImageButton homeButton, backButton;

    WaveFragment waveFragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_processed_results);

        //Get rid of action bar in this Class/Activity
        getSupportActionBar().hide();

        homeButton = findViewById(R.id.resultImage_Home_Btn);
        backButton = findViewById(R.id.resultImage_Back_Btn);

        homeButton.setOnClickListener(this);
        backButton.setOnClickListener(this);

        //Intent intent = getIntent();
        //File file = intent.get

        File file = (File)getIntent().getSerializableExtra("fileToPlay");
        new MyAsyncTaskAudio().execute(file);

    }//End of onCreate method

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.resultImage_Home_Btn:


                getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.fragment_container)).commit();

                Intent homePageIntent = new Intent(SoundProcessedResults.this, homepage.class);
                startActivity(homePageIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;


            case R.id.resultImage_Back_Btn:
                Intent audioListPageIntent = new Intent(SoundProcessedResults.this, audioList.class);
                startActivity(audioListPageIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;

        }//End of switch-case

    }//End of onClick method


    public class MyAsyncTaskAudio extends AsyncTask<Object, Integer, Object> {
        private ProgressDialog progressSoundDialog = null;

        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
            progressSoundDialog = new ProgressDialog(SoundProcessedResults.this);

            progressSoundDialog.show();
            progressSoundDialog.setIndeterminate(true);
            progressSoundDialog.setContentView(R.layout.progress_dialog);
            progressSoundDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            progressSoundDialog.setCancelable(false);

        }


        @Override
        protected Boolean doInBackground(Object... params){
            File file = (File) params[0];

            //Do heavy work
            //processSoundFile(file);
            //return true;
            InputStream stream = null;
            try {

                stream = getContentResolver().openInputStream(Uri.fromFile(file));
                int ava = stream.available();
                //byte[] bitmap = new byte[5120];
                byte[] bitmap = new byte[stream.available()];
                byte[] bm2;

                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                int nRead = Integer.MAX_VALUE;

                // Wave files have a 32 byte header
                // TODO::If to check if wave file?
                stream.skip(4);
                stream.read(bitmap);

                Bundle bundle = new Bundle();
                bundle.putByteArray("bitmap", bitmap);

                //WaveFragment waveFragment = new WaveFragment();
                waveFragment = new WaveFragment();
                waveFragment.setArguments(bundle);

                //FragmentManager fragmentManager = getSupportFragmentManager();
                //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();


                        // Add wave fragment
                        //fragmentTransaction.add(R.id.fragment_container, waveFragment).addToBackStack(null).commitAllowingStateLoss();
                        fragmentTransaction.replace(R.id.fragment_container, waveFragment).addToBackStack(null).commitAllowingStateLoss();
                        //getSupportFragmentManager().beginTransaction().remove(R.id.fragment_container).commit();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            //super.onPostExecute(o);
            progressSoundDialog.dismiss();
        }


    }//End of MyAsyncTaskAudio class


}//End of SoundProcessedResults class