package com.gmbprods.gerome.moviesearch;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PresentMoviesActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        return PresentMoviesFragment.newInstance();
    }
}
