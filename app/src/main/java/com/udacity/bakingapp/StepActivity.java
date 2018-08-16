package com.udacity.bakingapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.udacity.bakingapp.Model.Step;
import com.udacity.bakingapp.Utils.Keys;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StepActivity extends AppCompatActivity implements VideoFragment.OnFragmentInteractionListener {

    private String mVideoUri;
    private String mRecipeName;
    private ArrayList<Step> mStepArrayList = new ArrayList<>();
    private int mCurrentStep = 0;

    @BindView(R.id.btn_previous_step)
    Button btn_previous_step;

    @BindView(R.id.btn_next_step)
    Button btn_next_step;

    @BindView(R.id.fl_player_container)
    FrameLayout fl_player_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        ButterKnife.bind(this);

        if(savedInstanceState != null){
            mCurrentStep = savedInstanceState.getInt(Keys.CURRENT_STEP,0);
            mRecipeName = savedInstanceState.getString(Keys.RECIPE_NAME_KEY);
            if(mStepArrayList.isEmpty())
                mStepArrayList = savedInstanceState.getParcelableArrayList(Keys.STEPS_LIST);
        }else {
            Intent intent = getIntent();
            if(intent != null){
                mStepArrayList = getIntent().getParcelableArrayListExtra(Keys.STEPS_LIST);
                mCurrentStep = getIntent().getIntExtra(Keys.STEP_CLICKED,0);
                mRecipeName = getIntent().getStringExtra(Keys.RECIPE_NAME_KEY);
            }

            playVideo();
        }

        setActionBar();
        handleStepNavigationButtonsVisibility();
    }

    private void setActionBar() {
        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setTitle(mRecipeName);
    }

    public void playVideo(){

        Step step = mStepArrayList.get(mCurrentStep);
        mVideoUri = step.getVideoURL();

        Bundle bundle = new Bundle();

        VideoFragment videoFragment = new VideoFragment();
        bundle.putString(Keys.STEP_VIDEO_URL, mVideoUri);
        bundle.putString(Keys.STEP_VIDEO_DESCRIPTION, step.getDescription());
        bundle.putString(Keys.STEP_THUMBNAIL_URL, step.getThumbnailURL());
        videoFragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fl_player_container, videoFragment)
                .commit();

    }

    @OnClick(R.id.btn_previous_step)
    void previousStep(View view){
        mCurrentStep = mCurrentStep - 1;
        handleStepNavigationButtonsVisibility();
        playVideo();
    }

    @OnClick(R.id.btn_next_step)
    void nextStep(View view){
        mCurrentStep = mCurrentStep + 1;
        handleStepNavigationButtonsVisibility();
        playVideo();
    }

    private void handleStepNavigationButtonsVisibility() {
        if(mCurrentStep != 0){
            btn_previous_step.setVisibility(View.VISIBLE);
        }else{
            btn_previous_step.setVisibility(View.INVISIBLE);
        }

        if(mCurrentStep == mStepArrayList.size()-1){
            btn_next_step.setVisibility(View.INVISIBLE);
        }else{
            btn_next_step.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(Keys.STEPS_LIST, mStepArrayList);
        outState.putInt(Keys.CURRENT_STEP, mCurrentStep);
        outState.putString(Keys.RECIPE_NAME_KEY, mRecipeName);
    }


    @Override
    public void onFragmentInteraction(int currentStep) {
        mCurrentStep = currentStep;
    }
}
