package com.example.android.bakingtime;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.android.bakingtime.MainActivity.KEY_RECIPE;

/**
 * Created by mihirnewalkar on 9/16/17.
 */

public class RecipeActivity extends AppCompatActivity {

    public static final String LOG_TAG = RecipeActivity.class.getName();

    private String id = "";
    private String ingredients = "";
    private ArrayList<RecipeSteps> recipeSteps = new ArrayList<>();
    private RecipeStepsAdapter mRecipeStepsAdapter;

    public static final String KEY_RECIPE_STEP="recipeStep";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Intent intent = getIntent();

//        final List <String> recipeStepsList = new ArrayList<String>();

        if (intent != null) {

            Recipes currentRecipe = intent.getParcelableExtra(KEY_RECIPE);

            this.setTitle(currentRecipe.getName());

            ingredients = currentRecipe.getIngredients();
            recipeSteps = currentRecipe.getRecipeSteps();
            Log.i(LOG_TAG,"Recipe Step size "+String.valueOf(recipeSteps.size()));

            for (int i = 0; i<recipeSteps.size(); i++) {
                String stepId = currentRecipe.getRecipeSteps().get(i).getStepId();
                String stepShortDescription = currentRecipe.getRecipeSteps().get(i).getShortDescription();
                String stepDescription = currentRecipe.getRecipeSteps().get(i).getDescription();
                Log.i(LOG_TAG,"Recipe Steps: " + stepId + stepShortDescription + stepDescription);

//                recipeStepsList.add(stepId + ".  " + stepShortDescription);
            }

        }

        TextView titleTV = (TextView) findViewById(R.id.ingredients_tv);
        titleTV.setText(ingredients);

        ListView listView = (ListView) findViewById(R.id.list_view_steps);
        mRecipeStepsAdapter = new RecipeStepsAdapter(this,new ArrayList<RecipeSteps>());

//        mRecipeStepsAdapter.swapData(recipeSteps);
//        mRecipeStepsAdapter.addAll(recipeSteps);
        if (listView.getAdapter() == null) {
//            mRecipeStepsAdapter = new RecipeStepsAdapter(this, new ArrayList<RecipeSteps>());
            mRecipeStepsAdapter.swapData(recipeSteps);
            listView.setAdapter(mRecipeStepsAdapter);
            Log.i(LOG_TAG,"Listview null");
        }
        else {
//            mRecipeStepsAdapter.swapData(recipeSteps);
            ((RecipeStepsAdapter)listView.getAdapter()).swapData(recipeSteps);
            Log.i(LOG_TAG,"Listview not null");
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent (RecipeActivity.this, StepActivity.class);

                RecipeSteps currentStep = mRecipeStepsAdapter.getItem(i);
                Log.i(LOG_TAG,"Inside OnClick step");
                Log.i(LOG_TAG,"Description = " + currentStep.getStepId() + currentStep.getShortDescription() + currentStep.getDescription());

                intent.putExtra(KEY_RECIPE_STEP,currentStep);

                startActivity(intent);
            }
        });
    }
}
