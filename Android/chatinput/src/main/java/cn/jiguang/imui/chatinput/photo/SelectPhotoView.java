package cn.jiguang.imui.chatinput.photo;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import cn.jiguang.imui.chatinput.R;
import cn.jiguang.imui.chatinput.listener.OnFileSelectedListener;
import cn.jiguang.imui.chatinput.model.FileItem;
import cn.jiguang.imui.chatinput.model.VideoItem;

public class SelectPhotoView extends FrameLayout {

    private final static int MSG_WHAT_SCAN_SUCCESS = 1;
    private final static int MSG_WHAT_SCAN_FAILED = 0;

    private Context mContext;

    private RecyclerView mRvPhotos; // Select photo view
    private PhotoAdapter mPhotoAdapter;
    private ProgressBar mProgressBar;

    private HashMap<String, Integer> mMedias = new HashMap<>(); // All photo or video files
    private List<FileItem> mFileItems = new ArrayList<>();

    private Handler mMediaHandler;

    private OnFileSelectedListener mOnFileSelectedListener;
    private long mLastUpdateTime;

    public SelectPhotoView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public SelectPhotoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SelectPhotoView(@NonNull Context context, @Nullable AttributeSet attrs,
                           @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.layout_chatinput_selectphoto, this);
        mContext = context;

        mProgressBar = (ProgressBar) findViewById(R.id.aurora_progressbar_selectphoto);
        mRvPhotos = (RecyclerView) findViewById(R.id.aurora_recyclerview_selectphoto);
        mRvPhotos.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        mRvPhotos.setHasFixedSize(true);
        mMediaHandler = new MediaHandler(this);
    }

    public void initData() {
        if (hasPermission()) {
            mProgressBar.setVisibility(View.VISIBLE);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (getPhotos() && getVideos()) {
                        Collections.sort(mFileItems);
                        mMediaHandler.sendEmptyMessage(MSG_WHAT_SCAN_SUCCESS);
                    } else {
                        mMediaHandler.sendEmptyMessage(MSG_WHAT_SCAN_FAILED);
                    }
                }
            }).start();
        }
    }

    private boolean hasPermission() {
        return  android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M
                || mContext.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Update Select photo view every 30 seconds.
     */
    public void updateData() {
        if (hasPermission()) {
            if ((mLastUpdateTime !=0 && System.currentTimeMillis() - mLastUpdateTime >= 30 * 1000) || mFileItems.size() == 0) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (getPhotos() && getVideos()) {
                            Collections.sort(mFileItems);
                            mMediaHandler.sendEmptyMessage(MSG_WHAT_SCAN_SUCCESS);
                        } else {
                            mMediaHandler.sendEmptyMessage(MSG_WHAT_SCAN_FAILED);
                        }
                    }
                }).start();
            }
        }
    }

    private boolean getPhotos() {
        Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = getContext().getContentResolver();
        String[] projection = new String[]{
                MediaStore.Images.ImageColumns.DATA, MediaStore.Images.ImageColumns.DISPLAY_NAME,
                MediaStore.Images.ImageColumns.SIZE, MediaStore.Images.ImageColumns.DATE_ADDED
        };
        Cursor cursor = contentResolver.query(imageUri, projection, null, null,
                MediaStore.Images.Media.DATE_ADDED + " desc");

        if (cursor == null) {
            return false;
        }
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
                File file = new File(path);
                if (file.exists()) {
                    String fileName =
                            cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                    String size = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.SIZE));
                    String date = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
                    if (!mMedias.containsKey(fileName)) {
                        mMedias.put(fileName, 1);
                        FileItem item = new FileItem(path, fileName, size, date);
                        item.setType(FileItem.Type.Image);
                        mFileItems.add(item);
                    }
                }
            }
        }
        cursor.close();
        return true;
    }

    private boolean getVideos() {
        Uri videoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        ContentResolver cr = getContext().getContentResolver();
        String[] projection = new String[]{
                MediaStore.Video.VideoColumns.DATA, MediaStore.Video.VideoColumns.DURATION,
                MediaStore.Video.VideoColumns.SIZE, MediaStore.Video.VideoColumns.DISPLAY_NAME,
                MediaStore.Video.VideoColumns.DATE_ADDED
        };

        Cursor cursor = cr.query(videoUri, projection, null, null, null);
        if (cursor == null) {
            return false;
        }
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                File file = new File(path);
                if (file.exists()) {
                    String name = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
                    String date = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED));
                    String size = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
                    long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));

                    if (!mMedias.containsKey(name)) {
                        mMedias.put(name, 1);
                        VideoItem item = new VideoItem(path, name, size, date, duration / 1000);
                        item.setType(FileItem.Type.Video);
                        mFileItems.add(item);
                    }
                }
            }
        }
        cursor.close();
        return true;
    }

    static class MediaHandler extends Handler {
        WeakReference<SelectPhotoView> mViewReference;
        MediaHandler(SelectPhotoView view) {
            mViewReference= new WeakReference<SelectPhotoView>(view);
        }
        @Override
        public void handleMessage(Message msg) {
            final SelectPhotoView view = mViewReference.get();
            if (view != null) {
                view.mProgressBar.setVisibility(View.GONE);
                switch (msg.what) {
                    case MSG_WHAT_SCAN_SUCCESS:
                        view.mLastUpdateTime = System.currentTimeMillis();
                        if (view.mPhotoAdapter == null) {
                            view.mPhotoAdapter = new PhotoAdapter(view.mFileItems);
                            view.mRvPhotos.setAdapter(view.mPhotoAdapter);
                        } else {
                            view.mPhotoAdapter.notifyDataSetChanged();
                        }
                        view.mPhotoAdapter.setOnPhotoSelectedListener(view.mOnFileSelectedListener);
                        break;
                    case MSG_WHAT_SCAN_FAILED:
                        Toast.makeText(view.mContext, view.mContext.getString(R.string.sdcard_not_prepare_toast),
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }
    }

    public List<FileItem> getSelectFiles() {
        if (mPhotoAdapter == null) {
            return null;
        }
        return mPhotoAdapter.getSelectedFiles();
    }

    public void resetCheckState() {
        mPhotoAdapter.resetCheckedState();
    }

    public void setOnFileSelectedListener(OnFileSelectedListener listener) {
        mOnFileSelectedListener = listener;
    }
}
