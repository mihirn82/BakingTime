package com.example.android.bakingtime;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mihirnewalkar on 9/16/17.
 */

public class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getName();
    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Returns new URL object from the given string URL.
     */
    public static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    public static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return an {@link Recipes} object by parsing out information
     * about the first news from the input newsJSON string.
     */
    private static List<Recipes> extractFeatureFromJson(String recipesJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(recipesJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding recipes to
        List<Recipes> recipes = new ArrayList<>();

//        List<RecipeSteps> recipeSteps = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.

        try {
            JSONArray recipesArray = new JSONArray(recipesJSON);

            // If there are results in the features array
            for (int i = 0; i < recipesArray.length(); i++) {

                // Extract out the first feature
                JSONObject currentRecipes = recipesArray.getJSONObject(i);

                // Extract recipe details
                String id = currentRecipes.getString("id");
                String name = currentRecipes.getString("name");
                int servings = currentRecipes.getInt("servings");

                String ingredientString = "";

                JSONArray ingredientsArray = currentRecipes.getJSONArray("ingredients");
                for (int j = 0; j < ingredientsArray.length(); j++) {
                    JSONObject currentIngredients = ingredientsArray.getJSONObject(j);

                    //Extract ingredient details
                    String quantity = currentIngredients.getString("quantity");
                    String measure = currentIngredients.getString("measure");
                    String ingredient = currentIngredients.getString("ingredient");

                    ingredientString = ingredientString + "\u2022 " + ingredient + " (" + quantity + " " + measure + ")\n";
//                    Log.i(LOG_TAG,name + ": " + ingredientString);
                }

                JSONArray stepsArray = currentRecipes.getJSONArray("steps");

                String stepId = "";
                String shortDescription = "";
                String description = "";
                String videoURL;

                List<RecipeSteps> recipeSteps = new ArrayList<>();

                for (int k = 0; k < stepsArray.length(); k++) {
                    JSONObject currentSteps = stepsArray.getJSONObject(k);

                    //Extract steps details
                    stepId = currentSteps.getString("id");
                    shortDescription = currentSteps.getString("shortDescription");
                    description = currentSteps.getString("description");
                    videoURL = currentSteps.getString("videoURL");

//                    Log.i(LOG_TAG,name + ": " + stepId + ": " + shortDescription);

                    RecipeSteps recipeSteps1 = new RecipeSteps(stepId,shortDescription,description,videoURL);
                    recipeSteps.add(recipeSteps1);
                }

                Recipes recipes1 = new Recipes(id,name,servings,ingredientString,new ArrayList<RecipeSteps>(recipeSteps));
                recipes.add(recipes1);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG,   "Problem parsing the news JSON results", e);
        }
        return recipes;
    }

    /**
     * Query the API dataset and return a list of {@link Recipes} objects.
     */
    public static List<Recipes> fetchRecipesData(String requestUrl) {

        Log.v(LOG_TAG, "Inside fetchRecipesData");

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Movies}
        List<Recipes> recipes = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link Movies}
        return recipes;
    }
}
