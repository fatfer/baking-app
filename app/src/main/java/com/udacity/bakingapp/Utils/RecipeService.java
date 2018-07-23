package com.udacity.bakingapp.Utils;

import com.udacity.bakingapp.Model.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipeService {

    @GET("baking.json")
    Call<ArrayList<Recipe>> getRecipes();
}
