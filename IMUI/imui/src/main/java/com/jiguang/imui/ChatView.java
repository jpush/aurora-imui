package com.jiguang.imui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 聊天界面
 */
public class ChatView extends RelativeLayout {

    public ChatView(Context context) {
        super(context);
        init(null, 0);
    }

    public ChatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ChatView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray typedArray = getContext().obtainStyledAttributes(
                attrs, R.styleable.ChatView, defStyle, 0);

        typedArray.recycle();
    }

    public void setOppositeUserAvatar(String path) {
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;

    }

    public void setMyAvatar(String path) {

    }

    /**
     * 在聊天界面中增加文字消息
     *
     * @param text       消息内容
     * @param isOpposite 是否为对方发送的消息
     */
    public void addTextMessage(String text, boolean isOpposite) {
        View chatBobbleView = View.inflate(getContext(), R.layout.chat_bobble_view, this);
        RelativeLayout rlChat = (RelativeLayout) chatBobbleView.findViewById(R.id.rl_chat);
        CircleImageView avatar;
        FrameLayout msgContainer = (FrameLayout) chatBobbleView.findViewById(R.id.container_msg);

        if (isOpposite) {
            avatar = (CircleImageView) chatBobbleView.findViewById(R.id.avatar_opposite);
            chatBobbleView.findViewById(R.id.avatar_my).setVisibility(View.INVISIBLE);
            msgContainer.setBackgroundResource(R.drawable.chat_bubble_opposite);
        } else {
            avatar = (CircleImageView) chatBobbleView.findViewById(R.id.avatar_my);
            chatBobbleView.findViewById(R.id.avatar_opposite).setVisibility(View.INVISIBLE);
            msgContainer.setBackgroundResource(R.drawable.chat_bubble_my);
        }


    }

    public void addImageMessage(String imgPath) {

    }
}
