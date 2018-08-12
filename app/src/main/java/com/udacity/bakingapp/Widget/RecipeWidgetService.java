package com.udacity.bakingapp.Widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.udacity.bakingapp.Model.Ingredient;
import com.udacity.bakingapp.Model.Recipe;
import com.udacity.bakingapp.Utils.Keys;

import java.util.List;


public class RecipeWidgetService extends IntentService {

    public static final String ACTION_SHOW_RECIPE =
            "com.udacity.bakingapp.Widget.action.water_plants";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public RecipeWidgetService(String name) {
        super(name);
    }

    public RecipeWidgetService(){
        super("RecipeWidgetService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SHOW_RECIPE.equals(action)) {
                handleActionShowRecipe();
            }
        }
    }

    private void handleActionShowRecipe() {
        SharedPreferences sharedpreferences =
                getSharedPreferences(Keys.WIDGET_SHARED_PREFERENCES,MODE_PRIVATE);
        String jsonRecipe = sharedpreferences.getString(Keys.JSON_RESULT, "");
        StringBuilder stringBuilder = new StringBuilder();
        Gson gson = new Gson();
        Recipe recipe = gson.fromJson(jsonRecipe, Recipe.class);
        List<Ingredient> ingredientList = recipe.getIngredients();
        for(Ingredient ingredient : ingredientList){
            String quantity = String.valueOf(ingredient.getQuantity());
            String measure = ingredient.getMeasure();
            String ingredientName = ingredient.getIngredient();
            String line = quantity + " " + measure + " " + ingredientName;
            stringBuilder.append( line + "\n");
        }
        String ingredientsString = stringBuilder.toString();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidget.class));
        RecipeWidget.updateWidgetRecipe(this, appWidgetManager, appWidgetIds, ingredientsString);
    }

    public static void startActionShowRecipe(Context context) {
        Intent intent = new Intent(context, RecipeWidgetService.class);
        intent.setAction(ACTION_SHOW_RECIPE);
        context.startService(intent);
    }

}
