/*
* Homework 7
* XMLAsyncTask.java
* Nikhil Nagori, Hozefa Haveliwala
* */

package com.tedhour.tedhour;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Admin on 03-03-2017.
 */

public class XMLAsyncTask extends android.os.AsyncTask<String, Void, ArrayList<Podcast>> {

    setItems setItemsInterface;

    public XMLAsyncTask(setItems setItemsInterface) {
        this.setItemsInterface = setItemsInterface;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        setItemsInterface.showProgressDialog();

    }

    @Override
    protected ArrayList<Podcast> doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = con.getInputStream();

                try {
                    return XMLPullUtil.PullParser.parseXML(in);
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Podcast> result) {
        super.onPostExecute(result);
        setItemsInterface.dismissProgressDialog();
        setItemsInterface.setNewsArrayList(result);
    }

    static public interface setItems {
        public void showProgressDialog();

        public void dismissProgressDialog();

        public void setNewsArrayList(ArrayList<Podcast> aL);
    }
}
