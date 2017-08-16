package cn.jiguang.imui.chatinput.record;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.widget.AppCompatImageButton;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import cn.jiguang.imui.chatinput.R;
import cn.jiguang.imui.chatinput.listener.RecordVoiceListener;


public class RecordVoiceButton extends AppCompatImageButton {

    private final static String TAG = "RecordVoiceButton";

    private File myRecAudioFile;

    private static final int MIN_INTERVAL_TIME = 1000;// 1s
    private final static int CANCEL_RECORD = 5;
    private final static int START_RECORD = 7;
    private final static int RECORD_DENIED_STATUS = 1000;
    //依次为开始录音时刻，按下录音时刻
    private long startTime, time1;


    private MediaRecorder recorder;

    private ObtainDecibelThread mThread;

    private Handler mVolumeHandler;
    public static boolean mIsPressed = false;
    private Context mContext;
    private Timer timer = new Timer();
    private boolean isTimerCanceled = false;
    private boolean mTimeUp = false;
    private final MyHandler myHandler = new MyHandler(this);
    private RecordVoiceBtnStyle mStyle;
    private RecordVoiceListener mListener;
    private RecordControllerView mControllerView;
    private boolean mSetController = false;
    private int mDuration;

    public RecordVoiceButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init(context, attrs);
    }

    public RecordVoiceButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mStyle = RecordVoiceBtnStyle.parse(context, attrs);
        mVolumeHandler = new ShowVolumeHandler(this);
    }

    public void setRecordVoiceListener(RecordVoiceListener listener) {
        mListener = listener;
    }

    /**
     * Require, must set file path and file name before recording
     *
     * @param path     file to be saved.
     * @param fileName file name
     */
    public void setVoiceFilePath(String path, String fileName) {
        if (null == path || TextUtils.isEmpty(path)
                || null == fileName || TextUtils.isEmpty(fileName)) {
            throw new IllegalArgumentException("File path and file name must be set");
        }
        File destDir = new File(path);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        //录音文件的命名格式
        myRecAudioFile = new File(path, fileName + ".m4a");
        Log.i(TAG, "Create file success file path: " + myRecAudioFile.getAbsolutePath());
    }

    public String getRecordFile() {
        if (myRecAudioFile != null) {
            return myRecAudioFile.getAbsolutePath();
        }
        return null;
    }

    private boolean isSdCardExist() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.setPressed(true);
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //TODO animation
                float[] vaules = new float[]{0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f, 1.1f, 1.2f, 1.3f, 1.25f, 1.2f, 1.15f, 1.1f, 1.0f};
                AnimatorSet set = new AnimatorSet();
                set.playTogether(ObjectAnimator.ofFloat(this, "scaleX", vaules),
                        ObjectAnimator.ofFloat(this, "scaleY", vaules));
                set.setDuration(150);
                set.start();
                if (mControllerView != null && !mSetController) {
                    mControllerView.setRecordButton(this);
                    mSetController = true;
                }
//                this.setText(mStyle.getTapDownText());
                mIsPressed = true;
                time1 = System.currentTimeMillis();
                //检查sd卡是否存在
                if (isSdCardExist()) {
                    if (isTimerCanceled) {
                        timer = createTimer();
                    }
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            android.os.Message msg = myHandler.obtainMessage();
                            msg.what = START_RECORD;
                            msg.sendToTarget();
                        }
                    }, 500);
                } else {
                    Toast.makeText(this.getContext(), mContext.getString(R.string.sdcard_not_exist_toast),
                            Toast.LENGTH_SHORT).show();
                    this.setPressed(false);
                    mIsPressed = false;
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
                mIsPressed = false;
                this.setPressed(false);
                //松开录音按钮时刻
                long time2 = System.currentTimeMillis();
                if (time2 - time1 < 500) {
                    cancelTimer();
                    if (mControllerView != null) {
                        mControllerView.resetState();
                    }
                    return true;
                } else if (time2 - time1 < 1000) {
                    if (mControllerView != null) {
                        mControllerView.resetState();
                    }
                    cancelRecord();
                } else if (mControllerView != null) {
                    mControllerView.onActionUp();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mThread == null) {
                    mThread = new ObtainDecibelThread();
                    mThread.start();
                }
                if (mControllerView != null) {
                    mControllerView.onActionMove(event.getRawX(), event.getY());
                }
                break;
//            case MotionEvent.ACTION_CANCEL:// 当手指移动到view外面，会cancel
////                this.setText(mStyle.getVoiceBtnText());
//                cancelRecord();
//                break;
        }

        return true;
    }


    private void cancelTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            isTimerCanceled = true;
        }
    }

    private Timer createTimer() {
        timer = new Timer();
        isTimerCanceled = false;
        return timer;
    }

    private void initDialogAndStartRecord() {
        startRecording();
    }

    //录音完毕
    public void finishRecord(boolean isPreview) {
        cancelTimer();
        stopRecording();

        long intervalTime = System.currentTimeMillis() - startTime;
        if (intervalTime < MIN_INTERVAL_TIME) {
            Toast.makeText(getContext(), mContext.getString(R.string.time_too_short_toast), Toast.LENGTH_SHORT).show();
            myRecAudioFile.delete();
        } else {
            if (myRecAudioFile != null && myRecAudioFile.exists()) {
                MediaPlayer mp = new MediaPlayer();
                try {
                    FileInputStream fis = new FileInputStream(myRecAudioFile);
                    mp.setDataSource(fis.getFD());
                    mp.prepare();
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //某些手机会限制录音，如果用户拒接使用录音，则需判断mp是否存在
                if (mp != null) {
                    mDuration = mp.getDuration() / 1000;//即为时长 是s
                    if (mDuration < 1) {
                        mDuration = 1;
                    }
                    // TODO finish callback here
                    if (null != mListener && !isPreview) {
                        mListener.onFinishRecord(myRecAudioFile, mDuration);
                    }
                } else {
                    Toast.makeText(mContext, mContext.getString(R.string.record_voice_permission_request),
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void finishRecord() {
        if (null != mListener) {
            mListener.onFinishRecord(myRecAudioFile, mDuration);
        }
    }

    //取消录音，清除计时
    public void cancelRecord() {
        mTimeUp = false;
        cancelTimer();
        stopRecording();
        if (myRecAudioFile != null) {
            myRecAudioFile.delete();
        }

        if (mListener != null) {
            mListener.onCancelRecord();
        }
    }

    private void startRecording() {
        try {
            if (mListener != null) {
                mListener.onStartRecord();
            }
            if (mControllerView != null) {
                mControllerView.onActionDown();
            }
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            // .m4a 格式可以在 iOS 上直接播放
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC);
            recorder.setOutputFile(myRecAudioFile.getAbsolutePath());
            myRecAudioFile.createNewFile();
            recorder.prepare();
            recorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
                @Override
                public void onError(MediaRecorder mediaRecorder, int i, int i2) {
                    Log.i("RecordVoiceController", "recorder prepare failed!");
                }
            });
            recorder.start();
            startTime = System.currentTimeMillis();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(mContext, mContext.getString(R.string.illegal_state_toast), Toast.LENGTH_SHORT).show();
            cancelTimer();
            dismissDialog();
            if (mThread != null) {
                mThread.exit();
                mThread = null;
            }
            if (myRecAudioFile != null) {
                myRecAudioFile.delete();
            }
            recorder.release();
            recorder = null;
        } catch (NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(mContext, mContext.getString(R.string.aurora_record_voice_file_not_exist),
                    Toast.LENGTH_SHORT).show();
        } catch (RuntimeException e) {
            Toast.makeText(mContext, mContext.getString(R.string.record_voice_permission_denied),
                    Toast.LENGTH_SHORT).show();
            cancelTimer();
            dismissDialog();
            if (mThread != null) {
                mThread.exit();
                mThread = null;
            }
            if (myRecAudioFile != null) {
                myRecAudioFile.delete();
            }
            recorder.release();
            recorder = null;
        }


        mThread = new ObtainDecibelThread();
        mThread.start();

    }

    //停止录音，隐藏录音动画
    private void stopRecording() {
        if (mThread != null) {
            mThread.exit();
            mThread = null;
        }
        releaseRecorder();
    }

    public void releaseRecorder() {
        if (recorder != null) {
            try {
                recorder.stop();
            } catch (Exception e) {
                Log.d("RecordVoice", "Catch exception: stop recorder failed!");
            } finally {
                recorder.release();
                recorder = null;
            }
        }
    }

    public void setRecordController(RecordControllerView controllerView) {
        mControllerView = controllerView;
    }

    private class ObtainDecibelThread extends Thread {

        private volatile boolean running = true;

        public void exit() {
            running = false;
        }

        @Override
        public void run() {
            while (running) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (recorder == null || !running) {
                    break;
                }
                try {
                    int x = recorder.getMaxAmplitude();
                    if (x != 0) {
                        int f = (int) (10 * Math.log(x) / Math.log(10));
                        if (f < 20) {
                            mVolumeHandler.sendEmptyMessage(0);
                        } else if (f < 26) {
                            mVolumeHandler.sendEmptyMessage(1);
                        } else if (f < 32) {
                            mVolumeHandler.sendEmptyMessage(2);
                        } else if (f < 38) {
                            mVolumeHandler.sendEmptyMessage(3);
                        } else {
                            mVolumeHandler.sendEmptyMessage(4);
                        }
                    }
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    public void dismissDialog() {
//        this.setText(mStyle.getVoiceBtnText());
    }

    /**
     * 录音动画控制
     */
    private static class ShowVolumeHandler extends Handler {

        private final WeakReference<RecordVoiceButton> lButton;

        public ShowVolumeHandler(RecordVoiceButton button) {
            lButton = new WeakReference<RecordVoiceButton>(button);
        }

        @Override
        public void handleMessage(android.os.Message msg) {
            RecordVoiceButton controller = lButton.get();
            if (controller != null) {
                //TODO show wave view
            }
        }
    }

    private static class MyHandler extends Handler {
        private final WeakReference<RecordVoiceButton> lButton;

        public MyHandler(RecordVoiceButton button) {
            lButton = new WeakReference<RecordVoiceButton>(button);
        }

        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            RecordVoiceButton controller = lButton.get();
            if (controller != null) {
                switch (msg.what) {
                    case START_RECORD:
                        if (mIsPressed) {
                            controller.initDialogAndStartRecord();
                        }
                        break;
                }
            }
        }
    }

}
