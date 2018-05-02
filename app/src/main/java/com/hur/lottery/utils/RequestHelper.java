package com.hur.lottery.utils;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.hur.lottery.entity.BaseRequestBean;
import com.hur.lottery.entity.Constant;

/**
 * <pre>
 *   author  : syk
 *   e-mail  : shenyukun1024@gmail.com
 *   time    : 2018/04/16 15:35
 *   desc    : 上传Json帮助类
 *   version : 1.0
 * </pre>
 */
public class RequestHelper {
    /**
     * 获取登录上传Json
     *
     * @param tel      手机号
     * @param password 密码
     * @return 上传Json
     */
    public static String getLoginBody(String tel, String password) {
        // 初始化要查询的请求体
        BaseRequestBean requestBean = new BaseRequestBean();
        requestBean.setTel(tel);
        requestBean.setPassword(EncryptUtils.encryptMD5ToString(password));
        requestBean.setClientid(SPUtils.getInstance().getString(Constant.DEVICE_TOKEN));
        // 通过Gson转为Json格式的字符串
        return new Gson().toJson(requestBean);
    }

    /**
     * 获取验证码上传Json
     *
     * @param tel 手机号
     * @return 上传Json
     */
    public static String getCodeBody(String tel) {
        // 初始化要查询的请求体
        BaseRequestBean requestBean = new BaseRequestBean();
        requestBean.setTel(tel);
        // 通过Gson转为Json格式的字符串
        return new Gson().toJson(requestBean);
    }

    /**
     * 获取注册上传Json
     *
     * @param tel       手机号
     * @param password  密码
     * @param password2 确认密码
     * @param code      验证码
     * @param camilo    邀请码
     * @return 上传Json
     */
    public static String getRegisterBody(String tel, String password, String password2,
                                              String code, String camilo) {
        // 初始化要查询的请求体
        BaseRequestBean requestBean = new BaseRequestBean();
        requestBean.setTel(tel);
        requestBean.setPassword(EncryptUtils.encryptMD5ToString(password));
        requestBean.setPassword2(EncryptUtils.encryptMD5ToString(password2));
        requestBean.setCode(code);
        requestBean.setClientid(SPUtils.getInstance().getString(Constant.DEVICE_TOKEN));
        requestBean.setCamilo(camilo);
        // 通过Gson转为Json格式的字符串
        return new Gson().toJson(requestBean);
    }

    /**
     * 获取数据上传Json
     *
     * @param type  类型1--->实时数据,2--->历史数据
     * @param limit 最大限量
     * @return 数据上传Json
     */
    public static String getDataBody(int type, int limit) {
        // 初始化要查询的请求体
        BaseRequestBean requestBean = new BaseRequestBean();
        requestBean.setType(type);
        requestBean.setLimit(limit);
        // 通过Gson转为Json格式的字符串
        return new Gson().toJson(requestBean);
    }

    /**
     * 获取超大极限上传Json
     *
     * @param limit 最大限量
     * @return 超大极限Json
     */
    public static String getLimitBody(int limit) {
        // 初始化要查询的请求体
        BaseRequestBean requestBean = new BaseRequestBean();
        requestBean.setLimit(limit);
        // 通过Gson转为Json格式的字符串
        return new Gson().toJson(requestBean);
    }

    /**
     * 获取充值上传Json
     *
     * @param tel    手机号
     * @param camilo 邀请码
     * @return 充值上传Json
     */
    public static String getChargeBody(String tel, String camilo) {
        // 初始化要查询的请求体
        BaseRequestBean requestBean = new BaseRequestBean();
        requestBean.setTel(tel);
        requestBean.setCamilo(camilo);
        // 通过Gson转为Json格式的字符串
        return new Gson().toJson(requestBean);
    }
}
