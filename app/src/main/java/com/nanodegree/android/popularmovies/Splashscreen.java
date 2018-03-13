package com.nanodegree.android.popularmovies;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.nanodegree.android.popularmovies.databinding.ActivitySplashscreenBinding;

public class Splashscreen extends Activity {

    ActivitySplashscreenBinding Binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        Binding = DataBindingUtil.setContentView(this, R.layout.activity_splashscreen);

        Api api=new Api(this);
        api.new MovieRequest(true).execute();
        animate_Views();

    }


    //this function animate the splashscreen view objects
    private void animate_Views() {

        Animation animate = AnimationUtils.loadAnimation(this, R.anim.show);
        animate.reset();
        Binding.splash.clearAnimation();
        Binding.splash.startAnimation(animate);

        animate = AnimationUtils.loadAnimation(this, R.anim.move);
        animate.reset();

        Binding.imageView1.clearAnimation();
        Binding.imageView1.startAnimation(animate);

        animate = AnimationUtils.loadAnimation(this, R.anim.move2);
        animate.reset();

        Binding.imageView2.clearAnimation();
        Binding.imageView2.startAnimation(animate);

        Binding.imageView3.animate().alpha(1f).setDuration(3000);
        Binding.progressBar.animate().alpha(.9f).setDuration(6000);

    }


}
