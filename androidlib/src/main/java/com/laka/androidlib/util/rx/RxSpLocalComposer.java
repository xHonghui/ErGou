package com.laka.androidlib.util.rx;

import com.laka.androidlib.util.SPHelper;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @Author:Rayman
 * @Date:2019/1/29
 * @Description:针对SP数据存储composer
 */

public class RxSpLocalComposer {

    /**
     * 加载数据到Sp
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> compose(final String fileName, final String keyName) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource apply(@NonNull Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .flatMap(new Function<T, ObservableSource<?>>() {
                            @Override
                            public ObservableSource<?> apply(T t) throws Exception {
                                // 先读取Sp的数据，有Sp的数据就优先显示
                                T localObject = (T) SPHelper.getObject(fileName, keyName, t.getClass());
                                // 保存新的数据到Sp。这样就保证每次更新都有新的数据写入了。
                                SPHelper.saveObject(fileName, keyName, t);
                                if (localObject != null) {
                                    return createData(localObject);
                                } else {
                                    return createData(t);
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
}
