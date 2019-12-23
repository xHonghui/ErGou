package com.laka.androidlib.features.login;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;

/**
 * @Author Lyf
 * @CreateTime 2018/2/5
 * @Description A AbstractLoginPresenter deals with certain tasks.
 * For instance, initializing an interceptor to checking login params.
 **/
public abstract class AbstractLoginPresenter implements LoginBasicContact.Presenter {


    private LoginBasicContact.View mLoginView;
    private ArrayMap<String, Object> mLoginParams = new ArrayMap<>();
    private LoginInterceptor mLoginInterceptor = new LoginInterceptor();

    public AbstractLoginPresenter(LoginBasicContact.View mLoginView) {
        this.mLoginView = mLoginView;
    }


    public abstract void onLoginWithPassWord(@NonNull ArrayMap<String, Object> mLoginParams);

    public abstract void onLoginWithPlatforms(@NonNull ArrayMap<String, Object> mLoginParams);

    public abstract void onLoginWithVerifyCode(@NonNull ArrayMap<String, Object> mLoginParams);

    // get code for logging
    public abstract void getVerifyCode(@NonNull ArrayMap<String, Object> mLoginParams);

    public abstract void logout();

    @Override
    public void getVerifyCode(@NonNull String phoneNumKey, @NonNull String phoneNumValue) {

        if (mLoginInterceptor.checkPhoneNum(phoneNumValue)) {
            mLoginParams.clear();
            mLoginParams.put(phoneNumKey, phoneNumValue);
            getVerifyCode(mLoginParams);
        }
    }

    @Override
    public void onLoginWithVerifyCode(@NonNull String phoneNumKey, @NonNull String phoneNumValue,
                                      @NonNull String verifyCodeKey, @NonNull String verifyCodeValue) {

        if (mLoginInterceptor.checkLoginWithVerifyCode(phoneNumValue, verifyCodeValue)) {

            mLoginParams.clear();
            mLoginParams.put(phoneNumKey, phoneNumValue);
            mLoginParams.put(verifyCodeKey, verifyCodeValue);

            // Call an abstract method and delivery the next task to the subclass.
            onLoginWithVerifyCode(mLoginParams);
        }

    }

    @Override
    public void onLoginWithPassWord(@NonNull String phoneNumKey, @NonNull String phoneNumValue,
                                    @NonNull String passWordKey, @NonNull String passWordValue) {

        if (mLoginInterceptor.checkLoginWithPassWord(phoneNumValue, passWordValue)) {

            mLoginParams.clear();
            mLoginParams.put(phoneNumKey, phoneNumValue);
            mLoginParams.put(passWordKey, passWordValue);

            // Call an abstract method and delivery the next task to the subclass.
            onLoginWithPassWord(mLoginParams);
        }

    }

    @Override
    public <T> void onLoginWithPlatforms(@Nullable T loginInfo, boolean isSuccess) {

    }

    @Override
    public void onLoginWithUnKnowParams(String... params) {

    }

    @Override
    public LoginBasicContact.View getView() {
        return mLoginView;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }


}