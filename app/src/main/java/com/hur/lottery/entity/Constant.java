package com.hur.lottery.entity;

import android.os.Environment;

/**
 * <pre>
 *   author  : syk
 *   e-mail  : shenyukun1024@gmail.com
 *   time    : 2018/03/27 14:49
 *   desc    : 常量
 *   version : 1.0
 * </pre>
 */

public class Constant {
    /**
     * 是否为测试环境
     */
    public static final boolean DEBUG = true;
    /**
     * 是否要刷新界面
     */
    public static final String IS_NEED_REFRESH = "IS_NEED_REFRESH";
    /**
     * 推送服务唯一标识
     */
    public static final String DEVICE_TOKEN = "DEVICE_TOKEN";
    /**
     * 用户登录后的Token
     */
    public static final String USER_TOKEN = "USER_TOKEN";
    /**
     * 用户账号
     */
    public static final String USER_ACCOUNT = "USER_ACCOUNT";
    /**
     * 用户密码
     */
    public static final String USER_PASSWORD = "USER_PASSWORD";
    /**
     * 友盟Umeng Message Secret
     */
    public static final String PUSH_SECRET = "b3748b59e93df2c87af599d4d7557c01";
    /**
     * 快速点击时间
     */
    public static final int FAST_CLICK = 200;
    /**
     * 下线广播
     */
    public static final String OFF_LINE_BROADCAST = "OFF_LINE_BROADCAST";
    /**
     * 极限通知
     */
    public static final String LIMIT_COUNT = "LIMIT_COUNT";
    /**
     * 超大极限
     */
    public static final String BIG_LIMIT_COUNT = "BIG_LIMIT_COUNT";
    /**
     * 崩溃日志保存目录
     */
    public static final String CRASH_LOG_DIR = Environment.getExternalStorageDirectory()
            .getAbsoluteFile() + "/DebugMessage/CrashLogs";
}
