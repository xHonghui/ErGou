package com.laka.androidlib.util.rx;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * @Author:Rayman
 * @Date:2018/6/6
 * @Description:RxJava线程切换Composer
 */

public final class RxSchedulerComposer {

    /**
     * 默认IO加载与解绑
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> normalSchedulersTransformer() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource apply(@NonNull Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
