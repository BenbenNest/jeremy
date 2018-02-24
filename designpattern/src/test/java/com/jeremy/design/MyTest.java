package com.jeremy.design;

import com.jeremy.design.observer.MySubject;
import com.jeremy.design.observer.Observer1;
import com.jeremy.design.observer.Observer2;
import com.jeremy.design.observer.Subject;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class MyTest {

    @Test
    public void testObserver() {
        Subject sub = new MySubject();
        sub.add(new Observer1());
        sub.add(new Observer2());
        sub.operation();
    }


}