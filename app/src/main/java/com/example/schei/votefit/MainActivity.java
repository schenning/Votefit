package com.example.schei.votefit;
import android.os.Bundle;
import android.os. AsyncTask;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;



public class MainActivity extends Activity{
    private static String urlString;
    private String userID = null;
    private ProcessJSON test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(userID == null) {
            urlString = "http://5.39.92.119/rms/new.php";
            //new ProcessJSON().execute(urlString);
            test = new ProcessJSON();
            test.execute(urlString);

            userID = test.getId();
            Log.i("hei.", "hei");
            //Log.i("id: ", id.toString());
        }
        System.out.println("userID er satt til " + userID);

        if (userID != null){
            Log.i("success", "success");
        }
    }

    public void setID(String id1){
        userID = id1;
        Log.i("setter id", "userId er satt");
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

        protected void onPostExecute(String stream){
            TextView name = (TextView) findViewById(R.id.name);
            //tv.setText(stream);

            /*
                Important in JSON DATA
                -------------------------
                * Square bracket ([) represents a JSON array
                * Curly bracket ({) represents a JSON object
                * JSON object contains key/value pairs
                * Each key is a String and value may be different data types
             */

            //..........Process JSON DATA................
            if(stream !=null){
                try{
                    // Get the full HTTP Data as JSONObject
                    JSONObject reader= new JSONObject(stream);

                    // Get the JSONObject "id".......
                    // ....................
                    //String id = reader.getString("id");
                    id = reader.getString("id");

                    Log.i("id fra server: ", id);

                }catch(JSONException e){
                    e.printStackTrace();
                }
            } // if statement end

        } // onPostExecute() end

        public String getId(){
            System.out.println(id);
            System.out.println("getID blir kj;rt");
            return id;
        }
    } // ProcessJSON class end
} // Activity end