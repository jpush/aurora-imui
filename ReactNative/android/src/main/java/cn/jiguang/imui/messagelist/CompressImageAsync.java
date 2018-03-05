package cn.jiguang.imui.messagelist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class CompressImageAsync extends AsyncTask<String, Void, String> {



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
        try {
            imgFile = new File(outputPath);
            imgFile.createNewFile();
            fileOutput = new FileOutputStream(imgFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fileOutput);
            fileOutput.flush();
            outputPath = imgFile.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            outputPath = null;
        } catch (IOException e) {
            e.printStackTrace();
            outputPath = null;
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

}
