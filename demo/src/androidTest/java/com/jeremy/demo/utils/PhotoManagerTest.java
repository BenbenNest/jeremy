package com.jeremy.demo.utils;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.jeremy.demo.activity.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by changqing on 2017/11/20.
 */

@RunWith(AndroidJUnit4.class)
public class PhotoManagerTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void sortFile() {
//        String root;
//        String photoDir = File.separator + "DCIM" + File.separator + "Camera";
//        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            root = Environment.getExternalStorageDirectory().getAbsolutePath();
//        } else {
//            root = mainActivity.getActivity().getFilesDir().getAbsolutePath();
//        }
//        String photoPath = root + photoDir;
//        File newDir = new File(root + File.separator + "Photo_Mu" + File.separator);
//        if (!newDir.exists()) {
//            newDir.mkdirs();
//        }
//        PhotoManager.sortFile(new File(photoPath));
//        try {
//            Thread.sleep(1000 * 60 * 10);
//        } catch (Exception e) {
//
//        }
    }


}