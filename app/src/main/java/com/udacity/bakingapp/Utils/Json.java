package com.udacity.bakingapp.Utils;

import com.udacity.bakingapp.Model.Ingredient;
import com.udacity.bakingapp.Model.Recipe;
import com.udacity.bakingapp.Model.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Json {
    private static final String JSON_ID_KEY = "id";
    private static final String JSON_NAME_KEY = "name";
    private static final String JSON_INGREDIENTS_KEY = "ingredients";
    private static final String JSON_QUANTITY_KEY = "quantity";
    private static final String JSON_MEASURE_KEY = "measure";
    private static final String JSON_INGREDIENT_KEY = "ingredient";
    private static final String JSON_STEPS_KEY = "steps";
    private static final String JSON_SHORT_DESCRIPTION_KEY = "shortDescription";
    private static final String JSON_DESCRIPTION_KEY = "description";
    private static final String JSON_VIDEO_URL_KEY = "videoURL";
    private static final String JSON_THUMBNAIL_URL__KEY = "thumbnailURL";
    private static final String JSON_SERVINGS_KEY = "servings";
    private static final String JSON_IMAGE_KEY = "image";


    public static List<Recipe> parseRecipeJson(String json) throws JSONException {
        List<Recipe> recipes = new ArrayList<>();
        JSONArray results = new JSONArray(json);

        for( int i = 0; i < results.length(); i++) {
            JSONObject result = results.getJSONObject(i);
            Recipe recipe = new Recipe();

            JSONArray ingredients = result.getJSONArray(JSON_INGREDIENTS_KEY);
            List<Ingredient> ingredientList = getIngredients(ingredients);

            JSONArray steps = result.getJSONArray(JSON_STEPS_KEY);
            List<Step> stepList = getSteps(steps);

            recipe.setIngredients(ingredientList);
            recipe.setSteps(stepList);

            recipe.setId(result.optInt(JSON_ID_KEY));
            recipe.setImage(result.optString(JSON_IMAGE_KEY));
            recipe.setServings(result.optInt(JSON_SERVINGS_KEY));
            recipe.setName(result.optString(JSON_NAME_KEY));

            recipes.add(recipe);
        }

        return recipes;
    }

    private static List<Step> getSteps(JSONArray steps) throws JSONException {
        List<Step> stepList = new ArrayList<>();

        for( int i = 0; i < steps.length(); i++){
            JSONObject stepJSON = steps.getJSONObject(i);
            Step step = getStep(stepJSON);
            stepList.add(step);
        }

        return stepList;
    }

    private static Step getStep(JSONObject stepJSON) {

        Step step = new Step();

        step.setId(stepJSON.optInt(JSON_ID_KEY));
        step.setDescription(stepJSON.optString(JSON_DESCRIPTION_KEY));
        step.setShortDescription(stepJSON.optString(JSON_SHORT_DESCRIPTION_KEY));
        step.setThumbnailURL(stepJSON.optString(JSON_THUMBNAIL_URL__KEY));
        step.setVideoURL(stepJSON.optString(JSON_VIDEO_URL_KEY));

        return step;
    }

    private static List<Ingredient> getIngredients(JSONArray ingredients) throws JSONException {
        List<Ingredient> ingredientList = new ArrayList<>();

        for( int i = 0; i < ingredients.length(); i++){
            JSONObject ingredientJSON = ingredients.getJSONObject(i);
            Ingredient ingredient = getIngredient(ingredientJSON);
            ingredientList.add(ingredient);
        }

        return ingredientList;
    }

    private static Ingredient getIngredient(JSONObject ingredientJSON) {

        Ingredient ingredient = new Ingredient();

        ingredient.setIngredient(ingredientJSON.optString(JSON_INGREDIENT_KEY));
        ingredient.setMeasure(ingredientJSON.optString(JSON_MEASURE_KEY));
        ingredient.setQuantity(ingredientJSON.optInt(JSON_QUANTITY_KEY));

        return ingredient;
    }


   /* @NonNull
    private static Recipe getRecipe(JSONObject result) throws JSONException {
        Recipe recipe = new Recipe();

        recipe.setmPosterPath(result.optString(JSON_POSTER_PATH_KEY));
        recipe.setmAdult(result.optBoolean(JSON_ADULT_KEY));
        recipe.setOverview(result.optString(JSON_OVERVIEW_KEY));
        recipe.setmReleaseDate(result.optString(JSON_RELEASE_DATE_KEY));
        recipe.setmGenreIDs(jsonArrayToList(result.optJSONArray(JSON_GENRE_IDS_KEY)));
        recipe.setId(result.optString(JSON_ID_KEY));
        recipe.setOriginalTitle(result.optString(JSON_ORIGINAL_TITLE_KEY));
        recipe.setOriginalLanguage(result.optString(JSON_ORIGINAL_LANGUAGE_KEY));
        recipe.setTitle(result.optString(JSON_TITLE_KEY));
        recipe.setmBackdropPath(result.optString(JSON_BACKDROP_PATH_KEY));
        recipe.setmPopularity(result.optInt(JSON_POPULARITY_KEY));
        recipe.setmVoteCount(result.getInt(JSON_VOTE_COUNT_KEY));
        recipe.setVideo(result.optBoolean(JSON_VIDEO_KEY));
        recipe.setmVoteAverage(result.optInt(JSON_VOTE_AVERAGE_KEY));

        return recipe;
    }
    */


    private static List<String> jsonArrayToList(JSONArray array) throws JSONException {
        ArrayList<String> list = new ArrayList<>();

        for( int i = 0; i < array.length(); i++){
            list.add(array.getString(i));
        }

        return list;
    }
}