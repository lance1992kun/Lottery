package com.hur.lottery.push;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hur.lottery.LotteryApp;
import com.hur.lottery.entity.Constant;
import com.hur.lottery.ui.activity.LoginActivity;
import com.hur.lottery.ui.activity.MainActivity;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

/**
 * <pre>
 *   author  : syk
 *   e-mail  : shenyukun1024@gmail.com
 *   time    : 2018/04/01 11:20
 *   desc    : 自定义动作
 *   version : 1.0
 * </pre>
 */

public class CustomNotificationHandler extends UmengNotificationClickHandler {
    @Override
    public void launchApp(Context context, UMessage msg) {
        // 写入需要传的值
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constant.IS_NEED_REFRESH, true);
        Intent mIntent = new Intent();
        // 如果已经登录进入MainActivity否则进入登录界面
        if (LotteryApp.getInstance().isLogin()) {
            mIntent.setClass(context, MainActivity.class);
        } else {
            mIntent.setClass(context, LoginActivity.class);
        }
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.putExtras(bundle);
        context.startActivity(mIntent);
    }
}
