package com.example.android.bakingtime;

import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by mihirnewalkar on 10/2/17.
 */

public class StepFragment extends Fragment {

//    public static final String LOG_TAG = StepFragment.class.getName();
    public static final String LOG_TAG = "StepFragment";

    private String stepId;
    private String shortDescription = "";
    private String description = "";
    private String videoURL = "";
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private RecipeSteps currentStep;
    private Recipes currentRecipe;
    private ArrayList<RecipeSteps> recipeSteps = new ArrayList<>();
    private RecipeStepsAdapter mRecipeStepsAdapter;
//    private String selectedStep;
    private String mSelectedStep;

    private MediaSource mMediaSource = null;
    private long mExoPlayerSeekState = -1L;

    public StepFragment() {
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_step,container,false);
        ButterKnife.bind(getActivity(),rootView);

        Log.i(LOG_TAG,"Inside StepFragment");

//        mPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.playerView);
        mPlayerView = rootView.findViewById(R.id.playerView);

        if (savedInstanceState != null) {
            currentStep = savedInstanceState.getParcelable("currentStep");
        } else {

//            currentRecipe = (Recipes) getArguments().getParcelable("RECIPE");
            currentRecipe = getArguments().getParcelable("RECIPE");

            Log.i(LOG_TAG,"Step number = "+ mSelectedStep);
            if (mSelectedStep == null) {
                mSelectedStep = getArguments().getString(FragmentActionListener.KEY_SELECTED_STEP,"0");
            }

            Log.i(LOG_TAG,"Step number = "+mSelectedStep);

            recipeSteps = currentRecipe.getRecipeSteps();

            mRecipeStepsAdapter = new RecipeStepsAdapter(getActivity(),new ArrayList<RecipeSteps>());
            mRecipeStepsAdapter.swapData(recipeSteps);
            currentStep = mRecipeStepsAdapter.getItem(Integer.parseInt(mSelectedStep));
        }

        stepId = currentStep.getStepId();
        shortDescription = currentStep.getShortDescription();
        description = currentStep.getDescription();
        videoURL = currentStep.getVideoURL();
        extractMediaSource(Uri.parse(videoURL));

        Log.i(LOG_TAG,description + " " + videoURL);

        Log.i(LOG_TAG,"Step ID = " + stepId);
//        initializePlayer(Uri.parse(videoURL));

//        TextView shortDescriptionTV = (TextView) rootView.findViewById(R.id.recipe_step_tv);
        TextView shortDescriptionTV = rootView.findViewById(R.id.recipe_step_tv);
        shortDescriptionTV.setText(shortDescription);

//        TextView descriptionTV = (TextView) rootView.findViewById(R.id.description_tv);
        TextView descriptionTV = rootView.findViewById(R.id.description_tv);
        descriptionTV.setText(description);

        if (getActivity().findViewById(R.id.activity_recipe_portrait) != null) {

//            Button previousButton = (Button) rootView.findViewById(R.id.previous_button);
//            Button nextButton = (Button) rootView.findViewById(R.id.next_button);
            Button previousButton = rootView.findViewById(R.id.previous_button);
            Button nextButton = rootView.findViewById(R.id.next_button);

            if (stepId.equals("0")) {
                previousButton.setClickable(false);
                previousButton.setEnabled(false);
            } else {
                previousButton.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View view) {
                        mSelectedStep = String.valueOf(Integer.parseInt(mSelectedStep) - 1);

                        currentStep = mRecipeStepsAdapter.getItem(Integer.parseInt(mSelectedStep));
                        Log.i(LOG_TAG,"Current Step =" + Integer.parseInt(mSelectedStep));

                        getFragmentManager().beginTransaction().detach(getActivity().getFragmentManager().findFragmentById(R.id.fragmentContainer)).
                                attach(getActivity().getFragmentManager().findFragmentById(R.id.fragmentContainer)).commit();
                    }
                });
            }

            Log.i (LOG_TAG,"Size = " + recipeSteps.size());

            if (stepId.equals (String.valueOf(recipeSteps.size() - 1))) {
                nextButton.setClickable(false);
                nextButton.setEnabled(false);
            } else {
                nextButton.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View view) {
                        mSelectedStep = String.valueOf(Integer.parseInt(mSelectedStep) + 1);

                        currentStep = mRecipeStepsAdapter.getItem(Integer.parseInt(mSelectedStep));
                        Log.i(LOG_TAG,"Current Step =" + Integer.parseInt(mSelectedStep));

                        getFragmentManager().beginTransaction().detach(getActivity().getFragmentManager().findFragmentById(R.id.fragmentContainer)).
                                attach(getActivity().getFragmentManager().findFragmentById(R.id.fragmentContainer)).commit();
                    }
                });
            }

        }

        return rootView;
    }

    private void extractMediaSource(Uri videoUrl) {
        if (mMediaSource == null) {
            String userAgent = Util.getUserAgent(getActivity(),"Baking Time");
            mMediaSource = new ExtractorMediaSource(videoUrl, new DefaultDataSourceFactory(getActivity(), userAgent),
                    new DefaultExtractorsFactory(), null, null);
        }
    }

    private void initializePlayer() {
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            Log.i(LOG_TAG,"I am here");
//            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity().getApplicationContext(),trackSelector,loadControl);
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity().getApplicationContext(), trackSelector, loadControl);
            Log.i(LOG_TAG,"I am here2");
            mPlayerView.setPlayer(mExoPlayer);
            Log.i(LOG_TAG,"I am here3");
//            String userAgent = Util.getUserAgent(getActivity(),"Baking Time");
//            MediaSource mediaSource = new ExtractorMediaSource(videoURL,new DefaultDataSourceFactory(getActivity(),userAgent),
//                    new DefaultExtractorsFactory(),null,null);
//            mExoPlayer.prepare(mediaSource);
            mExoPlayer.prepare(mMediaSource, false, true);
            if (mExoPlayerSeekState > -1) {
                mExoPlayer.seekTo(mExoPlayerSeekState);
                mExoPlayerSeekState = -1;
            }
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        mExoPlayerSeekState = mExoPlayer.getCurrentPosition();
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer=null;
    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        releasePlayer();
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(LOG_TAG,"Orientation change onSaveInstanceState");
        outState.putParcelable("currentStep",currentStep);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(LOG_TAG,"On Resume");
        if (Util.SDK_INT <= 23 || mExoPlayer == null) {
            initializePlayer();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(LOG_TAG,"On Start");
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(LOG_TAG,"On Pause");
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(LOG_TAG,"On Stop");
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }
}
