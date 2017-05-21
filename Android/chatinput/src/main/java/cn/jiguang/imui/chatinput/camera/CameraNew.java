package cn.jiguang.imui.chatinput.camera;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.jiguang.imui.chatinput.R;
import cn.jiguang.imui.chatinput.listener.OnCameraCallbackListener;


public class CameraNew implements CameraSupport {

    private static final String TAG = "CameraNew";

    private Context mContext;
    private CameraDevice mCamera;
    private CameraManager mManager;
    private File mPhoto;
    private TextureView mTextureView;
    private String mCameraId;
    private Size mPreviewSize;
    private CameraCaptureSession mPreviewSession;
    private CaptureRequest.Builder mBuilder;
    private HandlerThread mBackgroundThread;
    private Handler mBackgroundHandler;
    private int mWidth;
    private int mHeight;
    private static SparseIntArray ORIENTATIONS = new SparseIntArray();
    private static SparseIntArray INVERSE_ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 0);
        ORIENTATIONS.append(Surface.ROTATION_90, 90);
        ORIENTATIONS.append(Surface.ROTATION_180, 180);
        ORIENTATIONS.append(Surface.ROTATION_270, 270);
    }

    static {
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_0, 270);
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_90, 180);
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_180, 90);
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_270, 0);
    }

    private static final int SENSOR_ORIENTATION_DEFAULT_DEGREES = 90;
    private static final int SENSOR_ORIENTATION_INVERSE_DEGREES = 270;
    private final static int REQUEST_CAMERA_PERMISSION = 200;
    private OnCameraCallbackListener mOnCameraCallbackListener;
    private Size mVideoSize;
    private MediaRecorder mMediaRecorder;
    private String mNextVideoAbsolutePath;
    private Integer mSensorOrientation;
    private boolean mIsFacingBack = true;
    private Surface mSurface;

    public CameraNew(final Context context, TextureView textureView) {
        this.mContext = context;
        this.mManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        this.mTextureView = textureView;
    }

    public void setCameraCallbackListener(OnCameraCallbackListener listener) {
        mOnCameraCallbackListener = listener;
    }

    @Override
    public CameraSupport open(final int cameraId, int width, int height, boolean isFacingBack) {
        mWidth = width;
        mHeight = height;
        mIsFacingBack = isFacingBack;
        try {
            mCameraId = cameraId + "";
            CameraCharacteristics characteristics = mManager.getCameraCharacteristics(mCameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null;
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                Log.e("CameraNew", "Lacking privileges to access aurora_menuitem_camera service, please request permission first.");
                return null;
            }
            mManager.openCamera(mCameraId, mStateCallback, null);
            startBackgroundThread();
        } catch (Exception e) {
            // TODO handle
            e.printStackTrace();
        }
        return this;
    }

    CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            mCamera = camera;
            createCameraPreview();

        }

        @Override
        public void onDisconnected(CameraDevice camera) {
            // TODO handle
            mCamera.close();
            mCamera = null;
        }

        @Override
        public void onError(CameraDevice camera, int error) {
            mCamera.close();
            mCamera = null;
        }
    };

    CameraCaptureSession.CaptureCallback captureCallbackListener = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);
//            createCameraPreview();
        }
    };

    public void createCameraPreview() {
        try {
            SurfaceTexture texture = mTextureView.getSurfaceTexture();
            assert texture != null;
            CameraCharacteristics characteristics = mManager.getCameraCharacteristics(mCameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            int deviceOrientation = ((Activity) mContext).getWindowManager().getDefaultDisplay().getOrientation();
            int totalRotation = sensorToDeviceRotation(characteristics, deviceOrientation);
            boolean swapRotation = totalRotation == 90 || totalRotation == 270;
            int rotatedWidth = mWidth;
            int rotatedHeight = mHeight;
            if (swapRotation) {
                rotatedWidth = mHeight;
                rotatedHeight = mWidth;
            }
            Size[] sizes = map.getOutputSizes(SurfaceTexture.class);
            mVideoSize = chooseVideoSize(map.getOutputSizes(MediaRecorder.class));
            mPreviewSize = getPreferredPreviewSize(sizes, rotatedWidth, rotatedHeight);
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            mSurface = new Surface(texture);
            mBuilder = mCamera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mBuilder.addTarget(mSurface);
            mCamera.createCaptureSession(Arrays.asList(mSurface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                    if (null == mCamera) {
                        return;
                    }
                    mPreviewSession = cameraCaptureSession;
                    updatePreview();
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(mContext, "Camera configuration change", Toast.LENGTH_SHORT).show();
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private Size chooseVideoSize(Size[] choices) {
        for (Size option : choices) {
            if (option.getWidth() == option.getHeight() * 4 / 3 && option.getWidth() <= 1080) {
                return option;
            }
        }
        Log.e(TAG, "Can't find any suitable video size");
        return choices[choices.length - 1];
    }

    private Size getPreferredPreviewSize(Size[] sizes, int width, int height) {
        List<Size> collectorSizes = new ArrayList<>();
        for (Size option : sizes) {
            if (option.getWidth() == width && option.getHeight() == height) {
                return option;
            }
            if (width > height) {
                if (option.getWidth() > width && option.getHeight() > height) {
                    collectorSizes.add(option);
                }
            } else {
                if (option.getHeight() > width && option.getWidth() > height) {
                    collectorSizes.add(option);
                }
            }
        }
        if (collectorSizes.size() > 0) {
            return Collections.min(collectorSizes, new Comparator<Size>() {
                @Override
                public int compare(Size s1, Size s2) {
                    return Long.signum(s1.getWidth() * s1.getHeight() - s2.getWidth() * s2.getHeight());
                }
            });
        }
        return sizes[0];
    }

    private void updatePreview() {
        if (null == mCamera) {
            Log.e("CameraNew", "Update preview error, return");
        }
        mBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        try {
            mPreviewSession.setRepeatingRequest(mBuilder.build(), captureCallbackListener, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    public void stopBackgroundThread() {
        if (mBackgroundThread != null) {
            mBackgroundThread.quitSafely();
            try {
                mBackgroundThread.join();
                mBackgroundThread = null;
                mBackgroundHandler = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getOrientation(final int cameraId) {
        try {
            String[] cameraIds = mManager.getCameraIdList();
            CameraCharacteristics characteristics = mManager.getCameraCharacteristics(cameraIds[cameraId]);
            return characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
        } catch (CameraAccessException e) {
            // TODO handle
            e.printStackTrace();
            return 0;
        }
    }

    private int sensorToDeviceRotation(CameraCharacteristics characteristics, int deviceOrientation) {
        mSensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
        deviceOrientation = ORIENTATIONS.get(deviceOrientation);
        return (mSensorOrientation + deviceOrientation + 360) % 360;
    }

    @Override
    public void release() {
        closePreviewSession();
        if (mCamera != null) {
            mCamera.close();
            mCamera = null;
        }
        if (mMediaRecorder != null) {
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
        stopBackgroundThread();
    }

    @Override
    public void setOutputFile(File file) {
        this.mPhoto = file;
    }

    @Override
    public void takePicture() {
        if (null == mCamera) {
            return;
        }
        try {
            CameraCharacteristics characteristics = mManager.getCameraCharacteristics(mCamera.getId());
            Size[] jpegSizes = null;
            if (characteristics != null) {
                jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                        .getOutputSizes(ImageFormat.JPEG);
            }
            int width = 640;
            int height = 480;
            if (jpegSizes != null && 0 < jpegSizes.length) {
                width = jpegSizes[0].getWidth();
                height = jpegSizes[0].getHeight();
            }
            final ImageReader reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
            List<Surface> outputSurfaces = new ArrayList<Surface>(2);
            outputSurfaces.add(reader.getSurface());
            outputSurfaces.add(new Surface(mTextureView.getSurfaceTexture()));
            final CaptureRequest.Builder builder = mCamera.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            builder.addTarget(reader.getSurface());
            builder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
            builder.set(CaptureRequest.JPEG_ORIENTATION, getOrientation(Integer.parseInt(mCameraId)));
            ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader imageReader) {
                    Image image = null;
                    try {
                        image = imageReader.acquireLatestImage();
                        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                        byte[] bytes = new byte[buffer.capacity()];
                        buffer.get(bytes);
                        save(bytes);
                        if (mOnCameraCallbackListener != null) {
                            mOnCameraCallbackListener.onTakePictureCompleted(mPhoto.getAbsolutePath());
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (image != null) {
                            image.close();
                        }
                    }

                }
            };
            reader.setOnImageAvailableListener(readerListener, mBackgroundHandler);
            final CameraCaptureSession.CaptureCallback captureCallback = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                    if (mSurface != null) {
                        mSurface.release();
                    }
                    createCameraPreview();
                }
            };
            mCamera.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                    try {
                        cameraCaptureSession.capture(builder.build(), captureCallback, mBackgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {

                }
            }, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void save(byte[] bytes) throws IOException {
        OutputStream outputStream = new FileOutputStream(mPhoto);
        // 前置摄像头水平翻转照片
        if (!mIsFacingBack) {
            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            int w = bmp.getWidth();
            int h = bmp.getHeight();
            Matrix matrix = new Matrix();
            matrix.postScale(-1, 1);
            Bitmap convertBmp = Bitmap.createBitmap(bmp, 0, 0, w, h, matrix, true);
            convertBmp.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        } else {
            outputStream.write(bytes);
        }
        outputStream.close();
    }

    public void setUpMediaRecorder() {
        if (null == mContext) {
            return;
        }
        Activity activity = (Activity) mContext;
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mMediaRecorder.setVideoSize(mVideoSize.getWidth(), mVideoSize.getHeight());
        mMediaRecorder.setVideoFrameRate(30);
        mNextVideoAbsolutePath = getVideoFilePath(activity);
        mMediaRecorder.setOutputFile(mNextVideoAbsolutePath);
        mMediaRecorder.setVideoEncodingBitRate(10000000);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        // if the device's natural orientation is portrait:
        if ((rotation == Surface.ROTATION_0
                || rotation == Surface.ROTATION_180) && height > width ||
                (rotation == Surface.ROTATION_90
                        || rotation == Surface.ROTATION_270) && width > height) {
            switch (rotation) {
                case Surface.ROTATION_0:
                    Log.e(TAG, "Rotation 0");
                    if (mIsFacingBack) {
                        mMediaRecorder.setOrientationHint(90);
                    } else {
                        mMediaRecorder.setOrientationHint(270);
                    }
                    break;
                case Surface.ROTATION_90:
                    Log.e(TAG, "Rotation 90");
                    if (mIsFacingBack) {
                        mMediaRecorder.setOrientationHint(0);
                    } else {
                        mMediaRecorder.setOrientationHint(180);
                    }
                    break;
                case Surface.ROTATION_180:
                    Log.e(TAG, "Rotation 180");
                    if (mIsFacingBack) {
                        mMediaRecorder.setOrientationHint(270);
                    } else {
                        mMediaRecorder.setOrientationHint(90);
                    }
                    break;
                case Surface.ROTATION_270:
                    Log.e(TAG, "Rotation 270");
                    if (mIsFacingBack) {
                        mMediaRecorder.setOrientationHint(180);
                    } else {
                        mMediaRecorder.setOrientationHint(0);
                    }
                    break;
                default:
                    Log.e(TAG, "Unknown screen orientation. Defaulting to " +
                            "portrait.");
                    if (mIsFacingBack) {
                        mMediaRecorder.setOrientationHint(90);
                    } else {
                        mMediaRecorder.setOrientationHint(270);
                    }
                    break;
            }
        }
        try {
            mMediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startRecordingVideo() {
        if (null == mCamera || !mTextureView.isAvailable() || mPreviewSize == null) {
            return;
        }
        try {
            if (mOnCameraCallbackListener != null) {
                mOnCameraCallbackListener.onStartVideoRecord();
            }
            closePreviewSession();
            setUpMediaRecorder();
            SurfaceTexture texture = mTextureView.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            mBuilder = mCamera.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
            List<Surface> surfaces = new ArrayList<>();
            // Set up Surface for aurora_menuitem_camera preview
            Surface previewSurface = new Surface(texture);
            surfaces.add(previewSurface);
            mBuilder.addTarget(previewSurface);

            // set up Surface for MediaRecorder
            Surface recorderSurface = mMediaRecorder.getSurface();
            surfaces.add(recorderSurface);
            mBuilder.addTarget(recorderSurface);

            mCamera.createCaptureSession(surfaces, new CameraCaptureSession.StateCallback() {

                @Override
                public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                    mPreviewSession = cameraCaptureSession;
                    updatePreview();
                    mMediaRecorder.start();
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(mContext, mContext.getString(R.string.record_video_failed),
                            Toast.LENGTH_SHORT).show();
                }
            }, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void cancelRecordingVideo() {
        resetRecordState();
        startPreview();
        if (mNextVideoAbsolutePath != null) {
            File file = new File(mNextVideoAbsolutePath);
            if (file.exists() && file.isFile()) {
                file.delete();
            }
        }
        if (mOnCameraCallbackListener != null) {
            mOnCameraCallbackListener.onCancelVideoRecord();
        }
    }

    @Override
    public String finishRecordingVideo() {
        resetRecordState();
        if (mOnCameraCallbackListener != null) {
            mOnCameraCallbackListener.onFinishVideoRecord(mNextVideoAbsolutePath);
        }
        return mNextVideoAbsolutePath;
    }

    private void resetRecordState() {
        try {
            if (mPreviewSession != null) {
                mPreviewSession.stopRepeating();
                mPreviewSession.abortCaptures();
            }
            if (mMediaRecorder != null) {
                mMediaRecorder.stop();
                mMediaRecorder.reset();
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
            Toast.makeText(mContext, mContext.getString(R.string.record_video_failed), Toast.LENGTH_SHORT).show();
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

    private void closePreviewSession() {
        if (mPreviewSession != null) {
            mPreviewSession.close();
            mPreviewSession = null;
        }
    }

    private void startPreview() {
        if (null == mCamera || !mTextureView.isAvailable() || null == mPreviewSize) {
            return;
        }
        try {
            SurfaceTexture texture = mTextureView.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            mBuilder = mCamera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);

            Surface previewSurface = new Surface(texture);
            mBuilder.addTarget(previewSurface);

            mCamera.createCaptureSession(Arrays.asList(previewSurface), new CameraCaptureSession.StateCallback() {

                @Override
                public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                    mPreviewSession = cameraCaptureSession;
                    updatePreview();
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
                    Log.e(TAG, "Preview failed");
                }
            }, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
}
