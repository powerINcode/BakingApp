package com.example.powerincode.bakingapp.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.powerincode.bakingapp.R;

import butterknife.BindView;

/**
 * Created by powerman23rus on 05.12.17.
 * Enjoy ;)
 */

public class GroupBlock extends BaseView {
    @BindView(R.id.tv_group_name)
    TextView mGroupNameTextView;

    private String mGroupName;

    public GroupBlock(@NonNull Context context) {
        super(context);
    }

    public GroupBlock(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GroupBlock(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void initialization() {
        super.initialization();

    }

    @Override
    protected int getViewId() {
        return R.layout.view_group_block;
    }

    public String getGroupName() {
        return mGroupName;
    }

    public void setGroupName(String groupName) {
        mGroupName = groupName;
        mGroupNameTextView.setText(mGroupName);
    }
}
