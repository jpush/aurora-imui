package cn.jiguang.imui.messages;


import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.view.TextureView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@SuppressWarnings("deprecation")
public class CameraOld implements CameraSupport {

    private Camera camera;
    private TextureView mTextureView;
    private int mWidth;
    private int mHeight;
    private File mPhoto;

    public CameraOld(int width, int height, TextureView textureView) {
        this.mWidth = width;
        this.mHeight = height;
        this.mTextureView = textureView;
    }

    @Override
    public CameraSupport open(int cameraId, int width, int height) {
        this.camera = Camera.open(cameraId);
        Camera.Parameters params = camera.getParameters();
        params.setPictureFormat(ImageFormat.JPEG);
        params.setPreviewSize(mWidth, mHeight);
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        camera.setParameters(params);
        camera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                camera.takePicture(null, null, mPictureCallback);
            }
        });
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
}
