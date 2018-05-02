package com.hur.lottery.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.hur.lottery.R;
import com.hur.lottery.base.BaseActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * <pre>
 *   @author  : syk
 *   e-mail  : shenyukun1024@gmail.com
 *   time    : 2018/03/27 14:30
 *   desc    : 欢迎页面
 *   version : 1.0
 * </pre>
 */

public class SplashActivity extends BaseActivity {
    /**
     * 初始化数据
     *
     * @param bundle 传递过来的 bundle
     */
    @Override
    public void initData(Bundle bundle) {

    }

    /**
     * 绑定布局
     *
     * @return 布局文件
     */
    @Override
    public int bindLayout() {
        return R.layout.activity_splash;
    }

    /**
     * 初始化布局
     *
     * @param savedInstanceState 保存的数据
     * @param view               View对象
     */
    @Override
    public void initView(Bundle savedInstanceState, View view) {

    }

    /**
     * 业务处理
     */
    @Override
    public void doBusiness() {
        // 判断权限
        PermissionUtils.permission(PermissionConstants.STORAGE)
                .callback(new PermissionUtils.SimpleCallback() {
                    @Override
                    public void onGranted() {
                        go2MainActivity();
                    }

                    @Override
                    public void onDenied() {

                    }
                }).request();
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
     * 跳转主界面
     */
    private void go2MainActivity() {
        Observable.just(1).delay(3000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        ActivityUtils.startActivity(LoginActivity.class);
                        ActivityUtils.finishActivity(SplashActivity.class);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
