package com.hur.lottery.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.hur.lottery.LotteryApp;
import com.hur.lottery.entity.Constant;
import com.umeng.message.UmengNotifyClickActivity;

/**
 * <pre>
 *   @author  : syk
 *   e-mail  : shenyukun1024@gmail.com
 *   time    : 2018/05/14 10:10
 *   desc    : 小米、华为、魅族三方推送界面
 *   version : 1.0
 * </pre>
 */
public class ThirdPushActivity extends UmengNotifyClickActivity {

//    private TextView mShowText;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        // setContentView(R.layout.activity_third);
//        mShowText = (TextView) findViewById(R.id.mShowText);
    }

    @Override
    public void onMessage(Intent intent) {
        super.onMessage(intent);
//        final String body = intent.getStringExtra(AgooConstants.MESSAGE_BODY);
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                mShowText.setText(body);
//            }
//        });
        // 写入需要传的值
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constant.IS_NEED_REFRESH, true);
        Intent mIntent = new Intent();
        // 如果已经登录进入MainActivity否则进入登录界面
        if (LotteryApp.getInstance().isLogin()) {
            mIntent.setClass(this, MainActivity.class);
        } else {
            mIntent.setClass(this, LoginActivity.class);
        }
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
    }
}
