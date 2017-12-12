package com.example.powerincode.bakingapp.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.powerincode.bakingapp.R;
import com.example.powerincode.bakingapp.network.models.Ingredient;
import com.example.powerincode.bakingapp.network.models.Recipe;
import com.example.powerincode.bakingapp.screens.recipe.RecipeDetailActivity;


/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidget extends AppWidgetProvider {

    public static void updateRecipe(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, Recipe recipe) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, recipe);
        }
    }

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, Recipe recipe) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);

        if (recipe != null) {
            views.setTextViewText(R.id.tv_recipe_title, recipe.name);
            Intent intent = RecipeDetailActivity.getIntent(context, recipe);
            PendingIntent recipeDetailPendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.root_container, recipeDetailPendingIntent);

            StringBuilder ingredients = new StringBuilder();
            for (int i = 0; i < recipe.ingredients.size(); i++) {
                Ingredient ingredient = recipe.ingredients.get(i);

                if (i != 0) {
                    ingredients.append(",\n");
                }

                ingredients.append(ingredient.quantity)
                        .append(" ")
                        .append(ingredient.measure)
                        .append(" ")
                        .append(context.getString(R.string.of))
                        .append(" ")
                        .append(ingredient.ingredient);
            }

            views.setTextViewText(R.id.tv_recipe_ingredients, ingredients.toString());

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RecipeWidgetService.startUpdateRecipeService(context, null);
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

