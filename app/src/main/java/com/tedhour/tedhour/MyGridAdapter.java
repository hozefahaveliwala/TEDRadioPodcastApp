/*
* Homework 7
* MyGridAdapter.java
* Nikhil Nagori, Hozefa Haveliwala
* */

package com.tedhour.tedhour;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Admin on 03-05-2017.
 */

public class MyGridAdapter extends RecyclerView.Adapter<MyGridAdapter.ViewHolder> {
    Context context;
    int resource;
    static List<Podcast> podcastObjects;
    static MyGridAdapter.setMediaButtons sMB;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewtitle;
        ImageView imgIcon, imgPlay;

        public ViewHolder(View view) {
            super(view);
            textViewtitle = (TextView) view.findViewById(R.id.textViewTitle);
            imgPlay = (ImageView) view.findViewById(R.id.imageView2);
            imgIcon = (ImageView) view.findViewById(R.id.imageViewEpisode);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getClickedItem(getAdapterPosition());
                }
            });
        }
    }


    public MyGridAdapter(Context context, int resource, List<Podcast> podcastObjects, MyGridAdapter.setMediaButtons sMB) {

        this.context = context;
        this.resource = resource;
        this.podcastObjects = podcastObjects;
        this.sMB = sMB;
    }

    @Override
    public MyGridAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(resource, parent, false);
        return new MyGridAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyGridAdapter.ViewHolder holder, final int position) {
        final Podcast podcast = podcastObjects.get(position);
        holder.textViewtitle.setText(podcast.getTitle());
        Picasso.with(context)
                .load(podcast.getImageURL())
                .resize(150,150)
                .into(holder.imgIcon);
        holder.imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, podcast.getTitle(), Toast.LENGTH_SHORT).show();
                sMB.playPodcast(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return podcastObjects.size();
    }
    private static void getClickedItem(int pos) {
        sMB.itemClicked(podcastObjects.get(pos));
    }

    static public interface setMediaButtons {
        void playPodcast(int position);
        void itemClicked(Podcast p);
    }
}
