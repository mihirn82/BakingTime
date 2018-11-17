package com.example.android.bakingtime;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.anything;

@RunWith(JUnit4.class)
public class MainActivityScreenTest {

//    public static final String RECIPE_NAME = "Nutella Pie";

    @Rule
    public ActivityTestRule<MainActivity>mAcitivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void onClickCardViewItem_OpensRecipeActivity() {

        onData(anything()).inAdapterView(withId(R.id.list_view_recipes)).atPosition(0).perform(click());

//        onView(withId(R.id.list_view_recipes)).check(matches(withText(containsString(RECIPE_NAME))));
    }
}
