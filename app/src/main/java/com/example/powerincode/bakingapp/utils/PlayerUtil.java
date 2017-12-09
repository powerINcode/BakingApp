package com.example.powerincode.bakingapp.utils;

import android.content.Context;
import android.media.MediaDataSource;
import android.net.Uri;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

/**
 * Created by powerman23rus on 06.12.17.
 * Enjoy ;)
 */

public class PlayerUtil {
    public static final PlayerUtil shared = new PlayerUtil();

    public SimpleExoPlayer getPlayer(Context context) {
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(context, trackSelector);

        return player;
    }

    public MediaSource getMediaSource(Context context, String url) {
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        DataSource.Factory dataSourceFactory = getDataSourceFactory(context);

        MediaSource videoSource = new ExtractorMediaSource(Uri.parse(url),
                dataSourceFactory, extractorsFactory, null, null);

        return videoSource;
    }

    private DataSource.Factory getDataSourceFactory(Context context) {
        return  new DefaultDataSourceFactory(context,
                Util.getUserAgent(context, "Baking App"));
    }
}
