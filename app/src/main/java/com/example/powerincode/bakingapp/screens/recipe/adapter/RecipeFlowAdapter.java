package com.example.powerincode.bakingapp.screens.recipe.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.powerincode.bakingapp.common.screen.BaseFragment;
import com.example.powerincode.bakingapp.screens.recipe.fragments.RecipeStepFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by powerman23rus on 05.12.17.
 * Enjoy ;)
 */

public class RecipeFlowAdapter extends FragmentPagerAdapter {
    private final ArrayList<BaseFragment> mFragments;

    public RecipeFlowAdapter(FragmentManager fm, ArrayList<BaseFragment> fragments) {
        super(fm);

        mFragments = fragments;
    }

    public void add(BaseFragment fragment) {
        mFragments.add(fragment);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }



    @Override
    public int getCount() {
        if(mFragments == null) return 0;
        return mFragments.size();
    }
}
