package com.jeremy.lychee.utils;

public class SystemInfo {

    static public boolean isDebugMode() {
        return "{_DEBUG_}".length()!=0;
    }

    static public String getBuildNumber() {
        final String PH_BUILD_NUM = "{_BUILD_NUM_}";
        return PH_BUILD_NUM.length() > 10 ? "" : PH_BUILD_NUM;
    }
}
