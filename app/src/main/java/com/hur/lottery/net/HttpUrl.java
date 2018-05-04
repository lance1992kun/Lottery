package com.hur.lottery.net;

/**
 * <pre>
 *     author : syk
 *     e-mail : shenyukun1024@gmail.com
 *     time   : 2017/04/07
 *     desc   : 联网地址常量
 *     version: 1.0
 * </pre>
 */

public final class HttpUrl {
    /**
     * 服务器地址
     */
    private static final String BASE_URL = "http://47.92.166.89/lottery-calculate-app/";
    /**
     * 用户充值
     */
    public static final String USER_CHARGE = BASE_URL + "api/user/charge";
    /**
     * 获取实时数据/当日历史数据
     */
    public static final String GET_ORDER_DATA = BASE_URL + "api/order/data";
    /**
     * 获取超大极限数据
     */
    public static final String GET_ORDER_LIMIT = BASE_URL + "api/order/limit";
    /**
     * 获取短信验证码
     */
    public static final String GET_CODE = BASE_URL + "api/user/code";
    /**
     * 用户注册
     */
    public static final String USER_REGISTER = BASE_URL + "api/user/register";
    /**
     * 用户登录
     */
    public static final String USER_LOGIN = BASE_URL + "api/user/login";
    /**
     * 用户登出
     */
    public static final String USER_LOGOUT = BASE_URL + "api/user/logout";
}
