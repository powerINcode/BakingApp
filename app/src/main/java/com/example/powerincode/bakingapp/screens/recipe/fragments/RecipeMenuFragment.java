package com.example.powerincode.bakingapp.screens.recipe.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.powerincode.bakingapp.R;
import com.example.powerincode.bakingapp.common.screen.BaseFragment;
import com.example.powerincode.bakingapp.common.view.GroupBlock;
import com.example.powerincode.bakingapp.network.models.Recipe;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by powerman23rus on 06.12.17.
 * Enjoy ;)
 */

public class RecipeMenuFragment extends BaseFragment {
    public static final String EXTRA_RECIPE = "EXTRA_RECIPE";
    public static final String BUNDLE_SELECTED_ITEM_INDEX = "BUNDLE_SELECTED_ITEM_INDEX";
    public static final String BUNDLE_SCROLL_OFFSET = "BUNDLE_SCROLL_OFFSET";

    public interface OnMenuTappedListener {
        void onTapped(int menuIndex);
    }

    public static RecipeMenuFragment getFragment(Recipe recipe) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_RECIPE, recipe);

        RecipeMenuFragment fragment = new RecipeMenuFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @BindView(R.id.sv_menu_scroll)
    ScrollView mRootScrollView;

    @BindView(R.id.ll_recipe_menu)
    LinearLayout mMenuContainer;

    private Recipe mRecipe;
    private OnMenuTappedListener mEventListener;
    private int mSelectedPosition;
    private ArrayList<GroupBlock> mGroupBlocks = new ArrayList<>();

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_recipe_menu;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (getParentFragment() instanceof OnMenuTappedListener) {
            mEventListener = (OnMenuTappedListener) getParentFragment();
        }
    }

    public void selectMenu(int menuIndex) {
        if (menuIndex < mGroupBlocks.size()) {
            mGroupBlocks.get(mSelectedPosition).setBackgroundColor(getResources().getColor(android.R.color.white));
            mGroupBlocks.get(menuIndex).setBackgroundColor(getResources().getColor(R.color.colorBlackHalf));
            mSelectedPosition = menuIndex;
        }
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);

        if (getArguments() != null) {
            mRecipe = getArguments().getParcelable(EXTRA_RECIPE);

            setRecipeBlock(0, getString(R.string.ingredients));
            for (int i = 0; i < mRecipe.steps.size(); i++) {
                setRecipeBlock(i + 1, mRecipe.steps.get(i).shortDescription);
            }
        }

        if (savedInstanceState != null) {
            selectMenu(savedInstanceState.getInt(BUNDLE_SELECTED_ITEM_INDEX));
            mRootScrollView.setScrollY(savedInstanceState.getInt(BUNDLE_SCROLL_OFFSET));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(BUNDLE_SELECTED_ITEM_INDEX, mSelectedPosition);
        outState.putInt(BUNDLE_SCROLL_OFFSET, mRootScrollView.getScrollY());
        super.onSaveInstanceState(outState);
    }

    private void setRecipeBlock(final int menuIndex, String name) {
        GroupBlock block = new GroupBlock(getContext());
        block.setGroupName(name);
        block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEventListener != null) {
                    mEventListener.onTapped(menuIndex);
                }
            }
        });

        mMenuContainer.addView(block);
        mGroupBlocks.add(block);
    }
}
