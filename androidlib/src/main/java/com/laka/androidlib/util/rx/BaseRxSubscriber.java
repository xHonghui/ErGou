package com.laka.androidlib.util.rx;

import com.laka.androidlib.mvp.IBaseLoadingView;
import com.laka.androidlib.mvp.IBaseView;
import com.laka.androidlib.net.response.BaseResponse;
import com.laka.androidlib.util.LogUtils;
import com.laka.androidlib.util.rx.constant.RequestCommonCode;
import com.laka.androidlib.util.rx.exception.ApiException;
import com.laka.androidlib.util.rx.exception.HttpExceptionHandler;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @Author:Rayman
 * @Date:2018/12/18
 * @Description:统一处理RxJava流事件 .
 * T：代表当前Model返回的数据类型
 * D：代表当前需要操作的View
 */

public abstract class BaseRxSubscriber<T, D extends IBaseView> implements Observer<T> {

    protected Disposable disposable;
    protected D view;

    public BaseRxSubscriber(D view) {
        this.view = view;
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.disposable = d;
    }

    @Override
    public void onNext(T t) {
        handleLoadingUI();
        // 提前拦截处理公共code
        if (t instanceof BaseResponse) {
            BaseResponse response = (BaseResponse) t;
            if (response.getCode() == RequestCommonCode.LK_NOT_LOGIN
                    || response.getCode() == RequestCommonCode.LK_WRONG_USER_TOKEN) {
                LogUtils.error(response.getCode() + ":" + response.getMsg());
            } else if (response.getCode() == RequestCommonCode.LK_ERROR_REDIS_CONNECT_FATLED) {

            }
        }
    }

    @Override
    public void onError(Throwable e) {
        // 现在暂时只对APIException做处理，假若需要扩展的话，需要定义一个统一错误处理类
        e = HttpExceptionHandler.unifiedError(e);
        handleLoadingUI();
        if (view != null) {
            view.showErrorMsg(e.getMessage());
        }
    }

    @Override
    public void onComplete() {
        handleLoadingUI();
    }

    private void handleLoadingUI() {
        if (view == null) {
            return;
        }
        if (view instanceof IBaseLoadingView) {
            IBaseLoadingView loadingView = (IBaseLoadingView) view;
            loadingView.dismissLoading();
        }
    }
}
