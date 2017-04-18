package cn.jiguang.imui.chatinput.listener;



public interface OnCameraCallbackListener {


    /**
     * Fires when take picture finished.
     *
     * @param photoPath Return the absolute path of picture file.
     */
    void onTakePictureCompleted(String photoPath);

    void onStartVideoRecord();

    /**
     * Fires when record video finished.
     *
     * @param videoPath Return the absolute path of video file.
     */
    void onFinishVideoRecord(String videoPath);

    void onCancelVideoRecord();
}
