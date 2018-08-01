package com.udacity.bakingapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.udacity.bakingapp.Model.Step;
import com.udacity.bakingapp.Utils.Keys;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StepActivity extends AppCompatActivity implements VideoFragment.OnFragmentInteractionListener {

    private String mVideoUri;
    ArrayList<Step> mStepArrayList = new ArrayList<>();
    private int currentStep = 0;

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

        Intent intent = getIntent();
        if(intent != null){
            mStepArrayList = getIntent().getParcelableArrayListExtra(Keys.stepsList);
            currentStep = getIntent().getIntExtra(Keys.stepClicked,0);
        }

        ButterKnife.bind(this);

        playVideo();
    }

    public void playVideo(){

        Step step = mStepArrayList.get(currentStep);
        mVideoUri = step.getVideoURL();

        fl_player_container.setVisibility(View.VISIBLE);
        String videoDescription = step.getDescription();

        Bundle bundle = new Bundle();

        VideoFragment videoFragment = new VideoFragment();
        bundle.putString(Keys.stepVideoURL, mVideoUri);
        bundle.putString(Keys.stepVideoDescription, videoDescription);
        videoFragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fl_player_container, videoFragment)
                .commit();

    }

    @OnClick(R.id.btn_previous_step)
    void previousStep(View view){
        currentStep = currentStep - 1;
        handleStepNavigationButtonsVisibility();
        playVideo();
    }

    @OnClick(R.id.btn_next_step)
    void nextStep(View view){
        currentStep = currentStep + 1;
        handleStepNavigationButtonsVisibility();
        playVideo();
    }

    private void handleStepNavigationButtonsVisibility() {
        if(currentStep != 0){
            btn_previous_step.setVisibility(View.VISIBLE);
        }else{
            btn_previous_step.setVisibility(View.INVISIBLE);
        }

        if(currentStep == mStepArrayList.size()-1){
            btn_next_step.setVisibility(View.INVISIBLE);
        }else{
            btn_next_step.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
