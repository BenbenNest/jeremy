package com.jeremy.demo.jacoco;

import android.content.Context;
import android.util.Log;

import com.jeremy.library.utils.StorageUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by changqing on 2018/7/4.
 */

public class CoverageUtils {
    private CoverageUtils instance;

    private CoverageUtils() {

    }

    public CoverageUtils getInstance() {
        synchronized (CoverageUtils.class) {
            if (instance == null) {
                instance = new CoverageUtils();
            }
        }
        return instance;
    }

    public static void creatCoverageFile(Context context) {
        File file = StorageUtils.getCoverageFile(context);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                Log.v("jeremy", e.getMessage());
            }
        }
    }

    public static void writeCoverageReport(Context context) {
        try (OutputStream out = new FileOutputStream(StorageUtils.getCoverageFile(context))) {
            Object agent = Class.forName("org.jacoco.agent.rt.RT")
                    .getMethod("getAgent")
                    .invoke(null);

            out.write((byte[]) agent.getClass()
                    .getMethod("getExecutionData", boolean.class)
                    .invoke(agent, false));
        } catch (Exception e) {
            Log.v("jeremy", e.getMessage());
        }
    }

}
