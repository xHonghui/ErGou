package com.laka.ergou.common.util.rx;

import com.google.gson.JsonSyntaxException;
import com.laka.androidlib.mvp.IBaseListView;
import com.laka.androidlib.mvp.IBaseView;
import com.laka.androidlib.util.ApplicationUtils;
import com.laka.androidlib.util.LogUtils;
import com.laka.androidlib.util.rx.BaseRxSubscriber;
import com.laka.androidlib.util.rx.exception.ApiException;
import com.laka.androidlib.util.rx.exception.NetWorkException;
import com.laka.ergou.mvp.login.LoginModuleNavigator;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * @Author:Rayman
 * @Date:2019/1/18
 * @Description:当前项目RxJavaSubscriber
 */

public class RxSubscriber<T, D extends IBaseView> extends BaseRxSubscriber<T, D> {

    public RxSubscriber(D view) {
        super(view);
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        // 统一处理错误
        LogUtils.info("输出e的错误信息：" + e.getMessage());
        handleError(e);
    }

    /**
     * 统一处理错误，例如Token错误跳转等
     */
    private void handleError(Throwable throwable) {
        // 统一处理Api错误
        if (throwable instanceof ApiException) {
            ApiException apiException = (ApiException) throwable;
            switch (apiException.getCode()) {
                case ApiException.USER_NOT_LOGIN:
                case ApiException.TOKEN_ERROR:
                    // 强制跳转到用户登陆页面
                    LoginModuleNavigator.INSTANCE.startLoginActivity(ApplicationUtils.getApplication());
                    break;
                default:
                    break;
            }
            // 假若实现的View是IBaseListView--View层接口，显示EmptyView
            if (view instanceof IBaseListView) {
                ((IBaseListView) view).showGetDataErrorView();
            }
        } else if (throwable instanceof UnknownHostException ||
                throwable instanceof ConnectException ||
                throwable instanceof SocketTimeoutException ||
                throwable instanceof SocketException ||
                throwable instanceof NetWorkException) {
            // 假若抛出的是网路问题，同时当前View是继承了IBaseListView--View层接口，那就默认显示网路错误
            if (view instanceof IBaseListView) {
                ((IBaseListView) view).showGetDataNetWorkErrorView();
            }
        } else if (throwable instanceof NumberFormatException ||
                throwable instanceof IllegalArgumentException ||
                throwable instanceof JsonSyntaxException) {
            // 假若抛出的是JSON等异常，同时当前View是继承了IBaseListView--View层接口，那就默认显示数据错误
            if (view instanceof IBaseListView) {
                ((IBaseListView) view).showGetDataErrorView();
            }
        }
    }
}
