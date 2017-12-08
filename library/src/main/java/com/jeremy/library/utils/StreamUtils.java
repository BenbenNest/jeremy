package com.jeremy.library.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by changqing on 2017/12/7.
 */

public class StreamUtils {

    /**
     * @param oStream
     * @description 关闭数据流
     */
    public static void closeStream(Closeable oStream) {
        if (null != oStream) {
            try {
                oStream.close();
            } catch (IOException e) {
                oStream = null;//赋值为null,等待垃圾回收
                e.printStackTrace();
            }
        }
    }

}
