package com.example.schei.votefit;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os. AsyncTask;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import org.json.JSONObject;
import org.json.JSONException;


public class MainActivity extends Activity{
    private static String urlString;
    private String userID = null;
    private ProcessJSON jsonProcess;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        if( preferences.contains("id")) {
            userID = preferences.getString("id",null);
            Log.i("containsid",userID);
        }

        if(userID == null) {

            fetchUserID();
            /*SharedPreferences.Editor edit = preferences.edit();
        edit.putString("id", userID);
        edit.commit();*/
            //Log.i("coNOTtter fetch",userID);




        }
    }


    public void jumpToVote(View view) {
        Intent intent;
        intent = new Intent(MainActivity.this, VoteActivity.class);
        startActivity(intent);
    }

    private void fetchUserID(){
        urlString = "http://5.39.92.119/rms/new.php";
        jsonProcess = new ProcessJSON();
        jsonProcess.execute(urlString);
        //System.out.println("userID er satt til " + userID);
    }

    public void setID ( String id){
        userID  = id;
        System.out.println("setIDTest" + userID);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString("id", userID);
        edit.apply();
        //Log.i("coNOTtter fetch",userID);

    }



    private class ProcessJSON extends AsyncTask<String, Void, String>{
        private String id;

        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];

            HTTPDataHandler hh = new HTTPDataHandler();
            stream = hh.GetHTTPData(urlString);

            // Return the data from specified url
            return stream;
        }

        @Override
        protected void onPostExecute(String stream){

            //..........Process JSON DATA................
            if(stream !=null){
                try{
                    // Get the full HTTP Data as JSONObject
                    JSONObject reader= new JSONObject(stream);

                    id = reader.getString("id");
                    setID(id);

                }catch(JSONException e){
                    e.printStackTrace();
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
        } // onPostExecute() end
    } // ProcessJSON class end
} // Activity end