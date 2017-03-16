package cn.jiguang.imui.messages;


import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.Log;
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

    private Camera camera;
    private TextureView mTextureView;
    private int mWidth;
    private int mHeight;
    private File mPhoto;
    private OnCameraCallbackListener mCameraCallbackListener;

    public CameraOld(int width, int height, TextureView textureView) {
        this.mWidth = width;
        this.mHeight = height;
        this.mTextureView = textureView;
    }

    @Override
    public CameraSupport open(int cameraId, int width, int height) {
        this.camera = Camera.open(cameraId);
        Camera.Parameters params = camera.getParameters();
        /*获取摄像头支持的PictureSize列表*/
        List<Size> pictureSizeList = params.getSupportedPictureSizes();
        /*从列表中选取合适的分辨率*/
        Size picSize = getProperSize(pictureSizeList, ((float) width) / height);
        if (null != picSize) {
            params.setPictureSize(picSize.width, picSize.height);
        } else {
            picSize = params.getPictureSize();
        }
        /*获取摄像头支持的PreviewSize列表*/
        List<Size> previewSizeList = params.getSupportedPreviewSizes();
        Size preSize = getProperSize(previewSizeList, ((float) width) / height);
        if (null != preSize) {
            Log.v("CameraOld", preSize.width + "," + preSize.height);
            params.setPreviewSize(preSize.width, preSize.height);
        }

        /*根据选出的PictureSize重新设置SurfaceView大小*/
        float w = picSize.width;
        float h = picSize.height;
        mTextureView.setLayoutParams(new RelativeLayout.LayoutParams((int) (height * (w / h)), height));

        params.setJpegQuality(100); // 设置照片质量
        if (params.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }


        camera.cancelAutoFocus();//只有加上了这一句，才会自动对焦。
        params.setPictureFormat(ImageFormat.JPEG);
        camera.setParameters(params);
        try {
            camera.setPreviewTexture(mTextureView.getSurfaceTexture());
            camera.setDisplayOrientation(90);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this;
    }

    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {
            if (mPhoto == null) {
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(mPhoto);
                fos.write(bytes);
                fos.close();

                // TODO take picture callback
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public int getOrientation(int cameraId) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        return info.orientation;
    }

    @Override
    public void release() {
        camera.setPreviewCallback(null);
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    @Override
    public void setOutputFile(File file) {
        this.mPhoto = file;
    }

    @Override
    public void takePicture() {
        camera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {
                OutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(mPhoto);
                    outputStream.write(bytes);
                    outputStream.close();
                    if (mCameraCallbackListener != null) {
                        mCameraCallbackListener.onTakePictureCompleted(mPhoto);
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
}
