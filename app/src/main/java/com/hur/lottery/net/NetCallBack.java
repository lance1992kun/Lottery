package com.hur.lottery.net;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.hur.lottery.entity.BaseResponse;
import com.lzy.okgo.callback.AbsCallback;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * <pre>
 *   @author  : syk
 *   e-mail  : shenyukun1024@gmail.com
 *   time    : 2018/04/26 17:59
 *   desc    : 自定义网络回调
 *   version : 1.0
 * </pre>
 */
public abstract class NetCallBack<T> extends AbsCallback<T> {

    @Override
    public T convertResponse(Response response) {
        ResponseBody responseBody = response.body();
        if (responseBody == null) {
            return null;
        }
        T data;
        Gson gson = new Gson();
        JsonReader jsonReader = new JsonReader(responseBody.charStream());
        data = gson.fromJson(jsonReader, BaseResponse.class);
        return data;
    }
}
