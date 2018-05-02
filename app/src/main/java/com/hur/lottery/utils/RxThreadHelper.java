package com.hur.lottery.utils;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * <pre>
 *   author  : syk
 *   e-mail  : shenyukun1024@gmail.com
 *   time    : 2018/04/16 15:30
 *   desc    : 线程辅助类
 *   version : 1.0
 * </pre>
 */
public class RxThreadHelper {
    /**
     * 网络线程切换
     *
     * @param <T>
     * @return 观察者对象
     */
    public static <T> ObservableTransformer<T, T> onNetWork() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<T> netObservable) {
                return netObservable
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
