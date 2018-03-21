package cn.jiguang.imui.chatinput.model;

import android.support.annotation.NonNull;

import java.text.NumberFormat;

public class FileItem implements Comparable<FileItem> {

    private String mFilePath;
    private String mFileName;
    private String mSize;

    /**
     * 创建时间
     */
    private String mDate;

    private Type mType;

    public FileItem(@NonNull String path, String name, String size, String date) {
        this.mFilePath = path;
        this.mFileName = name;
        this.mSize = size;
        this.mDate = date;
    }

    public String getFilePath() {
        return mFilePath;
    }

    public void setFilePath(String mFilePath) {
        this.mFilePath = mFilePath;
    }

    public String getFileName() {
        return mFileName;
    }

    public void setFileName(String mFileName) {
        this.mFileName = mFileName;
    }

    public String getFileSize() {
        NumberFormat ddf1 = NumberFormat.getNumberInstance();
        ddf1.setMaximumFractionDigits(2);

        String sizeDisplay;
        long size = Long.valueOf(mSize);
        if (size > 1048576.0) {
            double result = size / 1048576.0;
            sizeDisplay = ddf1.format(result) + "M";
        } else if (size > 1024) {
            double result = size / 1024;
            sizeDisplay = ddf1.format(result) + "K";
        } else {
            sizeDisplay = ddf1.format(size) + "B";
        }
        return sizeDisplay;
    }

    public long getLongFileSize() {
        if (mSize != null) {
            return Long.valueOf(mSize);
        } else {
            return 0L;
        }
    }

    public String getDate() {
        return mDate;
    }

    public Type getType() {
        return mType;
    }

    public void setType(Type type) {
        this.mType = type;
    }

    @Override
    public int compareTo(@NonNull FileItem fileItem) {
        return (int) (Long.valueOf(fileItem.getDate()) - Long.valueOf(mDate));
    }

    public enum Type {
        Image(0), Video(1);

        private final int code;

        Type(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }

    @Override
    public String toString() {
        if (mType == Type.Image) {
            return "{mediaType: image, " + "mediaPath: " + mFilePath + "}";
        } else {
            return "{mediaType: video, " + "mediaPath: " + mFilePath + "}";
        }
    }
}
