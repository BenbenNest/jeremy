package com.jeremy.library.utils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by changqing on 2017/11/26.
 */

public class FileUtils {

    public static boolean isImage(String type) {
        String[] imagType = {".jpg", ".png", ".bmp", ".gif", "jpeg"};
        List<String> imageTypeLists = Arrays.asList(imagType);
        if (imageTypeLists.contains(type)) {
            return true;
        } else {
            return false;
        }
    }


}
