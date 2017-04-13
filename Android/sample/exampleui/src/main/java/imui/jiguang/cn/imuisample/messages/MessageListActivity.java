package imui.jiguang.cn.imuisample.messages;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.Toast;

import com.volokh.danylo.video_player_manager.manager.PlayerItemChangeListener;
import com.volokh.danylo.video_player_manager.manager.SingleVideoPlayerManager;
import com.volokh.danylo.video_player_manager.manager.VideoPlayerManager;
import com.volokh.danylo.video_player_manager.meta.MetaData;
import com.volokh.danylo.visibility_utils.calculator.DefaultSingleItemCalculatorCallback;
import com.volokh.danylo.visibility_utils.calculator.ListItemsVisibilityCalculator;
import com.volokh.danylo.visibility_utils.calculator.SingleListViewItemActiveCalculator;
import com.volokh.danylo.visibility_utils.scroll_utils.ItemsPositionGetter;
import com.volokh.danylo.visibility_utils.scroll_utils.RecyclerViewItemPositionGetter;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cn.jiguang.imui.chatinput.ChatInputView;
import cn.jiguang.imui.chatinput.record.RecordVoiceButton;
import cn.jiguang.imui.chatinput.utils.FileItem;
import cn.jiguang.imui.commons.ImageLoader;
import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.messages.MsgListAdapter;
import imui.jiguang.cn.imuisample.R;
import imui.jiguang.cn.imuisample.models.DefaultUser;
import imui.jiguang.cn.imuisample.models.MyMessage;
import imui.jiguang.cn.imuisample.views.ChatView;

public class MessageListActivity extends Activity implements ChatView.OnKeyboardChangedListener,
        ChatView.OnSizeChangedListener, View.OnTouchListener {

    private final int REQUEST_RECORD_VOICE_PERMISSION = 0x0001;
    private final int REQUEST_CAMERA_PERMISSION = 0x0002;
    private final int REQUEST_PHOTO_PERMISSION = 0x0003;

    private Context mContext;

    private ChatView mChatView;
    private MsgListAdapter<MyMessage> mAdapter;
    private List<MyMessage> mData;

    private InputMethodManager mImm;
    private Window mWindow;
    private String mReceiverAvatar;
    private String mSenderAvatar;

    private ListItemsVisibilityCalculator mItemsVisibilityCalculator;

    private final VideoPlayerManager<MetaData> mVideoPlayerManager =
            new SingleVideoPlayerManager(new PlayerItemChangeListener() {
                @Override
                public void onPlayerItemChanged(MetaData currentItemMetaData) {

                }
            });

    private int mScrollState = AbsListView.OnScrollListener.SCROLL_STATE_IDLE;

    private ItemsPositionGetter mItemsPositionGetter;

    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.chat_activity);

        this.mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mWindow = getWindow();

        mChatView = (ChatView) findViewById(R.id.chat_view);
        mChatView.initModule();
        mChatView.setTitle("Deadpool");
        mData = getMessages();
        initMsgAdapter();

        mChatView.setKeyboardChangedListener(this);
        mChatView.setOnSizeChangedListener(this);
        mChatView.setOnTouchListener(this);

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        mChatView.getMessageListView().setLayoutManager(mLayoutManager);

        mItemsPositionGetter = new RecyclerViewItemPositionGetter(mLayoutManager,
                mChatView.getMessageListView());

        mItemsVisibilityCalculator = new SingleListViewItemActiveCalculator(
                new DefaultSingleItemCalculatorCallback(), mAdapter.getMessageList());

        mChatView.getMessageListView().addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                mScrollState = newState;

                if (newState == RecyclerView.SCROLL_STATE_IDLE && !mData.isEmpty()) {
                    mItemsVisibilityCalculator.onScrollStateIdle(mItemsPositionGetter,
                            mLayoutManager.findFirstVisibleItemPosition(),
                            mLayoutManager.findLastVisibleItemPosition() - 1);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!mData.isEmpty()) {
                    mItemsVisibilityCalculator.onScroll(mItemsPositionGetter,
                            mLayoutManager.findFirstVisibleItemPosition(),
                            mLayoutManager.findLastVisibleItemPosition() - mLayoutManager.findFirstVisibleItemPosition() + 1,
                            mScrollState);
                }
            }
        });

        mChatView.setMenuClickListener(new ChatInputView.OnMenuClickListener() {
            @Override
            public boolean onSubmit(CharSequence input) {
                if (input.length() == 0) {
                    return false;
                }
                MyMessage message = new MyMessage(input.toString(), IMessage.MessageType.SEND_TEXT);
                message.setUserInfo(new DefaultUser("1", "Ironman", "ironman"));
                mData.add(0, message);
                mAdapter.addToStart(message, true);
                return true;
            }

            @Override
            public void onSendFiles(List<FileItem> list) {
                if (list == null || list.isEmpty()) {
                    return;
                }

                MyMessage message;
                for (FileItem item : list) {
                    if (item.getType() == FileItem.Type.Image) {
                        message = new MyMessage(null, IMessage.MessageType.SEND_IMAGE);

                    } else if (item.getType() == FileItem.Type.Video) {
                        message = new MyMessage(null, IMessage.MessageType.SEND_VIDEO);
                        message.setVideoPlayerManager(mVideoPlayerManager);

                    } else {
                        throw new RuntimeException("Invalid FileItem type. Must be Type.Image or Type.Video");
                    }

                    message.setContentFile(item.getFilePath());
                    message.setUserInfo(new DefaultUser("1", "Ironman", "ironman"));
                    mData.add(0, message);

                    final MyMessage fMsg = message;
                    MessageListActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.addToStart(fMsg, true);
                        }
                    });
                }
            }

            @Override
            public void onVoiceClick() {
                if ((ActivityCompat.checkSelfPermission(MessageListActivity.this,
                        Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(mContext,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(mContext,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                    ActivityCompat.requestPermissions(MessageListActivity.this, new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_VOICE_PERMISSION);
                }
            }

            @Override
            public void onPhotoClick() {
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MessageListActivity.this, new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    }, REQUEST_PHOTO_PERMISSION);
                }
            }

            @Override
            public void onCameraClick() {
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) mContext, new String[]{
                            Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, REQUEST_CAMERA_PERMISSION);
                }

                File rootDir = mContext.getFilesDir();
                String fileDir = rootDir.getAbsolutePath() + "/photo";
                mChatView.setCameraCaptureFile(fileDir, "temp_photo");
            }

            @Override
            public void onVideoRecordFinished(String filePath) {
                Log.e("MessageListActivity", "Video Path: " + filePath);
                final MyMessage message = new MyMessage(null, IMessage.MessageType.SEND_VIDEO);
                message.setContentFile(filePath);
                message.setUserInfo(new DefaultUser("1", "Ironman", "ironman"));
                message.setVideoPlayerManager(mVideoPlayerManager);
                mData.add(0, message);
                MessageListActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.addToStart(message, true);
                    }
                });
            }
        });

        mChatView.setRecordVoiceListener(new RecordVoiceButton.RecordVoiceListener() {
            @Override
            public void onStartRecord() {
                // Show record voice interface
                // 设置存放录音文件目录
                File rootDir = mContext.getFilesDir();
                String fileDir = rootDir.getAbsolutePath() + "/voice";
                mChatView.setRecordVoiceFile(fileDir, new DateFormat().format("yyyy_MMdd_hhmmss",
                        Calendar.getInstance(Locale.CHINA)) + "");
            }

            @Override
            public void onFinishRecord(File voiceFile, int duration) {
                MyMessage message = new MyMessage(null, IMessage.MessageType.SEND_VOICE);
                message.setUserInfo(new DefaultUser("1", "Ironman", "ironman"));
                message.setContentFile(voiceFile.getPath());
                message.setDuration(duration);
                mAdapter.addToStart(message, true);
            }

            @Override
            public void onCancelRecord() {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mData.isEmpty()) {
            mChatView.getMessageListView().post(new Runnable() {
                @Override
                public void run() {
                    mItemsVisibilityCalculator.onScrollStateIdle(mItemsPositionGetter,
                            mLayoutManager.findFirstVisibleItemPosition(),
                            mLayoutManager.findLastVisibleItemPosition());
                }
            });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mVideoPlayerManager.resetMediaPlayer();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_RECORD_VOICE_PERMISSION) {
            if (grantResults.length <= 0 || grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // Permission denied
                Toast.makeText(mContext, "User denied permission, can't use record voice feature.",
                        Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length <= 0 || grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // Permission denied
                Toast.makeText(mContext, "User denied permission, can't use take aurora_menuitem_photo feature.",
                        Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_PHOTO_PERMISSION) {
            if (grantResults.length <= 0 || grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // Permission denied
                Toast.makeText(mContext, "User denied permission, can't use select aurora_menuitem_photo feature.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private List<MyMessage> getMessages() {
        List<MyMessage> list = new ArrayList<>();
        Resources res = getResources();
        String[] messages = res.getStringArray(R.array.messages_array);
        for (int i = 0; i < 10; i++) {
            MyMessage message;
            if (i % 2 == 0) {
                message = new MyMessage(messages[i], IMessage.MessageType.RECEIVE_TEXT);
                message.setUserInfo(new DefaultUser("0", "DeadPool", "deadpool"));
            } else {
                message = new MyMessage(messages[i], IMessage.MessageType.SEND_TEXT);
                message.setUserInfo(new DefaultUser("1", "IronMan", "ironman"));
            }
            list.add(message);
        }
        return list;
    }

    private void initMsgAdapter() {
        ImageLoader imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url) {
//                Picasso.with(mContext).load(url).into(imageView);
                imageView.setImageResource(getResources().getIdentifier(url, "drawable", getPackageName()));
            }
        };
        MsgListAdapter.HoldersConfig holdersConfig = new MsgListAdapter.HoldersConfig();
        // Use default layout
        mAdapter = new MsgListAdapter<MyMessage>("0", holdersConfig, imageLoader);
//        mAdapter.setLayoutManager(mLayoutManager);

        // If you want to customise your layout, try to create custom ViewHolder:
        // holdersConfig.setSenderTxtMsg(CustomViewHolder.class, layoutRes);
        // holdersConfig.setReceiverTxtMsg(CustomViewHolder.class, layoutRes);
        // CustomViewHolder must extends ViewHolders defined in MsgListAdapter.
        // Current ViewHolders are TxtViewHolder, VoiceViewHolder.

        mAdapter.setOnMsgClickListener(new MsgListAdapter.OnMsgClickListener<MyMessage>() {
            @Override
            public void onMessageClick(MyMessage message) {
                Toast.makeText(mContext, mContext.getString(R.string.message_click_hint),
                        Toast.LENGTH_SHORT).show();
                // do something
            }
        });

        mAdapter.setMsgLongClickListener(new MsgListAdapter.OnMsgLongClickListener<MyMessage>() {
            @Override
            public void onMessageLongClick(MyMessage message) {
                Toast.makeText(mContext, mContext.getString(R.string.message_long_click_hint),
                        Toast.LENGTH_SHORT).show();
                // do something
            }
        });

        mAdapter.setOnAvatarClickListener(new MsgListAdapter.OnAvatarClickListener<MyMessage>() {
            @Override
            public void onAvatarClick(MyMessage message) {
                DefaultUser userInfo = (DefaultUser) message.getUserInfo();
                Toast.makeText(mContext, mContext.getString(R.string.avatar_click_hint),
                        Toast.LENGTH_SHORT).show();
                // do something
            }
        });

        MyMessage message = new MyMessage("Hello World", IMessage.MessageType.RECEIVE_TEXT);
        message.setUserInfo(new DefaultUser("0", "Deadpool", "deadpool"));
        mData.add(message);
        mAdapter.addToEnd(mData, true);
        mAdapter.setOnLoadMoreListener(new MsgListAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore(int page, int totalCount) {
                if (totalCount < mData.size()) {
                    loadNextPage();
                }
            }
        });

        mChatView.setAdapter(mAdapter);
    }

    private void loadNextPage() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.addToEnd(mData, true);
            }
        }, 1000);
    }

    @Override
    public void onKeyBoardStateChanged(int state) {
        switch (state) {
            case ChatInputView.KEYBOARD_STATE_INIT:
                ChatInputView chatInputView = mChatView.getChatInputView();
                if (mImm != null) {
                    mImm.isActive();
                }
                if (chatInputView.getMenuState() == View.INVISIBLE || (!chatInputView.getSoftInputState()
                        && chatInputView.getMenuState() == View.GONE)) {

                    mWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                            | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    chatInputView.dismissMenuLayout();
                }
                break;
        }
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (oldh - h > 300) {
            mChatView.setMenuHeight(oldh - h);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                ChatInputView chatInputView = mChatView.getChatInputView();
                if (view.getId() == chatInputView.getInputView().getId()) {
                    if (chatInputView.getMenuState() == View.VISIBLE && !chatInputView.getSoftInputState()) {
                        chatInputView.dismissMenuAndResetSoftMode();
                        return false;
                    } else {
                        return false;
                    }
                }
                if (chatInputView.getMenuState() == View.VISIBLE) {
                    chatInputView.dismissMenuLayout();
                }
                if (chatInputView.getSoftInputState()) {
                    View v = getCurrentFocus();
                    if (mImm != null && v != null) {
                        mImm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        mWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                        chatInputView.setSoftInputState(false);
                    }
                }
                break;
        }
        return false;
    }
}
