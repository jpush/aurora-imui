package imui.jiguang.cn.imuisample.models;

import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import com.volokh.danylo.video_player_manager.manager.VideoPlayerManager;
import com.volokh.danylo.video_player_manager.meta.CurrentItemMetaData;
import com.volokh.danylo.video_player_manager.meta.MetaData;
import com.volokh.danylo.video_player_manager.ui.VideoPlayerView;
import com.volokh.danylo.visibility_utils.items.ListItem;

import java.util.Date;
import java.util.UUID;

import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.commons.models.IUser;
import cn.jiguang.imui.messages.VideoViewHolder;


public class MyMessage implements IMessage, ListItem {

    private long id;
    private String text;
    private Date createAt;
    private MessageType type;
    private IUser user;
    private String contentFile;
    private int duration;

    private final Rect mCurrentViewRect = new Rect();

    private VideoPlayerManager<MetaData> mVideoPlayerManager;

    public MyMessage(String text, MessageType type) {
        this.text = text;
        this.type = type;
        this.id = UUID.randomUUID().getLeastSignificantBits();
    }

    public void setVideoPlayerManager(VideoPlayerManager<MetaData> videoPlayerManager) {
        mVideoPlayerManager = videoPlayerManager;
    }

    @Override
    public String getId() {
        return String.valueOf(id);
    }

    @Override
    public IUser getUserInfo() {
        if (user == null) {
            return new DefaultUser("0", "user1", null);
        }
        return user;
    }

    public void setUserInfo(IUser user) {
        this.user = user;
    }

    public void setContentFile(String path) {
        this.contentFile = path;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public int getDuration() {
        return duration;
    }

    @Override
    public VideoPlayerManager<MetaData> getVideoPlayManager() {
        return mVideoPlayerManager;
    }

    @Override
    public Date getCreatedAt() {
        return createAt == null ? new Date() : createAt;
    }

    @Override
    public MessageType getType() {
        return type;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String getContentFile() {
        return contentFile;
    }

    @Override
    public int getVisibilityPercents(View view) {
        int percents = 100;
        view.getLocalVisibleRect(mCurrentViewRect);

        int height = view.getHeight();
        if (viewIsPartiallyHiddenTop()) {
            percents = (height - mCurrentViewRect.top) * 100 / height;
        } else if (viewIsPartiallyHiddenBottom(height)) {
            percents = mCurrentViewRect.bottom * 100 / height;
        }
        return percents;
    }

    @Override
    public void setActive(View newActiveView, int newActiveViewPosition) {
        Log.i("MyMessage", type.toString());

        if (type == MessageType.SEND_VIDEO || type == MessageType.RECEIVE_VIDEO) {
            VideoViewHolder viewHolder = (VideoViewHolder) newActiveView.getTag();
            playNewVideo(new CurrentItemMetaData(newActiveViewPosition, newActiveView),
                    viewHolder.mVideoView, mVideoPlayerManager);
        }
    }

    @Override
    public void deactivate(View currentView, int position) {
        if (type == MessageType.SEND_VIDEO || type == MessageType.RECEIVE_VIDEO) {
            mVideoPlayerManager.stopAnyPlayback();
        }
    }

    private boolean viewIsPartiallyHiddenTop() {
        return mCurrentViewRect.top > 0;
    }

    private boolean viewIsPartiallyHiddenBottom(int height) {
        return mCurrentViewRect.bottom > 0 && mCurrentViewRect.bottom < height;
    }

    private void playNewVideo(MetaData currentItemMetaData, VideoPlayerView playerView,
                              VideoPlayerManager<MetaData> playerManager) {
        playerManager.playNewVideo(currentItemMetaData, playerView, contentFile);
    }
}