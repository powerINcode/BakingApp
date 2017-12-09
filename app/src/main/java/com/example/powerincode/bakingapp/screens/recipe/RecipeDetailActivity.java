package com.example.powerincode.bakingapp.screens.recipe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import com.example.powerincode.bakingapp.R;
import com.example.powerincode.bakingapp.common.screen.BaseActivity;
import com.example.powerincode.bakingapp.network.models.Recipe;


public class RecipeDetailActivity extends BaseActivity {
    public static String EXTRA_RECIPE = "EXTRA_RECIPE";
    public static String EXTRA_RECIPE_STEP_INDEX = "EXTRA_RECIPE_STEP_INDEX";

    public static Intent getIntent(Context from, Recipe recipe) {
        Intent intent = new Intent(from, RecipeDetailActivity.class);
        intent.putExtra(EXTRA_RECIPE, recipe);

        return intent;
    }

    private IRecipeDetailFragment mFragment;
    private int mStepPosition = 0;

    @Override
    protected int getActivityId() {
        return R.layout.activity_recipe_detail;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mStepPosition = savedInstanceState.getInt(EXTRA_RECIPE_STEP_INDEX);
        }

        if (getIntent() != null ) {
            Recipe recipe = getIntent().getParcelableExtra(EXTRA_RECIPE);

            if (!isTablet()) {
                mFragment = attachFragmentToDefault(RecipeDetailPhoneFragment.getFragment(recipe, mStepPosition));
            } else {
                mFragment = attachFragmentToDefault(RecipeDetailTabletFragment.getFragment(recipe, mStepPosition));
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (!mFragment.onBackTapped()) {
            super.onBackPressed();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(EXTRA_RECIPE_STEP_INDEX, mFragment.getStepPosition());
        super.onSaveInstanceState(outState);
    }
}
