package com.example.powerincode.bakingapp.screens.recipe;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Button;

import com.example.powerincode.bakingapp.R;
import com.example.powerincode.bakingapp.common.screen.BaseFragment;
import com.example.powerincode.bakingapp.network.models.Recipe;
import com.example.powerincode.bakingapp.network.models.Step;
import com.example.powerincode.bakingapp.screens.recipe.adapter.RecipeFlowAdapter;
import com.example.powerincode.bakingapp.screens.recipe.fragments.RecipeDetailFragment;
import com.example.powerincode.bakingapp.screens.recipe.fragments.RecipeMenuFragment;
import com.example.powerincode.bakingapp.screens.recipe.fragments.RecipeStepFragment;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by powerman23rus on 06.12.17.
 * Enjoy ;)
 */

public class RecipeDetailPhoneFragment extends BaseFragment implements IRecipeDetailFragment, ViewPager.OnPageChangeListener,
        View.OnClickListener,
        RecipeMenuFragment.OnMenuTappedListener {

    public static String EXTRA_RECIPE = "EXTRA_RECIPE";
    public static String EXTRA_RECIPE_STEP_INDEX = "EXTRA_RECIPE_STEP_INDEX";
    public static RecipeDetailPhoneFragment getFragment(Recipe recipe, int menuPosition) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_RECIPE, recipe);
        bundle.putInt(EXTRA_RECIPE_STEP_INDEX, menuPosition == 0 ? -1 : menuPosition);

        RecipeDetailPhoneFragment fragment = new RecipeDetailPhoneFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @BindView(R.id.vp_recipe_flow)
    ViewPager mRecipeFlowViewPager;

    @BindView(R.id.btn_go)
    Button mGoButton;

    private Recipe mRecipe;
    private int mMenuIndex;
    private RecipeFlowAdapter mRecipeFlowAdapter;
    private ArrayList<BaseFragment> mFragments = new ArrayList<>();

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_recipe_detail_phone;
    }

    @Override
    public void onResume() {
        configureButton(mRecipeFlowViewPager.getCurrentItem());

        super.onResume();
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);

        mGoButton.setVisibility(View.INVISIBLE);

        if (getArguments() != null) {
            mRecipe = getArguments().getParcelable(EXTRA_RECIPE);
            mMenuIndex = getArguments().getInt(EXTRA_RECIPE_STEP_INDEX);

            // setTitle
            ActionBar actionBar = getBaseActivity().getSupportActionBar();

            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setTitle(mRecipe.name);
            }

            RecipeMenuFragment menuFragment = RecipeMenuFragment.getFragment(mRecipe);
            mFragments.add(menuFragment);
            mFragments.add(RecipeDetailFragment.getFragment(mRecipe));

            for(Step step : mRecipe.steps) {
                mFragments.add(RecipeStepFragment.getFragment(step));
            }

            mRecipeFlowAdapter = new RecipeFlowAdapter(getChildFragmentManager(), mFragments);
            mRecipeFlowViewPager.addOnPageChangeListener(this);
            mRecipeFlowViewPager.setAdapter(mRecipeFlowAdapter);
            mRecipeFlowAdapter.notifyDataSetChanged();

            mGoButton.setOnClickListener(this);
            showStep();
        } else {
            finishWithActivity();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mMenuIndex = position > 1 ? position - 1 : 0;

        for (BaseFragment fragment : mFragments) {
            stopVideoIn(fragment);
        }

        BaseFragment fragment = mFragments.get(position);
        RecipeStepFragment playableFragment = castToStepFragment(fragment);
        if (playableFragment != null) {
            playableFragment.play();
        }

        configureButton(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View view) {
        int nextItem = mRecipeFlowViewPager.getCurrentItem() + 1;
        if (nextItem < mRecipeFlowAdapter.getCount()) {
            mRecipeFlowViewPager.setCurrentItem(nextItem);
        } else if (isLastRecipeStep(mRecipeFlowViewPager.getCurrentItem())) {
            mRecipeFlowViewPager.setCurrentItem(0);
        }
    }

    @Override
    public void onTapped(int menuIndex) {
        mRecipeFlowViewPager.setCurrentItem(getViewPagerIndex(menuIndex));
    }

    @Override
    public boolean onBackTapped() {
        if (mRecipeFlowViewPager.getCurrentItem() != 0) {
            mRecipeFlowViewPager.setCurrentItem(getPreviousStep());
            return true;
        }

        return false;
    }

    @Override
    public int getStepPosition() {
        return mMenuIndex;
    }

    private void stopVideoIn(BaseFragment fragment) {
        if (fragment instanceof RecipeStepFragment) {
            final RecipeStepFragment playableFragment = (RecipeStepFragment) fragment;
            playableFragment.stop();
        }
    }

    private boolean isLastRecipeStep(int fragmentPosition) {
        return fragmentPosition == mRecipeFlowAdapter.getCount() - 1;
    }

    private int getPreviousStep() {
        int previousStep = mRecipeFlowViewPager.getCurrentItem() - 1;
        return previousStep >= 0 ? previousStep : 0;
    }

    private int getViewPagerIndex(int stepIndex) {
        return stepIndex + 1;
    }

    private RecipeStepFragment castToStepFragment(BaseFragment fragment) {
        return fragment instanceof RecipeStepFragment ? (RecipeStepFragment) fragment : null;
    }

    private void showStep() {
        int step = mMenuIndex + 1;
        if (step < mRecipeFlowAdapter.getCount()) {
            mRecipeFlowViewPager.setCurrentItem(step);
        }
    }

    private void configureButton(int position) {
        if (!isOrientationLandscape()) {
            mGoButton.setVisibility(View.VISIBLE);

            if (position == 0) {
                mGoButton.setVisibility(View.INVISIBLE);
            } else if (position == 1) {
                mGoButton.setText(R.string.ready_go);
            } else if (isLastRecipeStep(position)) {
                mGoButton.setText(R.string.back_to_start);
            } else {
                mGoButton.setText(R.string.next_step);
            }
        }
    }
}
