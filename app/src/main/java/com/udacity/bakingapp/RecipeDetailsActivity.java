package com.udacity.bakingapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.udacity.bakingapp.Model.Recipe;
import com.udacity.bakingapp.Model.Step;
import com.udacity.bakingapp.Utils.Keys;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class RecipeDetailsActivity extends AppCompatActivity implements RecipeDetailFragment.OnFragmentInteractionListener, VideoFragment.OnFragmentInteractionListener {

    Recipe mRecipe;
    List<Step> mSteps;

    private String mVideoUri;
    private int mCurrentStep = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        mRecipe = (Recipe) (intent != null ? intent.getExtras()
                .getParcelable(Keys.RECIPE_KEY) : null);

        mSteps = mRecipe.getSteps();

        setActionBar();

        if(savedInstanceState == null) {
            RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
            recipeDetailFragment.setRecipe(mRecipe);
            recipeDetailFragment.setRecipeSteps(mRecipe.getSteps());

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fl_recipe_detail, recipeDetailFragment)
                    .commit();

            if(findViewById(R.id.recipe_details_activity_tablet) != null) {
                Bundle bundle = new Bundle();
                VideoFragment videoFragment = new VideoFragment();
                bundle.putString(Keys.STEP_VIDEO_URL, mSteps.get(0).getVideoURL());
                bundle.putString(Keys.STEP_VIDEO_DESCRIPTION, mSteps.get(1).getDescription());
                videoFragment.setArguments(bundle);

                fragmentManager.beginTransaction()
                        .replace(R.id.fl_player_container, videoFragment)
                        .commit();
            }
        }

    }


    private void setActionBar() {
        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setTitle(mRecipe.getName());
    }


    private void playVideo(){

        Step step = mSteps.get(mCurrentStep);
        mVideoUri = step.getVideoURL();
        String videoDescription = step.getDescription();

        Bundle bundle = new Bundle();

        VideoFragment videoFragment = new VideoFragment();
        bundle.putString(Keys.STEP_VIDEO_URL, mVideoUri);
        bundle.putString(Keys.STEP_VIDEO_DESCRIPTION, videoDescription);
        videoFragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fl_player_container, videoFragment)
                .commit();

    }

    @Override
    public void onFragmentInteraction(int clickedItemIndex) {
        if(findViewById(R.id.recipe_details_activity_tablet) != null) {
            mCurrentStep = clickedItemIndex;
            playVideo();
        }else {
            Intent intent = new Intent(this, StepActivity.class);
            intent.putParcelableArrayListExtra(Keys.STEPS_LIST, new ArrayList<>(mSteps));
            intent.putExtra(Keys.STEP_CLICKED, clickedItemIndex);
            intent.putExtra(Keys.RECIPE_NAME_KEY, mRecipe.getName());
            this.startActivity(intent);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
