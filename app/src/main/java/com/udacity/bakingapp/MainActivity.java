package com.udacity.bakingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.Toast;

import com.udacity.bakingapp.Adapter.RecipeAdapter;
import com.udacity.bakingapp.Model.Recipe;
import com.udacity.bakingapp.Utils.Keys;
import com.udacity.bakingapp.Utils.Network;
import com.udacity.bakingapp.Utils.RecipeService;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.ListItemClickListener  {

    @BindView(R.id.rv_recipes)
    RecyclerView rv_recipes;

    private ArrayList<Recipe> mRecipes;
    private LinearLayoutManager mLayoutManager;
    private GridLayoutManager mLayoutManagerTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        getRecipes();
    }

    private void getRecipes() {

        new RecipesQueryTask(new RecipesQueryTaskCompleteListener())
                .execute();
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Recipe recipe = mRecipes.get(clickedItemIndex);
        Intent intent = new Intent(this, RecipeDetailsActivity.class);
        intent.putExtra(Keys.recipeKey, recipe);
        this.startActivity(intent);
    }

    public class RecipesQueryTaskCompleteListener implements AsyncTaskCompleteListener<ArrayList<Recipe>>
    {

        @Override
        public void onTaskComplete(ArrayList<Recipe> result) {
            mRecipes = result;
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
