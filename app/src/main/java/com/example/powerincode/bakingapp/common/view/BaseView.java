package com.example.powerincode.bakingapp.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import butterknife.ButterKnife;

/**
 * Created by powerman23rus on 02.12.17.
 * Enjoy ;)
 */

public abstract class BaseView extends FrameLayout{
    protected abstract int getViewId();
    protected int[] getStyleableId() {
        return new int[0];
    }

    public BaseView(@NonNull Context context) {
        super(context);
        setup(context, null, 0);
    }

    public BaseView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setup(context, attrs, 0);
    }

    public BaseView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup(context, attrs, defStyleAttr);
    }

    private void setup(Context context, AttributeSet attrs, int defStyleAttr) {
        View view = LayoutInflater.from(context).inflate(getViewId(), this, false);
        addView(view);
        ButterKnife.bind(this, view);

        if (getStyleableId().length != 0) {
            TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, getStyleableId(), 0, 0);
            try {
                obtainStyleAttrs(typedArray);
            }  finally {
                typedArray.recycle();
            }
        }

        initialization();
    }

    public void initialization() {}

    protected void obtainStyleAttrs(TypedArray typedArray) {

    }
}
