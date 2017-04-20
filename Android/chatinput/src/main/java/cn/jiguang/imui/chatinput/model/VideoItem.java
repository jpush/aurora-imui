package cn.jiguang.imui.chatinput.model;

public class VideoItem extends FileItem {

    private long mDuration;

    public VideoItem(String path, String name, String size, String date, long duration) {
        super(path, name, size, date);
        mDuration = duration;
    }

    @Override
    public Type getType() {
        return Type.Video;
    }

    public long getDuration() {
        return mDuration;
    }

    public void setDuration(long duration) {
        mDuration = duration;
    }
}
