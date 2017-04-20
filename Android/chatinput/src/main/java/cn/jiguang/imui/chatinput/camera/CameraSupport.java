package cn.jiguang.imui.chatinput.camera;

import java.io.File;

import cn.jiguang.imui.chatinput.listener.OnCameraCallbackListener;


public interface CameraSupport {
    CameraSupport open(int cameraId, int width, int height, boolean isFacingBack);
    int getOrientation(int cameraId);
    void release();
    void setOutputFile(File file);
    void takePicture();
    void setCameraCallbackListener(OnCameraCallbackListener listener);
    void startRecordingVideo();
    void cancelRecordingVideo();
    String finishRecordingVideo();
}
