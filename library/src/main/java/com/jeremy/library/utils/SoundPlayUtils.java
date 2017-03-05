package com.jeremy.library.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.jeremy.library.R;

/**
 * Created by changqing.zhao on 2017/3/5.
 *
 * Android 中除了 MediaPlayer 播放音频之外还提供了 SoundPool 来播放音效， SoundPool 使用音效池的概念来管理多个短促的音效，例如它可以开始就加载 20 个音效，以后在程序中按音效的 ID 进行播放。
 * SoundPool 主要用于播放一些较短的声音片段，与 MediaPlayer 相比， SoundPool 的优势在于 CPU 资源占用量低和反应延迟小。另外， SoundPool 还支持自行设置声音的品质、音量、 播放比率等参数。
 */
public class SoundPlayUtils {
    public static final int SOUND_SHAKE = 1;
    public static SoundPool mSoundPlayer = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
    public static SoundPlayUtils soundPlayUtils;
    static Context mContext;

    public static SoundPlayUtils init(Context context) {
        if (soundPlayUtils == null) {
            soundPlayUtils = new SoundPlayUtils();
        }
        // 初始化声音
        mContext = context;
        mSoundPlayer.load(mContext, R.raw.shake_audio, 1);// 1

//        mSoundPlayer.load(mContext, R.raw.beng, 1);// 1
//        mSoundPlayer.load(mContext, R.raw.click, 1);// 2
//        mSoundPlayer.load(mContext, R.raw.diang, 1);// 3
//        mSoundPlayer.load(mContext, R.raw.ding, 1);// 4
//        mSoundPlayer.load(mContext, R.raw.gone, 1);// 5
//        mSoundPlayer.load(mContext, R.raw.popup, 1);// 6
//        mSoundPlayer.load(mContext, R.raw.water, 1);// 7
//        mSoundPlayer.load(mContext, R.raw.ying, 1);// 8

        return soundPlayUtils;
    }

    public static void play(int soundID) {
        mSoundPlayer.play(soundID, 1, 1, 0, 0, 1);
    }

}
