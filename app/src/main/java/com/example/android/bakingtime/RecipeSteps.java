package com.example.android.bakingtime;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mihirnewalkar on 9/16/17.
 */

public class RecipeSteps implements Parcelable{

    //Id of the recipeStep
    private String mStepId;

    //Name of the recipeStep.
    private String mShortDescription;

    /*
    * Create a new RecipeSteps object.
    *
    * @param stepId
    * @param shortDescription
    * */
    public RecipeSteps (String stepId, String shortDescription) {
        mStepId = stepId;
        mShortDescription = shortDescription;
    }

    protected RecipeSteps(Parcel in) {
        mStepId = in.readString();
        mShortDescription = in.readString();
    }

    public static final Creator<RecipeSteps> CREATOR = new Creator<RecipeSteps>() {
        @Override
        public RecipeSteps createFromParcel(Parcel in) {
            return new RecipeSteps(in);
        }

        @Override
        public RecipeSteps[] newArray(int size) {
            return new RecipeSteps[size];
        }
    };

    public String getStepId() {return mStepId; }
    public String getShortDescription() {return mShortDescription; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mStepId);
        parcel.writeString(mShortDescription);
    }
}
