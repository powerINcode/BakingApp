package com.example.powerincode.bakingapp.screens.recipe.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.powerincode.bakingapp.R;
import com.example.powerincode.bakingapp.common.screen.BaseActivity;
import com.example.powerincode.bakingapp.common.screen.BaseFragment;
import com.example.powerincode.bakingapp.network.models.Step;
import com.example.powerincode.bakingapp.utils.PlayerUtil;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import butterknife.BindView;

/**
 * Created by powerman23rus on 05.12.17.
 * Enjoy ;)
 */

public class RecipeStepFragment extends BaseFragment {
    public static final String EXTRA_RECIPE_STEP = "EXTRA_RECIPE_STEP_INDEX";
    public static final String BUNDLE_PLAY_POSITION = "BUNDLE_PLAY_POSITION";

    public static RecipeStepFragment getFragment(Step step) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_RECIPE_STEP, step);

        RecipeStepFragment fragment = new RecipeStepFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @BindView(R.id.fl_full_screen_video)
    FrameLayout mFullScreenVideoContainer;

    @BindView(R.id.fl_video)
    FrameLayout mVideoContainer;

    @BindView(R.id.tv_video_unavailavle)
    TextView mVideoUnavailableTextView;

    @BindView(R.id.sp_step_video_player)
    SimpleExoPlayerView mSimpleExoPlayerView;

    @BindView(R.id.tv_short_description)
    TextView mShortDescriptionTextView;

    @BindView(R.id.tv_description)
    TextView mDescriptionTextView;


    private Step mStep;
    private SimpleExoPlayer mPlayer;
    private PlayerUtil playerUtil = PlayerUtil.shared;
    private long mLastSeekPosition;
    private Player.EventListener mPlayerEventListener;

    public Step getStep() {
        return mStep;
    }

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_recipe_step;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (getParentFragment() instanceof Player.EventListener) {
            mPlayerEventListener = (Player.EventListener) getParentFragment();
        }
    }

    @Override
    public void onResume() {
        setFullScreenMode(mStep != null &&
                !mStep.videoURL.isEmpty() &&
                isOrientationLandscape());
        super.onResume();
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);

        if (getArguments() != null) {
            mStep = getArguments().getParcelable(EXTRA_RECIPE_STEP);

            if (mStep != null) {

                if (!TextUtils.isEmpty(mStep.videoURL)) {
                    mVideoUnavailableTextView.setVisibility(View.GONE);
                    mSimpleExoPlayerView.setVisibility(View.VISIBLE);

                    configurePlayer(mStep.videoURL);

                    if (savedInstanceState != null) {
                        mLastSeekPosition = savedInstanceState.getLong(BUNDLE_PLAY_POSITION);

                    }

                    mPlayer.seekTo(mLastSeekPosition);
                } else {
                    mVideoUnavailableTextView.setVisibility(View.VISIBLE);
                    mSimpleExoPlayerView.setVisibility(View.GONE);
                }

                mShortDescriptionTextView.setText(mStep.shortDescription);
                mDescriptionTextView.setText(mStep.description);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mPlayer != null) {
            outState.putLong(BUNDLE_PLAY_POSITION, mPlayer.getCurrentPosition());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }

        super.onDestroy();
    }

    public void play() {
        if(mPlayer == null)  return;
        mPlayer.setPlayWhenReady(true);
    }

    public void stop() {
        if(mPlayer == null)  return;
        mPlayer.setPlayWhenReady(false);
    }

    private void configurePlayer(String url) {
        mPlayer = playerUtil.getPlayer(getContext());
        mSimpleExoPlayerView.setPlayer(mPlayer);
        // Prepare the player with the source.
        mPlayer.prepare(playerUtil.getMediaSource(getContext(), url));

        setFullScreenMode(isOrientationLandscape());

        if (mPlayerEventListener != null) {
            mPlayer.addListener(mPlayerEventListener);
        }
    }

    private void reattachPlayerView(ViewGroup to) {
        ViewGroup parent = (ViewGroup) mSimpleExoPlayerView.getParent();

        if (parent == null) {
            return;
        }

        parent.removeView(mSimpleExoPlayerView);
        mSimpleExoPlayerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        to.addView(mSimpleExoPlayerView);
    }

    private void setFullScreenMode(boolean isFullScreen) {
        if (mPlayer != null) {
            if (isFullScreen) {
                getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
                mFullScreenVideoContainer.setVisibility(View.VISIBLE);
                getBaseActivity().getSupportActionBar().hide();
                reattachPlayerView(mFullScreenVideoContainer);
            } else {
                getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                getBaseActivity().getSupportActionBar().show();
                mFullScreenVideoContainer.setVisibility(View.INVISIBLE);
                reattachPlayerView(mVideoContainer);
            }

            mPlayer.setPlayWhenReady(true);
        }
    }
}
