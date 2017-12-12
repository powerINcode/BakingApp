package com.example.powerincode.bakingapp.screens.main.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.powerincode.bakingapp.R;
import com.example.powerincode.bakingapp.common.adapter.BaseAdapter;
import com.example.powerincode.bakingapp.common.adapter.BaseViewHolder;
import com.example.powerincode.bakingapp.common.view.ImageLoader;
import com.example.powerincode.bakingapp.network.models.Recipe;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by powerman23rus on 02.12.17.
 * Enjoy ;)
 */

public class RecipeAdapter extends BaseAdapter<Recipe, RecipeAdapter.RecipeViewHolder> {

    private RecipeAdapterEvent mEventListener;

    public interface RecipeAdapterEvent {
        void onRecipeClick(Recipe recipe);
    }

    public RecipeAdapter() {
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    public RecipeAdapter(Context context, @Nullable ArrayList<Recipe> data, RecipeAdapterEvent e) {
        super(context, data);
        mEventListener = e;
    }

    public class RecipeViewHolder extends BaseViewHolder<Recipe> implements View.OnClickListener {

        @BindView(R.id.iv_recipe_image_view)
        ImageLoader mRecipeImageLoader;

        @BindView(R.id.tv_recipe_name)
        TextView recipeNameTextView;

        private Recipe mRecipe;

        public RecipeViewHolder(final View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void bind(Recipe item) {
            mRecipe = item;
            recipeNameTextView.setText(mRecipe.name);

            if (!TextUtils.isEmpty(mRecipe.image)) {
                mRecipeImageLoader.init(mRecipe.image);
            } else {
                mRecipeImageLoader.init(R.drawable.ic_recipe);
            }
        }

        @Override
        public void onClick(View view) {
            if (mEventListener != null) {
                mEventListener.onRecipeClick(mRecipe);
            }
        }
    }
}
