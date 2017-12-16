package com.jeremy.demo;

import com.jeremy.demo.utils.PhotoManager;

import org.junit.Test;

import java.io.File;

/**
 * Created by changqing on 2017/11/25.
 */

public class PhotoManagerTest {

    @Test
    public void sortPhoto() {
        String root="/Users/didi/benben/temp/photo";
        File newDir = new File(root + File.separator + "Photo_Mu" + File.separator);
        if (!newDir.exists()) {
            newDir.mkdirs();
        }
        PhotoManager.sortFile(new File(root));
        try {
            Thread.sleep(1000 * 60 * 5);
        } catch (Exception e) {

        }
    }

}
