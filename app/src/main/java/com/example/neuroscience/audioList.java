package com.example.neuroscience;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.AsyncPlayer;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public class audioList extends AppCompatActivity implements AudioListAdaptor.onItemListClick, View.OnClickListener {
    private ConstraintLayout playerSheet;
    private BottomSheetBehavior bottomSheetBehavior;
    private RecyclerView audioList;
    private File[] allFiles;

    private AudioListAdaptor audioListAdaptor;
    private MediaPlayer mediaPlayer = null;
    private boolean isPlaying = false;
    public File fileToPlay = null;
    private TextView playerHeader, playerFileName;
    private ImageButton playButton, processAudioButton;
    private SeekBar playerSeekBar;
    private Handler seekBarHandler;
    private Runnable updateSeekBar;

    //ProgressDialog progressSoundDialog = null;

    //private ProgressDialog progressSoundDialog = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_list);

        //Get rid of action bar in this Class/Activity
        getSupportActionBar().hide();

        playerSheet = findViewById(R.id.player_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(playerSheet);
        audioList = findViewById(R.id.audio_list_view);
        playerHeader = findViewById(R.id.player_header_title);
        playerFileName = findViewById(R.id.player_fileName);
        playButton = findViewById(R.id.player_play_btn);
        playerSeekBar = findViewById(R.id.player_seekBar);
        processAudioButton = findViewById(R.id.process_audio_Btn);


        playButton.setOnClickListener(this);
        processAudioButton.setOnClickListener(this);

        //Grab files from directory
        String path = audioList.this.getExternalFilesDir("/").getAbsolutePath();
        File directory = new File(path);
        allFiles = directory.listFiles();


        audioListAdaptor = new AudioListAdaptor(allFiles, this);
        audioList.setHasFixedSize(true);
        audioList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        audioList.setAdapter(audioListAdaptor);


        //Initialize item touch helper to delete a file
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                //When item swipe
                //Remove file
                //audioList.deleteFile(viewHolder.getAdapterPosition());
                //deleteFile(viewHolder.getAdapterPosition());


                //Notify Adaptor
                audioListAdaptor.notifyDataSetChanged();
            }
        }).attachToRecyclerView(audioList);


        //This function will help prevent the media player sheet from not disappearing and not slide down
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            //Here we don't allow the player sheet to go fully down
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState == BottomSheetBehavior.STATE_HIDDEN){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }

            }
            //This method gives us Live data of current POSITION of player sheet that is playing
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                //We do not need to put anything here
            }
        });//End of addBottomSheetCallBack method

        //This function is to change the position of seekBar
        playerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if(fileToPlay != null) {

                    pauseAudio();
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(fileToPlay != null) {
                    int progress = seekBar.getProgress();
                    mediaPlayer.seekTo(progress);
                    resumeAudio();
                }
            }
        });//End of setOnSeekBarChangeListener

    }//End of onCreate method

    @Override
    public void onClickListener(File file, int position) {
        //Log.d("PLAY_LOG", "FILE PLAYING" + file.getName());
        //This is where we control the media player
        fileToPlay = file;
        if(isPlaying){
            stopAudio();
            playAudio(fileToPlay);

        } else {

            playAudio(fileToPlay);

            //new MyAsyncTaskAudio().execute(file);

            //processSoundFile(fileToPlay);
            //Toast.makeText(this, "File was clicked!", Toast.LENGTH_LONG).show();
        }
    }//End of onClickListener

    private void pauseAudio(){
        mediaPlayer.pause();
        playButton.setImageResource(R.drawable.player_play_btn);
        isPlaying = false;
        seekBarHandler.removeCallbacks(updateSeekBar);
    }

    private void resumeAudio(){
        mediaPlayer.start();
        playButton.setImageResource(R.drawable.player_pause_btn);
        isPlaying = true;

        updateRunnable();
        seekBarHandler.postDelayed(updateSeekBar, 0);

    }

    //This method stops current audio that is playing
    private void stopAudio() {

        playButton.setImageResource(R.drawable.player_play_btn);
        playerHeader.setText("Stopped");
        isPlaying = false;
        mediaPlayer.stop();
        seekBarHandler.removeCallbacks(updateSeekBar);
    }

    //This method plays the actual Audio
    private void playAudio(File fileToPlay) {

        mediaPlayer = new MediaPlayer();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        try {
            mediaPlayer.setDataSource(fileToPlay.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        playButton.setImageResource(R.drawable.player_pause_btn);
        playerFileName.setText(fileToPlay.getName());
        playerHeader.setText("Playing");
        isPlaying = true;


        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stopAudio();
                playerHeader.setText("Finished");
            }
        });

        //This will return total duration of media player and set it to the playerSeekBar as Maximum progress
        playerSeekBar.setMax(mediaPlayer.getDuration());

        //Update seekBar using Handler
        seekBarHandler = new Handler();
        updateRunnable();
        seekBarHandler.postDelayed(updateSeekBar, 0);
        //Toast.makeText(this, "File was clicked!", Toast.LENGTH_LONG).show();

    }//End of playAudio

    private void updateRunnable() {
        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                playerSeekBar.setProgress(mediaPlayer.getCurrentPosition());
                seekBarHandler.postDelayed(this, 500);
            }
        };
    }//End of updateRunnable method

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.player_play_btn:
                if(isPlaying){
                    pauseAudio();
                }else{
                    if(fileToPlay != null){
                        resumeAudio();
                    }
                }
                break;
            case R.id.process_audio_Btn:
                Intent resultImageActivityIntent = new Intent(audioList.this, SoundProcessedResults.class);
                resultImageActivityIntent.putExtra("fileToPlay", fileToPlay);
                startActivity(resultImageActivityIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

        }//End of switch case
    }//End of onClick method

    @Override
    protected void onStop() {
        super.onStop();
        if(isPlaying) {
            stopAudio();
        }
    }//End of onStop method


    /*
    public void processSoundFile(File fileToPlay){
        fileToPlay = this.fileToPlay;
        InputStream stream = null;

        try {

            stream = getContentResolver().openInputStream(Uri.fromFile(fileToPlay));
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
            WaveFragment waveFragment = new WaveFragment();
            waveFragment.setArguments(bundle);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Add wave fragment
                    fragmentTransaction.add(R.id.fragment_container, waveFragment).addToBackStack(null).commitAllowingStateLoss();
                }
            });


            //super.onActivityResult(requestCode, resultCode, data);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }//End of processSoundFile
    */

    public class MyAsyncTaskAudio extends AsyncTask<Object, Integer, Boolean>{
        ProgressDialog progressSoundDialog = null;
        //private ProgressDialog progressSoundDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressSoundDialog = new ProgressDialog(audioList.this);

            progressSoundDialog.show();
            //progressSoundDialog.setIndeterminate(true);
            progressSoundDialog.setContentView(R.layout.progress_dialog_sound);
            progressSoundDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            progressSoundDialog.setCancelable(false);



        }

        @Override
        protected Boolean doInBackground(Object... params) {
            File file = (File) params[0];
            //Do heavy work
            try {
                processSoundFile(file);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }

           return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            progressSoundDialog.dismiss();
        }


        private void processSoundFile(File fileToPlay) throws IOException {
            //File processFile = fileToPlay;
            //fileToPlay = this.fileToPlay;
            InputStream stream = null;


                stream = getContentResolver().openInputStream(Uri.fromFile(fileToPlay));
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
                WaveFragment waveFragment = new WaveFragment();
                waveFragment.setArguments(bundle);

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Add wave fragment
                        fragmentTransaction.add(R.id.fragment_container, waveFragment).addToBackStack(null).commitAllowingStateLoss();
                    }
                });


        }//End of processSoundFile
    }//End of MyAsyncTaskAudio class


}//End of audioList class