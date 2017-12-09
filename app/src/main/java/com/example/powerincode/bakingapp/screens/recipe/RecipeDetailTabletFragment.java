package com.example.powerincode.bakingapp.screens.recipe;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.powerincode.bakingapp.R;
import com.example.powerincode.bakingapp.common.screen.BaseFragment;
import com.example.powerincode.bakingapp.network.models.Recipe;
import com.example.powerincode.bakingapp.network.models.Step;
import com.example.powerincode.bakingapp.screens.recipe.fragments.RecipeDetailFragment;
import com.example.powerincode.bakingapp.screens.recipe.fragments.RecipeMenuFragment;
import com.example.powerincode.bakingapp.screens.recipe.fragments.RecipeStepFragment;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;

import butterknife.BindView;

/**
 * Created by powerman23rus on 06.12.17.
 * Enjoy ;)
 */

public class RecipeDetailTabletFragment extends BaseFragment implements IRecipeDetailFragment,
        RecipeMenuFragment.OnMenuTappedListener,
        Player.EventListener,
        View.OnClickListener {
    public static String EXTRA_RECIPE = "EXTRA_RECIPE";
    public static String EXTRA_RECIPE_STEP_INDEX = "EXTRA_RECIPE_STEP_INDEX";

    public static RecipeDetailTabletFragment getFragment(Recipe recipe, int menuPosition) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_RECIPE, recipe);
        bundle.putInt(EXTRA_RECIPE_STEP_INDEX, menuPosition);

        RecipeDetailTabletFragment fragment = new RecipeDetailTabletFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @BindView(R.id.btn_next_full_screen)
    Button mNextStepButton;

    @BindView(R.id.fl_menu_fragment)
    FrameLayout mMenuContainer;

    @BindView(R.id.fl_steps_fragment)
    FrameLayout mStepContainer;

    private Recipe mRecipe;
    private RecipeMenuFragment mMenuFragment;
    private BaseFragment mStepFragment;
    private int mMenuIndex;
    private int mStepIndex;

    @Override
    public boolean onBackTapped() {
        if (mMenuIndex != 0 && isOrientationLandscape()) {
            mMenuIndex = getPreviousStep();
            attachStepFragment(mMenuIndex);
            return true;
        }
        return false;
    }

    @Override
    public int getStepPosition() {
        return mMenuIndex;
    }

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_recipe_detail_tablet;
    }

    @Override
    public void onResume() {
        if (isOrientationLandscape()) {
            mMenuContainer.setVisibility(View.GONE);
        } else {
            mMenuContainer.setVisibility(View.VISIBLE);
        }

        super.onResume();

        mMenuFragment.selectMenu(mMenuIndex);
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);

        if (getArguments() != null) {
            mRecipe = getArguments().getParcelable(EXTRA_RECIPE);
            mMenuIndex = getArguments().getInt(EXTRA_RECIPE_STEP_INDEX);

            // setTitle
            ActionBar actionBar = getBaseActivity().getSupportActionBar();

            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setTitle(mRecipe.name);
            }

            attachMenuFragment();
            onTapped(mMenuIndex);

            mNextStepButton.setOnClickListener(this);
        } else {
            finishWithActivity();
        }
    }

    @Override
    public void onTapped(int menuIndex) {
        if (menuIndex >= getStepsCount()) {
            return;
        }

        mMenuIndex = menuIndex;
        attachStepFragment(menuIndex);
        configureNextButton();
        mMenuFragment.selectMenu(mMenuIndex);
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playWhenReady && playbackState == Player.STATE_ENDED && isOrientationLandscape()) {
            mNextStepButton.setVisibility(View.VISIBLE);
        } else {
            mNextStepButton.setVisibility(View.GONE);
        }
    }

    public int getStepsCount() {
        return mRecipe.steps.size() + 1;
    }

    @Override
    public void onClick(View view) {
        if (!isLastStep()) {
            onTapped(mMenuIndex + 1);
        } else {
            onTapped(0);
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }

    private void configureNextButton() {
        if (isOrientationLandscape()) {

            if (isLastStep()) {
                mNextStepButton.setText(getResources().getString(R.string.back_to_start));
            } else {
                mNextStepButton.setText(getResources().getString(R.string.next_step));
            }

            FrameLayout.LayoutParams buttonLayoutParams = null;

            if (!isVideoAvailable()) {
                mNextStepButton.setVisibility(View.VISIBLE);
                buttonLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL);
            } else {
                mNextStepButton.setVisibility(View.GONE);
                buttonLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
            }

            mNextStepButton.setLayoutParams(buttonLayoutParams);
        } else {
            mNextStepButton.setVisibility(View.GONE);
        }
    }

    private boolean isVideoAvailable() {
        if (mMenuIndex == 0) {
            return false;
        } else {
            Step step = mRecipe.steps.get(mMenuIndex - 1);
            return step.videoURL != null && !step.videoURL.isEmpty();
        }
    }

    private void attachMenuFragment() {
        RecipeMenuFragment fragment = RecipeMenuFragment.getFragment(mRecipe);
        mMenuFragment = attachSmartFragment(R.id.fl_menu_fragment, fragment, fragment.getUnicTag(0));
    }

    private void attachStepFragment(int stepPosition) {
        // for ingredients
        if (stepPosition == 0) {
            mStepFragment = RecipeDetailFragment.getFragment(mRecipe);
            mStepIndex = -1;
        }  else {
            // for steps
            stepPosition--;
            if (stepPosition < mRecipe.steps.size()) {
                Step step = mRecipe.steps.get(stepPosition);
                mStepFragment = RecipeStepFragment.getFragment(step);
                mStepIndex = mRecipe.steps.indexOf(step);
            }
        }

        configureNextButton();
        mStepFragment = attachSmartFragment(R.id.fl_steps_fragment, mStepFragment, mStepFragment.getUnicTag(stepPosition));
    }

    private int getPreviousStep() {
        int previousStep = mMenuIndex - 1;
        return previousStep >= 0 ? previousStep : 0;
    }

    private boolean isLastStep() {
        return mStepIndex == mRecipe.steps.size() - 1;
    }
}
