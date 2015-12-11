package com.example.schei.votefit;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;


import org.json.JSONObject;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


public class VoteActivity extends Activity {
    String userID;
    public ProcessJSONa jsonProcess;
    private static String urlString;

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

    private void getUserQueue(){
        urlString = "http://5.39.92.119/rms/view.php?id=" + userID;
        jsonProcess = new ProcessJSONa();
        jsonProcess.execute(urlString);
        //System.out.println("userID er satt til " + userID);
    }


  /*  private void getUserPhoto(){
        urlString = "http://5.39.92.119/rms/pictures/"+userID;
        jsonProcess = new ProcessJSON();
        jsonProcess.execute(urlString);
        //System.out.println("userID er satt til " + userID);
    }
*/


}

   private class ProcessJSONa extends AsyncTask<String, Void, String> {
        private String id;
        private String stringValue;
        private boolean finished;
        List<String> photoUserIdQueue;

        protected String doInBackground(String... strings) {
            String stream = null;
            String urlString = strings[0];

            HTTPDataHandler hh = new HTTPDataHandler();
            stream = hh.GetHTTPData(urlString);

            // Return the data from specified url
            return stream;
        }

        //@Override
        protected void onPostExecute(String stream) {

            //..........Process JSON DATA................
            if (stream != null) {
                try {
                    // Get the full HTTP Data as JSONObject
                    JSONObject reader = new JSONObject(stream);
                    finished = reader.getString("done");
                    if (finished == true) {
                        //loadRetrybuttonerrormessaga();

                    } else {
                        photoUserIdQueue = new ArrayList<String>(10);
                        for (int i = 0; i < 10; i++) {
                            stringValue = "" + i;
                            photoUserIdQueue.add(i, reader.getString(stringValue));
                        }


                    }
                    catch(JSONException.e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }

            // if statement end
                            /*
                    Important in JSON DATA
                    -------------------------
                    * Square bracket ([) represents a JSON array
                    * Curly bracket ({) represents a JSON object
                    * JSON object contains key/value pairs
                    * Each key is a String and value may be different data types
                 */
