package com.example.powerincode.bakingapp.common.screen;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.powerincode.bakingapp.R;

import butterknife.ButterKnife;

/**
 * Created by powerman23rus on 02.12.17.
 * Enjoy ;)
 */

public abstract class BaseFragment extends Fragment {
    public final String TAG = BaseFragment.this.getClass().getSimpleName();

    protected boolean isOrientationLandscape() {
        if(!isAdded()) return false;
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    protected boolean isTablet() {
        return getResources().getBoolean(R.bool.isTablet);
    }

    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    public String getUnicTag(int id) {
        return TAG + " " + id;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(getFragmentId(), container, false);
        ButterKnife.bind(this, view);
        onViewCreated(savedInstanceState);
        return view;
    }

    protected abstract int getFragmentId();

    protected void onViewCreated(Bundle savedInstanceState) {

    }

    public <T extends BaseFragment> T attachFragmentToDefault(T fragment) {
        return attachSmartFragment(R.id.default_fragment_container, fragment, fragment.TAG);
    }

    public <T extends BaseFragment> T attachSmartFragment(int containerId, T fragment) {
        return attachSmartFragment(containerId, fragment, fragment.TAG);
    }

    public <T extends BaseFragment> T attachSmartFragment(int containerId, T fragment, String tag) {
        T fragmentByTag = (T) getChildFragmentManager().findFragmentByTag(tag);

        if (fragmentByTag == null) {
            fragmentByTag = fragment;
        } else {
            fragmentByTag.setArguments(fragment.getArguments());
        }

        return attachFragment(containerId, fragmentByTag, tag);
    }

    public <T extends BaseFragment> T attachFragment(int containerId, T fragment, String tag) {
        getChildFragmentManager()
                .beginTransaction()
                .replace(containerId, fragment, tag)
                .addToBackStack(null)
                .commit();

        return fragment;
    }

    public void finish() {
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }

    public void finishWithActivity() {
        finish();
        getActivity().finish();
    }
}
