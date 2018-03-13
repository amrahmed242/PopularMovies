package com.nanodegree.android.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import org.json.JSONException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;



public class Api {

    private Context mContext;

    public Api(Context context) {
        mContext = context;
    }

    //this function check the setting of the user
    private Boolean setupSharedPreferences() {
        Resources resources=mContext.getResources();
        String Key=resources.getString(R.string.SORTING_ORDER_KEY);
        Boolean Default=resources.getBoolean(R.bool.DEFAULT_SORTING_VALUE);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return sharedPreferences.getBoolean(Key,Default);

    }

    /*
    * placing all the background process and network calls & parsing
    * Below in the splash screen to improve the UI response time
    * and reduce waiting time for the user
    *
    */

    //this function create the URL for the query of the movies from the movie api, takes a Boolean "value of sorting" as a parameter
    private URL Generate_API_URL(Boolean Sorting_By_Popular){
        URL url=null;
        Uri builtUri;

        Resources resources=mContext.getResources();
        String API=resources.getString(R.string.API);
        String API_KEY=resources.getString(R.string.API_KEY);
        String POPULAR_API=resources.getString(R.string.SORTING_BY_POPULAR_API);
        String RATING_API=resources.getString(R.string.SORTING_BY_RATING_API);

        if(Sorting_By_Popular){

            builtUri = Uri.parse(POPULAR_API).buildUpon()
                    .appendQueryParameter(API,API_KEY).build();
        }

        else {
            builtUri = Uri.parse(RATING_API).buildUpon()
                    .appendQueryParameter(API,API_KEY).build();
        }

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    //this function initiate the api call and get the response with a scanner, return string (api response if available)
    private String getDataFromApi(URL url) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream in = httpURLConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();

            if (hasInput) {return scanner.next();}
            else          {return null;}

        }
        finally {httpURLConnection.disconnect();}
    }

    //this function parce the json respone of the api url
    private void Parce_Movies(String JSON_OBJECT_STRING) {

        try {MovieParser movieParser = new MovieParser();
            MainActivity.setMovies(movieParser.Parse(JSON_OBJECT_STRING));
        } catch (JSONException e) {e.printStackTrace();}
    }




    //created an async task to make the network call in it to prevent runtime crashes, then sends the results to main activity
    public class MovieRequest extends AsyncTask<String, Void, String[]> {

        private Boolean Root_Splash=false;

        public MovieRequest(Boolean root_Splash) {
            Root_Splash = root_Splash;
        }


        @Override
        protected String[] doInBackground(String... params) {

            URL ApiRequestUrl = Generate_API_URL(setupSharedPreferences());

            try {
                String JSON_Response = getDataFromApi(ApiRequestUrl);
                return new String[]{JSON_Response};

            } catch (Exception e) { e.printStackTrace(); return null;}
        }

        @Override
        protected void onPostExecute(String[] JSON_Data) {

            if (JSON_Data != null) {

                final String Data=JSON_Data[0];

                Parce_Movies(Data);


                //check if the AsyncTask is called py the splash screen and not the MainActivity..if so, an intent handler will start
                if(Root_Splash) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(mContext, MainActivity.class);
                            mContext.startActivity(intent);
                        }
                    }, 3500);

                    //extra handler to prevent activity transition glitches :)
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ((Activity)mContext).finish();
                        }
                    }, 5000);


                }


            }


        }
    }



}
