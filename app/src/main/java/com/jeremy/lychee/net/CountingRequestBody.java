package com.jeremy.lychee.net;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

public class CountingRequestBody {
    public interface UploadProgressListener {
        void onProgressChanged(int max, int progress);
        void onComplete();
    }

    static public RequestBody create(final MediaType contentType, final File file, UploadProgressListener listener) {
        if (file == null) throw new NullPointerException("content == null");

        return new RequestBody() {
            private static final int BUFFER_SIZE = 4096;

            @Override
            public MediaType contentType() {
                return contentType;
            }

            @Override
            public long contentLength() {
                return file.length();
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                byte[] buffer = new byte[BUFFER_SIZE];
                FileInputStream in = new FileInputStream(file);
                long totalRead = 0;
                try {
                    int read;
                    while ((read = in.read(buffer)) != -1) {
                        totalRead += read;
                        if (listener != null) listener.onProgressChanged((int) contentLength(), (int) totalRead);
                        sink.write(buffer, 0, read);
                        sink.flush();
                    }
                } finally {
                    in.close();
                    if (listener != null ) listener.onComplete();
                }
            }
        };
    }
}
