package com.laka.androidlib.util.rx;

import com.google.gson.JsonSyntaxException;
import com.laka.androidlib.mvp.IBaseLoadingView;
import com.laka.androidlib.mvp.IBaseView;
import com.laka.androidlib.util.rx.callback.ResponseCallBack;
import com.laka.androidlib.util.rx.constant.RequestCommonCode;
import com.laka.androidlib.util.rx.exception.ApiException;
import com.laka.androidlib.util.rx.exception.BaseException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @Author:summer
 * @Date:2019/1/17
 * @Description:针对淘宝客数据结构进行处理，使用taobaokeApi构建retrofix时使用
 */

public abstract class RxTaoBaoKeSubscriber<T, D extends IBaseView> implements Observer<T> {

    protected Disposable disposable;
    protected D view;
    private ResponseCallBack<T> mCallBack;

    public RxTaoBaoKeSubscriber(D view, ResponseCallBack<T> callBack) {
        this.view = view;
        mCallBack = callBack;
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.disposable = d;
    }


    /**
     * 解析数据成功（请求成功/失败）
     */
    @Override
    public void onNext(T t) {
        handleLoadingUI();
        if (mCallBack == null) {
            return;
        }
        mCallBack.onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
        handleLoadingUI();
        if (mCallBack != null) {
            // 请求失败
            mCallBack.onFail(new ApiException(RequestCommonCode.LK_ERROR_OTHER, e.getMessage()));
        }
    }

    @Override
    public void onComplete() {

    }

    private void handleLoadingUI() {
        if (view instanceof IBaseLoadingView) {
            IBaseLoadingView loadingView = (IBaseLoadingView) view;
            loadingView.dismissLoading();
        }
    }
}
