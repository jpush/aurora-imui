package cn.jiguang.imui.chatinput.camera;

import android.app.Activity;
import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.media.MediaRecorder;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@SuppressWarnings("deprecation")
public class CameraOld implements CameraSupport {

    private final static String TAG = "CameraOld";

    private Camera mCamera;
    private TextureView mTextureView;
    private File mPhoto;
    private Context mContext;
    private OnCameraCallbackListener mCameraCallbackListener;
    private MediaRecorder mMediaRecorder;
    private String mNextVideoAbsolutePath;
    private int mCameraId;
    private Size mPreviewSize;
    private static final int SENSOR_ORIENTATION_DEFAULT_DEGREES = 90;
    private static final int SENSOR_ORIENTATION_INVERSE_DEGREES = 270;
    private static SparseIntArray ORIENTATIONS = new SparseIntArray();
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 0);
        ORIENTATIONS.append(Surface.ROTATION_90, 90);
        ORIENTATIONS.append(Surface.ROTATION_180, 180);
        ORIENTATIONS.append(Surface.ROTATION_270, 270);
    }

    public CameraOld(Context context, TextureView textureView) {
        this.mContext = context;
        this.mTextureView = textureView;
    }

    @Override
    public CameraSupport open(int cameraId, int width, int height) {
        this.mCameraId = cameraId;
        this.mCamera = Camera.open(cameraId);
        Camera.Parameters params = mCamera.getParameters();
        /*获取摄像头支持的PictureSize列表*/
        List<Size> pictureSizeList = params.getSupportedPictureSizes();
        /*从列表中选取合适的分辨率*/
        mPreviewSize = getProperSize(pictureSizeList, ((float) width) / height);
        if (null != mPreviewSize) {
            params.setPictureSize(mPreviewSize.width, mPreviewSize.height);
        } else {
            mPreviewSize = params.getPictureSize();
        }
        /*获取摄像头支持的PreviewSize列表*/
        List<Size> previewSizeList = params.getSupportedPreviewSizes();
        Size preSize = getProperSize(previewSizeList, ((float) width) / height);
        if (null != preSize) {
            Log.v("CameraOld", preSize.width + "," + preSize.height);
            params.setPreviewSize(preSize.width, preSize.height);
        }

        /*根据选出的PictureSize重新设置SurfaceView大小*/
        float w = mPreviewSize.width;
        float h = mPreviewSize.height;
        mTextureView.setLayoutParams(new RelativeLayout.LayoutParams((int) (height * (w / h)), height));

        params.setJpegQuality(100); // 设置照片质量
        if (params.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }


        mCamera.cancelAutoFocus();//只有加上了这一句，才会自动对焦。
        params.setPictureFormat(ImageFormat.JPEG);
        mCamera.setParameters(params);
        try {
            mCamera.setPreviewTexture(mTextureView.getSurfaceTexture());
            mCamera.setDisplayOrientation(90);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this;
    }

    @Override
    public int getOrientation(int cameraId) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        return info.orientation;
    }

    @Override
    public void release() {
        mCamera.setPreviewCallback(null);
        mCamera.stopPreview();
        mCamera.lock();
        mCamera.release();
        mCamera = null;
        releaseMediaRecorder();
    }

    @Override
    public void setOutputFile(File file) {
        this.mPhoto = file;
    }

    @Override
    public void takePicture() {
        mCamera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {
                OutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(mPhoto);
                    outputStream.write(bytes);
                    outputStream.close();
                    if (mCameraCallbackListener != null) {
                        mCameraCallbackListener.onTakePictureCompleted(mPhoto.getAbsolutePath());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void setCameraCallbackListener(OnCameraCallbackListener listener) {
        mCameraCallbackListener = listener;
    }

    public static Size getProperSize(List<Size> sizeList, float displayRatio) {
        //先对传进来的size列表进行排序
        Collections.sort(sizeList, new SizeComparator());

        Size result = null;
        for (Size size : sizeList) {
            float curRatio = ((float) size.width) / size.height;
            if (curRatio - displayRatio == 0) {
                result = size;
            }
        }
        if (null == result) {
            for (Size size : sizeList) {
                float curRatio = ((float) size.width) / size.height;
                if (curRatio == 3f / 4) {
                    result = size;
                }
            }
        }
        return result;
    }

    private static class SizeComparator implements Comparator<Camera.Size> {
        @Override
        public int compare(Size lhs, Size rhs) {
            // TODO Auto-generated method stub
            if (lhs.width < rhs.width
                    || lhs.width == rhs.width && lhs.height < rhs.height) {
                return -1;
            } else if (!(lhs.width == rhs.width && lhs.height == rhs.height)) {
                return 1;
            }
            return 0;
        }

    }

    public void setUpMediaRecorder() {
        if (null == mContext) {
            return;
        }
        Activity activity = (Activity) mContext;
        mMediaRecorder = new MediaRecorder();
        mCamera.stopPreview();
        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        if (mNextVideoAbsolutePath == null || mNextVideoAbsolutePath.isEmpty()) {
            mNextVideoAbsolutePath = getVideoFilePath(activity);
        }
        mMediaRecorder.setOutputFile(mNextVideoAbsolutePath);
        mMediaRecorder.setVideoFrameRate(30);
        mMediaRecorder.setVideoEncodingBitRate(10000000);
        mMediaRecorder.setVideoSize(mPreviewSize.width, mPreviewSize.height);
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        switch (getOrientation(mCameraId)) {
            case SENSOR_ORIENTATION_DEFAULT_DEGREES:
                mMediaRecorder.setOrientationHint(ORIENTATIONS.get(rotation));
                break;
            case SENSOR_ORIENTATION_INVERSE_DEGREES:
                mMediaRecorder.setOrientationHint(rotation);
                break;
        }
        try {
            mMediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getVideoFilePath(Context context) {
        String fileDir = context.getFilesDir().getAbsolutePath() + "/video";
        File destDir = new File(fileDir);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        return destDir.getAbsolutePath() + "/" + System.currentTimeMillis() + ".mp4";
    }

    @Override
    public void startRecordingVideo() {
        try {
            setUpMediaRecorder();
            mMediaRecorder.start();
        } catch (RuntimeException e) {
            e.printStackTrace();
            Log.e(TAG, "MediaRecorder start error");
            releaseMediaRecorder();
        }
    }

    private void releaseMediaRecorder() {
        if (mMediaRecorder != null) {
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }

    @Override
    public void stopRecordingVideo() {
        mMediaRecorder.stop();
        mMediaRecorder.reset();
        Log.e(TAG, "Stop recording video");
        mCamera.startPreview();
        if (mCameraCallbackListener != null) {
            mCameraCallbackListener.onRecordVideoCompleted(mNextVideoAbsolutePath);
        }
        mNextVideoAbsolutePath = null;
    }
}
