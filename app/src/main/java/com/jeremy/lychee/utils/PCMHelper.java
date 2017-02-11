package com.jeremy.lychee.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Helper class about PCM.
 */
public class PCMHelper {
    private final static int HEADER_LENGTH = 44;

    private final static byte[] RIFF_array = {0x52, 0x49, 0x46, 0x46};
    private final static byte[] WAVE_array = {0x57, 0x41, 0x56, 0x45};
    private final static byte[] fmt_array = {0x66, 0x6D, 0x74, 0x20};
    private final static byte[] data_array = {0x64, 0x61, 0x74, 0x61};

    /**
     * Add wave header at front pcm data.
     *
     * @param data       byte array of pcm data
     * @param channel    pcm channel, e.g. 1 or 2
     * @param sampleRate pcm sample rate, e.g. 22050, 44100 etc.
     * @param bits       pcm bits, e.g. 8 or 16
     * @return
     */
    public static byte[] PCM2Wave(byte[] data, int channel, int sampleRate, int bits) {
        int riffSize = data.length + HEADER_LENGTH;
        int chunk = channel * bits / 8;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            // Write RIFF header
            dos.write(RIFF_array);
            dos.write(intToByteArray(riffSize));
            dos.write(WAVE_array);

            // Write format header
            dos.write(fmt_array);
            dos.write(intToByteArray(16));
            dos.write(shortToByteArray(1));
            dos.write(shortToByteArray(channel));
            dos.write(intToByteArray(sampleRate));
            dos.write(intToByteArray(sampleRate * chunk));
            dos.write(shortToByteArray(2));
            dos.write(shortToByteArray(bits));

            // Write data section
            dos.write(data_array);
            dos.write(intToByteArray(data.length));
            dos.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return baos.toByteArray();
    }

    /**
     * Add wave header at front pcm data.
     *
     * @param pcmFile    file of pcm data
     * @param channel    pcm channel, e.g. 1 or 2
     * @param sampleRate pcm sample rate, e.g. 22050, 44100 etc.
     * @param bits       pcm bits, e.g. 8 or 16
     * @return
     */
    public static void PCM2Wave(File pcmFile, int channel, int sampleRate, int bits) {
        int riffSize = (int) pcmFile.length() + HEADER_LENGTH;
        int chunk = channel * bits / 8;

        try {
            RandomAccessFile randomAccessWriter = new RandomAccessFile(pcmFile, "rw");
            randomAccessWriter.seek(0);

            // Write RIFF header
            randomAccessWriter.write(RIFF_array);
            randomAccessWriter.write(intToByteArray(riffSize));
            randomAccessWriter.write(WAVE_array);

            // Write format header
            randomAccessWriter.write(fmt_array);
            randomAccessWriter.write(intToByteArray(16));
            randomAccessWriter.write(shortToByteArray(1));
            randomAccessWriter.write(shortToByteArray(channel));
            randomAccessWriter.write(intToByteArray(sampleRate));
            randomAccessWriter.write(intToByteArray(sampleRate * chunk));
            randomAccessWriter.write(shortToByteArray(2));
            randomAccessWriter.write(shortToByteArray(bits));

            // Write data section
            randomAccessWriter.write(data_array);
            randomAccessWriter.write(intToByteArray((int) pcmFile.length()));
//            dos.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * att. Little Endian
     *
     * @param value
     * @return
     */
    private static byte[] intToByteArray(int value) {
        return new byte[]{(byte) value, (byte) (value >>> 8),
                (byte) (value >>> 16), (byte) (value >>> 24)};
    }


    /**
     * att. Little Endian
     *
     * @param value
     * @return
     */
    private static byte[] shortToByteArray(int value) {
        return new byte[]{(byte) value, (byte) (value >>> 8)};
    }
}