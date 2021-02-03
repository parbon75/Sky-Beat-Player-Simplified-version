package com.parbon.skybeatplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    //Initialize variable
    TextView playerPosition, playerDuration;
    SeekBar seekBar;
    ImageView btRew, btPlay, btPause, btFf;

    MediaPlayer mediaPlayer;
    Handler handler = new Handler();
    Runnable runnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assign Variable
        playerPosition = findViewById(R.id.player_position);
        playerDuration = findViewById(R.id.player_duration);
        seekBar = findViewById(R.id.seek_bar);
        btRew = findViewById(R.id.bt_rew);
        btPlay = findViewById(R.id.bt_play);
        btPause = findViewById(R.id.bt_pause);
        btPlay = findViewById(R.id.bt_play);
        btFf = findViewById(R.id.bt_ff);

        //Initialize media player
        mediaPlayer = MediaPlayer.create(this, R.raw.music);

        //Initialize Runnable
        runnable = new Runnable() {
            @Override
            public void run() {
                //set progress on seek bar
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                //Handler post delay for 0.5 second
                handler.postDelayed(this, 500);
            }
        };

        //Get duration of media player
        int duration = mediaPlayer.getDuration();
        //Convert millisecond to minute and second
        String sDuration = convertFormat(duration);
        //set Duration on text view
        playerDuration.setText(sDuration);
        btPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hide play button
                btPlay.setVisibility(View.GONE);
                //Show pause button
                btPause.setVisibility(View.VISIBLE);
                //Set max on seek bar
                seekBar.setMax(mediaPlayer.getDuration());
                //Start handler
                handler.postDelayed(runnable, 0);

            }
        });

        btPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hide the pause button
                btPause.setVisibility(View.GONE);
                //show the play button
                btPlay.setVisibility(View.VISIBLE);
                //pause the media player
                mediaPlayer.pause();
                //Stop handler
                handler.removeCallbacks(runnable);
            }
        });

        btFf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get current position of media player
                int currentPosition = mediaPlayer.getCurrentPosition();
                //get duration of media player
                int duration = mediaPlayer.getDuration();
                //check condition
                if (mediaPlayer.isPlaying() && duration != currentPosition) {
                    //if the song is playing and the song is not at the end
                    //fast forward for 5 seconds
                    currentPosition = currentPosition + 5000;
                    //set current position as the text view
                    playerPosition.setText(convertFormat(currentPosition));
                    //set progress on seek bar
                    mediaPlayer.seekTo(currentPosition);
                }
            }
        });

        btRew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get current position of media player
                int currentPosition = mediaPlayer.getCurrentPosition();
                //get duration of media player
                //int duration = mediaPlayer.getDuration();
                //check condition
                if (mediaPlayer.isPlaying() && currentPosition > 5000) {
                    //if the song is playing and the song has been played for more than 5 seconds
                    //rewind for 5 seconds
                    currentPosition = currentPosition - 5000;
                    //set current position as the text view
                    playerPosition.setText(convertFormat(currentPosition));
                    //set progress on seek bar
                    mediaPlayer.seekTo(currentPosition);
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //check condition
                if(fromUser){
                    //when drag the seek bar
                    //set progress on seek bar
                    mediaPlayer.seekTo(progress);
                }
                //set current position to text view
                playerPosition.setText(convertFormat(mediaPlayer.getCurrentPosition()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //hide pause button
                btPause.setVisibility(View.GONE);
                //show play button
                btPlay.setVisibility(View.VISIBLE);
                //set media player to the initial position
                mediaPlayer.seekTo(0);
            }
        });

    }

    @SuppressLint("DefaultLocale")
    private String convertFormat(int duration) {
        return String.format("%02d:%02d"
                , TimeUnit.MILLISECONDS.toMinutes(duration)
                , TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
    }
}