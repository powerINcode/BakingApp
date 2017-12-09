package com.example.powerincode.bakingapp.screens.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.example.powerincode.bakingapp.R;
import com.example.powerincode.bakingapp.common.SimpleIdlingResource;
import com.example.powerincode.bakingapp.common.screen.BaseFragment;
import com.example.powerincode.bakingapp.network.models.Recipe;
import com.example.powerincode.bakingapp.screens.main.adapter.RecipeAdapter;
import com.example.powerincode.bakingapp.screens.recipe.RecipeDetailActivity;
import com.example.powerincode.bakingapp.utils.NetworkUtil;

import java.util.ArrayList;

import butterknife.BindView;


public class RecipeListFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<ArrayList<Recipe>>,RecipeAdapter.RecipeAdapterEvent {
    public static final int LOADER_ID = 10010;
    private static String BUNDLE_RECIPES_LIST = "BUNDLE_RECIPES_LIST";

    @BindView(R.id.rv_recipe_list)
    RecyclerView mRecipeListRecyclerView;

    @BindView(R.id.pb_recipes)
    ProgressBar mRecipesLoading;

    RecipeAdapter mRecipeAdapter;

    private ArrayList<Recipe> mRecipes;
    private static SimpleIdlingResource mIdlingResource;

    public RecipeListFragment() {
        // Required empty public constructor
    }

    public static SimpleIdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }

        return mIdlingResource;
    }

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_recipe_list;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);

        if (savedInstanceState != null) {
            mRecipes = savedInstanceState.getParcelableArrayList(BUNDLE_RECIPES_LIST);
        }

        mRecipeAdapter = new RecipeAdapter(getContext(), mRecipes, this);
        if (!isTablet()) {
            mRecipeListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        } else {
            mRecipeListRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        }
        mRecipeListRecyclerView.setHasFixedSize(true);
        mRecipeListRecyclerView.setAdapter(mRecipeAdapter);

        if (mRecipes == null) {
            mRecipesLoading.setVisibility(View.VISIBLE);
            getLoaderManager().restartLoader(LOADER_ID, null, this);
        }
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(BUNDLE_RECIPES_LIST, mRecipes);

        super.onSaveInstanceState(outState);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<ArrayList<Recipe>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<ArrayList<Recipe>>(getContext()) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();

                if (mRecipes != null) {
                    deliverResult(mRecipes);
                } else {
                    forceLoad();
                    getIdlingResource().setIdleState(false);
                }
            }

            @Override
            public void deliverResult(ArrayList<Recipe> data) {
                mRecipes = data;
                super.deliverResult(data);
            }

            @Override
            public ArrayList<Recipe> loadInBackground() {
                return NetworkUtil.getRecipeList();
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Recipe>> loader, ArrayList<Recipe> data) {
        mRecipeAdapter.swapData(data);
        mRecipesLoading.setVisibility(View.INVISIBLE);
        mIdlingResource.setIdleState(true);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Recipe>> loader) {

    }

    @Override
    public void onRecipeClick(Recipe recipe) {
        startActivity(RecipeDetailActivity.getIntent(getContext(), recipe));
    }
}
