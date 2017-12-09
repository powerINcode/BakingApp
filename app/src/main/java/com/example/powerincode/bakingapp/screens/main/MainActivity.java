package com.example.powerincode.bakingapp.screens.main;

import android.os.Bundle;
import android.support.test.espresso.IdlingResource;

import com.example.powerincode.bakingapp.R;
import com.example.powerincode.bakingapp.common.screen.BaseActivity;

public class MainActivity extends BaseActivity {
    @Override
    protected int getActivityId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        attachFragmentToDefault(new RecipeListFragment());
    }

    public IdlingResource getIdlingResource() {
        return RecipeListFragment.getIdlingResource();
    }
}
