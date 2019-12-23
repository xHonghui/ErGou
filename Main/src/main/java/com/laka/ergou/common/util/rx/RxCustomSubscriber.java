package com.laka.ergou.common.util.rx;

import com.google.gson.JsonSyntaxException;
import com.laka.androidlib.BuildConfig;
import com.laka.androidlib.mvp.IBaseLoadingView;
import com.laka.androidlib.mvp.IBaseView;
import com.laka.androidlib.util.ApplicationUtils;
import com.laka.androidlib.util.LogUtils;
import com.laka.androidlib.util.rx.callback.ResponseCallBack;
import com.laka.androidlib.util.rx.constant.RequestCommonCode;
import com.laka.androidlib.util.rx.exception.ApiException;
import com.laka.androidlib.util.rx.exception.BaseException;
import com.laka.ergou.mvp.login.LoginModuleNavigator;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @Author:summer
 * @Date:2019/1/17
 * @Description:针对自己服务器数据结构做的处理模型,使用CustomApi构建retrofix时使用
 */

public abstract class RxCustomSubscriber<T, D extends IBaseView> implements Observer<T> {

    protected Disposable disposable;
    protected D view;
    private ResponseCallBack<T> mCallBack;
    /**
     * 请求接口时，接口返回错误码是 4 或者 5，是否在这里直接拦截并且统一跳转登录页面
     */
    private boolean mIsToLogin = true;

    public RxCustomSubscriber(D view, ResponseCallBack<T> callBack) {
        this.view = view;
        mCallBack = callBack;
    }

    public RxCustomSubscriber(D view, ResponseCallBack<T> callBack, boolean isToLogin) {
        this.view = view;
        this.mCallBack = callBack;
        this.mIsToLogin = isToLogin;
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.disposable = d;
    }


    /**
     * 请求成功，code=0
     */
    @Override
    public void onNext(T t) {
        handleLoadingUI();
        if (mCallBack != null) {
            mCallBack.onSuccess(t);
        }
    }

    @Override
    public void onError(Throwable e) {
        handleLoadingUI();
        if (e.getMessage() != null) {
            view.showErrorMsg(e.getMessage());
        }
        if (e instanceof BaseException) {
            mCallBack.onFail((BaseException) e);
            BaseException exception = (BaseException) e;
            if (exception.getCode() == RequestCommonCode.LK_WRONG_USER_TOKEN
                    || exception.getCode() == RequestCommonCode.LK_NOT_LOGIN) {
                if (mIsToLogin) {
                    // 强制跳转到用户登陆页面
                    LoginModuleNavigator.INSTANCE.startLoginActivity(ApplicationUtils.getApplication());
                } else {
                    if (exception.getCode() == RequestCommonCode.LK_WRONG_USER_TOKEN) {
                        mCallBack.onFail(new ApiException(RequestCommonCode.LK_WRONG_USER_TOKEN, RequestCommonCode.USER_TOKEN_INVALID_MSG));
                    } else {
                        mCallBack.onFail(new ApiException(RequestCommonCode.LK_NOT_LOGIN, RequestCommonCode.USER_NOT_LOGIN_MSG));
                    }
                }
            }
        } else if (e instanceof JsonSyntaxException) {  // 需要针对性处理的异常
            if (mCallBack != null) {
                LogUtils.error("数据解析异常");
                e.printStackTrace();
                mCallBack.onFail(new ApiException(RequestCommonCode.LK_ERROR_JSON_FORMAT, RequestCommonCode.DATA_FORMAT_EXCEPTION_MSG));
            }
        } else { // 异常错误
            if (BuildConfig.DEBUG) {
                if (mCallBack != null) {
                    String str = e.getMessage();
                    mCallBack.onFail(new ApiException(RequestCommonCode.LK_ERROR_OTHER, e.getMessage()));
                }
            } else {
                if (mCallBack != null) {
                    mCallBack.onFail(new ApiException(RequestCommonCode.LK_ERROR_OTHER, RequestCommonCode.REQUEST_FAIL));
                }
            }
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
