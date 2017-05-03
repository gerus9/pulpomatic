package com.gerus.pulpomatic.utils;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by gerus-mac on 01/05/17.
 */

public class UImages {

    private static final String PATH = Environment.getExternalStorageDirectory().getPath();
    private static final String IMAGE_EXTENSION = ".png";

    public static final String PATH_IMAGE = PATH+"/map"+IMAGE_EXTENSION;


    public static Bitmap prcCompressBitmap(Bitmap poBitmap) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(PATH_IMAGE);
            poBitmap.compress(Bitmap.CompressFormat.PNG, 80, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return poBitmap;
    }
}
