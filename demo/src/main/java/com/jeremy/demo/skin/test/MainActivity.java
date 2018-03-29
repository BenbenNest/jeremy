package com.jeremy.demo.skin.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jeremy.demo.R;
import com.jeremy.demo.skin.test.fragment.MusicFragment;
import com.jeremy.demo.skin.test.fragment.RadioFragment;
import com.jeremy.demo.skin.test.fragment.VideoFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * 换肤
 * 颜色: colors.xml 配置需要替换的颜色name 为不同的颜色值
 * 图片： 同上
 * 选择器：同上 (如 颜色选择器，皮肤包中的颜色选择器会使用皮肤包中的颜色)
 * 字体：strings.xml 配置 typeface 路径指向 assets 目录下字体文件
 * 自定义View 需要实现SkinViewSupport接口自行实现换肤逻辑(包括support中的View )
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);

        List<Fragment> list = new ArrayList<>();
        list.add(new MusicFragment());
        list.add(new VideoFragment());
        list.add(new RadioFragment());
        List<String> listTitle = new ArrayList<>();
        listTitle.add("音乐");
        listTitle.add("视频");
        listTitle.add("电台");
        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter
                (getSupportFragmentManager(), list, listTitle);
        viewPager.setAdapter(myFragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * 进入换肤
     *
     * @param view
     */
    public void skinSelect(View view) {
        startActivity(new Intent(this, SkinActivity.class));
    }
}
