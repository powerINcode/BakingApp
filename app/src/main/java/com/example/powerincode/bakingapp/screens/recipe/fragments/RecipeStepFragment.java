package com.example.powerincode.bakingapp.screens.recipe.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.powerincode.bakingapp.R;
import com.example.powerincode.bakingapp.common.screen.BaseFragment;
import com.example.powerincode.bakingapp.common.view.ImageLoader;
import com.example.powerincode.bakingapp.network.models.Step;
import com.example.powerincode.bakingapp.utils.PlayerUtil;
import com.google.android.exoplayer2.ExoPlaybackException;
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

public class RecipeStepFragment extends BaseFragment implements Player.EventListener {
    public static final String EXTRA_RECIPE_STEP = "EXTRA_RECIPE_STEP_INDEX";
    public static final String BUNDLE_PLAY_POSITION = "BUNDLE_PLAY_POSITION";
    public static final String BUNDLE_IS_PLAY_WHEN_READY = "BUNDLE_IS_PLAY_WHEN_READY";

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

    @BindView(R.id.il_thumbnail)
    ImageLoader mThumbnailLoader;

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
    private boolean mIsPlayWhenReady = true;
    private Player.EventListener mPlayerEventListener;

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
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mStep != null) {
            configurePlayer(mStep.videoURL);
            setFullScreenMode(mStep != null &&
                    !mStep.videoURL.isEmpty() &&
                    isOrientationLandscape());
        }
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

                    if (savedInstanceState != null) {
                        mLastSeekPosition = savedInstanceState.getLong(BUNDLE_PLAY_POSITION);
                        mIsPlayWhenReady = savedInstanceState.getBoolean(BUNDLE_IS_PLAY_WHEN_READY);

                    }

                } else {
                    mVideoUnavailableTextView.setVisibility(View.VISIBLE);
                    mSimpleExoPlayerView.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(mStep.thumbnailURL)) {
                    mThumbnailLoader.init(mStep.thumbnailURL);
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

        outState.putBoolean(BUNDLE_IS_PLAY_WHEN_READY, mIsPlayWhenReady);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        releasePlayer();
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
        setFullScreenMode(isOrientationLandscape());

        if (mPlayerEventListener != null) {
            mPlayer.addListener(mPlayerEventListener);
        }

        mPlayer.addListener(this);

        if (mSimpleExoPlayerView != null && url != null && !url.isEmpty()) {
            mPlayer.prepare(playerUtil.getMediaSource(getContext(), url));
        }

        mPlayer.seekTo(mLastSeekPosition);
    }

    private void releasePlayer() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
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

            mPlayer.setPlayWhenReady(mIsPlayWhenReady);
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        mIsPlayWhenReady = playWhenReady;
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }
}
