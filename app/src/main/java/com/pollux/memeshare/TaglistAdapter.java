package com.pollux.memeshare;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import static com.pollux.memeshare.DatabaseListStatic.addElement;
import static com.pollux.memeshare.DatabaseListStatic.addTag;
import static com.pollux.memeshare.DatabaseListStatic.getLinesToWrite;
import static com.pollux.memeshare.DatabaseListStatic.hasTag;
import static com.pollux.memeshare.DatabaseListStatic.removeTag;
import static com.pollux.memeshare.MainActivity.latestClick;
import static com.pollux.memeshare.MainActivity.latestClickImageObject;
import static com.pollux.memeshare.MainActivity.tagList;

/**
 * Created by pollux on 25.04.17.
 */

public class TaglistAdapter {
    private static Activity activityContext;

    public TaglistAdapter(Activity _context) {
        activityContext = _context;
    }

    public static void mAddTag(String _tag) {
        addTag(latestClick, _tag, tagList);
        latestClickImageObject.addImageTag(_tag);
    }

    public static void mRemTag(String _tag) {
        removeTag(latestClick, _tag, tagList);
        latestClickImageObject.removeImageTag(_tag);
    }

    public static boolean mHasTag(String _filepath){
        if(hasTag(_filepath, tagList)) return true;
        return false;
    }

    /* ----------------------------------------------------------------------------------------------------
     * Usage    : function that writes changes to the local info file (taglist.dat)
     * Returns  : 0 on success, -1 on failure
     */
    public static int writeToLocalFile() {
        try {
            File fptagList = new File(activityContext.getFilesDir(), "taglist.dat");
            FileOutputStream fOut = new FileOutputStream(fptagList);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fOut));

            ArrayList<String> lines = getLinesToWrite(tagList);
            Log.d("DATABASE", "successfully grabbed ArrayList with " + lines.size() + " lines");
            if (!lines.isEmpty()) {
                for (String elem : lines) {
                    bw.write(elem);
                    bw.newLine();
                }
            } else {
                Log.d("DATABASE", "saving tagList failed (empty)");
                return 0;
            }
            bw.close();
            fOut.close();
            Log.d("DATABASE", "saved tagList successfully");
            return 0;
        } catch (Exception e) {
            Log.d("DATABASE", "saving tagList failed");
            e.printStackTrace();
            return -1;
        }


    }
}
