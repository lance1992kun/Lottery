package com.hur.lottery.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hur.lottery.LotteryApp;
import com.hur.lottery.R;
import com.hur.lottery.base.BaseFragment;
import com.hur.lottery.entity.BaseResponse;
import com.hur.lottery.entity.Constant;
import com.hur.lottery.enums.SettingType;
import com.hur.lottery.net.HttpRequest;
import com.hur.lottery.net.HttpUrl;
import com.hur.lottery.net.NetCallBack;
import com.hur.lottery.ui.activity.LoginActivity;
import com.hur.lottery.utils.RequestHelper;
import com.hur.lottery.utils.RxThreadHelper;
import com.hur.lottery.widget.ChargeDialog;
import com.hur.lottery.widget.SettingDialog;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * <pre>
 *   author  : syk
 *   e-mail  : shenyukun1024@gmail.com
 *   time    : 2018/04/04 09:35
 *   desc    : 我的界面
 *   version : 1.0
 * </pre>
 */

public class ProfileFragment extends BaseFragment {

    private TextView mPhoneText = null;
    private TextView mMemberText = null;
    private TextView mInviteText = null;
    private TextView mLimitText = null;
    private TextView mBigLimitText = null;
    private TextView mVersionText = null;
    private SettingDialog settingDialog = null;
    private ChargeDialog chargeDialog = null;

    /**
     * 获取实时消息界面
     *
     * @return 实时消息界面
     */
    public static ProfileFragment newInstance() {
        Bundle args = new Bundle();
        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initData(Bundle bundle) {

    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_profile;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        mPhoneText = (TextView) view.findViewById(R.id.mPhoneText);
        mMemberText = (TextView) view.findViewById(R.id.mMemberText);
        mInviteText = (TextView) view.findViewById(R.id.mInviteText);
        mLimitText = (TextView) view.findViewById(R.id.mLimitText);
        mBigLimitText = (TextView) view.findViewById(R.id.mBigLimitText);
        mVersionText = (TextView) view.findViewById(R.id.mVersionText);

        view.findViewById(R.id.mLimitLay).setOnClickListener(this);
        view.findViewById(R.id.mBigLimitLay).setOnClickListener(this);
        view.findViewById(R.id.mUserChargeLay).setOnClickListener(this);
        view.findViewById(R.id.mExitBtn).setOnClickListener(this);
    }

    @Override
    public void doBusiness() {
        // 登录的手机号
        mPhoneText.setText(SPUtils.getInstance().getString(Constant.USER_ACCOUNT));
        // 极限通知
        String limit = SPUtils.getInstance().getInt(Constant.LIMIT_COUNT, 13) + "";
        mLimitText.setText(limit);
        // 超大极限
        String bigLimit = SPUtils.getInstance().getInt(Constant.BIG_LIMIT_COUNT, 23) + "";
        mBigLimitText.setText(bigLimit);
        // 目前写死
        mMemberText.setText("普通会员");
        mInviteText.setText("邀请码");
        mVersionText.setText("1.0.0");
    }

    @Override
    public void onWidgetClick(View view) {
        switch (view.getId()) {
            // 极限通知
            case R.id.mLimitLay:
                showSettingDialog(SettingType.TYPE_LIMIT);
                break;
            // 超大极限
            case R.id.mBigLimitLay:
                showSettingDialog(SettingType.TYPE_BIG_LIMIT);
                break;
            // 用户充值
            case R.id.mUserChargeLay:
                if (chargeDialog != null) {
                    chargeDialog.dismiss();
                    chargeDialog = null;
                }
                chargeDialog = new ChargeDialog();
                chargeDialog.init(new ChargeDialog.OnDismissListener() {
                    @Override
                    public void onDismiss(String value) {
                        userCharge(value);
                    }
                });
                chargeDialog.show(getFragmentManager(), null);
                break;
            // 退出登录
            case R.id.mExitBtn:
                logout();
                break;
            default:
                break;
        }
    }

    /**
     * 根据类型显示设置界面
     *
     * @param type 类型
     */
    private void showSettingDialog(SettingType type) {
        // 重置对话框
        if (settingDialog != null) {
            settingDialog.dismiss();
            settingDialog = null;
        }
        settingDialog = new SettingDialog();
        // 极限通知
        if (type == SettingType.TYPE_LIMIT) {
            settingDialog.init(type, new SettingDialog.OnDismissListener() {
                @Override
                public void onDismiss(int value) {
                    SPUtils.getInstance().put(Constant.LIMIT_COUNT, value);
                    String limit = value + "";
                    mLimitText.setText(limit);
                }
            });
        }
        // 超大极限
        else {
            settingDialog.init(type, new SettingDialog.OnDismissListener() {
                @Override
                public void onDismiss(int value) {
                    SPUtils.getInstance().put(Constant.BIG_LIMIT_COUNT, value);
                    String bigLimit = value + "";
                    mBigLimitText.setText(bigLimit);
                }
            });
        }
        // 显示对话框
        settingDialog.show(getFragmentManager(), null);
    }

    /**
     * 测试用户充值功能
     */
    private void userCharge(String value) {
        // 用户充值
        OkGo.<BaseResponse<String>>post(HttpUrl.USER_CHARGE)
                .tag(this)
                .headers("token", SPUtils.getInstance().getString(Constant.USER_TOKEN))
                .upJson(RequestHelper.getChargeBody("15810476449", value))
                .execute(new NetCallBack<BaseResponse<String>>() {
                    @Override
                    public void onStart(Request<BaseResponse<String>, ? extends Request> request) {
                        super.onStart(request);
                        showLoading();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dismissLoading();
                    }

                    @Override
                    public void onSuccess(Response<BaseResponse<String>> response) {
                        LogUtils.e("code--->" + response.body().getCode() + "\n" +
                                "msg--->" + response.body().getMsg() + "\n" +
                                "data--->" + response.body().getData());
                    }
                });
    }

    /**
     * 退出登录
     */
    private void logout() {
        HttpRequest.logout()
                .compose(RxThreadHelper.<BaseResponse<String>>onNetWork())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showLoading();
                    }
                })
                .subscribe(new Observer<BaseResponse<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseResponse<String> stringBaseResponse) {
                        // 登出成功
                        if (stringBaseResponse.getCode() == 1) {
                            // 清除登录状态并跳转到登录界面
                            LotteryApp.getInstance().setLogin(false);
                            Intent mIntent = new Intent(getActivity(), LoginActivity.class);
                            mIntent.putExtra(Constant.IS_LOGOUT, true);
                            ActivityUtils.startActivity(mIntent);
                            ActivityUtils.finishActivity(getActivity());
                        }
                        // 登出失败
                        else {
                            ToastUtils.showShort(stringBaseResponse.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissLoading();
                    }

                    @Override
                    public void onComplete() {
                        dismissLoading();
                    }
                });
    }

    @Override
    public void onDataRefresh() {

    }
}
