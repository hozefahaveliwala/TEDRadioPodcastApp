/*
* Homework 7
* MainActivity.java
* Nikhil Nagori, Hozefa Haveliwala
* */
package com.tedhour.tedhour;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements MyListAdapter.setMediaButtons, MyGridAdapter.setMediaButtons, XMLAsyncTask.setItems {
    ProgressDialog pD1;
    int isList = 1;
    ArrayList<Podcast> podcastList, podcastList2;
    RecyclerView rV1;
    MyListAdapter adapter;
    MyGridAdapter adapter2;
    LinearLayout lL1;
    MediaPlayer mediaPlayer = new MediaPlayer();
    ImageView img;
    SeekBar sB;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        rV1 = (RecyclerView) findViewById(R.id.recyclerView1);
        img = (ImageView) findViewById(R.id.imageViewPlay);
        lL1 = (LinearLayout) findViewById(R.id.linearLayout1);
        sB = (SeekBar) findViewById(R.id.seekBar);

        //seekbar settings
        sB.setMax(10000);
        sB.setProgress(0);

        podcastList2 = new ArrayList<>();
        //mediaPlayer.setOnPreparedListener(this);
        podcastList = new ArrayList<>();

        //call AsyncTask if connected
        if (isConnected() == true) {
            new XMLAsyncTask(MainActivity.this).execute(getResources().getString(R.string.Link));
        } else {
            Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //pause image to play image and vice versa
                if (img.getTag().equals("pause")) {
                    mediaPlayer.pause();
                    img.setTag("play");
                    img.setImageResource(android.R.drawable.ic_media_play);

                } else if (img.getTag().equals("play")) {
                    mediaPlayer.start();
                    handler.postDelayed(updateProgress, 1000);
                    img.setTag("pause");
                    img.setImageResource(android.R.drawable.ic_media_pause);
                }

            }
        });

        //Listener for MediaPlay.prepareAsync
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                handler.postDelayed(updateProgress, 1000);

            }
        });

        //Listener for compeletion of track
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                lL1.setVisibility(View.INVISIBLE);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_change) {
            lL1.setVisibility(View.INVISIBLE);
            mediaPlayer.reset();
            if (isList == 1) {
                isList = 0;
                podcastList2 = podcastList;
                sortDsc(podcastList2);
                setNewsArrayList(podcastList2);
            } else {
                isList = 1;
                setNewsArrayList(podcastList);
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //Overrding for method in Interface
    @Override
    public void showProgressDialog() {
        pD1 = new ProgressDialog(MainActivity.this);
        pD1.setMessage("Loading Episodes");
        pD1.show();
    }

    //Overrding for method in Interface
    @Override
    public void dismissProgressDialog() {
        pD1.dismiss();
    }

    //Overrding for method in Interface from XMLAsyncTask
    @Override
    public void setNewsArrayList(ArrayList<Podcast> aL) {
        if (aL != null) {

            if (isList == 1) {
                podcastList = aL;
                adapter = new MyListAdapter(MainActivity.this, R.layout.list_item, podcastList, this);
                adapter.notifyDataSetChanged();
                rV1.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                rV1.setAdapter(adapter);
            } else {

                adapter2 = new MyGridAdapter(MainActivity.this, R.layout.grid_item, aL, this);
                adapter2.notifyDataSetChanged();
                rV1.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                rV1.setAdapter(adapter2);
            }

        } else {
            Toast.makeText(MainActivity.this, "No Data", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isConnected() {
        ConnectivityManager cM = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cM.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected() == true) {
            return true;
        }
        return false;
    }

    //Overrding for method in Interface from Adapter
    @Override
    public void playPodcast(int position) {
        lL1.setVisibility(View.VISIBLE);
        img.setTag("pause");
        img.setImageResource(android.R.drawable.ic_media_pause);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.reset();
        streamPodcast(podcastList.get(position).getMP3URL());

    }

    //Overrding for method in Interface from Adapter
    @Override
    public void itemClicked(Podcast p) {
        Intent intent = new Intent(MainActivity.this,EpisodeDetailActivity.class);
        intent.putExtra("Podcast",p);
        startActivity(intent);
    }

    private void streamPodcast(String url) {
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Problem playing podcast.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        mediaPlayer.reset();
        super.onStop();
    }

    @Override
    protected void onResume() {
        lL1.setVisibility(View.INVISIBLE);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mediaPlayer.release();
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    Runnable updateProgress = new Runnable() {
        @Override
        public void run() {
            if(mediaPlayer.isPlaying()) {
                long currentSeconds = mediaPlayer.getCurrentPosition() / 10000;
                long totalSeconds = mediaPlayer.getDuration() / 10000;
                Double percentage = ((double) currentSeconds / totalSeconds) * 10000;
                sB.setProgress(percentage.intValue());
                Log.d("Postion",String.valueOf(percentage.intValue()));
                handler.postDelayed(this, 1000);
            }
        }
    };

    //custom sort for the Podcast Arraylist
    private void sortDsc(ArrayList<Podcast> appArray) {
        Collections.sort(appArray, new Comparator<Podcast>() {
            @Override
            public int compare(Podcast o1, Podcast o2) {
                SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");
                Date d1 = null, d2 = null;
                try {
                    d1 = format.parse(o1.getPublicationDate().substring(5));
                    d2 = format.parse(o2.getPublicationDate().substring(5));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (d2.compareTo(d1) > 0)
                    return 1;
                else if (d2.compareTo(d1) < 0)
                    return -1;
                else
                    return 0;
            }

        });
    }
}
