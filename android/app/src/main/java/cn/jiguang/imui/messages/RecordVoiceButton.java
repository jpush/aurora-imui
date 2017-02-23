package cn.jiguang.imui.messages;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import cn.jiguang.imui.R;

public class RecordVoiceButton extends Button {

    private final static String TAG = "RecordVoiceButton";

    private File myRecAudioFile;

    private static final int MIN_INTERVAL_TIME = 1000;// 1s
    private final static int CANCEL_RECORD = 5;
    private final static int START_RECORD = 7;
    private final static int RECORD_DENIED_STATUS = 1000;
    //依次为按下录音键坐标、手指离开屏幕坐标、手指移动坐标
    float mTouchY1, mTouchY2, mTouchY;
    private final float MIN_CANCEL_DISTANCE = 300f;
    //依次为开始录音时刻，按下录音时刻，松开录音按钮时刻
    private long startTime, time1, time2;

    private Dialog recordIndicator;

    private ImageView mVolumeIv;
    private TextView mRecordHintTv;

    private MediaRecorder recorder;

    private ObtainDecibelThread mThread;

    private Handler mVolumeHandler;
    public static boolean mIsPressed = false;
    private Context mContext;
    private Timer timer = new Timer();
    private Timer mCountTimer;
    private boolean isTimerCanceled = false;
    private boolean mTimeUp = false;
    private final MyHandler myHandler = new MyHandler(this);
    private static Drawable[] res;
    private RecordVoiceBtnStyle mStyle;
    private RecordVoiceListener mListener;

    public RecordVoiceButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        init(context, attrs);
    }

    public RecordVoiceButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mStyle = RecordVoiceBtnStyle.parse(context, attrs);
        mVolumeHandler = new ShowVolumeHandler(this);
        res = new Drawable[]{mStyle.getMic_1(), mStyle.getMic_2(), mStyle.getMic_3(),
                mStyle.getMic_4(), mStyle.getMic_5(), mStyle.getCancelRecord()};
    }

    public void setRecordVoiceListener(RecordVoiceListener listener) {
        mListener = listener;
    }

    /**
     * Require, must set file path and file name before recording
     * @param path file to be saved.
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
        myRecAudioFile = new File(path, fileName + ".amr");
        Log.i(TAG, "Create file success file path: " + myRecAudioFile.getAbsolutePath());
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
                this.setText(mStyle.getTapDownText());
                mIsPressed = true;
                time1 = System.currentTimeMillis();
                mTouchY1 = event.getY();
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
                    this.setText(mStyle.getVoiceBtnText());
                    mIsPressed = false;
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
                this.setText(mStyle.getVoiceBtnText());
                mIsPressed = false;
                this.setPressed(false);
                mTouchY2 = event.getY();
                time2 = System.currentTimeMillis();
                if (time2 - time1 < 500) {
                    cancelTimer();
                    return true;
                } else if (time2 - time1 < 1000) {
                    cancelRecord();
                } else if (mTouchY1 - mTouchY2 > MIN_CANCEL_DISTANCE) {
                    cancelRecord();
                } else if (time2 - time1 < 60000)
                    finishRecord();
                break;
            case MotionEvent.ACTION_MOVE:
                mTouchY = event.getY();
                //手指上滑到超出限定后，显示松开取消发送提示
                if (mTouchY1 - mTouchY > MIN_CANCEL_DISTANCE) {
                    this.setText(mContext.getString(R.string.cancel_record_voice_hint));
                    mVolumeHandler.sendEmptyMessage(CANCEL_RECORD);
                    if (mThread != null) {
                        mThread.exit();
                    }
                    mThread = null;
                } else {
                    this.setText(mContext.getString(R.string.release_send_voice_hint));
                    if (mThread == null) {
                        mThread = new ObtainDecibelThread();
                        mThread.start();
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:// 当手指移动到view外面，会cancel
                this.setText(mStyle.getVoiceBtnText());
                cancelRecord();
                break;
        }

        return true;
    }


    private void cancelTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            isTimerCanceled = true;
        }
        if (mCountTimer != null) {
            mCountTimer.cancel();
            mCountTimer.purge();
        }
    }

    private Timer createTimer() {
        timer = new Timer();
        isTimerCanceled = false;
        return timer;
    }

    private void initDialogAndStartRecord() {
        recordIndicator = new Dialog(getContext());
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_record_voice, null);
        recordIndicator.setContentView(view);
        mVolumeIv = (ImageView) recordIndicator.findViewById(R.id.jmui_volume_hint_iv);
        mRecordHintTv = (TextView) recordIndicator.findViewById(R.id.jmui_record_voice_tv);
        mRecordHintTv.setText(mContext.getString(R.string.move_to_cancel_hint));
        startRecording();
        recordIndicator.show();
    }

    //录音完毕加载 ListView item
    private void finishRecord() {
        cancelTimer();
        stopRecording();
        if (recordIndicator != null) {
            recordIndicator.dismiss();
        }

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
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //某些手机会限制录音，如果用户拒接使用录音，则需判断mp是否存在
                if (mp != null) {
                    int duration = mp.getDuration() / 1000;//即为时长 是s
                    if (duration < 1) {
                        duration = 1;
                    } else if (duration > 60) {
                        duration = 60;
                    }
                    // TODO finish callback here
                    if (null != mListener) {
                        mListener.onFinishRecord();
                    }
                } else {
                    Toast.makeText(mContext, mContext.getString(R.string.record_voice_permission_request),
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    //取消录音，清除计时
    private void cancelRecord() {
        //可能在消息队列中还存在HandlerMessage，移除剩余消息
        mVolumeHandler.removeMessages(56, null);
        mVolumeHandler.removeMessages(57, null);
        mVolumeHandler.removeMessages(58, null);
        mVolumeHandler.removeMessages(59, null);
        mTimeUp = false;
        cancelTimer();
        stopRecording();
        if (recordIndicator != null) {
            recordIndicator.dismiss();
        }
        if (myRecAudioFile != null) {
            myRecAudioFile.delete();
        }
    }

    private void startRecording() {
        try {
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
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
            mCountTimer = new Timer();
            mCountTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mTimeUp = true;
                    android.os.Message msg = mVolumeHandler.obtainMessage();
                    msg.what = 55;
                    Bundle bundle = new Bundle();
                    bundle.putInt("restTime", 5);
                    msg.setData(bundle);
                    msg.sendToTarget();
                    mCountTimer.cancel();
                }
            }, 56000);

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
            }catch (Exception e){
                Log.d("RecordVoice", "Catch exception: stop recorder failed!");
            }finally {
                recorder.release();
                recorder = null;
            }
        }
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
        if (recordIndicator != null) {
            recordIndicator.dismiss();
        }
        this.setText(mStyle.getVoiceBtnText());
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
                int restTime = msg.getData().getInt("restTime", -1);
                // 若restTime>0, 进入倒计时
                if (restTime > 0) {
                    controller.mTimeUp = true;
                    android.os.Message msg1 = controller.mVolumeHandler.obtainMessage();
                    msg1.what = 60 - restTime + 1;
                    Bundle bundle = new Bundle();
                    bundle.putInt("restTime", restTime - 1);
                    msg1.setData(bundle);
                    //创建一个延迟一秒执行的HandlerMessage，用于倒计时
                    controller.mVolumeHandler.sendMessageDelayed(msg1, 1000);
                    controller.mRecordHintTv.setText(String.format(controller.mContext
                            .getString(R.string.rest_record_time_hint), restTime + ""));
                    // 倒计时结束，发送语音, 重置状态
                } else if (restTime == 0) {
                    controller.finishRecord();
                    controller.setPressed(false);
                    controller.mTimeUp = false;
                    // restTime = -1, 一般情况
                } else {
                    // 没有进入倒计时状态
                    if (!controller.mTimeUp) {
                        if (msg.what < CANCEL_RECORD) {
                            controller.mRecordHintTv.setText(controller.mContext.getString(R.string
                                    .move_to_cancel_hint));
                        }
                        else {
                            controller.mRecordHintTv.setText(controller.mContext.getString(R.string
                                    .cancel_record_voice_hint));
                        }
                        // 进入倒计时
                    } else {
                        if (msg.what == CANCEL_RECORD) {
                            controller.mRecordHintTv.setText(controller.mContext.getString(R.string
                                    .cancel_record_voice_hint));
                            if (!mIsPressed) {
                                controller.cancelRecord();
                            }
                        }
                    }
                    controller.mVolumeIv.setImageDrawable(res[msg.what]);
                }
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

    /**
     * Callback will invoked when record voice is finished
     */
    public interface RecordVoiceListener {

        void onFinishRecord();


    }
}
