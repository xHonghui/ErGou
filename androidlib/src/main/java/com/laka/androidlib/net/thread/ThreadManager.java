package com.laka.androidlib.net.thread;


import android.os.Looper;

import com.laka.androidlib.util.LogUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @Author Lyf
 * @CreateTime 2018/2/6
 * @Description
 **/
public class ThreadManager {

    private final static String TAG = ThreadManager.class.getSimpleName();

    /**
     * This method is used to do something on two separately different threads(threads are called one after the other) in an async way.
     * For instance, you can do some heavy cpu operations on subThread and then updates your views on UI thread.
     *
     * @param subscribeListener do something in subThread(non-ui thread)
     * @param observerListener  do something in mainThread(ui thread)
     * @param <T>               after do something in subThread,
     *                          you may want to pass a T(bean) object as a result to ui thread to update views.
     */
    public static <T> void execute(final SubscribeListener<T> subscribeListener,
                                   final ObserverListener<T> observerListener) {

        Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> emitter) throws Exception {

                T value = subscribeListener.runOnSubThread();

                if( value != null){
                    emitter.onNext(value);
                }

                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<T>() {
                    @Override
                    public void accept(T t) throws Exception {
                        observerListener.runOnUiThread(t);
                    }
                });

    }

    public static void runOnUiThread(Runnable runnable){
        runOnUiThread(runnable,0);
    }

    /**
     * Run on Ui Thread.
     */
    public static void runOnUiThread(Runnable runnable, long delay) {

        Observable.just(runnable)
                .delay(delay, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Runnable>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Runnable runnable) {
                        //LogUtils.debug(TAG, "runOnUiThread-onNext=" + runnable);
                        runnable.run();
                    }

                    @Override
                    public void onError(Throwable e) {
                        //LogUtils.debug(TAG, "runOnUiThread-onError=" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        //LogUtils.debug(TAG, "runOnUiThread-onComplete");
                    }
                });
    }

    /**
     * 判断是否在UI线程
     *
     * @return 判断是否在UI线程
     */
    public static boolean isUIThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

}
