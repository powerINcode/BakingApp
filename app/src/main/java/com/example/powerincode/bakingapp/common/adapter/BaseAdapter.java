package com.example.powerincode.bakingapp.common.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by powerman23rus on 02.12.17.
 * Enjoy ;)
 */

public abstract class BaseAdapter<D, T extends BaseViewHolder<D>> extends RecyclerView.Adapter<T> {
    protected Context mContext;
    protected ArrayList<D> mData;

    @Override
    public void onBindViewHolder(T holder, int position) {
        holder.bind(mData.get(position));
    }

    @Override
    public int getItemCount() {
        if (mData == null) {
            return 0;
        }

        return mData.size();
    }

    public BaseAdapter() {
    }

    public BaseAdapter(Context context, @Nullable ArrayList<D> data) {
        mData = data;
        mContext = context;
    }

    public void swapData(ArrayList<D> newData) {
        mData = newData;
        notifyDataSetChanged();
    }
}
