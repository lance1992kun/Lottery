package com.hur.lottery.net;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.hur.lottery.entity.BaseResponse;
import com.lzy.okgo.convert.Converter;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * <pre>
 *   @author  : syk
 *   e-mail  : shenyukun1024@gmail.com
 *   time    : 2018/04/28 15:31
 *   desc    : Json转换器
 *   version : 1.0
 * </pre>
 */
public class JsonConvert<T> implements Converter<T> {

    JsonConvert() {
    }

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
