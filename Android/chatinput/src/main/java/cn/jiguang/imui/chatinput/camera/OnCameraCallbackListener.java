package cn.jiguang.imui.chatinput.camera;

import java.io.File;


public interface OnCameraCallbackListener {


    /**
     * Fires when take picture finished.
     *
     * @param file Return the picture file.
     */
    void onTakePictureCompleted(File file);

    /**
     * Fires when record video finished.
     *
     * @param file Return the video file.
     */
    void onRecordVideoCompleted(File file);
}
