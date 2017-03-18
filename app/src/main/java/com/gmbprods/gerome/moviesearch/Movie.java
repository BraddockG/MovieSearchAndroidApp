package com.gmbprods.gerome.moviesearch;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.util.UUID;

/**
 * Created by Gerome on 09/02/2017.
 */

public class Movie {
    private String mTitle;
    private String mPlot;
    private String mID;
    private String mUrl;
    private String mPosterUrl;
    private Bitmap mPosterBitmap;

    public String getID() {
        return mID;
    }

    public void setID(String mID) {
        this.mID = mID;
    }



    public Bitmap getPosterBitmap() {
        return mPosterBitmap;
    }

    public void setPosterBitmap(Bitmap mPosterBitmap) {
        this.mPosterBitmap = mPosterBitmap;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getPlot() {
        return mPlot;
    }

    public void setPlot(String mPlot) {
        this.mPlot = mPlot;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getPosterUrl() {
        return mPosterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.mPosterUrl = posterUrl;
    }

    @Override
    public String toString() {
        return mID;
    }
}


