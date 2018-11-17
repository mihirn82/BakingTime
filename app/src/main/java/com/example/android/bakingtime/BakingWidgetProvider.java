package com.example.android.bakingtime;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.View;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class BakingWidgetProvider extends AppWidgetProvider {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {


        SharedPreferences preferences = context.getSharedPreferences(RecipeActivity.myRecipe,Context.MODE_PRIVATE);
        String ingredients = preferences.getString(RecipeActivity.Ingredients,"0");
        String recipeName  = preferences.getString(RecipeActivity.RECIPE_NAME,"0");

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_widget_provider);
        Intent intent = new Intent(context,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);


        if (ingredients == "0") {
            views.setViewVisibility(R.id.ingredients_list, View.INVISIBLE);
            views.setOnClickPendingIntent(R.id.empty_view_image,pendingIntent);

        } else {
            views.setViewVisibility(R.id.empty_view,View.INVISIBLE);
            views.setTextViewText(R.id.recipe_name_tv,recipeName);
            views.setTextViewText(R.id.ingredients_widget_tv,ingredients);
            views.setOnClickPendingIntent(R.id.ingredients_list,pendingIntent);
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

