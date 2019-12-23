package com.laka.androidlib.features.login;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.laka.androidlib.util.ApplicationUtils;
import com.laka.androidlib.util.PhoneUtils;


/**
 * @Author Lyf
 * @CreateTime 2018/2/1
 * @Description A LoginInterceptor class is a checking params interceptor.
 *  return true if the params are legal, return false if the params are illegal.
 **/
public class LoginInterceptor {

    private final static String PHONE_NUM_NULL = "请输入手机号码";
    private final static String PASS_WORD_NULL = "请输入密码";
    private final static String VERIFY_CODE_NULL = "请输入验证码";

    private final static String PHONE_NUM_ERROR = "手机号码错误，请重新输入";

    public boolean checkLoginWithPassWord( @NonNull String phoneNum, @NonNull String passWord) {
        return checkPhoneNum(phoneNum) && checkPhonePassWord(passWord);
    }

    public boolean checkLoginWithVerifyCode(@NonNull String phoneNum, @NonNull String verificationCode) {
        return checkPhoneNum(phoneNum) && checkVerifyCode(verificationCode);
    }

    /**
     * Checking passWord whether is legal.
     *
     * @return true legal，false illegal
     */
    public boolean checkPhoneNum(@NonNull String phoneNum) {

        if (phoneNum == null || phoneNum.length() == 0) {
            showToast(PHONE_NUM_NULL);
            return false;
        }
        if (!PhoneUtils.isPhoneNum(phoneNum)) {
            showToast(PHONE_NUM_ERROR);
            return false;
        }
        return true;
    }

    /**
     * Checking passWord whether is legal.
     *
     * @return true legal，false illegal
     */
    public boolean checkPhonePassWord(@NonNull String passWord) {

        if (passWord == null || passWord.length() == 0) {
            showToast(PASS_WORD_NULL);
            return false;
        }

        return true;
    }

    /**
     * Checking verificationCode whether is legal.
     *
     * @return true legal，false illegal
     */
    public boolean checkVerifyCode(@NonNull String verificationCode) {

        if (verificationCode == null || verificationCode.length() == 0) {
            showToast(VERIFY_CODE_NULL);
            return false;
        }

        return true;
    }

    private void showToast(String text) {
        Toast.makeText(ApplicationUtils.getApplication(), text, Toast.LENGTH_SHORT).show();
    }


}
