package com.jeremy.lychee.videoplayer;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.qihoo.jia.play.jinbase.GL2JNILib;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class VideoRenderer extends GLTextureView implements GLSurfaceView.Renderer {
    public VideoRenderer(Context context) {
        super(context);
        init();
    }

    public VideoRenderer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VideoRenderer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.setRenderer(this);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GL2JNILib.init(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GL2JNILib.step();
    }
}
