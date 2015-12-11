package com.example.schei.votefit;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;


public class VoteActivity extends Activity {
    String userID;

    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Intent intent = getIntent();
        setContentView(R.layout.activity_vote);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

            userID = preferences.getString("id",null);

        Log.i("user iden er:", userID);


    }

}
