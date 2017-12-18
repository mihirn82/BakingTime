package com.example.android.bakingtime;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by mihirnewalkar on 9/16/17.
 */

public class RecipesLoader extends AsyncTaskLoader<List<Recipes>>{

    /** Tag for log messages */
    private static final String LOG_TAG = RecipesLoader.class.getName();

    /** Query URL */
    private String mUrl;


    public RecipesLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        Log.v(LOG_TAG,"Inside onStartLoading");
        forceLoad();
    }

    @Override
    public List<Recipes> loadInBackground() {
        Log.v(LOG_TAG,"Inside loadInBackground");
        // Don't perform the request if there are no URLs, or the first URL is null.
        if (mUrl == null) {
            return null;
        }

        List<Recipes> recipes = QueryUtils.fetchRecipesData(mUrl);
        return recipes;
    }
}
