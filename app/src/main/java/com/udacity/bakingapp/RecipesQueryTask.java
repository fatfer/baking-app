package com.udacity.bakingapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.udacity.bakingapp.Model.Recipe;
import com.udacity.bakingapp.Utils.Json;
import com.udacity.bakingapp.Utils.RecipeService;
import com.udacity.bakingapp.Utils.RecipeServiceClient;

import org.json.JSONException;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipesQueryTask extends AsyncTask<Void, Void, Void>{

    private final AsyncTaskCompleteListener<List<Recipe>> listener;
    private RecipeService mRecipeService;

    RecipesQueryTask(AsyncTaskCompleteListener<List<Recipe>> listener){
        this.listener = listener;
        mRecipeService = new RecipeServiceClient().mRecipeService;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        getRecipes();
        return null;
    }


    private void getRecipes() {
        Call<ArrayList<Recipe>> call = mRecipeService.getRecipes();

        call.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {

                ArrayList<Recipe> recipes = response.body();
                listener.onTaskComplete(recipes);

            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                Log.d("ERROR", t.toString());
            }
        });
    }


}
