package cn.jiguang.imui.chatinput.camera;

import java.io.File;


public interface CameraSupport {
    CameraSupport open(int cameraId, int width, int height);
    int getOrientation(int cameraId);
    void release();
    void setOutputFile(File file);
    void takePicture();
    void setCameraCallbackListener(OnCameraCallbackListener listener);
    void startRecordingVideo();
    void cancelRecordingVideo();
    void finishRecordingVideo();
}
