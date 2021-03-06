package com.example.schei.votefit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;
import org.json.JSONException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class VoteActivity extends Activity {
    private String userID;
    public ProcessJSONa jsonProcess;
    public ProcessJSONgetID jsonProcessID;

    private static String urlString;
    private boolean finished;
    SharedPreferences preferences;
    private List<String> photosQueue;
    private List<Integer> photosQueTest;
    int currentPhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        if( preferences.contains("id")) {
            userID = preferences.getString("id",null);
            Log.i("containsid",userID);
        }

        if(userID == null) {
            fetchUserID();
        }
        //userID = "11";




        photosQueTest = new ArrayList<Integer>();
        for (int k = 1; k<= 33; k++){
            if (k!= 11) {
                photosQueTest.add(k);
            }
        }
        long seed = System.nanoTime();
        Collections.shuffle(photosQueTest, new Random(seed));

        getPhoto(photosQueTest.get(0));
        photosQueTest.remove(0);

        getUserQueue();

    }

    public void goToProfile(View view) {
        Intent intent;
        intent = new Intent(VoteActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    public void onButtonClick(View view){
        switch(view.getId()){

            case R.id.up:
                Log.i("upb presssed", "ahah") ;
                upVote((ArrayList<Integer>) photosQueTest) ;
                //getPhoto(1);
                break;

            case R.id.button3:

                Log.i("downbut pressed", "ahah") ;
                //getPhoto(2);
                downVote((ArrayList<Integer>) photosQueTest);
                break;


        }


    }

    // OnClick function when upVote-button is pressed
    public void upVote( ArrayList<Integer> lst){
        if( lst.size() > 0 ){
            currentPhoto = lst.get(0);
            lst.remove(0);
        }

            RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
            String url = "http://5.39.92.119/rms/viewed.php?id=" + userID;

        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.
                Log.i("!Form serverup", response);
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("id", "" + currentPhoto); //Add the data you'd like to send to the server.
                MyData.put("vote", "up");
                Log.i("currentPhoto", "" + currentPhoto );
                return MyData;
            }
        };
        MyRequestQueue.add(MyStringRequest);
        if (lst.size() == 0){
            Intent intent = new Intent(VoteActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            Context context = getApplicationContext();
            CharSequence text = "Sorry! No more pictures to show";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        }
        else
        getPhoto(lst.get(0));
        //lst.remove(0);

    }

    // Onclick function when downvote button is pressed
    private void downVote( ArrayList <Integer> lst){
        if( lst.size() > 0 ){
            currentPhoto = lst.get(0);
            lst.remove(0);
        }
        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        String url = "http://5.39.92.119/rms/viewed.php?id="+ userID;
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.
                Log.i("!Form server", response);
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("id", "" + currentPhoto); //Add the data you'd like to send to the server.
                MyData.put("vote", "down");
                Log.i("currentPhoto", "" + currentPhoto );
                return MyData;
            }
        };
        MyRequestQueue.add(MyStringRequest);
        if (lst.size() == 0){
            Intent intent = new Intent(VoteActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            Context context = getApplicationContext();
            CharSequence text = "Sorry! No more pictures to show";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        else
        getPhoto(lst.get(0));
        //lst.remove(0);




    }
    // Fetches userID's not seen pictures. // Why is this a void function??
    // The person who wrote this is a terrible programmer

    private void getUserQueue() {
        urlString = "http://5.39.92.119/rms/view.php?id=" + userID;
        jsonProcess = new ProcessJSONa();
        jsonProcess.execute(urlString);

    }

    protected void setPhotosQueue(List<String> photosQueue){
        this.photosQueue = photosQueue;
        System.out.println("queue er 123satt"  + photosQueue);
    }

    private void setFinished(boolean fin){
        finished = fin;
    }


    // Download image in from web server and display it on ImageView
    private DownloadImageTask getPhoto(int id) {
        urlString = "http://5.39.92.119/rms/pictures/" + id;
        //urlString = "https://s-media-cache-ak0.pinimg.com/736x/14/3b/34/143b34feb80859120ebe4a1f9a676c17.jpg";
        DownloadImageTask imageTask = new DownloadImageTask(( ImageView) findViewById(R.id.image));
        imageTask.execute(urlString);

        return imageTask;
    }

    class ProcessJSONa extends AsyncTask<String, Void, String> {
        private String id;
        private String stringValue;
        private String finished;
        List<String> photoUserIdQueue;

        protected String doInBackground(String... strings) {
            String stream;
            String urlString = strings[0];

            HTTPDataHandler hh = new HTTPDataHandler();
            stream = hh.GetHTTPData(urlString);

            // Return the data from specified url
            return stream;
        }

        //@Override
        protected void onPostExecute(String stream) {

            if (stream != null) {
                try {
                    // Get the full HTTP Data as JSONObject
                    JSONObject reader = null;
                    try {
                        reader = new JSONObject(stream);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    finished = reader.getString("done");

                    int length = reader.length();

                    if (finished == "true") {
                        setPhotosQueue(makeArray(length, reader));
                        //loadRetrybuttonerrormessaga();
                        setFinished(true);

                    } else {
                        setPhotosQueue(makeArray(length, reader));
                        setFinished(false);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        private List<String> makeArray(int length, JSONObject reader) {
            photoUserIdQueue = new ArrayList<String>(length);
            try {
                for (int i = 0; i < length; i++) {
                    stringValue = "" + i;
                    photoUserIdQueue.add(i, reader.getString(stringValue));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return photoUserIdQueue;
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    // Handle ID
    private void fetchUserID(){
        urlString = "http://5.39.92.119/rms/new.php";
        jsonProcessID = new ProcessJSONgetID();
        jsonProcessID.execute(urlString);
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
    private class ProcessJSONgetID extends AsyncTask<String, Void, String>{
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


    /**
    public class HttpExampleActivity extends Activity {
        private static final String DEBUG_TAG = "HttpExample";
        private EditText urlText;
        private TextView textView;


        /**
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.main);
            urlText = (EditText) findViewById(R.id.myUrl);
            textView = (TextView) findViewById(R.id.myText);
        }
        **/
        /**
        // When user clicks button, calls AsyncTask.
        // Before attempting to fetch the URL, makes sure that there is a network connection.
        public void myClickHandler(View view) {
            // Gets the URL from the UI's text field.
            String stringUrl = urlText.getText().toString();
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                new DownloadWebpageTask().execute(stringUrl);
            } else {
                textView.setText("No network connection available.");
            }
        }

        // Uses AsyncTask to create a task away from the main UI thread. This task takes a
        // URL string and uses it to create an HttpUrlConnection. Once the connection
        // has been established, the AsyncTask downloads the contents of the webpage as
        // an InputStream. Finally, the InputStream is converted into a string, which is
        // displayed in the UI by the AsyncTask's onPostExecute method.
        private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... urls) {

                // params comes from the execute() call: params[0] is the url.
                try {
                    return downloadUrl(urls[0]);
                } catch (IOException e) {
                    return "Unable to retrieve web page. URL may be invalid.";
                }
            }
            // onPostExecute displays the results of the AsyncTask.
            @Override
            protected void onPostExecute(String result) {
                textView.setText(result);
            }
        } **/


    }

