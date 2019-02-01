package com.example.android.bakingtime;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;

import static com.example.android.bakingtime.MainActivity.KEY_RECIPE;

/**
 * Created by mihirnewalkar on 9/16/17.
 */

public class RecipeActivity extends AppCompatActivity implements FragmentActionListener{

    public static final String LOG_TAG = RecipeActivity.class.getName();

    private Bundle bundle;

    public static final String KEY_RECIPE_STEP="recipeStep";
    public static final String myRecipe = "myRecipe";
    public static final String Ingredients = "Ingredients";
    public static final String RECIPE_NAME = "recipeName";

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    String selectedStep;

    Recipes currentRecipe;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        ButterKnife.bind(this);
        fragmentManager = getFragmentManager();

        Intent intent = getIntent();


        if (intent != null) {

            currentRecipe = intent.getParcelableExtra(KEY_RECIPE);

            bundle = new Bundle();
            bundle.putParcelable("RECIPE",currentRecipe);

            this.setTitle(currentRecipe.getName());

            SharedPreferences.Editor editor = getSharedPreferences(myRecipe,Context.MODE_PRIVATE).edit();
            editor.putString(RECIPE_NAME,currentRecipe.getName());
            editor.putString(Ingredients,currentRecipe.getIngredients());
            editor.commit();

            AppWidgetManager widgetManager = AppWidgetManager.getInstance(getApplicationContext());
            int[] appWidgetIds = widgetManager.getAppWidgetIds(new ComponentName(getApplicationContext(), BakingWidgetProvider.class));
            Intent updateIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE, null, getApplicationContext(), BakingWidgetProvider.class);
            updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            sendBroadcast(updateIntent);
        }

        if (findViewById(R.id.activity_recipe_portrait) != null){
            Log.i(LOG_TAG,"Orientation is Portrait");
            addRecipeFragment();
            Log.i(LOG_TAG,"Returned from Recipe Fragment Portrait");

        } else if (findViewById(R.id.activity_recipe_landscape) != null) {
            Log.i(LOG_TAG,"Orientation is Landscape");
            addRecipeFragment();
            Log.i(LOG_TAG,"Returned from Recipe Fragment Landscape");
            addStepFragment(selectedStep);
            Log.i(LOG_TAG,"Returned from Step Fragment Landscape");
        }
    }

    private void addRecipeFragment() {
        fragmentTransaction = fragmentManager.beginTransaction();
        RecipeFragment recipeFragment = new RecipeFragment();
        recipeFragment.setFragmentActionListener(this);

        recipeFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentContainer,recipeFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        Log.i(LOG_TAG,"I am committed for Recipe Fragment");
    }

    private void addStepFragment(String selectedStep) {
        fragmentTransaction = fragmentManager.beginTransaction();
//        StepFragment stepFragment = new StepFragment();
        StepFragment stepFragment = (StepFragment) fragmentManager.findFragmentByTag(StepFragment.LOG_TAG);
        if (stepFragment == null) {
            stepFragment = new StepFragment();
        }

        bundle.putString(FragmentActionListener.KEY_SELECTED_STEP,selectedStep);
        stepFragment.setArguments(bundle);

        if (findViewById(R.id.activity_recipe_portrait) != null) {
            fragmentTransaction.replace(R.id.fragmentContainer,stepFragment);
        } else {
            fragmentTransaction.replace(R.id.fragmentContainer2,stepFragment);
        }
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        Log.i(LOG_TAG,"I am committed for Step Fragment");
    }

    @Override
    public void onBackPressed() { //or use on menu item clicked
        super.onBackPressed();

        Log.i (LOG_TAG,"I pressed Back!!!");
    }

    @Override
    public void onStepSelected(String step) {
        selectedStep = step;
        addStepFragment(selectedStep);

        Log.i(LOG_TAG,"Inside callback method");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.i(LOG_TAG,"Orientation changed to landscape");
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.i(LOG_TAG,"Orientation changed to portrait");
        }
    }
}
