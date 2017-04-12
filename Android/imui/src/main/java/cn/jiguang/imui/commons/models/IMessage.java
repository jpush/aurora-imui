package cn.jiguang.imui.commons.models;


import com.volokh.danylo.video_player_manager.manager.VideoPlayerManager;
import com.volokh.danylo.video_player_manager.meta.MetaData;

import java.util.Date;


public interface IMessage {

    String getId();

    IUser getUserInfo();

    Date getCreatedAt();

    enum MessageType {
        SEND_TEXT,
        RECEIVE_TEXT,

        SEND_IMAGE,
        RECEIVE_IMAGE,

        SEND_VOICE,
        RECEIVE_VOICE,

        SEND_VIDEO,
        RECEIVE_VIDEO,

        SEND_LOCATION,
        RECEIVE_LOCATION,

        SEND_FILE,
        RECEIVE_FILE;

        public String type;

        private MessageType() {
        }
    }

    MessageType getType();

    String getText();

    String getContentFile();

    int getDuration();

    VideoPlayerManager<MetaData> getVideoPlayManager();
}
