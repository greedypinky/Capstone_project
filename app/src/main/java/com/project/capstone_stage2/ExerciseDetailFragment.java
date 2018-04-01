package com.project.capstone_stage2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ExerciseDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ExerciseDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExerciseDetailFragment extends Fragment {

    private static final String API_KEY="AIzaSyDy8aQ5OavXmxILBp_Oijz3O3VXlEcS7ys";
    private static String TAG = ExerciseDetailFragment.class.getSimpleName();
    public static String CURRENT_EXERCISE_KEY = "current_exercise";
    public static String CURRENT_EXERCISE_STEPS = "current_exerciseSteps";
    public static String CURRENT_EXERCISE_VIDEO = "current_exerciseVideo";
    public static String CURRENT_VIDEO_POSITION_KEY = "video_position";
    public static String CURRENT_WINDOW_POSITION_KEY = "current_window_position";
    public static String SHOW_VIDEO = "show_video";

    private boolean mTwoPaneMode = false;
    private TextView mPlaceHolder = null;
    private TextView mNoVideo = null;
    private TextView mExerciseSteps = null;
    private String mVideoURI = null;
    private String mVideoURIStr = null;
    private int mVideoPosition = -1;
    private String mExerciseID = "-1";
    private OnFragmentInteractionListener mListener;
    private boolean mDetailViewInitState = true;
    private YouTubePlayer mYoutubePlayer = null;
    private FrameLayout mFrameLayoutForVideo = null;
    boolean initSuccess = false;

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    // Constructor
    public ExerciseDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExerciseDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExerciseDetailFragment newInstance(String param1, String param2) {
        ExerciseDetailFragment fragment = new ExerciseDetailFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG,"onCreateView!");
        // Inflate the layout for this fragment
        View fragmentRootView = inflater.inflate(R.layout.fragment_exercise_detail2, container, false);

        // https://stackoverflow.com/questions/44000377/how-to-integrate-youtube-to-fragment?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
        mExerciseSteps = (TextView) fragmentRootView.findViewById(R.id.exercise_steps);
        mNoVideo = (TextView) fragmentRootView.findViewById(R.id.text_no_video);
        mPlaceHolder = (TextView) fragmentRootView.findViewById(R.id.text_get_start);
        mFrameLayoutForVideo = (FrameLayout)fragmentRootView.findViewById(R.id.youtube_fragment);

        // TODO: Restore the saved instance state
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(CURRENT_EXERCISE_KEY)) {
                mExerciseID = savedInstanceState.getString(CURRENT_EXERCISE_KEY);
            }
            if (savedInstanceState.containsKey(CURRENT_EXERCISE_STEPS)) {
                mExerciseSteps.setText(savedInstanceState.getString(CURRENT_EXERCISE_STEPS));
            }
            if (savedInstanceState.containsKey(CURRENT_EXERCISE_VIDEO)) {
                // TODO: Remember to replace the real video URL
                // mVideoURI = Uri.parse(savedInstanceState.getString(CURRENT_EXERCISE_VIDEO));
                mVideoURIStr = savedInstanceState.getString(CURRENT_EXERCISE_VIDEO);
            }
            if (savedInstanceState.containsKey(CURRENT_VIDEO_POSITION_KEY)) {
                mVideoPosition = savedInstanceState.getInt(CURRENT_VIDEO_POSITION_KEY);
            }
            if (savedInstanceState.containsKey(SHOW_VIDEO)) {
                mDetailViewInitState = savedInstanceState.getBoolean(SHOW_VIDEO);
            }
            // Restore the previous states if we have savedInstanceState
            initializeYoutubeFragment();
        }

        Log.d(TAG,"============== View initial state is ? " + mDetailViewInitState + " ============");
        if (mDetailViewInitState) {
            showGetStartPlaceHolderStr(mDetailViewInitState);
        }
        return fragmentRootView;
    }

    public void showVideo(boolean show) {
        if (show) {
            // if there is video for the exercise
            mNoVideo.setVisibility(View.GONE);
            mPlaceHolder.setVisibility(View.GONE);
            mExerciseSteps.setVisibility(View.VISIBLE);
            mDetailViewInitState = false;
        } else {
            // if no video for the exercise, only show steps
            mNoVideo.setVisibility(View.VISIBLE);
            mExerciseSteps.setVisibility(View.VISIBLE);
            mPlaceHolder.setVisibility(View.GONE);
            //mStepVideoView.setVisibility(View.GONE);
        }
    }

    public void showGetStartPlaceHolderStr(boolean isInit) {
        if (isInit) {
            Log.d(TAG,"======= Init is true so show the Initial place holder string! =======");
            mPlaceHolder.setVisibility(View.VISIBLE); // No Exercise is selected!
            // Not show video until the video is clicked
            mNoVideo.setVisibility(View.GONE);
            mExerciseSteps.setVisibility(View.GONE);
        } else {
            Log.d(TAG,"Not in the initial state!");
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // if Activity need to implement a call back listenr
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    // ====== Initialize the YouTubePlayerFragment Method =======
    /**
     * Initialize a YouTubePlayer, which can be used to play videos and control video playback.
     * One of the callbacks in listener will be invoked when the initialization succeeds or fails.
     */
    //Call this method after getting mVideoId
    public void initializeYoutubeFragment() {

        YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();


        youTubePlayerFragment.initialize(API_KEY, new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
                if (!wasRestored) {
                    mYoutubePlayer = player;
                    mYoutubePlayer.setShowFullscreenButton(false);
                    if (mVideoURIStr != null || !mVideoURIStr.isEmpty()) {
                        Log.d(TAG,"DEBUG:::onInitializationSuccess: Video Position -> " + mVideoPosition);
                        if (mVideoPosition > 0) {
                            // Resume the Video position
                            Log.d(TAG,"onInitializationSuccess - resume video position!");
                            //mYoutubePlayer.seekToMillis(mVideoPosition);
                            mYoutubePlayer.cueVideo(mVideoURIStr,mVideoPosition);
                            //mYoutubePlayer.play(); // start to play the video if we have a position
                            // mYoutubePlayer.seekToMillis(mVideoPosition);
                        } else {
                            Log.d(TAG,"DEBUG:::cueVideo without position");
                            mYoutubePlayer.cueVideo(mVideoURIStr);
                        }
                        initSuccess = true;
                    } else {
                        Toast.makeText(getContext(),getString(R.string.invalid_video_url),Toast.LENGTH_LONG);
                        initSuccess = false;
                        //showVideo(false);
                    }
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {
                Log.e(TAG,"unable to initialize the YouTube Player");
            }
        });
        // if only when the Detail Fragment is already attached to the parent activity class, add the youtubeplayer fragment
        if (isAdded()) {
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.add(R.id.youtube_fragment, youTubePlayerFragment).commit();
            mFrameLayoutForVideo.setVisibility(View.VISIBLE);
            showVideo(initSuccess);
        } else {
            Log.e(TAG,"Detail Fragment is not attached to the parent activity");
        }

    }
    // ========================================================================

    /**
     * when exercise is clicked, set the fragment data.
     * setFragmentData
     * @param bundle
     */
    public void setFragmentData(Bundle bundle) {
        String exerciseID = bundle.getString(ExerciseDetailActivity.EXERCISE_KEY);
        mExerciseSteps.setText(bundle.getString(ExerciseDetailActivity.EXERCISE_STEPS));
        mVideoURIStr = bundle.getString(ExerciseDetailActivity.EXERCISE_VIDEO_URL);
        Log.d(TAG, "setFragmentData:" + mExerciseSteps.getText());
    }

    private void resumeVideoSettings(){
        Bundle savedInstanceState = null;

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(CURRENT_EXERCISE_KEY)) {
                mExerciseID = savedInstanceState.getString(CURRENT_EXERCISE_KEY);
            }
            if (savedInstanceState.containsKey(CURRENT_EXERCISE_STEPS)) {
                mExerciseSteps.setText(savedInstanceState.getString(CURRENT_EXERCISE_STEPS));
            }
            if (savedInstanceState.containsKey(CURRENT_EXERCISE_VIDEO)) {
                // TODO: Remember to replace the real video URL
                // mVideoURI = Uri.parse(savedInstanceState.getString(CURRENT_EXERCISE_VIDEO));
                mVideoURIStr = savedInstanceState.getString(CURRENT_EXERCISE_VIDEO);
            }
            if (savedInstanceState.containsKey(CURRENT_VIDEO_POSITION_KEY)) {
                mVideoPosition = savedInstanceState.getInt(CURRENT_VIDEO_POSITION_KEY);
            }
            if (savedInstanceState.containsKey(SHOW_VIDEO)) {
                mDetailViewInitState = savedInstanceState.getBoolean(SHOW_VIDEO);
            }
            // Restore the previous states if we have savedInstanceState
            initializeYoutubeFragment();
        }

    }

    public void setData(String videoURI) {

        mVideoURIStr = videoURI;
        if (mVideoURIStr != null) {
            initializeYoutubeFragment();
            showVideo(true);
        } else {
            showVideo(false);
        }
    }

    public void setTwoPaneMode(boolean isTwoPane){

        mTwoPaneMode = isTwoPane;
    }


    @Override
    public void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
        if(mYoutubePlayer != null){
            // save the state of the video and release the resources
            Log.d(TAG, "background - onPause - save the video position before releasing the player!");
            mVideoPosition = mYoutubePlayer.getCurrentTimeMillis();
            // save the state of the video and release the resources
            // Service com.google.android.youtube.api.service.YouTubeService has leaked IntentReceiver
            releasePlayer();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
        super.onStop();
        if (mYoutubePlayer != null) {
            releasePlayer();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        if (mYoutubePlayer != null) {
            releasePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // come back to foreground mode
        Log.d(TAG, "onResume - initialize the player");
        Log.d(TAG, "===============onResume video URL is?" + mVideoURIStr + "=================");
        if (mVideoURIStr != null) {
            initializeYoutubeFragment();
            showVideo(true);
        } else {
            //showVideo(false);
            showGetStartPlaceHolderStr(mDetailViewInitState);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    // Release the player reference
    private void releasePlayer() {
    //mNotificationManager.cancelAll();
        if (mYoutubePlayer != null) {
            mYoutubePlayer.play();
            mYoutubePlayer.release();
            mYoutubePlayer = null;
        }
    }

    //TODO: Need to save the state of the video and the current step
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG,"==============onSaveInstanceState============");
        Log.d(TAG,"onSaveInstanceState mVideoPosition is:" + mVideoPosition);
        //mVideoPosition = mYoutubePlayer.getCurrentTimeMillis();
        outState.putInt(CURRENT_VIDEO_POSITION_KEY, mVideoPosition);
        outState.putString(CURRENT_EXERCISE_KEY, mExerciseID);
        outState.putString(CURRENT_EXERCISE_STEPS, (String)mExerciseSteps.getText());
        outState.putBoolean(SHOW_VIDEO, mDetailViewInitState);
        if (mVideoURIStr !=null) {
            outState.putString(CURRENT_EXERCISE_VIDEO, mVideoURIStr);
        }
        outState.putBoolean(SHOW_VIDEO,mDetailViewInitState); // remember the state of the detail view
    }
}
