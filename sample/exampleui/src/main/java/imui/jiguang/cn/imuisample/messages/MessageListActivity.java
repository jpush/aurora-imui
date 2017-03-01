package imui.jiguang.cn.imuisample.messages;


import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cn.jiguang.imui.commons.ImageLoader;
import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.messages.ChatInput;
import cn.jiguang.imui.messages.MessageList;
import cn.jiguang.imui.messages.MsgListAdapter;
import imui.jiguang.cn.imuisample.R;
import imui.jiguang.cn.imuisample.models.MyMessage;

public class MessageListActivity extends Activity {

    private TextView mTitle;
    private MessageList mMsgList;
    private ChatInput mChatInput;
    private MsgListAdapter<MyMessage> mAdapter;
    private Context mContext;
    private List<MyMessage> mData;
    InputMethodManager mImm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.chat_activity);
        this.mImm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        mTitle = (TextView) findViewById(R.id.title_tv);
        mTitle.setText("User1");
        mMsgList = (MessageList) findViewById(R.id.msg_list);
        mMsgList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mImm != null) {
                    mImm.hideSoftInputFromWindow(mChatInput.getInputView().getWindowToken(), 0); //强制隐藏键盘
                }
            }
        });
        mChatInput = (ChatInput) findViewById(R.id.chat_input);
        mData = getMessages();
        initMsgAdapter();
        mChatInput.setMenuClickListener(new ChatInput.OnMenuClickListener() {
            @Override
            public boolean onSubmit(CharSequence input) {
                mAdapter.addToStart(new MyMessage(input.toString(), IMessage.MessageType.SEND_TEXT), true);
                return true;
            }

            @Override
            public void onVoiceClick() {

            }

            @Override
            public void onPhotoClick() {

            }

            @Override
            public void onCameraClick() {

            }
        });
    }

    private List<MyMessage> getMessages() {
        List<MyMessage> list = new ArrayList<>();
        Resources res = getResources();
        String[] messages = res.getStringArray(R.array.messages_array);
        for (int i = 0; i < 10; i++) {
            MyMessage message = new MyMessage(messages[i], i % 2 == 0 ?
                    IMessage.MessageType.RECEIVE_TEXT: IMessage.MessageType.SEND_TEXT);
            list.add(message);
        }
        return list;
    }

    private void initMsgAdapter() {
        ImageLoader imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url) {
                Picasso.with(mContext).load(url).into(imageView);
            }
        };
        MsgListAdapter.HoldersConfig holdersConfig = new MsgListAdapter.HoldersConfig();
        mAdapter = new MsgListAdapter<MyMessage>("0", holdersConfig, imageLoader);
        mAdapter.setMsgLongClickListener(new MsgListAdapter.OnMsgLongClickListener<MyMessage>() {
            @Override
            public void onMessageLongClick(MyMessage message) {
                Toast.makeText(mContext, mContext.getString(R.string.message_long_click_hint),
                        Toast.LENGTH_SHORT).show();
                // do something
            }
        });
        mAdapter.addToStart(new MyMessage("Hello World", IMessage.MessageType.RECEIVE_TEXT), false);
        mAdapter.setOnLoadMoreListener(new MsgListAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore(int page, int totalCount) {
                if (totalCount < mData.size()) {
                    loadNextPage();
                }
            }
        });
        mMsgList.setAdapter(mAdapter);
    }

    private void loadNextPage() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.addToEnd(mData, true);
            }
        }, 1000);
    }
}
