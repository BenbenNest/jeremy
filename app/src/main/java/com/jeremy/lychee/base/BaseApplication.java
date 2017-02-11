package com.jeremy.lychee.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.KeyEvent;
import android.view.Window;

import com.squareup.leakcanary.RefWatcher;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Basic application functionality that should be shared among all browser applications.
 */
public class BaseApplication extends Application {
    public static Context baseContext;


    @Override
    public void onCreate() {
        super.onCreate();
        baseContext = getApplicationContext();
        ApplicationStatus.initialize(this);
    }


    /**
     * Interface to be implemented by listeners for window focus events.
     */
    public interface WindowFocusChangedListener {
        /**
         * Called when the window focus changes for {@code activity}.
         *
         * @param activity The {@link Activity} that has a window focus changed event.
         * @param hasFocus Whether or not {@code activity} gained or lost focus.
         */
        public void onWindowFocusChanged(Activity activity, boolean hasFocus);
    }

    private ObserverList<WindowFocusChangedListener> mWindowFocusListeners =
            new ObserverList<WindowFocusChangedListener>();

    /**
     * Intercepts calls to an existing Window.Callback. Most invocations are passed on directly
     * to the composed Window.Callback but enables intercepting/manipulating others.
     * <p>
     * This is used to relay window focus changes throughout the app and remedy a bug in the
     * appcompat library.
     */
    private class WindowCallbackProxy implements InvocationHandler {
        private final Window.Callback mCallback;
        private final Activity mActivity;

        public WindowCallbackProxy(Activity activity, Window.Callback callback) {
            mCallback = callback;
            mActivity = activity;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getName().equals("onWindowFocusChanged") && args.length == 1
                    && args[0] instanceof Boolean) {
                //onWindowFocusChanged((boolean) args[0]);
                // @mbrowser
                onWindowFocusChanged(((Boolean) args[0]).booleanValue());
                return null;
            } else if (method.getName().equals("dispatchKeyEvent") && args.length == 1
                    && args[0] instanceof KeyEvent) {
                return dispatchKeyEvent((KeyEvent) args[0]);
            } else {
                return method.invoke(mCallback, args);
            }
        }

        public void onWindowFocusChanged(boolean hasFocus) {
            mCallback.onWindowFocusChanged(hasFocus);

            for (WindowFocusChangedListener listener : mWindowFocusListeners) {
                listener.onWindowFocusChanged(mActivity, hasFocus);
            }
        }

        public boolean dispatchKeyEvent(KeyEvent event) {
            // TODO(aurimas): remove this once AppCompatDelegateImpl no longer steals
            // KEYCODE_MENU. (see b/20529185)
            if (event.getKeyCode() == KeyEvent.KEYCODE_MENU && mActivity.dispatchKeyEvent(event)) {
                return true;
            }
            return mCallback.dispatchKeyEvent(event);
        }
    }

    /**
     * Registers a listener to receive window focus updates on activities in this application.
     *
     * @param listener Listener to receive window focus events.
     */
    public void registerWindowFocusChangedListener(WindowFocusChangedListener listener) {
        mWindowFocusListeners.addObserver(listener);
    }

    /**
     * Unregisters a listener from receiving window focus updates on activities in this application.
     *
     * @param listener Listener that doesn't want to receive window focus events.
     */
    public void unregisterWindowFocusChangedListener(WindowFocusChangedListener listener) {
        mWindowFocusListeners.removeObserver(listener);
    }

    protected RefWatcher installLeakCanary() {
        return RefWatcher.DISABLED;
    }
}
