package com.example.android.bakingtime;

import android.app.Fragment;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RecipeFragment extends Fragment {

    public static final String LOG_TAG = RecipeFragment.class.getName();

    private String ingredients = "";
    private ArrayList<RecipeSteps> recipeSteps = new ArrayList<>();
    private RecipeStepsAdapter mRecipeStepsAdapter;
    private RecipeSteps currentStep;

    FragmentActionListener fragmentActionListener;
    Recipes currentRecipe;

    @BindView(R.id.ingredients_tv) TextView titleTV;
    @BindView(R.id.list_view_steps) ListView listView;
    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe,container,false);

        unbinder = ButterKnife.bind(getActivity(),rootView);

        Log.i(LOG_TAG,"Inside RecipeFragment");

        if (savedInstanceState != null) {
            currentRecipe = savedInstanceState.getParcelable("currentRecipe");
        } else {
            currentRecipe = (Recipes) getArguments().getParcelable("RECIPE");
        }

        ingredients = currentRecipe.getIngredients();
        recipeSteps = currentRecipe.getRecipeSteps();
        Log.i(LOG_TAG,"Recipe Step size "+String.valueOf(recipeSteps.size()));

        for (int i = 0; i<recipeSteps.size(); i++) {
            String stepId = currentRecipe.getRecipeSteps().get(i).getStepId();
            String stepShortDescription = currentRecipe.getRecipeSteps().get(i).getShortDescription();
            String stepDescription = currentRecipe.getRecipeSteps().get(i).getDescription();
            Log.i(LOG_TAG,"Recipe Steps: " + stepId + stepShortDescription + stepDescription);
        }


        TextView titleTV = (TextView) rootView.findViewById(R.id.ingredients_tv);
        titleTV.setText(ingredients);

        ListView listView = (ListView) rootView.findViewById(R.id.list_view_steps);
        mRecipeStepsAdapter = new RecipeStepsAdapter(getActivity(),new ArrayList<RecipeSteps>());

        if (listView.getAdapter() == null) {
            mRecipeStepsAdapter.swapData(recipeSteps);
            listView.setAdapter(mRecipeStepsAdapter);
            Log.i(LOG_TAG,"Listview null");
        }
        else {
            ((RecipeStepsAdapter)listView.getAdapter()).swapData(recipeSteps);
            Log.i(LOG_TAG,"Listview not null");
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                currentStep = mRecipeStepsAdapter.getItem(i);
                Log.i(LOG_TAG,"Inside OnClick step");
                Log.i(LOG_TAG,"Description = " + currentStep.getStepId() + currentStep.getShortDescription() + currentStep.getDescription());

                if (getActivity().findViewById(R.id.activity_recipe_portrait) != null) {
                    Log.i(LOG_TAG,"I am portrait");

                    if (fragmentActionListener != null) {
                        fragmentActionListener.onStepSelected(String.valueOf(i));
                    }

                } else {
                    Log.i(LOG_TAG,"I am landscape");
                    Log.i(LOG_TAG,"Value of i= "+i);
                    if (fragmentActionListener != null) {
                        fragmentActionListener.onStepSelected(String.valueOf(i));
                    }
                }
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            fragmentActionListener = (RecipeActivity)getActivity();
        }
    }

    public void setFragmentActionListener(FragmentActionListener fragmentActionListener) {
        this.fragmentActionListener = fragmentActionListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(LOG_TAG,"Orientation change onSaveInstanceState");
        outState.putParcelable("currentRecipe",currentRecipe);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}