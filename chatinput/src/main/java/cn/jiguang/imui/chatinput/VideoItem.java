package cn.jiguang.imui.chatinput;

/**
 * 视频类
 */
public class VideoItem extends FileItem {

  private String mDuration;

  public VideoItem(String path, String name, String size, String date, String duration) {
    super(path, name, size, date);
    mDuration = duration;
  }

  public String getDuration() {
    return mDuration;
  }

  public void setDuration(String duration) {
    mDuration = duration;
  }
}
