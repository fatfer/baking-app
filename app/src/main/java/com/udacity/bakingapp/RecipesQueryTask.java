package com.udacity.bakingapp;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.udacity.bakingapp.Model.Recipe;
import com.udacity.bakingapp.Utils.Json;
import com.udacity.bakingapp.Utils.Network;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class RecipesQueryTask extends AsyncTask<URL, Void, String>{

    private final Context context;
    private final AsyncTaskCompleteListener<List<Recipe>> listener;

    RecipesQueryTask(Context context, AsyncTaskCompleteListener<List<Recipe>> listener){
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected String doInBackground(URL... params) {
        URL apiUrl = params[0];
        String searchResults = null;
        try {
            searchResults = Network.getResponseFromHttpUrl(apiUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return searchResults;
    }

    @Override
    protected void onPostExecute(String searchResults) {
        if (searchResults != null && !searchResults.equals("")) {
            try {
                List<Recipe> mRecipes = Json.parseRecipeJson(searchResults);
                listener.onTaskComplete(mRecipes);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
        }
    }

}
