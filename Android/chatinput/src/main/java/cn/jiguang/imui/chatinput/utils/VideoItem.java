package cn.jiguang.imui.chatinput.utils;

import cn.jiguang.imui.chatinput.utils.FileItem;

/**
 * 视频类
 */
public class VideoItem extends FileItem {

    private long mDuration;

    public VideoItem(String path, String name, String size, String date, long duration) {
        super(path, name, size, date);
        mDuration = duration;
    }

    public long getDuration() {
        return mDuration;
    }

    public void setDuration(long duration) {
        mDuration = duration;
    }
}
