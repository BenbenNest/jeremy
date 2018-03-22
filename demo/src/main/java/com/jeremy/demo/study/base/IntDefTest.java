package com.jeremy.demo.study.base;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by changqing on 2018/3/22.
 */

public class IntDefTest {

    /**
     *
     Be careful with code abstractions
     Developers often use abstractions simply as a good programming practice, because
     abstractions can improve code flexibility and maintenance. However, abstractions
     come at a significant cost: generally they require a fair amount more code that
     needs to be executed, requiring more time and more RAM for that code to be mapped
     into memory. So if your abstractions aren't supplying a significant benefit, you
     should avoid them.

     For example, enums often require more than twice as much memory as static constants. You should strictly avoid using enums on Android.
     */

    /**
     我们在写代码的时候要注意类型的使用，以便于提高代码的扩展性和维护性，但是原型的使用一般会付出更多的内存的代价，
     所以如果没有特别大的好处，要尽量避免使用。
     对于枚举来说占用的内存往往是使用静态常量的两倍，因而我们要尽量避免在Android中使用枚举。
     */

    public static final int STATE_NONE = -1;
    public static final int STATE_LOADING = 0;
    public static final int STATE_SUCCESS = 1;
    public static final int STATE_ERROR = 2;
    public static final int STATE_EMPTY = 3;

    private @State
    int state;

    public void setState(@State int state) {
        this.state = state;
    }

    @State
    public int getState() {
        return this.state;
    }

    @IntDef({STATE_EMPTY, STATE_ERROR, STATE_LOADING, STATE_NONE, STATE_SUCCESS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
    }


}
