package com.laka.androidlib.util.rx;

import android.util.Log;

import com.laka.androidlib.util.LogUtils;
import com.laka.androidlib.util.rx.exception.ApiException;
import com.laka.androidlib.net.response.BaseResponse;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @Author:Rayman
 * @Date:2018/12/15
 * @Description:RxJava网络请求类Composer
 */

public class RxResponseComposer {

    private static final String TAG = "RxResponseComposer";

    private RxResponseComposer() {
        Log.i(TAG, "RxResponseComposer init");
    }

    public static <T> ObservableTransformer<BaseResponse<T>, T> flatResponse() {
        Log.i(TAG, "flatResponse");
        return new ObservableTransformer<BaseResponse<T>, T>() {
            @Override
            public ObservableSource<T> apply(Observable<BaseResponse<T>> upstream) {
                Log.i(TAG, "flatMap");
                return (ObservableSource<T>) upstream.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .flatMap(new Function<BaseResponse<T>, ObservableSource<?>>() {
                            @Override
                            public ObservableSource<?> apply(final BaseResponse<T> tBaseResponse) throws Exception {
                                if (tBaseResponse.isSuccess()) {
                                    LogUtils.info(tBaseResponse.toString());
                                    return createData(tBaseResponse.getData());
                                } else {
                                    LogUtils.error(TAG, "api error code：" + tBaseResponse.getCode() + "\nerror msg：" + tBaseResponse.getMsg());
                                    int code = tBaseResponse.getCode();
                                    String msg = tBaseResponse.getMsg();
                                    return createError(code, msg);
                                }
                            }
                        });
            }
        };
    }

    private static <T> ObservableSource<?> createData(final T data) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> e) throws Exception {
                e.onNext(data);
                e.onComplete();
            }
        });
    }

    private static ObservableSource createError(final int code, final String msg) {
        return Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter emitter) throws Exception {
                emitter.onError(new ApiException(code, msg));
                emitter.onComplete();
            }
        });
    }
}
