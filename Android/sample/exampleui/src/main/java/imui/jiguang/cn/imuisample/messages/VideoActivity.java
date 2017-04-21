package imui.jiguang.cn.imuisample.messages;

import android.app.Activity;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import imui.jiguang.cn.imuisample.R;

public class VideoActivity extends Activity {

    public static final String VIDEO_PATH = "videoPath";

    private static final String CURRENT_POSITION = "currentPosition";

    private VideoView mVideoView;
    private MediaController mMediaController;

    private int mSavedCurrentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        String videoPath = getIntent().getStringExtra(VIDEO_PATH);

        mVideoView = (VideoView) findViewById(R.id.videoview_video);

        mMediaController = new MediaController(this);
        mMediaController.setAnchorView(mVideoView);

        mVideoView.setMediaController(mMediaController);
        mVideoView.setVideoPath(videoPath);
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mVideoView.requestLayout();
                if (mSavedCurrentPosition != 0) {
                    mVideoView.seekTo(mSavedCurrentPosition);
                    mSavedCurrentPosition = 0;
                } else {
                    play();
                }
            }
        });
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mVideoView.setKeepScreenOn(false);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVideoView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSavedCurrentPosition = mVideoView.getCurrentPosition();
        mVideoView.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        pause();
    }

    private void play() {
        mVideoView.start();
        mVideoView.setKeepScreenOn(true);
    }

    private void pause() {
        mVideoView.pause();
        mVideoView.setKeepScreenOn(false);
    }
}
