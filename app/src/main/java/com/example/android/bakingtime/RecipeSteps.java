package com.example.android.bakingtime;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mihirnewalkar on 9/16/17.
 */

public class RecipeSteps implements Parcelable{

    //Id of the recipeStep
    private String mStepId;

    //Short Description of the recipeStep.
    private String mShortDescription;

    //Description of the recipeStep.
    private String mDescription;

    //VideoURL of the recipeStep.
    private String mVideoURL;

    //Thumbnail url of the recipeStep.
    private String mThumbnailURL;

    /*
    * Create a new RecipeSteps object.
    *
    * @param stepId
    * @param shortDescription
    * @param description
    * @param videoURL
    * @param thumbnailURL
    * */
    public RecipeSteps (String stepId, String shortDescription, String description, String videoURL, String thumbnailURL) {
        mStepId = stepId;
        mShortDescription = shortDescription;
        mDescription = description;
        mVideoURL = videoURL;
        mThumbnailURL = thumbnailURL;
    }

    protected RecipeSteps(Parcel in) {
        mStepId = in.readString();
        mShortDescription = in.readString();
        mDescription = in.readString();
        mVideoURL = in.readString();
        mThumbnailURL = in.readString();
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
    public String getDescription() {return mDescription; }
    public String getVideoURL() {return mVideoURL; }
    public String getThumbnailURL() {return mThumbnailURL; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mStepId);
        parcel.writeString(mShortDescription);
        parcel.writeString(mDescription);
        parcel.writeString(mVideoURL);
        parcel.writeString(mThumbnailURL);
    }
}
