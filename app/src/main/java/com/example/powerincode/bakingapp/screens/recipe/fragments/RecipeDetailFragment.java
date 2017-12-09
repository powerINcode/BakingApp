package com.example.powerincode.bakingapp.screens.recipe.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.example.powerincode.bakingapp.R;
import com.example.powerincode.bakingapp.common.screen.BaseFragment;
import com.example.powerincode.bakingapp.common.view.ImageLoader;
import com.example.powerincode.bakingapp.network.models.Ingredient;
import com.example.powerincode.bakingapp.network.models.Recipe;

import butterknife.BindView;


/**
 * A placeholder fragment containing a simple view.
 */
public class RecipeDetailFragment extends BaseFragment {
    public static String EXTRA_RECIPE = "EXTRA_RECIPE";

    public static RecipeDetailFragment getFragment(Recipe recipe) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_RECIPE, recipe);

        RecipeDetailFragment fragment = new RecipeDetailFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @BindView(R.id.iv_recipe_image)
    ImageLoader mRecipeImageLoader;

    @BindView(R.id.tv_ingredients)
    TextView mIngredientsTextView;

    Recipe mRecipe;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_recipe_detail;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);

        if (getArguments() != null) {
            mRecipe = getArguments().getParcelable(EXTRA_RECIPE);

            if (!TextUtils.isEmpty(mRecipe.image)) {
                mRecipeImageLoader.init(mRecipe.image);
            } else {
                mRecipeImageLoader.init(R.drawable.ic_recipe);
            }

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < mRecipe.ingredients.size(); i++) {
                Ingredient ingredient = mRecipe.ingredients.get(i);

                if (i != 0) {
                    sb.append(",\n");
                }

                sb.append(String.valueOf(i + 1))
                        .append(") ")
                        .append(ingredient.quantity)
                        .append(" ")
                        .append(ingredient.measure)
                        .append(" ")
                        .append(getString(R.string.of))
                        .append(" ")
                        .append(ingredient.ingredient);
            }

            mIngredientsTextView.setText(sb.toString());
        }

    }
}
