package com.example.audio2;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String LOG_TAG = "Main_Activity";
    TextView remained, progress;
    ImageView prev, play, pause, forward;
    SeekBar seekBar;
    MediaPlayer player;
    ArrayList<String> trackList = new ArrayList<>();
    int currentTrack =0;
    //int idFirstTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "for test");
        setContentView(R.layout.activity_main);
        Log.d(LOG_TAG, "for test too");
        remained = findViewById(R.id.remained);
        progress = findViewById(R.id.progress);
        prev = findViewById(R.id.prev);
        play = findViewById(R.id.play);
        pause = findViewById(R.id.pause);
        forward = findViewById(R.id.forward);
        seekBar = findViewById(R.id.seekBar);
        trackList=getIntent().getStringArrayListExtra("ListTrack");
        currentTrack=getIntent().getIntExtra("index", 0);
        Uri uri = Uri.parse(trackList.get(currentTrack));

        //idFirstTrack=R.raw.music;

        player = MediaPlayer.create(this, uri);
        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        forward.setOnClickListener(this);
        prev.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
                progress.setText(format());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                player.seekTo(seekBar.getProgress());

            }
        });
        Thread myThread =new MyThread();
        myThread.start();
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.play:
                seekBar.setMax(player.getDuration());
                play.setVisibility(View.GONE);
                pause.setVisibility(View.VISIBLE);
                player.start();
                break;
            case R.id.pause:
                play.setVisibility(View.VISIBLE);
                pause.setVisibility(View.GONE);
                player.pause();
                break;
            case R.id.forward:
                currentTrack++;
                if (currentTrack == trackList.size()){
                    currentTrack=0;
                }
                player.stop();
                Uri uri = Uri.parse(trackList.get(currentTrack));
                player = MediaPlayer.create(this, uri);
                player.start();
                break;
            case R.id.prev:
                currentTrack--;
                if (currentTrack == trackList.size()){
                    currentTrack=trackList.size()-1;
                }
                player.stop();
                uri = Uri.parse(trackList.get(currentTrack));
                player = MediaPlayer.create(this, uri);
                player.start();
                break;
        }
    }

    public String format(){
        int min=player.getCurrentPosition()/60000;
        int sec=player.getCurrentPosition()/1000-min*60;
        String result="";
        if (sec>9){
            result=""+min+":"+sec;
        }else {
            result=""+min+":0"+sec;
        }
        return result;
    }


    class MyThread  extends Thread{
        @Override
        public void run() {
            while (true){
                seekBar.setProgress(player.getCurrentPosition());
                try {
                    sleep(500);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }

            }
        }
    }
}