package com.jeremy.demo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jeremy.demo.R;
import com.jeremy.demo.activity.anim.Xml4AnimActivity;
import com.jeremy.demo.activity.anim.layoutTransitionActivity;

/**
 * anim文件夹下存放tween animation和frame animation；
 * xml文件里只有scale、rotate、translate、alpha、set五个标签；
 * 在代码中使用AnimationUtils.loadAnimation（）方法加载；
 * 使用mView.setAnimation(mAnimation)为mView加载动画；
 * 使用mView.startAnimation()开启动画；
 * <p>
 * animator文件夹下存放property animation，即属性动画，
 * xml文件里有animator、objectAnimator和set三个标签；
 * 在代码中使用AnimatorInflater.loadAnimator（）方法加载动画；
 * 使用mAnimation.setTarget（mView）为mView加载动画。
 * 使用mAnimation.start（）开启动画
 */
public class PropertyAnimActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_anim);


    }

    public void XmlAnimation(View view) {
        Xml4AnimActivity.startActivity(this);
    }

    public void layoutTransition(View view) {
        layoutTransitionActivity.startActivity(this);
    }

}
