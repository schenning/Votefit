package com.example.schei.votefit;

/**
 * Created by schei on 12/11/15.
 */
import android.os.Bundle;
import android.preference.PreferenceActivity;
public class Preferences extends PreferenceActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//load the preferences from an xml rsource
        addPreferencesFromResource(R.xml.preferences);
    }
}