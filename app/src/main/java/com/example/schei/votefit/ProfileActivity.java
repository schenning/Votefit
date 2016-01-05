package com.example.schei.votefit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    SharedPreferences preferences;
    private String userID;
    private String urlString;

    private ProcessJSONp jsonProcess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userID = preferences.getString("id", null);
        userID = "3";

        int id = Integer.parseInt(userID);

        getPhoto(id);
        getRating();
        //http://5.39.92.119/rms/profile.php?id=

    }

    public void goToVote(View view) {
        Intent intent;
        intent = new Intent(ProfileActivity.this, VoteActivity.class);
        startActivity(intent);
    }

    private void getRating(){
        urlString = "http://5.39.92.119/rms/profile.php?id="+userID;
        jsonProcess = new ProcessJSONp();
        jsonProcess.execute(urlString);
    }

    public void setRating (String ups, String downs){
        String status = "opp: " + ups + " ned: " + downs;
        final TextView textView3 = (TextView) findViewById(R.id.textView3);
        textView3.setText(status);

    }


    private DownloadImageTask getPhoto(int id) {
        urlString = "http://5.39.92.119/rms/pictures/" + id;
        //urlString = "https://s-media-cache-ak0.pinimg.com/736x/14/3b/34/143b34feb80859120ebe4a1f9a676c17.jpg";
        DownloadImageTask imageTask = new DownloadImageTask((ImageView) findViewById(R.id.image));
        imageTask.execute(urlString);

        return imageTask;
    }


    private class ProcessJSONp extends AsyncTask<String, Void, String> {
        private String ups;
        private String downs;

        protected String doInBackground(String... strings) {
            String stream = null;
            String urlString = strings[0];

            HTTPDataHandler hh = new HTTPDataHandler();
            stream = hh.GetHTTPData(urlString);

            // Return the data from specified url
            return stream;
        }

        @Override
        protected void onPostExecute(String stream) {

            //..........Process JSON DATA................
            if (stream != null) {
                try {
                    // Get the full HTTP Data as JSONObject
                    JSONObject reader = new JSONObject(stream);

                    ups = reader.getString("up");
                    downs = reader.getString("down");
                    setRating(ups, downs);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
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
}
