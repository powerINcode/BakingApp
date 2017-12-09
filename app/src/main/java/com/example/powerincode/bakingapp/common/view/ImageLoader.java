package com.example.powerincode.bakingapp.common.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.example.powerincode.bakingapp.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;

/**
 * Created by powerman23rus on 04.12.17.
 * Enjoy ;)
 */

public class ImageLoader extends BaseView {

    @BindView(R.id.iv_image_content)
    ImageView mContentImageView;

    @Override
    protected int getViewId() {
        return R.layout.view_image_loader;
    }

    public ImageLoader(@NonNull Context context) {
        super(context);
    }

    public ImageLoader(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageLoader(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(String url) {
        if (url == null || url.isEmpty()) {
            return;
        }

        Picasso.with(getContext()).load(url).into(mContentImageView, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                mContentImageView.setImageResource(R.drawable.ic_error);
            }
        });
    }

    public void init(int resourceId) {
        mContentImageView.setImageResource(resourceId);
    }
}
