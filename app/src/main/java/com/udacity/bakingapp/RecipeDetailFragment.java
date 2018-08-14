package com.udacity.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.udacity.bakingapp.Adapter.StepAdapter;
import com.udacity.bakingapp.Model.Ingredient;
import com.udacity.bakingapp.Model.Recipe;
import com.udacity.bakingapp.Model.Step;
import com.udacity.bakingapp.Utils.Keys;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecipeDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecipeDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeDetailFragment extends Fragment implements StepAdapter.ListItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    Recipe mRecipe;

    @BindView(R.id.tv_ingredients)
    TextView tv_ingredients;

    @BindView(R.id.rv_steps)
    RecyclerView rv_steps;


    LinearLayoutManager mLayoutManager;
    List<Step> mSteps;

    public RecipeDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecipeDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipeDetailFragment newInstance(String param1, String param2) {
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View viewRoot = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        ButterKnife.bind(this, viewRoot);

        if (savedInstanceState != null) {
            mRecipe = savedInstanceState.getParcelable(Keys.RECIPE_KEY);
            mSteps = savedInstanceState.getParcelableArrayList(Keys.STEPS_LIST);
        }

        List<Ingredient> ingredients = mRecipe.getIngredients();

        for (Ingredient i: ingredients) {
            tv_ingredients.append(" - " + i.getIngredient() + " (" + i.getQuantity() + " " + i.getMeasure() + ")\n");
        }

        mLayoutManager = new LinearLayoutManager(container.getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rv_steps.setLayoutManager(mLayoutManager);
        rv_steps.setHasFixedSize(true);

        ArrayList<Step> stepArrayList = new ArrayList<>(mSteps);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(rv_steps.getContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(container.getContext().getDrawable(R.drawable.white_line));

        rv_steps.addItemDecoration(itemDecoration);

        StepAdapter adapter = new StepAdapter(stepArrayList, this);
        rv_steps.setAdapter(adapter);

        return viewRoot;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mRecipe =  savedInstanceState.getParcelable(Keys.RECIPE_KEY);
            mSteps = savedInstanceState.getParcelableArrayList(Keys.STEPS_LIST);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(Keys.RECIPE_KEY, mRecipe);
        outState.putParcelableArrayList(Keys.STEPS_LIST, new ArrayList<>(mSteps));
    }

    public void setRecipe(Recipe recipe){
        this.mRecipe = recipe;
    }

    public void setRecipeSteps(List<Step> steps){
        this.mSteps = steps;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        mListener.onFragmentInteraction(clickedItemIndex);
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(int id);
    }
}
