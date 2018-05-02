package com.hur.lottery.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hur.lottery.R;
import com.hur.lottery.base.BaseActivity;
import com.hur.lottery.entity.Constant;
import com.hur.lottery.ui.fragment.HistoryFragment;
import com.hur.lottery.ui.fragment.IMFragment;
import com.hur.lottery.ui.fragment.LimitFragment;
import com.hur.lottery.ui.fragment.ProfileFragment;

/**
 * <pre>
 *   author  : syk
 *   e-mail  : shenyukun1024@gmail.com
 *   time    : 2018/03/27 15:28
 *   desc    : 主界面
 *   version : 1.0
 * </pre>
 */

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    /**
     * 抽屉布局
     */
    private DrawerLayout mDrawerLayout;
    /**
     * 界面集合
     */
    private Fragment[] mFragments = new Fragment[3];
    /**
     * 当前下标
     */
    private int curIndex = 0;
    /**
     * 是否刷新UI
     */
    private boolean isRefresh = false;
    /**
     * 上次点击返回键时间
     */
    private long lastClick = 0L;

    /**
     * 初始化数据
     *
     * @param bundle 传递过来的 bundle
     */
    @Override
    public void initData(Bundle bundle) {
        if (bundle != null) {
            // 是否刷新UI
            isRefresh = bundle.getBoolean(Constant.IS_NEED_REFRESH, false);
        }
        // 初始化各种界面
        mFragments[0] = IMFragment.newInstance();
        mFragments[1] = HistoryFragment.newInstance();
        mFragments[2] = LimitFragment.newInstance();
        // mFragments[3] = ProfileFragment.newInstance();
    }

    /**
     * 绑定布局
     *
     * @return 布局文件
     */
    @Override
    public int bindLayout() {
        return R.layout.activity_main;
    }

    /**
     * 初始化布局
     *
     * @param savedInstanceState 保存的数据
     * @param view               View对象
     */
    @Override
    public void initView(Bundle savedInstanceState, View view) {
        // 抽屉布局
        mDrawerLayout = F(R.id.mDrawerLayout);
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                // 设置布局只有2/3大小
                DrawerLayout.LayoutParams layoutParams
                        = (DrawerLayout.LayoutParams) drawerView.getLayoutParams();
                layoutParams.width = ScreenUtils.getScreenWidth() / 3 * 2;
                layoutParams.height = ScreenUtils.getScreenHeight();
                drawerView.setLayoutParams(layoutParams);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // 设置只有上层可点击
                drawerView.setClickable(true);
            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        // 标题栏
        Toolbar mToolBar = F(R.id.mToolBar);
        setSupportActionBar(mToolBar);
        // 导航栏监听
        ((RadioGroup) F(R.id.mNavigationGroup)).setOnCheckedChangeListener(this);
        // 添加界面到布局中
        FragmentUtils.add(getSupportFragmentManager()
                , ProfileFragment.newInstance(), R.id.mLeftLay);
        FragmentUtils.add(getSupportFragmentManager()
                , mFragments, R.id.fragment_container, curIndex);
    }

    /**
     * 业务处理
     */
    @Override
    public void doBusiness() {
        // 登录成功后才注册推送
        // App.getInstance().openPush();
        // 结束除自己以外的Activity
        ActivityUtils.finishOtherActivities(MainActivity.class);
        // 通知Fragment刷新UI并重置标识
        refreshData();
    }

    /**
     * 点击逻辑处理
     *
     * @param view 视图
     */
    @Override
    public void onWidgetClick(View view) {

    }

    /**
     * 刷新数据
     */
    private void refreshData() {
        if (isRefresh) {
            // 重置标识位
            isRefresh = false;
            if (curIndex == 0) {
                mRefresh.onDataRefresh();
            } else {
                // 切换Fragment到实时数据
                ((RadioButton) F(R.id.mImRb)).setChecked(true);
            }
        }
    }

    /**
     * 导航栏监听
     *
     * @param checkedId 哪个按钮
     */
    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            // 实时消息
            case R.id.mImRb:
                switchFragment(0);
                break;
            // 历史消息
            case R.id.mHistoryRb:
                switchFragment(1);
                break;
            // 超大极限
            case R.id.mLimitRb:
                switchFragment(2);
                break;
            // 我的
//            case R.id.mProfileRb:
//                switchFragment(3);
//                break;
            default:
                break;
        }
    }

    /**
     * 切换界面
     *
     * @param index 要切换到的界面
     */
    private void switchFragment(int index) {
        FragmentUtils.showHide(curIndex = index, mFragments);
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - lastClick > 2000) {
            ToastUtils.showShort("再按一次退出应用");
            lastClick = System.currentTimeMillis();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            // 是否刷新UI
            isRefresh = bundle.getBoolean(Constant.IS_NEED_REFRESH, false);
            // 通知Fragment刷新UI并重置标识
            refreshData();
        }
    }
}
