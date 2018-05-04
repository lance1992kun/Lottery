package com.hur.lottery.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hur.lottery.LotteryApp;
import com.hur.lottery.R;
import com.hur.lottery.base.BaseActivity;
import com.hur.lottery.entity.BaseResponse;
import com.hur.lottery.entity.Constant;
import com.hur.lottery.net.HttpRequest;
import com.hur.lottery.utils.RxThreadHelper;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * <pre>
 *   author  : syk
 *   e-mail  : shenyukun1024@gmail.com
 *   time    : 2018/04/01 10:51
 *   desc    : 登录界面
 *   version : 1.0
 * </pre>
 */

public class LoginActivity extends BaseActivity {

    /**
     * 手机号输入框
     */
    private TextInputEditText mPhoneInput;
    /**
     * 密码输入框
     */
    private TextInputEditText mPassWordInput;
    /**
     * 是否是登出过来的
     */
    private boolean isLogout = false;

    @Override
    public void initData(Bundle bundle) {
        if (bundle != null) {
            isLogout = bundle.getBoolean(Constant.IS_LOGOUT, false);
        }
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        // 手机号输入框
        mPhoneInput = F(R.id.mPhoneInput);
        // 密码输入框
        mPassWordInput = F(R.id.mPasswordInput);
        // 监听事件
        setOnClick(F(R.id.mLoginBtn));
        setOnClick(F(R.id.mRegisterBtn));
    }

    @Override
    public void doBusiness() {
        // 取出本地保存的账户与密码
        String tel = SPUtils.getInstance().getString(Constant.USER_ACCOUNT);
        String password = SPUtils.getInstance().getString(Constant.USER_PASSWORD);
        // 存在的话进行自动登录
        if (!TextUtils.isEmpty(tel) && !TextUtils.isEmpty(password)) {
            mPhoneInput.setText(tel);
            mPassWordInput.setText(password);
            if (!isLogout) {
                doLogin(tel, password);
            }
        }
    }

    @Override
    public void onWidgetClick(View view) {
        switch (view.getId()) {
            // 注册按钮
            case R.id.mRegisterBtn:
                ActivityUtils.startActivity(RegisterActivity.class);
                break;
            // 登录按钮
            case R.id.mLoginBtn:
                // 检测数据完整性
                check();
                break;
            default:
                break;
        }
    }

    /**
     * 检测数据完整性
     */
    private void check() {
        String tel = mPhoneInput.getText().toString().trim();
        String password = mPassWordInput.getText().toString().trim();
        if (!RegexUtils.isMobileSimple(tel)) {
            ToastUtils.showShort("请输入正确的手机号");
            return;
        }
        if (password.length() < 7) {
            ToastUtils.showShort("请输入不少于6位的密码");
            return;
        }
        // 调用接口进行登录
        doLogin(tel, password);
    }

    /**
     * 调用接口进行登录
     *
     * @param tel      手机号
     * @param password 密码
     */
    private void doLogin(final String tel, final String password) {
        // 进行联网请求
        HttpRequest.login(tel, password)
                .compose(RxThreadHelper.<BaseResponse<String>>onNetWork())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) {
                        showLoading("正在登录…");
                    }
                })
                .subscribe(new Observer<BaseResponse<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(BaseResponse<String> result) {
                        if (result.getCode() == 1) {
                            // 保存登录状态
                            LotteryApp.getInstance().setLogin(true);
                            // 保存Token到本地
                            SPUtils.getInstance().put(
                                    Constant.USER_TOKEN, result.getData());
                            // 保存用户名到本地
                            SPUtils.getInstance().put(Constant.USER_ACCOUNT, tel);
                            // 保存密码到本地
                            SPUtils.getInstance().put(Constant.USER_PASSWORD, password);
                            // 登录成功后跳转主界面
                            go2Main();
                        } else {
                            ToastUtils.showShort(result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissLoading();
                        e.printStackTrace();
                        ToastUtils.showShort("请求失败");
                    }

                    @Override
                    public void onComplete() {
                        dismissLoading();
                    }
                });
    }

    /**
     * 跳转主界面
     */
    private void go2Main() {
        ActivityUtils.startActivity(MainActivity.class);
        ActivityUtils.finishActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dispose();
    }
}
