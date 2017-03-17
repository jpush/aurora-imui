package cn.jiguang.imui.chatinput;

import java.io.File;


public interface CameraSupport {
    CameraSupport open(int cameraId, int width, int height);
    int getOrientation(int cameraId);
    void release();
    void setOutputFile(File file);
    void takePicture();
    void setCameraCallbackListener(OnCameraCallbackListener listener);
}
