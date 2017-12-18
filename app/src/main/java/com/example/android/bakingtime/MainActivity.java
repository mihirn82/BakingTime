package com.example.android.bakingtime;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.app.LoaderManager;
import android.net.Uri;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderCallbacks {

    /** URL for baking data from the baking app dataset */
    private static final String RECIPE_REQUEST_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    public static final String LOG_TAG = MainActivity.class.getName();

    /**
     * Constant value for the recipe loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int RECIPE_LOADER_ID = 1;

    /** Adapter for the list of movies */
    private RecipesAdapter mAdapter;

    public static final String KEY_RECIPE="recipe";

    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;

    private ProgressBar mProgress;

    private ListView recipesListView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find a reference to the {@link ListView} in the layout
        recipesListView = (ListView) findViewById(R.id.list_view_recipes);

        mProgress = (ProgressBar) findViewById(R.id.ProgressBar);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        recipesListView.setEmptyView(mEmptyStateTextView);

        // Create a new {@link ArrayAdapter} of recipes
        mAdapter = new RecipesAdapter(this, new ArrayList<Recipes>());

        recipesListView.setAdapter(mAdapter);

        recipesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent (MainActivity.this, RecipeActivity.class);

                Recipes currentRecipe = mAdapter.getItem(position);
                Log.i(LOG_TAG,"Id = " + currentRecipe.getId());
                Log.i(LOG_TAG,"Name = " + currentRecipe.getName());
                Log.i(LOG_TAG,"Ingredients = " + currentRecipe.getIngredients());

                for (int i = 0; i < currentRecipe.getRecipeSteps().size(); i++) {
                    Log.i(LOG_TAG,"StepId = " + currentRecipe.getRecipeSteps().get(i).getStepId());
                    Log.i(LOG_TAG,"Description = " + currentRecipe.getRecipeSteps().get(i).getShortDescription());
                }

                intent.putExtra(KEY_RECIPE,currentRecipe);

                startActivity(intent);
            }
        });


        // Get a reference to the LoaderManager, in order to interact with loaders.
        LoaderManager loaderManager = getLoaderManager();

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(RECIPE_LOADER_ID, null, this);
            Log.v(LOG_TAG,"Done initLoader");
        }
        else {
            mProgress.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_recipes);
        }
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG,"Inside onCreateLoader");
        Uri baseUri = Uri.parse(RECIPE_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        return new RecipesLoader(this, uriBuilder.toString());


    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        Log.v(LOG_TAG,"Inside onLoadFinished");

        mProgress.setVisibility(View.GONE);

        // Set empty state text to display "No recipes found."
        mEmptyStateTextView.setText(R.string.no_recipes);

        mAdapter.swapData((List<Recipes>) data);

    }

    @Override
    public void onLoaderReset(Loader loader) {

        Log.v(LOG_TAG,"Inside onLoaderReset");

        mAdapter.clear();
    }
}
