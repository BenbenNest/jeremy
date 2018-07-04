package com.jeremy.demo.activity;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.jeremy.demo.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

//import static android.support.test.espresso.Espresso.onView;
//import static android.support.test.espresso.action.ViewActions.click;
//import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
//import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
//import static android.support.test.espresso.matcher.ViewMatchers.withId;
//import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by changqing on 2018/7/3.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class HomeActivityInstrumentationTest {

    @Rule
    public ActivityTestRule<HomeActivity> mActivityRule = new ActivityTestRule<>(HomeActivity.class);

    @Test
    public void test(){

    }


    @Test
    public void jumpToPlugin() {

        try {
            Thread.sleep(2000);
        }catch (Exception e){

        }
        //        点击RecyclerView列表中第1个item
        onView(withId(R.id.recycler_view)).perform(scrollToPosition(0));
        onView(TestUtils.withRecyclerView(R.id.recycler_view).atPosition(0)).perform(click());

//        onView(withId(R.id.banner)).perform(click());
//        onView(withId(R.id.common_recyclerview)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

//        点击带有 “Effective Java ” 字符串的item
//        onView(withId(R.id.common_recyclerview))
//                .perform(RecyclerViewActions.actionOnItem(hasDescendant(withText("Binder")), click()));

        try {
            Thread.sleep(20000);
        } catch (InterruptedException ignored) {

        }
    }


}
