
package com.jeremy.lychee.channel;

import com.jeremy.lychee.base.ContentApplication;
import com.jeremy.lychee.utils.logger.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;

public class ChannelReader implements Const {

    private static final String TAG = "ChannelReader";

    private static final String ERR_CHANNEL_MD5 = ERROR + "01";

    private static final String ERR_FILE_NOT_FOUND1 = ERROR + "02";

    private static final String ERR_FILE_NOT_FOUND2 = ERROR + "03";

    private static final String ERR_IO_EXCEPTION = ERROR + "04";

    private static final String ERR_CHANNEL_LENGTH1 = ERROR + "05";
    
    private static final String ERR_CHANNEL_LENGTH2 = ERROR + "06";
    
    private static final String ERR_CHANNEL_CHAR_ILLEGAL = ERROR + "07";

    private static final String ERR_BEG_MAGIC_NOT_FOUND = ERROR + "08";
    
    private static final String ERR_PARSE_CHANNEL_NULL = ERROR + "09";
    
    private static final String ERR_DECRYPT_RESULT_NULL = ERROR + "10";

    private static final String INFO_CHANNEL_NOT_LOAD = ERROR + "11";

    public static String loadChannel(String fullPathApkName) {
        return loadChannel(getApkFile(fullPathApkName));
    }

    private static String loadChannel(File apkFile) {
        String channel = INFO_CHANNEL_NOT_LOAD;
        if (apkFile != null) {
            RandomAccessFile raf = null;
            try {
                raf = new RandomAccessFile(apkFile, "r");
                long length = raf.length();
                if (length > MAX_SEARCH_BYTE_LEN) {
                    byte[] bytes = new byte[MAX_SEARCH_BYTE_LEN];
                    raf.seek(length - MAX_SEARCH_BYTE_LEN);
                    raf.readFully(bytes);
                    String ch = parseChannelFromEnd(bytes);
                    if (ch != null && ch.length() > 0) {
                        channel = ch; // channel可能以err开头，即表示错误
                        if (isChannelLengthLegal(ch)) {
                            if (!isChannelCharLegal(ch)) {
                                Logger.t(TAG).i(ERR_CHANNEL_CHAR_ILLEGAL);
                                channel = ERR_CHANNEL_CHAR_ILLEGAL;
                            } 
                        } else {
                            Logger.t(TAG).i(ERR_CHANNEL_LENGTH2);
                            channel = ERR_CHANNEL_LENGTH2;
                        }
                    } else {
                        channel = ERR_PARSE_CHANNEL_NULL;
                    }
                }
            } catch (FileNotFoundException e) {
                Logger.t(TAG).i(ERR_FILE_NOT_FOUND1);
                channel = ERR_FILE_NOT_FOUND1;
                e.printStackTrace();
            } catch (IOException e) {
                channel = ERR_IO_EXCEPTION;
                Logger.t(TAG).i(ERR_IO_EXCEPTION);
                e.printStackTrace();
            } finally {
                if (raf != null) {
                    try {
                        raf.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            Logger.t(TAG).i(ERR_FILE_NOT_FOUND2);
            channel = ERR_FILE_NOT_FOUND2;
        }
        Logger.t(TAG).d("channel=" + channel);
        return channel;
    }

    private static String parseChannelFromEnd(byte[] data) {
        String channel = null;
        if (data != null) {
            ChannelBytes cbs = parseChannelBytesFromEnd(data);
            if (cbs != null) {
                int begIndex = cbs.mBegMagicStartIdx;
                if (cbs.mBegMagicStartIdx >= 0 && cbs.mBytes != null) {
                    channel = EncryptUtil.fromDesByteArrayToString(cbs.mBytes, D_KEY);
                    if (channel != null && channel.length() > 0) {
                        if (begIndex >= (MD5_MAGIC_LEN + MD5_LEN)) {
                            if (isMatchMagic(data, begIndex - MD5_MAGIC_LEN, MD5_MAGIC, MD5_MAGIC_LEN)) {
                                byte[] md5Bytes = new byte[MD5_LEN];
                                for (int i = 0, j = begIndex - MD5_MAGIC_LEN - MD5_LEN; i < MD5_LEN; i++, j++) {
                                    md5Bytes[i] = (byte) (data[j] + MD5_OFFSET);
                                }
                                
                                String readMd5 = new String(md5Bytes);
                                String md5 = Md5Util.md5LowerCase(channel);
                                if (md5 != null && !md5.equals(readMd5)) {
                                    // 被篡改
                                    channel = ERR_CHANNEL_MD5;
                                    Logger.t(TAG).i(ERR_CHANNEL_MD5);
                                }
                            } else {
                                // 没有找到MD5_MAGIC，不验证MD5
                                Logger.t(TAG).i("no MM, skip verify");
                            }
                        } else {
                            // MD5长度不够，不验证MD5
                            Logger.t(TAG).i("error len of md5, skip verify");
                        }
                        if(channel.toLowerCase().startsWith("np_me_")){
                            channel = channel.substring(3);
                        }
                    } else {
                        channel = ERR_DECRYPT_RESULT_NULL;
                        Logger.t(TAG).i(ERR_DECRYPT_RESULT_NULL);
                    }
                } else {
                    // mBegMagicStartIdx<0 都是parseChannelBytesFromEnd中的异常状态
                    if (cbs.mBytes != null) {
                        try {
                            channel = new String(cbs.mBytes, DEFAULT_CHARSET).trim();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            Logger.t(TAG).i("cbs.mBytes encoding error");
                        }
                    }
                }
            } else {
                // 不应该到这里。
                Logger.t(TAG).i("cbs=null");
            }
        }

        return channel;
    }

    // 从后往前
    private static ChannelBytes parseChannelBytesFromEnd(byte[] data) {
        ChannelBytes cb = new ChannelBytes();
        if (data != null) {
            int maxBegMagicStartIdx = data.length - BEG_MAGIC_LEN - END_MAGIC_LEN;
            int maxEndMagicStartIdx = data.length - END_MAGIC_LEN;

            if (maxBegMagicStartIdx >= 0 && maxEndMagicStartIdx > BEG_MAGIC_LEN) {
                int begMagicStartIdx = -1;
                int endMagicStartIdx = -1;

                for (int i = maxEndMagicStartIdx; i >= BEG_MAGIC_LEN; i--) {
                    if (isMatchMagic(data, i, END_MAGIC, END_MAGIC_LEN)) {
                        endMagicStartIdx = i;
                        Logger.t(TAG).d("endMagicStartIdx=" + endMagicStartIdx);
                        break;
                    }
                }

                if (endMagicStartIdx > 0) {
                    for (int j = endMagicStartIdx - BEG_MAGIC_LEN; j >= 0; j--) {
                        if (isMatchMagic(data, j, BEG_MAGIC, BEG_MAGIC_LEN)) {
                            begMagicStartIdx = j;
                            Logger.t(TAG).d("begMagicStartIdx=" + begMagicStartIdx);
                            break;
                        }
                    }

                    if (begMagicStartIdx >= 0) {
                        int chBytesLen = endMagicStartIdx - begMagicStartIdx - BEG_MAGIC_LEN;
                        if (chBytesLen > 0) {
                            Logger.t(TAG).d("chBytesLen=" + chBytesLen);
                            byte[] bytes = new byte[chBytesLen];
                            for (int k = 0, l = begMagicStartIdx + BEG_MAGIC_LEN; k < chBytesLen; k++, l++) {
                                bytes[k] = data[l];
                            }
                            cb.mBytes = bytes;
                            cb.mBegMagicStartIdx = begMagicStartIdx;
                        } else {
                            // channel长度错误
                            try {
                                Logger.t(TAG).i("error len of channel");
                                cb.mBegMagicStartIdx = -1;
                                cb.mBytes = ERR_CHANNEL_LENGTH1.getBytes(DEFAULT_CHARSET);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        // 找不到BEG_MAGIC
                        try {
                            Logger.t(TAG).i("no BM, error data");
                            cb.mBegMagicStartIdx = -1;
                            cb.mBytes = ERR_BEG_MAGIC_NOT_FOUND.getBytes(DEFAULT_CHARSET);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    // 找不到END_MAGIC
                    Logger.t(TAG).i("no EM, use default channel");
                    cb.mBegMagicStartIdx = -1;
                    cb.mBytes = getAssetChannelData();
                }

            }

        }

        return cb;

    }

    private static class ChannelBytes {

        public ChannelBytes() {
            mBytes = null;
            mBegMagicStartIdx = -1;
        }

        public byte[] mBytes;
        public int mBegMagicStartIdx;
    }

    private static boolean isMatchMagic(byte[] data, int startIdx, byte[] magic, int magicSize) {
        if (data != null && startIdx >= 0) {
            int len = data.length;
            if (len > 0) {
                int endIdx = startIdx + magicSize - 1;
                if (len > endIdx) {
                    for (int i = 0, j = startIdx; i < magicSize; i++, j++) {
                        if (data[j] != magic[i]) {
                            return false;
                        }
                    }
                    return true;
                }
            }
        }

        return false;
    }

    private static File getApkFile(String fullPathApkName) {
        File apk = null;
        if (fullPathApkName != null && fullPathApkName.length() > 0) {
            apk = new File(fullPathApkName);
        }
        return apk;
    }
    
    public static boolean isChannelLengthLegal(String channel) {
        if (channel != null) {
            int chLen = channel.length();
            if (chLen <= 0 || chLen > Const.MAX_CHANNEL_STRING_LEN) {
                return false;
            }
            
            return true;
        }

        return false;
    }
    
    public static boolean isChannelCharLegal(String channel) {
        if (channel != null) {
            int chLen = channel.length();
            for (int j = 0; j < chLen; j++) {
                char val = channel.charAt(j);
                if (!isCharLegal(val)) {
                    return false;
                }
            }
            
            return true;
        }
        
        return false;
    }
    
    private static boolean isCharLegal(char val) {
        if (val >= '0' && val <= '9') {
            return true;
        }

        if (val >= 'a' && val <= 'z') {
            return true;
        }

        if (val >= 'A' && val <= 'Z') {
            return true;
        }

        if (val == '_') {
            return true;
        }

        return false;
    }
    
    private static byte[] getAssetChannelData(){
        InputStream is = null;
        try {
            is = ContentApplication.getInstance().getAssets().open("cid.dat");
            byte[] data = new byte[is.available()];
            is.read(data);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                is.close();
            } catch (Exception e) {
            }
        }
    }

}
