package com.udacity.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.udacity.bakingapp.Model.Recipe;
import com.udacity.bakingapp.Utils.Network;

import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Recipe> mRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getRecipes();
    }

    private void getRecipes() {
        URL recipesUrl = Network.buildMovieReviewsUrl();
        new RecipesQueryTask(this, new RecipesQueryTaskCompleteListener())
                .execute(recipesUrl);
    }

    public class RecipesQueryTaskCompleteListener implements AsyncTaskCompleteListener<List<Recipe>>
    {

        @Override
        public void onTaskComplete(List<Recipe> result) {
            mRecipes = result;
        }
    }
}
