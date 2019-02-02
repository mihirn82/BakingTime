package com.example.android.bakingtime;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.IdRes;
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
    private FrameLayout fragmentContainer2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        ButterKnife.bind(this);
        fragmentManager = getFragmentManager();
        fragmentContainer2 = findViewById(R.id.fragmentContainer2);

        Intent intent = getIntent();


        if (intent != null) {

            currentRecipe = intent.getParcelableExtra(KEY_RECIPE);

            bundle = new Bundle();
            bundle.putParcelable("RECIPE",currentRecipe);

            this.setTitle(currentRecipe.getName());

            getSharedPreferences(myRecipe,Context.MODE_PRIVATE).edit()
                    .putString(RECIPE_NAME,currentRecipe.getName())
                    .putString(Ingredients,currentRecipe.getIngredients())
                    .apply();

            AppWidgetManager widgetManager = AppWidgetManager.getInstance(getApplicationContext());
            int[] appWidgetIds = widgetManager.getAppWidgetIds(new ComponentName(getApplicationContext(), BakingWidgetProvider.class));
            Intent updateIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE, null, getApplicationContext(), BakingWidgetProvider.class);
            updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            sendBroadcast(updateIntent);
        }

        addRecipeFragment();
        addStepFragment(selectedStep, true);
    }

    private void addRecipeFragment() {
        fragmentTransaction = fragmentManager.beginTransaction();
        RecipeFragment recipeFragment = (RecipeFragment) fragmentManager.findFragmentByTag(RecipeFragment.LOG_TAG);
        if (recipeFragment == null) {
            recipeFragment = new RecipeFragment();

            recipeFragment.setArguments(bundle);
        }

        recipeFragment.setFragmentActionListener(this);

        startFragment(recipeFragment, fragmentManager, R.id.fragmentContainer, RecipeFragment.LOG_TAG);

        Log.i(LOG_TAG,"I am committed for Recipe Fragment");
    }

    private void addStepFragment(String selectedStep, boolean isRestore) {
        StepFragment stepFragment = (StepFragment) fragmentManager.findFragmentByTag(StepFragment.LOG_TAG);
        if (stepFragment == null) {
            if (isRestore) {
                return;
            }
            stepFragment = new StepFragment();
            stepFragment.setArguments(bundle);
        }

        bundle.putString(FragmentActionListener.KEY_SELECTED_STEP, selectedStep);

        fragmentContainer2.setVisibility(View.VISIBLE);

        startFragment(stepFragment, fragmentManager, R.id.fragmentContainer2, StepFragment.LOG_TAG);

        Log.i(LOG_TAG,"I am committed for Step Fragment");
    }

    @Override
    public void onBackPressed() { //or use on menu item clicked
        super.onBackPressed();

        fragmentManager.popBackStack(StepFragment.LOG_TAG,FragmentManager.POP_BACK_STACK_INCLUSIVE);

        if (isPortrait() && fragmentContainer2.getVisibility() == View.VISIBLE) {
            fragmentContainer2.setVisibility(View.GONE);
        }

        if (fragmentManager.getBackStackEntryCount() == 0) {
            finish();
        }

        Log.i (LOG_TAG,"I pressed Back!!!");
    }

    @Override
    public void onStepSelected(String step) {
        selectedStep = step;
        addStepFragment(selectedStep,false);

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

    private void startFragment(Fragment fragment, FragmentManager fragmentManager, @IdRes int fragmentContainer, String tag) {
        fragmentManager.beginTransaction()
                .remove(fragment)
                .add(fragmentContainer, fragment, tag)
                .addToBackStack(tag)
                .commit();
    }

    private boolean isPortrait() {
        return findViewById(R.id.activity_recipe_portrait) != null;
    }
}
