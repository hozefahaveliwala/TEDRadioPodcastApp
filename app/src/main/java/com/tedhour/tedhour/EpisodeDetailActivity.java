/*
* Homework 7
* EpisodeDetailActivity.java
* Nikhil Nagori, Hozefa Haveliwala
* */
package com.tedhour.tedhour;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class EpisodeDetailActivity extends AppCompatActivity {
    TextView textViewTitle, textViewDesc, textViewDate, textViewDuration;
    ImageView imageViewEpisode, imageViewPlay;
    MediaPlayer mediaPlayer;
    SeekBar seekBar;
    Handler handler = new Handler();
    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode_detail);
        final Podcast pcast = (Podcast) getIntent().getSerializableExtra("Podcast");

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        textViewDesc = (TextView) findViewById(R.id.textViewDesc);
        textViewDate = (TextView) findViewById(R.id.textViewDate);
        textViewDuration = (TextView) findViewById(R.id.textViewDuration);
        imageViewEpisode = (ImageView) findViewById(R.id.imageViewMain);
        imageViewPlay = (ImageView) findViewById(R.id.imageViewPlay);
        seekBar = (SeekBar)findViewById(R.id.seekBar);

        seekBar.setMax(1000);
        textViewTitle.setText(pcast.getTitle());

        textViewDesc.setText("Description " + pcast.getDescription());
        SimpleDateFormat iformat = new SimpleDateFormat("dd MMM yyyy");
        SimpleDateFormat oformat = new SimpleDateFormat("MM/dd/yyyy");

        try {
            textViewDate.setText("Publication Date " + oformat.format(iformat.parse(pcast.getPublicationDate().substring(5))));
        } catch (ParseException e) {
            e.printStackTrace();
            textViewDate.setText("Publication Date " + pcast.getPublicationDate().substring(5));
        }

        textViewDuration.setText("Duration: " + pcast.getDuration());
        Picasso.with(this)
                .load(pcast.getImageURL())
                .into(imageViewEpisode);


        imageViewPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageViewPlay.getTag().equals("pause")) {
                    mediaPlayer.pause();
                    imageViewPlay.setTag("play");
                    imageViewPlay.setImageResource(android.R.drawable.ic_media_play);

                } else if (imageViewPlay.getTag().equals("play")) {
                    if (flag == 0) {
                        streamPodcast(pcast.getMP3URL());
                        flag = 1;
                    } else {
                        mediaPlayer.start();
                        handler.postDelayed(updateProgress, 1000);
                    }
                    imageViewPlay.setTag("pause");
                    imageViewPlay.setImageResource(android.R.drawable.ic_media_pause);
                }
            }
        });

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                handler.postDelayed(updateProgress, 1000);
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                imageViewPlay.setTag("play");
                imageViewPlay.setImageResource(android.R.drawable.ic_media_play);
                flag = 0;
            }
        });
    }

    private void streamPodcast(String url) {
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(EpisodeDetailActivity.this, "Problem playing podcast.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        mediaPlayer.release();
        handler.removeCallbacksAndMessages(null);
        finish();
        super.onBackPressed();
    }

    Runnable updateProgress = new Runnable() {
        @Override
        public void run() {
            long currentSeconds = mediaPlayer.getCurrentPosition() / 10000;
            long totalSeconds = mediaPlayer.getDuration() / 10000;
            Double percentage = ((double)currentSeconds/totalSeconds) * 10000;
            seekBar.setProgress(percentage.intValue());
            handler.postDelayed(this, 1000);
        }
    };
}
