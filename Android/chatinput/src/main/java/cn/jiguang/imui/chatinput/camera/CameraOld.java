package cn.jiguang.imui.chatinput.camera;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.media.MediaRecorder;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.jiguang.imui.chatinput.listener.CameraEventListener;
import cn.jiguang.imui.chatinput.listener.OnCameraCallbackListener;

@SuppressWarnings("deprecation")
public class CameraOld implements CameraSupport {

    private final static String TAG = "CameraOld";

    private Camera mCamera;
    private TextureView mTextureView;
    private File mPhoto, mLastPhoto;
    private File mDir;
    private Context mContext;
    private OnCameraCallbackListener mCameraCallbackListener;
    private MediaRecorder mMediaRecorder;
    private String mNextVideoAbsolutePath;
    private int mCameraId;
    private Size mPreviewSize;
    private static final int SENSOR_ORIENTATION_DEFAULT_DEGREES = 90;
    private static final int SENSOR_ORIENTATION_INVERSE_DEGREES = 270;
    private static SparseIntArray ORIENTATIONS = new SparseIntArray();
    private CameraEventListener mCameraEventListener;
    private boolean mIsTakingPicture = false;

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 0);
        ORIENTATIONS.append(Surface.ROTATION_90, 90);
        ORIENTATIONS.append(Surface.ROTATION_180, 180);
        ORIENTATIONS.append(Surface.ROTATION_270, 270);
    }

    private boolean mIsFacingBack = true;

    public CameraOld(Context context, TextureView textureView) {
        this.mContext = context;
        this.mTextureView = textureView;
        initPhotoPath();
    }

    @Override
    public CameraSupport open(int cameraId, int width, int height, boolean isFacingBack) {
        try {
            this.mCameraId = cameraId;
            this.mCamera = Camera.open(cameraId);
            mIsFacingBack = isFacingBack;
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
            ViewGroup.LayoutParams layoutParams = new FrameLayout.LayoutParams((int) (height * (w / h)), height);
            mTextureView.setLayoutParams(layoutParams);

            params.setJpegQuality(100); // 设置照片质量
            if (params.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            }


            mCamera.cancelAutoFocus();//只有加上了这一句，才会自动对焦。
            params.setPictureFormat(ImageFormat.JPEG);
            mCamera.setParameters(params);
            mCamera.setPreviewTexture(mTextureView.getSurfaceTexture());
            mCamera.setDisplayOrientation(90);
            mCamera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    public int getOrientation(int cameraId) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        return info.orientation;
    }

    @Override
    public void release() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.lock();
            mCamera.release();
            mCamera = null;
        }
        releaseMediaRecorder();
        mIsTakingPicture = false;
    }

    private void initPhotoPath() {
        String path = mContext.getFilesDir().getAbsolutePath() + "/photo";
        mDir = new File(path);
        if (!mDir.exists()) {
            mDir.mkdirs();
        }
    }

    @Override
    public void takePicture() {
        if(mIsTakingPicture){
            Log.i(TAG,"Is taking picture now,please wait.");
            return;
        }
        mIsTakingPicture = true;
        mCamera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {
                try {
                    final Activity activity = (Activity) mContext;
                    if (null == activity || null == mCamera) {
                        return;
                    }
                    mPhoto = new File(mDir,
                            new SimpleDateFormat("yyyy-MM-dd-HHmmss", Locale.getDefault()).format(new Date())
                                    + ".png");
                    OutputStream outputStream = new FileOutputStream(mPhoto);
                    Matrix matrix = new Matrix();
                    Bitmap rotateBmp;
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    int w = bmp.getWidth();
                    int h = bmp.getHeight();
                    // 前置摄像头水平翻转照片
                    if (!mIsFacingBack) {
                        matrix.postScale(-1, 1);
                        matrix.postRotate(90);
                        rotateBmp = Bitmap.createBitmap(bmp, 0, 0, w, h, matrix, true);
                    } else {
                        matrix.postRotate(90);
                        rotateBmp = Bitmap.createBitmap(bmp, 0, 0, w, h, matrix, true);
                    }
                    rotateBmp.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    mCamera.startPreview();
                    outputStream.close();
                    if (mCameraCallbackListener != null) {
                        if(mLastPhoto != null && mLastPhoto.getAbsolutePath().equals(mPhoto.getAbsolutePath())) // Forbid repeat
                            return;
                        mCameraCallbackListener.onTakePictureCompleted(mPhoto.getAbsolutePath());
                        mLastPhoto = mPhoto;
                        mIsTakingPicture = false ;
                    }
                    if (mCameraEventListener != null) {
                        mCameraEventListener.onFinishTakePicture();
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

    @Override
    public void setCameraEventListener(CameraEventListener listener) {
        mCameraEventListener = listener;
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
        mNextVideoAbsolutePath = getVideoFilePath(activity);
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
    public void cancelRecordingVideo() {
        mMediaRecorder.stop();
        mMediaRecorder.reset();
        mCamera.startPreview();
        if (mNextVideoAbsolutePath != null) {
            File file = new File(mNextVideoAbsolutePath);
            if (file.exists() && file.isFile()) {
                file.delete();
            }
        }
    }

    @Override
    public String finishRecordingVideo() {
        mMediaRecorder.stop();
        mMediaRecorder.reset();
        if (mCameraCallbackListener != null) {
            mCameraCallbackListener.onFinishVideoRecord(mNextVideoAbsolutePath);
        }
        return mNextVideoAbsolutePath;
    }

}