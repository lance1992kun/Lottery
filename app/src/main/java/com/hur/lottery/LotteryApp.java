package com.hur.lottery;

import com.blankj.utilcode.util.SPUtils;
import com.hur.lottery.base.BaseApplication;
import com.hur.lottery.entity.Constant;
import com.hur.lottery.push.CustomNotificationHandler;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

import org.android.agoo.huawei.HuaWeiRegister;

/**
 * <pre>
 *   @author  : syk
 *   e-mail  : shenyukun1024@gmail.com
 *   time    : 2018/04/23 14:18
 *   desc    : 自定义Application
 *   version : 1.0
 * </pre>
 */
public class LotteryApp extends BaseApplication {
    /**
     * App实例
     */
    private static LotteryApp sInstance;
    /**
     * 是否登录
     */
    private boolean isLogin = false;

    /**
     * 获取App实例
     *
     * @return App实例
     */
    public static LotteryApp getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化App
        sInstance = this;
        // 初始化友盟
        initPush();
    }

    /**
     * 初始化推送
     */
    private void initPush() {
        // 初始化common库
        // 参数1:上下文，不能为空
        // 参数2:设备类型，UMConfigure.DEVICE_TYPE_PHONE为手机
        // UMConfigure.DEVICE_TYPE_BOX为盒子，默认为手机
        // 参数3:Push推送业务的secret
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, Constant.PUSH_SECRET);
        PushAgent mPushAgent = PushAgent.getInstance(this);
        // 自定义动作
        CustomNotificationHandler notificationClickHandler = new CustomNotificationHandler();
        // 注册自定义动作
        mPushAgent.setNotificationClickHandler(notificationClickHandler);
        // 注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                // 注册成功会返回device token,保存在本地
                SPUtils.getInstance().put(Constant.DEVICE_TOKEN, deviceToken);
                // 初始化之后关闭推送，等待登录成功后再次开启
                // closePush();
            }

            @Override
            public void onFailure(String s, String s1) {
                // 注册失败的话清空device token,后续程序会进行判断
                SPUtils.getInstance().put(Constant.DEVICE_TOKEN, "");
            }
        });
        // 注册华为推送
        HuaWeiRegister.register(this);
    }

    /**
     * 用户是否登录
     *
     * @return 是否登录
     */
    public boolean isLogin() {
        return isLogin;
    }

    /**
     * 设置用户登录状态
     *
     * @param login 是否登录
     */
    public void setLogin(boolean login) {
        isLogin = login;
    }
}
