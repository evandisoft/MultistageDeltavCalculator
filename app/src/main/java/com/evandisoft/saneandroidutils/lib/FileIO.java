package com.evandisoft.saneandroidutils.lib;

import android.content.Context;
import android.media.MediaScannerConnection;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class FileIO {
    public static void writeStringToFile(Context context, String filename, String contents) {
        File file = new File(context.getFilesDir(), filename);
        MediaScannerConnection.scanFile(context, new String[]{file.getAbsolutePath()}, null, null);
        filename = filename.replaceAll(" ", "\\ ");
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(filename, 0);
            fileOutputStream.write(contents.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String readStringFromFile(Context context, String filename) {
        filename = filename.replaceAll(" ", "\\ ");
        try {
            FileInputStream fileInputStream = context.openFileInput(filename);
            byte[] buffer = new byte[((int) new File(context.getFilesDir(), filename).length())];
            fileInputStream.read(buffer);
            fileInputStream.close();
            return new String(buffer);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static File[] getAppFiles(Context context) {
        return context.getFilesDir().listFiles();
    }
}
