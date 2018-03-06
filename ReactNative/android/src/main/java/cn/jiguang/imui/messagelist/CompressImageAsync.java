package cn.jiguang.imui.messagelist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.WritableMap;


public class CompressImageAsync extends AsyncTask<String, Void, String> {

    private Callback mCallback;

    public CompressImageAsync(Callback callback) {
        this.mCallback = callback;
    }


    @Override
    protected String doInBackground(String... params) {
        String path = params[0];
        int quality = Integer.parseInt(params[1]);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        String outputPath = params[2];
        FileOutputStream fileOutput = null;
        File imgFile;
        WritableMap map = Arguments.createMap();
        try {
            imgFile = new File(outputPath);
            imgFile.createNewFile();
            fileOutput = new FileOutputStream(imgFile);
            String suffix = params[3];
            Bitmap.CompressFormat format;
            if (suffix.equals("jpg")) {
                format = Bitmap.CompressFormat.JPEG;
            } else if (suffix.equals("png")) {
                format = Bitmap.CompressFormat.PNG;
            } else format = Bitmap.CompressFormat.WEBP;
            bitmap.compress(format, quality, fileOutput);
            fileOutput.flush();
            outputPath = imgFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            outputPath = null;
            map.putInt("code", -1);
            map.putString("error", "compress error, should look at logcat.");
            mCallback.invoke(map);
        } finally {
            if (null != fileOutput) {
                try {
                    fileOutput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return outputPath;
    }

    @Override
    protected void onPostExecute(String outputPath) {
        WritableMap map = Arguments.createMap();
        map.putInt("code", 0);
        map.putString("thumbPath", outputPath);
        mCallback.invoke(map);
    }
}
