/*
* Homework 7
* Podcast.java
* Nikhil Nagori, Hozefa Haveliwala
* */

package com.tedhour.tedhour;

import java.io.Serializable;

/**
 * Created by Admin on 03-03-2017.
 */

public class Podcast implements Serializable {
    String title,description,publicationDate,imageURL,MP3URL,duration;

    public Podcast(String title, String description, String publicationDate, String imageURL, String MP3URL, String duration) {
        this.title = title;
        this.description = description;
        this.publicationDate = publicationDate;
        this.imageURL = imageURL;
        this.MP3URL = MP3URL;
        this.duration = duration;
    }

    public Podcast() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getMP3URL() {
        return MP3URL;
    }

    public void setMP3URL(String MP3URL) {
        this.MP3URL = MP3URL;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
