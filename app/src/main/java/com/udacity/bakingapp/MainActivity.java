package com.udacity.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.udacity.bakingapp.Model.Recipe;
import com.udacity.bakingapp.Utils.Network;
import com.udacity.bakingapp.Utils.RecipeService;

import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Recipe> mRecipes;
    RecipeService mRecipeService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getRecipes();
    }

    private void getRecipes() {

        new RecipesQueryTask(new RecipesQueryTaskCompleteListener())
                .execute();
    }

    public class RecipesQueryTaskCompleteListener implements AsyncTaskCompleteListener<List<Recipe>>
    {

        @Override
        public void onTaskComplete(List<Recipe> result) {
            mRecipes = result;
        }
    }
}
