package com.hur.lottery.base;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.CrashUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.Utils;
import com.hur.lottery.entity.Constant;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;

import java.util.logging.Level;

import okhttp3.OkHttpClient;

import static com.hur.lottery.entity.Constant.DEBUG;

/**
 * <pre>
 *   author  : syk
 *   e-mail  : shenyukun1024@gmail.com
 *   time    : 2018/03/27 14:30
 *   desc    : 基类App
 *   version : 1.0
 * </pre>
 */
public class BaseApplication extends Application {

    private static BaseApplication sInstance;

    public static BaseApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        // 初始化工具
        initUtil();
        // 初始化网络
        initNet();
    }

    /**
     * 初始化工具类
     */
    @SuppressLint("MissingPermission")
    private void initUtil() {
        // 初始化工具类
        Utils.init(this);
        // 打印信息
        LogUtils.getConfig()
                // 设置log总开关，包括输出到控制台和文件，默认开
                .setLogSwitch(DEBUG)
                // 设置是否输出到控制台开关，默认开
                .setConsoleSwitch(DEBUG)
                // 设置log全局标签，默认为空
                // 当全局标签不为空时，我们输出的log全部为该tag，
                // 为空时，如果传入的tag为空那就显示类名，否则显示tag
                .setGlobalTag(null)
                // 设置log头信息开关，默认为开
                .setLogHeadSwitch(true)
                // 打印log时是否存到文件的开关，默认关
                .setLog2FileSwitch(false)
                // 当自定义路径为空时，写入应用的/cache/log/目录中
                .setDir("")
                // 当文件前缀为空时，默认为"util"，即写入文件为"util-MM-dd.txt"
                .setFilePrefix("")
                // 输出日志是否带边框开关，默认开
                .setBorderSwitch(true)
                // log的控制台过滤器，和logcat过滤器同理，默认Verbose
                .setConsoleFilter(LogUtils.V)
                // log文件过滤器，和logcat过滤器同理，默认Verbose
                .setFileFilter(LogUtils.V)
                // log栈深度，默认为1
                .setStackDeep(1);
        // 崩溃工具,权限检查
        if (PermissionUtils.isGranted(PermissionConstants.STORAGE)) {
            CrashUtils.init(Constant.CRASH_LOG_DIR, new CrashUtils.OnCrashListener() {
                @Override
                public void onCrash(String crashInfo, Throwable e) {
                    // 打印错误异常信息
                    LogUtils.e(crashInfo);
                    // 重启应用
                    restartApp();
                }
            });
        }
    }

    /**
     * 初始化网络
     */
    private void initNet() {
        // OkHttp配置
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // log相关
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        // log打印级别，决定了log显示的详细程度
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        // log颜色级别，决定了log在控制台显示的颜色
        loggingInterceptor.setColorLevel(Level.INFO);
        //添加OkGo默认debug日志
        builder.addInterceptor(loggingInterceptor);
        // OKGo的统一配置
        OkGo.getInstance().init(this)
                // 自定义的OkHttpClient
                .setOkHttpClient(builder.build())
                // 自动重试次数3
                .setRetryCount(3);
    }

    /**
     * 重启应用
     */
    private void restartApp() {
        Intent intent = new Intent();
        intent.setClassName("com.hur.lottery.ui.activity", "SplashActivity");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent restartIntent = PendingIntent.getActivity(this, 0, intent, 0);
        AlarmManager manager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        if (manager != null) {
            manager.set(AlarmManager.RTC, System.currentTimeMillis() + 1, restartIntent);
            ActivityUtils.finishAllActivities();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }
}
