package com.example.powerincode.bakingapp.common.screen;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.example.powerincode.bakingapp.R;

import butterknife.ButterKnife;

/**
 * Created by powerman23rus on 02.12.17.
 * Enjoy ;)
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected abstract int getActivityId();

    protected boolean isOrientationLandscape() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    protected boolean isTablet() {
        return getResources().getBoolean(R.bool.isTablet);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getActivityId());

        ButterKnife.bind(this);
    }

    public <T extends BaseFragment> T attachFragmentToDefault(T fragment) {
        T fragmentByTag = (T) getSupportFragmentManager().findFragmentByTag(fragment.TAG);

        if (fragmentByTag == null) {
            fragmentByTag = fragment;
        } else {
            fragmentByTag.setArguments(fragment.getArguments());
        }

        return attachFragment(R.id.default_fragment_container, fragmentByTag, fragmentByTag.TAG);
    }

    public <T extends BaseFragment> T attachFragment(int containerId, T fragment, String tag) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(containerId, fragment, tag)
                .commit();

        return fragment;
    }
}
