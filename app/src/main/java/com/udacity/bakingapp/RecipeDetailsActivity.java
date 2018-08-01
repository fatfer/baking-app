package com.udacity.bakingapp;

import android.content.Intent;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.udacity.bakingapp.Adapter.RecipeAdapter;
import com.udacity.bakingapp.Adapter.StepAdapter;
import com.udacity.bakingapp.Model.Ingredient;
import com.udacity.bakingapp.Model.Recipe;
import com.udacity.bakingapp.Model.Step;
import com.udacity.bakingapp.Utils.Keys;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecipeDetailsActivity extends AppCompatActivity implements StepAdapter.ListItemClickListener {

    Recipe mRecipe;
    @BindView(R.id.tv_ingredients)
    TextView tv_ingredients;

    @BindView(R.id.rv_steps)
    RecyclerView rv_steps;

    LinearLayoutManager mLayoutManager;
    List<Step> mSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        mRecipe = (Recipe) (intent != null ? intent.getExtras()
                .getParcelable(Keys.recipeKey) : null);

        setActionBar();

        List<Ingredient> ingredients = mRecipe.getIngredients();

        for (Ingredient i: ingredients) {
            tv_ingredients.append(" - " + i.getIngredient() + " (" + i.getQuantity() + " " + i.getMeasure() + ")\n");
        }

        mSteps = mRecipe.getSteps();

        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rv_steps.setLayoutManager(mLayoutManager);
        rv_steps.setHasFixedSize(true);

        ArrayList<Step> stepArrayList = new ArrayList<>(mSteps);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(rv_steps.getContext(),DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(getDrawable(R.drawable.white_line));

        rv_steps.addItemDecoration(itemDecoration);

        StepAdapter adapter = new StepAdapter(stepArrayList, this);
        rv_steps.setAdapter(adapter);

    }


    private void setActionBar() {
        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setTitle(mRecipe.getName());
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

        Step step = mSteps.get(clickedItemIndex);
        Intent intent = new Intent(this, StepActivity.class);
        intent.putParcelableArrayListExtra(Keys.stepsList, new ArrayList<>(mSteps));
        intent.putExtra(Keys.stepClicked, clickedItemIndex);
        this.startActivity(intent);
    }

}
