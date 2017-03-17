package cn.jiguang.imui.chatinput;


import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class FileItem {

    private String mFilePath;
    private String mFileName;
    private String mSize;
    private String mDate;

    public FileItem(String path, String name, String size, String date) {
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
        //保留小数点后两位
        ddf1.setMaximumFractionDigits(2);
        long size = Long.valueOf(mSize);
        String sizeDisplay;
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
        return Long.valueOf(mSize);
    }

    public String getDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return format.format(Long.valueOf(mDate) * 1000);
    }
}
