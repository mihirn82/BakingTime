package com.example.android.bakingtime;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import static com.example.android.bakingtime.RecipeActivity.KEY_RECIPE_STEP;

/**
 * Created by mihirnewalkar on 10/1/17.
 */

public class StepActivity extends AppCompatActivity{

    public static final String LOG_TAG = StepActivity.class.getName();

    private String shortDescription = "";
    private String description = "";
    private String videoURL = "";
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        mPlayerView = (SimpleExoPlayerView) findViewById(R.id.playerView);

        Intent intent = getIntent();

        if (getIntent() != null) {
            RecipeSteps currentStep = intent.getParcelableExtra(KEY_RECIPE_STEP);
            shortDescription = currentStep.getShortDescription();
            description = currentStep.getDescription();
            videoURL = currentStep.getVideoURL();

            Log.i(LOG_TAG,description + " " + videoURL);
        } else {
            Log.i(LOG_TAG,"Intent is null");
        }

        initializePlayer(Uri.parse(videoURL));

        TextView shortDescriptionTV = (TextView) findViewById(R.id.recipe_step_tv);
        shortDescriptionTV.setText(shortDescription);

        TextView descriptionTV = (TextView) findViewById(R.id.description_tv);
        descriptionTV.setText(description);

//        // Find the view pager that will allow the user to swipe between fragments
//        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
//
//        // Create an adapter that knows which fragment should be shown on each page
//        StepsFragmentPagerAdapter adapter = new StepsFragmentPagerAdapter(getSupportFragmentManager());
//
//        // Set the adapter onto the view pager
//        viewPager.setAdapter(adapter);
    }

    private void initializePlayer(Uri videoURL) {
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            Log.i(LOG_TAG,"I am here");
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getApplicationContext(),trackSelector,loadControl);
            Log.i(LOG_TAG,"I am here2");
            mPlayerView.setPlayer(mExoPlayer);
            Log.i(LOG_TAG,"I am here3");
            String userAgent = Util.getUserAgent(this,"Baking Time");
            MediaSource mediaSource = new ExtractorMediaSource(videoURL,new DefaultDataSourceFactory(this,userAgent),
                    new DefaultExtractorsFactory(),null,null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer=null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }
}
