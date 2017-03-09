package cn.jiguang.imui.messages;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.Space;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cn.jiguang.imui.R;
import cn.jiguang.imui.commons.models.FileItem;
import cn.jiguang.imui.utils.ImgBrowserViewPager;

public class ChatInputView extends LinearLayout implements View.OnClickListener, TextWatcher {

    private EditText mChatInput;
    private ImageButton mSendBtn;
    private TextView mSendCountTv;
    private CharSequence mInput;
    private Space mInputMarginLeft;
    private Space mInputMarginRight;
    private ImageButton mVoiceBtn;
    private ImageButton mPhotoBtn;
    private ImageButton mCameraBtn;
    private FrameLayout mMenuContainer;
    private LinearLayout mRecordVoiceLl;
    private FrameLayout mPhotoFl;
    private RecordVoiceButton mRecordVoiceBtn;
    private ImgBrowserViewPager mImgViewPager;
    private ImageView mPhotoIv;
    private ProgressBar mProgressBar;
    private ImageButton mAlbumBtn;
    private OnMenuClickListener mListener;
    private ChatInputStyle mStyle;
    private InputMethodManager mImm;
    private Window mWindow;
    private int mLastClickId = 0;

    private int mMenuHeight = 300;
    private boolean mShowSoftInput = false;
    private List<FileItem> mPhotos = new ArrayList<>();
    private MyHandler myHandler = new MyHandler(this);

    public static final byte KEYBOARD_STATE_SHOW = -3;
    public static final byte KEYBOARD_STATE_HIDE = -2;
    public static final byte KEYBOARD_STATE_INIT = -1;
    private final static int SCAN_OK = 1;
    private final static int SCAN_ERROR = 0;

    public ChatInputView(Context context) {
        super(context);
        init(context);
    }

    public ChatInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ChatInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context) {
        inflate(context, R.layout.view_chat_input, this);
        mChatInput = (EditText) findViewById(R.id.chat_input_et);
        mVoiceBtn = (ImageButton) findViewById(R.id.voice_ib);
        mPhotoBtn = (ImageButton) findViewById(R.id.photo_ib);
        mSendBtn = (ImageButton) findViewById(R.id.send_msg_ib);
        mSendCountTv = (TextView) findViewById(R.id.send_count_tv);
        mCameraBtn = (ImageButton) findViewById(R.id.camera_ib);
        mInputMarginLeft = (Space) findViewById(R.id.input_margin_left);
        mInputMarginRight = (Space) findViewById(R.id.input_margin_right);
        mMenuContainer = (FrameLayout) findViewById(R.id.menu_container);
        mRecordVoiceLl = (LinearLayout) findViewById(R.id.record_voice_container);
        mPhotoFl = (FrameLayout) findViewById(R.id.photo_container);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mAlbumBtn = (ImageButton) findViewById(R.id.album_ib);
        mRecordVoiceBtn = (RecordVoiceButton) findViewById(R.id.record_btn);
        mImgViewPager = (ImgBrowserViewPager) findViewById(R.id.photo_vp);

        mImgViewPager.setOffscreenPageLimit(3);
        mImgViewPager.setPageMargin(40);
        mMenuContainer.setVisibility(GONE);
        mChatInput.addTextChangedListener(this);
        mSendBtn.setOnClickListener(this);
        mVoiceBtn.setOnClickListener(this);
        mPhotoBtn.setOnClickListener(this);
        mCameraBtn.setOnClickListener(this);
        this.mImm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        mWindow = ((Activity)context).getWindow();
        mChatInput.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    mShowSoftInput = true;
                    invisibleMenuLayout();
                }
                return false;
            }
        });
    }

    private void init(Context context, AttributeSet attrs) {
        init(context);
        mStyle = ChatInputStyle.parse(context, attrs);

        mChatInput.setMaxLines(mStyle.getInputMaxLines());
        mChatInput.setHint(mStyle.getInputHint());
        mChatInput.setText(mStyle.getInputText());
        mChatInput.setTextSize(TypedValue.COMPLEX_UNIT_PX, mStyle.getInputTextSize());
        mChatInput.setTextColor(mStyle.getInputTextColor());
        mChatInput.setHintTextColor(mStyle.getInputHintColor());
        mChatInput.setBackground(mStyle.getInputEditTextBg());
        mInputMarginLeft.getLayoutParams().width = mStyle.getInputMarginLeft();
        mInputMarginRight.getLayoutParams().width = mStyle.getInputMarginRight();
//        setCursor(mStyle.getInputCursorDrawable());
        mVoiceBtn.setImageDrawable(mStyle.getVoiceBtnIcon());
        mVoiceBtn.setBackground(mStyle.getVoiceBtnBg());
        mPhotoBtn.setBackground(mStyle.getPhotoBtnBg());
        mPhotoBtn.setImageDrawable(mStyle.getPhotoBtnIcon());
        mCameraBtn.setBackground(mStyle.getCameraBtnBg());
        mCameraBtn.setImageDrawable(mStyle.getCameraBtnIcon());
        mSendBtn.setBackground(mStyle.getSendBtnBg());
        mSendBtn.setImageDrawable(mStyle.getSendBtnIcon());
        mSendCountTv.setBackground(mStyle.getSendCountBg());

        if (getPaddingLeft() == 0
                && getPaddingRight() == 0
                && getPaddingTop() == 0
                && getPaddingBottom() == 0) {
            setPadding(
                    mStyle.getInputDefaultPaddingLeft(),
                    mStyle.getInputDefaultPaddingTop(),
                    mStyle.getInputDefaultPaddingRight(),
                    mStyle.getInputDefaultPaddingBottom()
            );
        }

    }

    private void setCursor(Drawable drawable) {
        try {
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(mChatInput, drawable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMenuClickListener(OnMenuClickListener listener) {
        mListener = listener;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int count, int after) {
        mInput = s;
        if (mInput.length() > 0) {
            mSendBtn.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.send_pres));
        } else {
            mSendBtn.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.send));
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    public EditText getInputView() {
        return mChatInput;
    }

    public RecordVoiceButton getRecordVoiceButton() {
        return mRecordVoiceBtn;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.send_msg_ib) {
            if (onSubmit()) {
                mChatInput.setText("");
            }
        } else {
            if (mMenuContainer.getVisibility() != VISIBLE) {
                dismissSoftInputAndShowMenu();
            } else if (view.getId() == mLastClickId) {
                dismissMenuAndResetSoftMode();
            }

            if (view.getId() == R.id.voice_ib) {
                if (mListener != null) {
                    mListener.onVoiceClick();
                }
                showRecordVoiceLayout();
            } else if (view.getId() == R.id.photo_ib) {
                if (mListener != null) {
                    mListener.onPhotoClick();
                }
                showPhotoLayout();
                if (mPhotos.size() == 0) {
                    getPhotos();
                }
            } else if (view.getId() == R.id.camera_ib) {
                if (mListener != null) {
                    mListener.onCameraClick();
                }

            }
            mLastClickId = view.getId();
        }
    }

    public void dismissMenuLayout() {
        mMenuContainer.setVisibility(GONE);
    }

    public void invisibleMenuLayout() {
        mMenuContainer.setVisibility(INVISIBLE);
    }

    public void showMenuLayout() {
        mMenuContainer.setVisibility(VISIBLE);
    }

    public void showRecordVoiceLayout() {
        mRecordVoiceLl.setVisibility(VISIBLE);
        mPhotoFl.setVisibility(GONE);
    }

    public void dismissRecordVoiceLayout() {
        mRecordVoiceLl.setVisibility(GONE);
    }

    public void showPhotoLayout() {
        mPhotoFl.setVisibility(VISIBLE);
        mRecordVoiceLl.setVisibility(GONE);
    }

    public void dismissPhotoLayout() {
        mPhotoFl.setVisibility(GONE);
    }

    /**
     * Set menu container's height, invoke this method once the menu was initialized.
     * @param height Height of menu, set same height as soft keyboard so that display to perfection.
     */
    public void setMenuContainerHeight(int height) {
        if(height > 0 && height != mMenuHeight){
            mMenuHeight = height;
            mMenuContainer.setLayoutParams(new LinearLayout
                    .LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height));
        }
    }

    private boolean onSubmit() {
        return mListener != null && mListener.onSubmit(mInput);
    }

    public boolean getSoftInputState() {
        return mShowSoftInput;
    }

    public void setSoftInputState(boolean state) {
        mShowSoftInput = state;
    }

    public int getMenuState() {
        return mMenuContainer.getVisibility();
    }

    /**
     * Menu items' callbacks
     */
    public interface OnMenuClickListener {

        /**
         * Fires when send button is on click.
         * @param input Input content
         * @return boolean
         */
        boolean onSubmit(CharSequence input);

        /**
         * Fires when voice button is on click.
         */
        void onVoiceClick();

        /**
         * Fires when photo button is on click.
         */
        void onPhotoClick();

        /**
         * Fires when camera button is on click.
         */
        void onCameraClick();
    }


    public void dismissMenuAndResetSoftMode() {
        mWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN); // 隐藏软键盘
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        dismissMenuLayout();
        mChatInput.requestFocus();
    }

    public void dismissSoftInputAndShowMenu() {
        //隐藏软键盘
        mWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN); // 隐藏软键盘
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        showMenuLayout();
        if (mImm != null) {
            mImm.hideSoftInputFromWindow(mChatInput.getWindowToken(), 0); //强制隐藏键盘
        }
        setMenuContainerHeight(mMenuHeight);
        mShowSoftInput = false;
    }

    public void dismissSoftInput() {
        if (mShowSoftInput) {
            if (mImm != null) {
                mImm.hideSoftInputFromWindow(mChatInput.getWindowToken(), 0);
                mShowSoftInput = false;
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void getPhotos() {
        mProgressBar.setVisibility(VISIBLE);
        new Thread(new Runnable() {

            @Override
            public void run() {
                Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver contentResolver = getContext().getContentResolver();
                String[] projection = new String[]{ MediaStore.Images.ImageColumns.DATA,
                        MediaStore.Images.ImageColumns.DISPLAY_NAME,
                        MediaStore.Images.ImageColumns.SIZE };
                Cursor cursor = contentResolver.query(imageUri, projection, null, null,
                        MediaStore.Images.Media.DATE_MODIFIED + " desc");
                if (cursor == null || cursor.getCount() == 0) {
                    myHandler.sendEmptyMessage(SCAN_ERROR);
                } else {
                    while (cursor.moveToNext()) {
                        //获取图片的路径
                        String path = cursor.getString(cursor
                                .getColumnIndex(MediaStore.Images.Media.DATA));
                        String fileName = cursor.getString(cursor
                                .getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                        String size = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.SIZE));
                        FileItem fileItem = new FileItem(path, fileName, size, null);
                        mPhotos.add(fileItem);
                    }
                    cursor.close();
                    //通知Handler扫描图片完成
                    myHandler.sendEmptyMessage(SCAN_OK);
                }
            }
        }).start();

    }

    private static class MyHandler extends Handler {
        private final WeakReference<ChatInputView> mChatInputView;

        public MyHandler(ChatInputView view) {
            mChatInputView = new WeakReference<ChatInputView>(view);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ChatInputView chatInputView = mChatInputView.get();
            if (chatInputView != null) {
                switch (msg.what) {
                    case SCAN_OK:
                        //关闭进度条
                        chatInputView.mProgressBar.setVisibility(GONE);
                        chatInputView.mImgViewPager.setAdapter(chatInputView.mAdapter);
                        break;
                    case SCAN_ERROR:
                        chatInputView.mProgressBar.setVisibility(GONE);
                        Toast.makeText(chatInputView.getContext(),
                                chatInputView.getContext().getString(R.string.sdcard_not_prepare_toast),
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }
    }

     PagerAdapter mAdapter = new PagerAdapter() {

        @Override
        public int getCount() {
            return mPhotos.size();
        }

         @Override
         public float getPageWidth(int position) {
             return (float) 0.8;
         }

         /**
         * 点击某张图片预览时，系统自动调用此方法加载这张图片左右视图（如果有的话）
         */
        @Override
        public View instantiateItem(ViewGroup container, int position) {
            mPhotoIv = new ImageView(container.getContext());
            mPhotoIv.setTag(position);
            mPhotoIv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            String path = mPhotos.get(position).getFilePath();
            if (path != null) {
                File file = new File(path);
                if (file.exists()) {
                    Bitmap bitmap = decodeFile(file);
                    if (bitmap != null) {
                        mPhotoIv.setImageBitmap(bitmap);
                    } else {
                        mPhotoIv.setImageResource(R.drawable.jmui_picture_not_found);
                    }
                }
            } else {
                mPhotoIv.setImageResource(R.drawable.jmui_picture_not_found);
            }
            container.addView(mPhotoIv, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            return mPhotoIv;
        }

        @Override
        public int getItemPosition(Object object) {
            View view = (View) object;
            int currentPage = mImgViewPager.getCurrentItem();
            if (currentPage == (Integer) view.getTag()) {
                return POSITION_NONE;
            } else {
                return POSITION_UNCHANGED;
            }
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    };

    private Bitmap decodeFile(File f){
        Bitmap b = null;
        try {
            //Decode image size
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;

            FileInputStream fis = new FileInputStream(f);
            BitmapFactory.decodeStream(fis, null, options);
            fis.close();

            int width = options.outWidth;
            int height = options.outHeight;
            int ratio = 0;
            //如果宽度大于高度，交换宽度和高度
            if (width > height) {
                int temp = width;
                width = height;
                height = temp;
            }
            //计算取样比例
            int sampleRatio = Math.max(width/900, height/1600);

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = sampleRatio;
            fis = new FileInputStream(f);
            b = BitmapFactory.decodeStream(fis, null, o2);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

}
