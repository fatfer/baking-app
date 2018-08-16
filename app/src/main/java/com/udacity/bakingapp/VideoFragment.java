package com.udacity.bakingapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;
import com.udacity.bakingapp.Utils.Keys;

import butterknife.BindView;
import butterknife.ButterKnife;


public class VideoFragment extends Fragment {

    private Uri mURL;
    private String mDescription;
    private String mThumbnailURL;

    @BindView(R.id.player_view)
    com.google.android.exoplayer2.ui.PlayerView mPlayerView;

    @BindView(R.id.iv_thumbnail)
    ImageView iv_thumbnail;

    @BindView(R.id.tv_step_description)
    TextView tv_step_description;

    private OnFragmentInteractionListener mListener;
    private long mVideoCurrentPosition = 0;
    private boolean mPlayWhenReady = true;

    SimpleExoPlayer mSimpleExoPlayer;
    DefaultBandwidthMeter bandwidthMeter;
    TrackSelection.Factory videoTrackSelectionFactory;
    TrackSelector trackSelector;
    DataSource.Factory dataSourceFactory;
    MediaSource videoSource;

    public VideoFragment() {
        // Required empty public constructor
    }

    public static VideoFragment newInstance(String param1, String param2) {
        VideoFragment fragment = new VideoFragment();
        Bundle args = new Bundle();
        args.putString(Keys.STEP_VIDEO_URL, param1);
        args.putString(Keys.STEP_VIDEO_DESCRIPTION, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mURL = Uri.parse(getArguments().getString(Keys.STEP_VIDEO_URL));
            mDescription = getArguments().getString(Keys.STEP_VIDEO_DESCRIPTION);
            mThumbnailURL = getArguments().getString(Keys.STEP_THUMBNAIL_URL);
        }

        if(savedInstanceState != null){
            mURL = Uri.parse(savedInstanceState.getString(Keys.STEP_VIDEO_URL));
            mDescription = savedInstanceState.getString(Keys.STEP_VIDEO_DESCRIPTION);
            mThumbnailURL = savedInstanceState.getString(Keys.STEP_THUMBNAIL_URL);
            mVideoCurrentPosition = savedInstanceState.getLong(Keys.VIDEO_PLAYER_POSITION);
            mPlayWhenReady = savedInstanceState.getBoolean(Keys.VIDEO_PLAY_WHEN_READY);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video, container, false);

        ButterKnife.bind(this, view);

        String url = mURL.toString();

        if (mThumbnailURL != null && !mThumbnailURL.isEmpty()) {
            Picasso.get().load(mThumbnailURL)
                    .placeholder(R.drawable.ic_room_service_black_24dp)
                    .error(R.drawable.ic_room_service_black_24dp).into(iv_thumbnail);
        }else{
            iv_thumbnail.setVisibility(View.GONE);
        }

        if(!(url.equals(""))) {
            initializeVideoPlayer(mURL);
        }else{
            mPlayerView.setVisibility(View.GONE);
        }

        tv_step_description.setText(mDescription);

        return view;
    }

    public void onButtonPressed(int currentStep) {
        if (mListener != null) {
            mListener.onFragmentInteraction(currentStep);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        if (mSimpleExoPlayer!=null) {
            releasePlayer();
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int currentStep);
    }

    public void initializeVideoPlayer(Uri videoUri){
        if(mSimpleExoPlayer == null){

            // 1. Create a default TrackSelector
            bandwidthMeter = new DefaultBandwidthMeter();
            videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);
            trackSelector =
                    new DefaultTrackSelector(videoTrackSelectionFactory);

            // 2. Create the player
            mSimpleExoPlayer =
                    ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);

            // Bind the player to the view.
            mPlayerView.setPlayer(mSimpleExoPlayer);

            // Produces DataSource instances through which media data is loaded.
            dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                    Util.getUserAgent(getContext(), "YOUR_APP_NAME"), bandwidthMeter);

            // This is the MediaSource representing the media to be played.
            videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(videoUri);

            // Prepare the player with the source.
            mSimpleExoPlayer.prepare(videoSource);

            //Set play when ready
            mSimpleExoPlayer.setPlayWhenReady(mPlayWhenReady);

            //Set position
            mSimpleExoPlayer.seekTo(mVideoCurrentPosition);
        }
    }

    private void releasePlayer() {
        if (mSimpleExoPlayer != null) {
            mVideoCurrentPosition = mSimpleExoPlayer.getCurrentPosition();
            mPlayWhenReady = mSimpleExoPlayer.getPlayWhenReady();
            mSimpleExoPlayer.stop();
            mSimpleExoPlayer.release();
            mSimpleExoPlayer = null;
            dataSourceFactory = null;
            videoSource = null;
            trackSelector = null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        releasePlayer();

        outState.putString(Keys.STEP_VIDEO_URL, mURL.toString());
        outState.putString(Keys.STEP_VIDEO_DESCRIPTION, mDescription);
        outState.putString(Keys.STEP_THUMBNAIL_URL, mThumbnailURL);
        outState.putLong(Keys.VIDEO_PLAYER_POSITION, mVideoCurrentPosition );
        outState.putBoolean(Keys.VIDEO_PLAY_WHEN_READY, mPlayWhenReady );
    }
}
