package com.nanodegree.android.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Handler;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.nanodegree.android.popularmovies.databinding.ActivityMainBinding;
import java.util.List;


public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    private static String KEY;
    private static int SNACKBAR_STATE=0;
    private static final int SNACKBAR_VALUE_SHOWN=1;
    private static List<MovieItem> Movies;
    private static Parcelable RecyclerView_state;
    private ActivityMainBinding Binding;
    public static void setMovies(List<MovieItem> movies) {
        Movies = movies;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        populate_recycler();
        setting_fab();

        //setting and change listener for the Preference
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        KEY=getResources().getString(R.string.SORTING_ORDER_KEY);

    }


    //welcome message for the user
    private void show_snack() {


        //delaying snackBar calls for a smoother activity transition

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                String Snack1_Text=getResources().getString(R.string.Snack1);
                Snackbar mSnackBar = Snackbar.make(findViewById(R.id.myCoordinatorLayout), Snack1_Text, Snackbar.LENGTH_LONG);
                TextView mainTextView = (mSnackBar.getView()).findViewById(android.support.design.R.id.snackbar_text);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
                    mainTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                else
                    mainTextView.setGravity(Gravity.CENTER_HORIZONTAL);
                mSnackBar.show();
            }
        },1500);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String Snack2_Text=getResources().getString(R.string.Snack2);
                Snackbar mSnackBar = Snackbar.make(findViewById(R.id.myCoordinatorLayout), Snack2_Text, 6000);
                TextView mainTextView = (mSnackBar.getView()).findViewById(android.support.design.R.id.snackbar_text);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
                    mainTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                else
                    mainTextView.setGravity(Gravity.CENTER_HORIZONTAL);
                mSnackBar.show();
            }
        },5500);

        }

    //fill the recycler with the parsed movies data
    private void populate_recycler (){

        MovieAdapter movieAdapter=new MovieAdapter(Movies);
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        Binding.Recycler.setLayoutManager(manager);
        Binding.Recycler.setAdapter(movieAdapter);
    }

    //set an onclick listener for the fab button to access setting activity
    private void setting_fab(){
        Binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {popupWindow();}
        });
    }

    private void popupWindow(){
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        final View popupView = layoutInflater.inflate(R.layout.popup_window, null);
        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        final PopupWindow popupWindow = new PopupWindow(popupView, popupView.getMeasuredWidth(), popupView.getMeasuredHeight(), true);
        popupWindow.showAtLocation(popupView, Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setIgnoreCheekPress();
        popupView.animate().alpha(.9f).setDuration(600);

        Button popular=popupView.findViewById(R.id.popular);
        Button topRated=popupView.findViewById(R.id.top_rated);


        popular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = prefs.edit();

                editor.putBoolean(KEY, true);
                editor.apply();
                popupView.animate().alpha(0f).setDuration(600);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {populate_recycler(); popupWindow.dismiss();}
                }, 1800);
            }
        });

        topRated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean(KEY, false);
                editor.apply();
                popupView.animate().alpha(0f).setDuration(600);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {populate_recycler(); popupWindow.dismiss();}
                }, 1800);
            }
        });


    }



    //overriding {onSaveInstanceState-onRestoreInstanceState} to save the state of the recycler view and SnackBar
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        RecyclerView_state=Binding.Recycler.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(getResources().getString(R.string.RECYCLER_STATE_KEY),RecyclerView_state);
        outState.putInt(getResources().getString(R.string.SNACKBAR_STATE_KEY),SNACKBAR_VALUE_SHOWN);

    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState!=null){
            RecyclerView_state=savedInstanceState.getParcelable(getResources().getString(R.string.RECYCLER_STATE_KEY));
            SNACKBAR_STATE=savedInstanceState.getInt(getResources().getString(R.string.SNACKBAR_STATE_KEY),SNACKBAR_VALUE_SHOWN);
        }
    }

    //overriding onSharedPreferenceChanged to detect Preference changes in the main run time
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if(key.equals(KEY)){

            /* creating an object from SplashScreen class
            to access the AsyncTask inner class to call the api request again */

           Api api=new Api(this);
           api.new MovieRequest(false).execute();

        }
    }

    //overriding onResume to restore the state of the RecyclerView
    @Override
    protected void onResume() {
        super.onResume();

        if(RecyclerView_state!=null){Binding.Recycler.getLayoutManager().onRestoreInstanceState(RecyclerView_state);}

        if(SNACKBAR_STATE!=1){
            show_snack();
            SNACKBAR_STATE=1;
        }
    }

    //overriding onDestroy to unRegister the SharedPreferences Listener
    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

}