package com.nanodegree.android.popularmovies;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;


public class SettingFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preference);
    }
}
