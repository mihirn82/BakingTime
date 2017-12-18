package com.example.android.bakingtime;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by mihirnewalkar on 9/15/17.
 */

public class Recipes implements Parcelable{

    //Id of the recipe
    private String mId;

    //Name of the recipe.
    private String mName;

    //Servings of the recipe.
    private int mServings;

    //Ingredients required for the recipe.
    private String mIngredients;

    //Steps to be performed for the recipe.
    private ArrayList<RecipeSteps> mRecipeSteps;


    /*
    * Create a new Recipes object.
    *
    * @param id
    * @param name
    * @param servings
    * @param ingredients
    * @param recipeSteps
    * */
    public Recipes(String id, String name, int servings, String ingredients, ArrayList<RecipeSteps> recipeSteps) {
        mId = id;
        mName = name;
        mServings = servings;
        mIngredients = ingredients;
        mRecipeSteps = recipeSteps;
    }

    protected Recipes(Parcel in) {
        mId = in.readString();
        mName = in.readString();
        mServings = in.readInt();
        mIngredients = in.readString();
        mRecipeSteps = in.readArrayList(RecipeSteps.class.getClassLoader());
    }

    public static final Creator<Recipes> CREATOR = new Creator<Recipes>() {
        @Override
        public Recipes createFromParcel(Parcel in) {
            return new Recipes(in);
        }

        @Override
        public Recipes[] newArray(int size) {
            return new Recipes[size];
        }
    };

    public String getId() {return mId; }
    public String getName() {return mName; }
    public int getServings() {return mServings; }
    public String getIngredients() {return mIngredients; }
    public ArrayList<RecipeSteps> getRecipeSteps() {return mRecipeSteps; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mId);
        parcel.writeString(mName);
        parcel.writeInt(mServings);
        parcel.writeString(mIngredients);
        parcel.writeList(mRecipeSteps);
    }
}