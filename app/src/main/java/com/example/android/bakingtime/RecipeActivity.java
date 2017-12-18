package com.example.android.bakingtime;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.bakingtime.MainActivity.KEY_RECIPE;

/**
 * Created by mihirnewalkar on 9/16/17.
 */

public class RecipeActivity extends AppCompatActivity {

    public static final String LOG_TAG = RecipeActivity.class.getName();

    private String id = "";
    private String ingredients = "";
    private List<RecipeSteps> recipeSteps = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Intent intent = getIntent();

        List <String> recipeStepsList = new ArrayList<String>();

        if (intent != null) {

            Recipes currentRecipe = intent.getParcelableExtra(KEY_RECIPE);

            this.setTitle(currentRecipe.getName());

            ingredients = currentRecipe.getIngredients();
            recipeSteps = currentRecipe.getRecipeSteps();

            for (int i = 0; i<recipeSteps.size(); i++) {
                String stepId = currentRecipe.getRecipeSteps().get(i).getStepId();
                String stepDescription = currentRecipe.getRecipeSteps().get(i).getShortDescription();
                Log.i(LOG_TAG,"Recipe Steps: " + stepId + stepDescription);

                recipeStepsList.add(stepId + ".  " + stepDescription);
            }

        }

        TextView titleTV = (TextView) findViewById(R.id.ingredients_tv);
        titleTV.setText(ingredients);

        ListView listView = (ListView) findViewById(R.id.list_view_steps);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, recipeStepsList);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent (RecipeActivity.this, StepActivity.class);

                startActivity(intent);
            }
        });
    }
}
