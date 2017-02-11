package com.jeremy.lychee.utils;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AudioUtil {

    private final int playBufSize;
    private final int recBufSize;
    private String mFileName = "/sdcard/tmp_audio.wav";
    private AudioRecord mRecorder;
    private AudioTrack mPlayer;
    private boolean recording;
    private boolean playing;

    static final int frequency = 16000;
    static final int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    static final int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;

    public AudioUtil(Context context) {
        mFileName = context.getExternalFilesDir(null).getAbsolutePath() + "/" + "live_audio.wav";
        File file = new File(mFileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        playBufSize = AudioTrack.getMinBufferSize(frequency, channelConfiguration, audioEncoding);
        mPlayer = new AudioTrack(AudioManager.STREAM_MUSIC, frequency, channelConfiguration, audioEncoding, playBufSize, AudioTrack.MODE_STREAM);

        recBufSize = AudioRecord.getMinBufferSize(frequency, channelConfiguration, audioEncoding);
        mRecorder = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency, channelConfiguration, audioEncoding, recBufSize);

//        AudioManager am = (AudioManager) ContentApplication.getInstance().getSystemService(Context.AUDIO_SERVICE);
//        int result = am.requestAudioFocus(null,
//                AudioManager.STREAM_MUSIC,
//                AudioManager.AUDIOFOCUS_GAIN);
//
//        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
//            // Start playback.
//        }
    }

    public void startPlay(PlayCallback callback) {

        mPlayer.play();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(mFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        playing = true;
        final FileInputStream finalFis = fis;
        new Thread() {
            @Override
            public void run() {
                while (playing) {
                    byte[] buffer = new byte[playBufSize];
                    if (finalFis != null) {
                        try {
                            int len = finalFis.read(buffer);
                            if (len != -1) {
                                mPlayer.write(buffer, 0, len);
                            } else {
                                playing = false;
                                PCMHelper.PCM2Wave(new File(mFileName), 1, frequency, 16);
                                callback.onPlayOver();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }.start();
    }

    //停止播放
    public void stopPlay() {
        playing = false;
        mPlayer.stop();
//        mPlayer.release();
//        mPlayer = null;
    }


    public void startRecord() {

        mRecorder.startRecording();//开始录制
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        final FileOutputStream finalFos = fos;
        recording = true;
        new Thread() {
            @Override
            public void run() {
                while (recording) {
                    byte[] buffer = new byte[recBufSize];
                    int len = mRecorder.read(buffer, 0, recBufSize);
                    try {
                        if (len <= 0) {
                            recording = false;
                        } else {
                            if (finalFos != null) {
                                finalFos.write(buffer, 0, len);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public void stopRecord() {
        recording = false;
        mRecorder.stop();
//        mRecorder.release();
//        mRecorder = null;
    }

    public void clean(){
        new File(mFileName).delete();
    }

    private void release() {
        mPlayer.release();
        mPlayer = null;
        mRecorder.release();
        mRecorder = null;
    }

    public File getFile(){
        return new File(mFileName);
    }
    public interface PlayCallback {
        void onPlayOver();
    }
}
