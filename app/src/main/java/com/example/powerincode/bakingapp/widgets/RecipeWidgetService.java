package com.example.powerincode.bakingapp.widgets;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.example.powerincode.bakingapp.network.models.Recipe;

import java.util.ArrayList;

/**
 * Created by powerman23rus on 10.12.17.
 * Enjoy ;)
 */

public class RecipeWidgetService extends IntentService {
    public static final String ACTION_UPDATE_RECIPE = "ACTION_UPDATE_RECIPE";
    public static final String BUNDLE_RECIPE = "BUNDLE_RECIPE";

    public static void startUpdateRecipeService(Context context, @Nullable Recipe recipe) {
        Intent intent = new Intent(context, RecipeWidgetService.class);
        intent.setAction(ACTION_UPDATE_RECIPE);

        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_RECIPE, recipe);

        intent.putExtras(bundle);
        context.startService(intent);
    }

    public RecipeWidgetService() {
        super(RecipeWidgetService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent != null && intent.getAction() != null) {
            if (intent.getAction().equals(ACTION_UPDATE_RECIPE)) {
                Recipe recipe = intent.getParcelableExtra(BUNDLE_RECIPE);
                updateRecipe(recipe);
            }
        }
    }

    public void updateRecipe(@Nullable Recipe recipe) {
        if (recipe == null) {
            recipe = new Recipe();
            recipe.name = "";
            recipe.ingredients = new ArrayList<>();
        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] widgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, IngredientsWidget.class));
        IngredientsWidget.updateRecipe(this, appWidgetManager, widgetIds, recipe);
    }
}
