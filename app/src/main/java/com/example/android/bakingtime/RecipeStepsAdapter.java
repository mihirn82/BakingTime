package com.example.android.bakingtime;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RecipeStepsAdapter extends ArrayAdapter<RecipeSteps> {

    private static final String LOG_TAG = RecipeStepsAdapter.class.getSimpleName();

    private List<RecipeSteps> mRecipeStepsList;


    public RecipeStepsAdapter(Activity context, ArrayList<RecipeSteps> recipeSteps) {
        super(context, 0,recipeSteps);
        mRecipeStepsList = recipeSteps;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_steps,parent,false);

            RecipeSteps currentStep = getItem(position);

            TextView stepNumberView = (TextView)listItemView.findViewById(R.id.recipe_step_number_tv);
            String sStepNumber = currentStep.getStepId();

            stepNumberView.setText(sStepNumber);

            TextView shortDescriptionView = (TextView)listItemView.findViewById(R.id.recipe_step_short_desc_tv);
            String sShortDescription = currentStep.getShortDescription();
            Log.i(LOG_TAG,sStepNumber + ". " + sShortDescription);
            shortDescriptionView.setText(sShortDescription);

        }
        return listItemView;
    }

    void swapData(List<RecipeSteps>recipeSteps){
        mRecipeStepsList.clear();
        mRecipeStepsList.addAll(recipeSteps);
        Log.i(LOG_TAG,recipeSteps.toString());
        notifyDataSetChanged();
    }
}
