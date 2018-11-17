package com.example.android.bakingtime;

public interface FragmentActionListener {

    String KEY_SELECTED_STEP = "KEY_SELECTED_STEP";

    void onStepSelected(String step);
}