package com.hur.lottery.net;

import com.blankj.utilcode.util.SPUtils;
import com.hur.lottery.entity.BaseResponse;
import com.hur.lottery.entity.Constant;
import com.hur.lottery.utils.RequestHelper;
import com.lzy.okgo.OkGo;
import com.lzy.okrx2.adapter.ObservableBody;

import io.reactivex.Observable;

/**
 * <pre>
 *   @author  : syk
 *   e-mail  : shenyukun1024@gmail.com
 *   time    : 2018/04/28 15:13
 *   desc    : 联网请求统一管理
 *   version : 1.0
 * </pre>
 */
public class HttpRequest {

    /**
     * 登录操作
     *
     * @param tel      电话
     * @param password 密码
     * @return 登录结果
     */
    public static Observable<BaseResponse<String>> login(String tel, String password) {
        return OkGo.<BaseResponse<String>>post(HttpUrl.USER_LOGIN)
                .upJson(RequestHelper.getLoginBody(tel, password))
                .converter(new JsonConvert<BaseResponse<String>>())
                .adapt(new ObservableBody<BaseResponse<String>>());
    }

    /**
     * 登出操作
     *
     * @return 登出结果
     */
    public static Observable<BaseResponse<String>> logout() {
        return OkGo.<BaseResponse<String>>post(HttpUrl.USER_LOGOUT)
                .headers("token", SPUtils.getInstance().getString(Constant.USER_TOKEN))
                .upJson(RequestHelper.getCodeBody(
                        SPUtils.getInstance().getString(Constant.USER_ACCOUNT)))
                .converter(new JsonConvert<BaseResponse<String>>())
                .adapt(new ObservableBody<BaseResponse<String>>());
    }

    /**
     * 获取手机验证码
     *
     * @param tel 手机号
     * @return 手机验证码
     */
    public static Observable<BaseResponse<String>> getCode(String tel) {
        return OkGo.<BaseResponse<String>>post(HttpUrl.GET_CODE)
                .upJson(RequestHelper.getCodeBody(tel))
                .converter(new JsonConvert<BaseResponse<String>>())
                .adapt(new ObservableBody<BaseResponse<String>>());
    }

    /**
     * 注册
     *
     * @param tel       手机号
     * @param password  密码
     * @param password2 确认密码
     * @param code      验证码
     * @param camilo    邀请码
     * @return 注册结果
     */
    public static Observable<BaseResponse<String>> register(
            String tel, String password, String password2, String code, String camilo) {
        return OkGo.<BaseResponse<String>>post(HttpUrl.USER_REGISTER)
                .upJson(RequestHelper.getRegisterBody(tel, password, password2, code, camilo))
                .converter(new JsonConvert<BaseResponse<String>>())
                .adapt(new ObservableBody<BaseResponse<String>>());
    }

    /**
     * 获取消息
     *
     * @param type 类型1:实时消息</br>2:历史消息
     * @return 消息
     */
    public static Observable<BaseResponse<String>> getOrderData(int type) {
        return OkGo.<BaseResponse<String>>post(HttpUrl.GET_ORDER_DATA)
                .headers("token", SPUtils.getInstance().getString(Constant.USER_TOKEN))
                .upJson(RequestHelper.getDataBody(type,
                        SPUtils.getInstance().getInt(Constant.LIMIT_COUNT, 13)))
                .converter(new JsonConvert<BaseResponse<String>>())
                .adapt(new ObservableBody<BaseResponse<String>>());
    }

    /**
     * 获取超大极限
     *
     * @return 超大极限
     */
    public static Observable<BaseResponse<String>> getOrderLimit() {
        return OkGo.<BaseResponse<String>>post(HttpUrl.GET_ORDER_LIMIT)
                .headers("token", SPUtils.getInstance().getString(Constant.USER_TOKEN))
                .upJson(RequestHelper.getLimitBody(
                        SPUtils.getInstance().getInt(Constant.BIG_LIMIT_COUNT, 23)))
                .converter(new JsonConvert<BaseResponse<String>>())
                .adapt(new ObservableBody<BaseResponse<String>>());
    }
}
