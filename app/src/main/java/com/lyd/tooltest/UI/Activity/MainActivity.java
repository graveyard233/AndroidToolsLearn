package com.lyd.tooltest.UI.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.flyco.tablayout.SlidingTabLayout;
import com.lyd.tooltest.Base.BaseActivity;
import com.lyd.tooltest.R;
import com.lyd.tooltest.UI.Fragment.EventBusFragment;
import com.lyd.tooltest.UI.Fragment.RetrofitFragment;
import com.lyd.tooltest.UI.Fragment.RxJavaFragment;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private static final String[] titles = new String[] {"Retrofit","RxJava","EventBus"};
    private ViewPager viewPager;
    private SlidingTabLayout tabLayout;

    @Override
    protected void initViews() {
        viewPager = findViewById(R.id.main_viewPager);
        tabLayout = findViewById(R.id.main_tabLayout);

        fragments.add(new RetrofitFragment());
        fragments.add(new RxJavaFragment());
        fragments.add(new EventBusFragment());

        tabLayout.setViewPager(viewPager,titles,this,fragments);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }
}