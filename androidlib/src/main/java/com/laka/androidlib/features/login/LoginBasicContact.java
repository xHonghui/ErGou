package com.laka.androidlib.features.login;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


/**
 * @Author Lyf
 * @CreateTime 2018/2/27
 * @Description A LoginContact defines two protocols for View and Presenter.
 *  Protocols define and limit what View and Presenter can do.
 **/
public class LoginBasicContact {

    public interface View {

        /**
         * Shows a dialog with a text tip.
         * @param text
         */
        void showLoading(@Nullable String text);

        /**
         * Hides a showing dialog.
         */
        void hideLoading();
    }

    public interface Presenter {

        View getView();

        /**
         * login with phoneNum and passWord.
         *
         * @param phoneNumKey   request name
         * @param phoneNumValue request value
         * @param passWordKey   request name
         * @param passWordValue request value
         */
        void onLoginWithPassWord(@NonNull String phoneNumKey, @NonNull String phoneNumValue,
                                 @NonNull String passWordKey, @NonNull String passWordValue);


        /**
         * login with phoneNum and verificationCode.
         */
        void onLoginWithVerifyCode(@NonNull String phoneNumKey,@NonNull String phoneNumValue, @NonNull String verifyCodeKey, @NonNull String verifyCodeValue);

        /**
         * login with loginInfo of other platforms.
         *
         * @param loginInfo loginInfo of other platforms.
         * @param <T>       A bean, saving loginInfo of other platforms.
         * @param isSuccess true if login success.
         */
        <T> void onLoginWithPlatforms(@Nullable T loginInfo, boolean isSuccess);


        /**
         * login with unKnow params for compatibility.
         * Note that : This method will not check the params whether is valid.
         * So that you have to check the params whether is valid by yourself.
         *
         * @param params unKnow params. These params will do nothing for checking.
         */
        void onLoginWithUnKnowParams(String... params);

        /*
        * LifeCycle of Activity.
        */
        void subscribe();

        void unSubscribe();

        void getVerifyCode(@NonNull String phoneNumKey, @NonNull String phoneNumValue);
    }

}
