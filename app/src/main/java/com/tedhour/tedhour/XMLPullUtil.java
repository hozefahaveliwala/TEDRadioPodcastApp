/*
* Homework 7
* XMLPullUtil.java
* Nikhil Nagori, Hozefa Haveliwala
* */

package com.tedhour.tedhour;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Admin on 03-03-2017.
 */

public class XMLPullUtil {

    static public class PullParser {
        static ArrayList<Podcast> parseXML(InputStream in) throws XmlPullParserException, IOException {
            XmlPullParser XPP = XmlPullParserFactory.newInstance().newPullParser();
            XPP.setInput(in, "UTF-8");
            Podcast pcast = null;
            ArrayList<Podcast> podcastArrayList = new ArrayList<>();
            int event = XPP.getEventType();
            int flag = 0;
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_TAG:

                        if (XPP.getName().equals("item")) {
                            pcast = new Podcast();
                            flag = 1;
                        } else if (XPP.getName().equals("title")) {
                            if (flag == 1)
                                pcast.setTitle(XPP.nextText().trim());
                        } else if (XPP.getName().equals("description")) {
                            if (flag == 1)
                                pcast.setDescription((XPP.nextText().trim()));
                        } else if (XPP.getName().equals("pubDate")) {
                            if (flag == 1)
                                pcast.setPublicationDate((XPP.nextText().trim().substring(0, 16)));
                        } else if (XPP.getName().equals("itunes:image")) {
                            if (flag == 1)
                                pcast.setImageURL(XPP.getAttributeValue(null, "href"));
                        } else if (XPP.getName().equals("itunes:duration")) {
                            if (flag == 1)
                                pcast.setDuration((XPP.nextText().trim()));
                        } else if (XPP.getName().equals("enclosure")) {
                            if (flag == 1)
                                pcast.setMP3URL(XPP.getAttributeValue(null, "url"));
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (XPP.getName().equals("item")) {
                            podcastArrayList.add(pcast);
                            Log.d("pcast", pcast.getTitle());
                            pcast = null;
                            flag = 0;
                        }
                        break;
                    default:
                        break;
                }
                event = XPP.next();
            }
            return podcastArrayList;
        }
    }
}
