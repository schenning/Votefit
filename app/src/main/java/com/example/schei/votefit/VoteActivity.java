package com.example.schei.votefit;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
import org.json.JSONException;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;


public class VoteActivity extends Activity {
    private String userID;
    public ProcessJSONa jsonProcess;
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
        userID = preferences.getString("id", null);
        userID = "123";
        /** photosQueue=new ArrayList<String>();
        for (int i=1; i<=7; i++){
            photosQueue.add(Integer.toString(i));
            Log.i("test", photosQueue.get(i - 1));
        }

         **/
        photosQueTest = new ArrayList<Integer>();
        for (int k = 1; k<= 7; k++){
            photosQueTest.add(k);

        }

        getPhoto(0);
        getUserQueue();



        //hvordan i f får man tak i userqueuen, glæmt av det.

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
        currentPhoto = lst.get(0);
        lst.remove(0);
        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        String url = "http://5.39.92.119/rms/viewed.php?id="+ userID;
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
        getPhoto(lst.get(0));
        //lst.remove(0);

    }

    // Onclick function when downvote button is pressed
    private void downVote( ArrayList <Integer> lst){
        currentPhoto = lst.get(0);
        lst.remove(0);
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

