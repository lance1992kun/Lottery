package com.hur.lottery.ui.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hur.lottery.R;
import com.hur.lottery.base.BaseActivity;
import com.hur.lottery.entity.BaseResponse;
import com.hur.lottery.net.HttpRequest;
import com.hur.lottery.utils.RxThreadHelper;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * <pre>
 *   author  : syk
 *   e-mail  : shenyukun1024@gmail.com
 *   time    : 2018/04/01 10:52
 *   desc    : 注册界面
 *   version : 1.0
 * </pre>
 */

public class RegisterActivity extends BaseActivity {

    /**
     * 手机号输入框
     */
    private TextInputEditText mPhoneInput;
    /**
     * 密码输入框
     */
    private TextInputEditText mPassWordInput;
    /**
     * 密码确认输入框
     */
    private TextInputEditText mPassWordInput2;
    /**
     * 邀请码输入框
     */
    private TextInputEditText mInvitationInput;
    /**
     * 验证码输入框
     */
    private TextInputEditText mCodeInput;
    /**
     * 获取验证码按钮
     */
    private Button mGetCodeBtn;
    /**
     * 60秒倒计时
     */
    private CountDownTimer timer;


    @Override
    public void initData(Bundle bundle) {

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_register;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        // 手机号输入框
        mPhoneInput = F(R.id.mPhoneInput);
        // 密码输入框
        mPassWordInput = F(R.id.mPasswordInput);
        // 确认密码输入框
        mPassWordInput2 = F(R.id.mPasswordConfirm);
        // 邀请码输入框
        mInvitationInput = F(R.id.mInvitationInput);
        // 验证码输入框
        mCodeInput = F(R.id.mCodeInput);
        // 获取验证码按钮
        mGetCodeBtn = F(R.id.mGetCodeBtn);
        // 监听
        setOnClick(mGetCodeBtn);
        setOnClick(F(R.id.mRegisterBtn));
    }

    @Override
    public void doBusiness() {

    }

    @Override
    public void onWidgetClick(View view) {
        switch (view.getId()) {
            // 获取验证码按钮
            case R.id.mGetCodeBtn:
                getCode();
                break;
            // 注册按钮
            case R.id.mRegisterBtn:
                // 检测必要数据是否输入完成
                check();
                break;
            default:
                break;
        }
    }

    /**
     * 获取手机验证码
     */
    private void getCode() {
        String phoneString = mPhoneInput.getText().toString().trim();
        if (!RegexUtils.isMobileSimple(phoneString)) {
            ToastUtils.showShort("请输入正确的手机号");
            return;
        }
        // 执行接口获取手机验证码
        doGetCode(phoneString);
    }

    /**
     * 检验必要数据是否输入完成
     */
    private void check() {
        String phoneString = mPhoneInput.getText().toString().trim();
        String password = mPassWordInput.getText().toString().trim();
        String password2 = mPassWordInput2.getText().toString().trim();
        String invitation = mInvitationInput.getText().toString().trim();
        String code = mCodeInput.getText().toString().trim();
        if (!RegexUtils.isMobileSimple(phoneString)) {
            ToastUtils.showShort("请输入正确的手机号码");
            return;
        }
        if (password.length() < 7) {
            ToastUtils.showShort("密码不少于6位！");
            return;
        } else if (!password.equals(password2)) {
            ToastUtils.showShort("2次输入的密码不正确,请确认！");
            return;
        }
        if (TextUtils.isEmpty(invitation)) {
            ToastUtils.showShort("请输入正确的邀请码！");
            return;
        }
        if (TextUtils.isEmpty(code)) {
            ToastUtils.showShort("请输入正确的验证码！");
            return;
        }
        // 进行接口请求
        doRegister(phoneString, password, password2, code, invitation);
    }

    /**
     * 获取手机验证码
     */
    private void doGetCode(String tel) {
        // 联网获取手机验证码
        HttpRequest.getCode(tel)
                .compose(RxThreadHelper.<BaseResponse<String>>onNetWork())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        // 显示对话框
                        showLoading("正在获取验证码…");
                    }
                })
                .subscribe(new Observer<BaseResponse<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(BaseResponse<String> stringBaseResponse) {
                        ToastUtils.showShort("获取验证码成功");
                        startTimer();
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
     * 进行注册请求
     *
     * @param tel       手机号
     * @param password  密码
     * @param password2 确认密码
     * @param code      验证码
     * @param camilo    邀请码
     */
    private void doRegister(String tel, String password, String password2, String code,
                            String camilo) {
        // 联网进行注册
        HttpRequest.register(tel, password, password2, code, camilo)
                .compose(RxThreadHelper.<BaseResponse<String>>onNetWork())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        // 显示对话框
                        showLoading("正在注册…");
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
                            // 注册成功跳转登录界面
                            ActivityUtils.startActivity(LoginActivity.class);
                            ActivityUtils.finishActivity(RegisterActivity.this);
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
     * 开启定时器刷新获取验证码文字
     */
    private void startTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        timer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String msg = millisUntilFinished / 1000 + "秒后重发";
                mGetCodeBtn.setEnabled(false);
                mGetCodeBtn.setText(msg);
            }

            @Override
            public void onFinish() {
                mGetCodeBtn.setText("从新获取");
                mGetCodeBtn.setEnabled(true);
            }
        };
        timer.start();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dispose();
    }
}
