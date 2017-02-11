
package com.jeremy.lychee.channel;

public interface Const {

    public static final int MAX_SEARCH_BYTE_LEN = 180;

    public static final int MAX_CHANNEL_STRING_LEN = 16;

    public static final int MAX_CHANNEL_BYTE_LEN = 32;

    // byte范围-128~127 这个魔数不能修改
    public static final byte[] BEG_MAGIC = {
            -10, -101, 97, 19
    };

    // byte范围-128~127 这个魔数不能修改
    public static final byte[] END_MAGIC = {
            20, -52, 59, 117
    };
    
    // byte范围-128~127 这个魔数不能修改
    public static final byte[] MD5_MAGIC = {
            -112, -52, -59, 11
    };

    public static final int BEG_MAGIC_LEN = BEG_MAGIC.length;

    public static final int END_MAGIC_LEN = END_MAGIC.length;
    
    public static final int MD5_MAGIC_LEN = MD5_MAGIC.length;
    
    public static final int MD5_LEN = 32;

    // 这个KEY不能修改
    public static final String D_KEY = "9CaP3uQ4";
    // 不能修改
    public static final String DEFAULT_CHARSET = "UTF-8";
    
    // 不能修改
    public static final byte MD5_OFFSET = 96;
    
//    public static final String PREFIX = "mse_";
    
    public static final String ERROR = "err";
}
