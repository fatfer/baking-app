package com.udacity.bakingapp;

import android.provider.SyncStateContract;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.udacity.bakingapp.CustomAssertions.RecyclerViewItemCountAssertion;
import com.udacity.bakingapp.Utils.Keys;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
public class RecipeListTest {


    @Rule
    public IntentsTestRule<MainActivity> mActivityIntentTestRule = new IntentsTestRule<>(
            MainActivity.class);

    private IdlingResource mIdlingResource;



    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityIntentTestRule.getActivity().getIdlingResource();
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @Test
    public void shouldHave4RecipesInRecyclerView(){
        onView(withId(R.id.rv_recipes)).check(new RecyclerViewItemCountAssertion(4));
    }

    @Test
    public void shouldHAveRecipeKeyExtraOnRecipeClick(){
        onView(withId(R.id.rv_recipes)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        intended(hasExtraWithKey(Keys.RECIPE_KEY));
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }
}
