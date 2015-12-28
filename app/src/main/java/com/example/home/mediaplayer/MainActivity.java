package com.example.home.mediaplayer;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    ImageButton rew,pause,play,fow;
    private int ftime=2000,btime=2000;
    private double timeElapsed=0,finalTime=0;
    MediaPlayer mp=null;
    SeekBar sb;
    TextView tv1,tv2;
    private Handler durationHandler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv1=(TextView)findViewById(R.id.textView);
        tv2=(TextView)findViewById(R.id.time);

        sb=(SeekBar)findViewById(R.id.seekBar);
        rew=(ImageButton)findViewById(R.id.imageButton);
        pause=(ImageButton)findViewById(R.id.imageButton2);
        play=(ImageButton)findViewById(R.id.imageButton3);
        fow=(ImageButton)findViewById(R.id.imageButton4);


       // mp=MediaPlayer.create(getApplication(), Uri.parse("http://192.168.163.50:1116/audio/a.mp3"));
       // AudioManager am=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
       // am.setStreamVolume(AudioManager.STREAM_MUSIC,am.getStreamMaxVolume(AudioManager.STREAM_MUSIC),AudioManager.FLAG_SHOW_UI);

        initialise();
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mp != null) {
                    if (mp.isPlaying()) {

                        mp.pause();
                        pause.setEnabled(false);
                        play.setEnabled(true);
                    }

                }
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mp != null) {
                    if (!mp.isPlaying()) {
                        mp.start();
                        timeElapsed = mp.getCurrentPosition();
                        sb.setProgress((int) timeElapsed);
                        durationHandler.postDelayed(updateSeekBarTime, 100);
                        play.setEnabled(false);
                        pause.setEnabled(true);
                    }

                }

            }
        });
        fow.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                    if((timeElapsed+ftime)<=finalTime)
                    {
                        timeElapsed=timeElapsed+ftime;
                        mp.seekTo((int)timeElapsed);

                    }
            }
        });
        rew.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                if ((timeElapsed - btime) > 0) {
                    timeElapsed = timeElapsed - btime;
                    mp.seekTo((int) timeElapsed);

                }
            }
        });
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mp.seekTo(sb.getProgress());
            }
        });

    }
    private Runnable updateSeekBarTime=new Runnable() {
        @Override
        public void run() {
            timeElapsed = mp.getCurrentPosition();
            sb.setProgress((int) timeElapsed);
            double timeRemaining = finalTime - timeElapsed;
            tv2.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining),TimeUnit.MILLISECONDS.toSeconds((long)timeRemaining)-TimeUnit.MINUTES.toSeconds((long)timeRemaining)));
            durationHandler.postDelayed(this,100);
        }
    };
    public void initialise()
    {
        tv1.setText("Dad Mummy");
        mp=MediaPlayer.create(this, R.raw.dm);
        finalTime=mp.getDuration();
        sb.setMax((int)finalTime);
        sb.setClickable(false);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mp!=null)
        {
            if(mp.isPlaying())
            {

                mp.stop();
            }
            mp.release();
            mp=null;

        }
    }
}
