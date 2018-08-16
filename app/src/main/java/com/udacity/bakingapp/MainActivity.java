package com.udacity.bakingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.udacity.bakingapp.Adapter.RecipeAdapter;
import com.udacity.bakingapp.Model.Recipe;
import com.udacity.bakingapp.Utils.Keys;
import com.udacity.bakingapp.Utils.Network;
import com.udacity.bakingapp.Widget.RecipeWidgetService;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.ListItemClickListener {

    @BindView(R.id.rv_recipes)
    RecyclerView rv_recipes;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private ArrayList<Recipe> mRecipes;
    private LinearLayoutManager mLayoutManager;
    private GridLayoutManager mLayoutManagerTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if(Network.isInternetAvailable(this)) {
            getRecipes();
            progressBar.setVisibility(View.VISIBLE);
        }else{
            Toast.makeText(MainActivity.this, R.string.error_no_internet, Toast.LENGTH_SHORT).show();
        }
    }

    private void getRecipes() {

        new RecipesQueryTask(new RecipesQueryTaskCompleteListener())
                .execute();
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

        Recipe recipe = mRecipes.get(clickedItemIndex);
        saveRecipeToSharedPreferences(recipe);
        Intent intent = new Intent(this, RecipeDetailsActivity.class);
        intent.putExtra(Keys.RECIPE_KEY, recipe);
        this.startActivity(intent);
    }

    private void saveRecipeToSharedPreferences(Recipe recipe){

        SharedPreferences.Editor editor = getSharedPreferences(Keys.WIDGET_SHARED_PREFERENCES, MODE_PRIVATE).edit();

        Gson gson = new Gson();
        String recipeJson = gson.toJson(recipe);

        editor.putString(Keys.JSON_RESULT, recipeJson);
        editor.putString(Keys.RECIPE_NAME_KEY, recipe.getName());
        editor.apply();

        RecipeWidgetService.startActionShowRecipe(this);

    }

    public class RecipesQueryTaskCompleteListener implements AsyncTaskCompleteListener<ArrayList<Recipe>>
    {

        @Override
        public void onTaskComplete(ArrayList<Recipe> result) {
            mRecipes = result;
            progressBar.setVisibility(View.INVISIBLE);
            drawRecipes();
        }
    }

    private void drawRecipes(){

       if(findViewById(R.id.main_activity_tablet) == null) {
           mLayoutManager = new LinearLayoutManager(getApplicationContext());
           mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

           rv_recipes.setLayoutManager(mLayoutManager);
           rv_recipes.setHasFixedSize(true);

           RecipeAdapter adapter = new RecipeAdapter(mRecipes, this);
           rv_recipes.setAdapter(adapter);
       }else{
           int recipeWidth = 1200;
           mLayoutManagerTablet = new GridLayoutManager(getApplicationContext(),calculateBestSpanCount(recipeWidth));
           mLayoutManagerTablet.setOrientation(LinearLayoutManager.VERTICAL);

           rv_recipes.setLayoutManager(mLayoutManagerTablet);
           rv_recipes.setHasFixedSize(true);

           RecipeAdapter adapter = new RecipeAdapter(mRecipes, this);
           rv_recipes.setAdapter(adapter);
       }
    }

    private int calculateBestSpanCount(int posterWidth) {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float screenWidth = outMetrics.widthPixels;
        return Math.round(screenWidth / posterWidth);
    }

}
