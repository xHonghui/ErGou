package com.laka.androidlib.util.anim;

/**
 * @Author:Rayman
 * @Date:2019/2/25
 * @Description:
 */

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/**
 * @Author Lyf
 * @CreateTime 2018/4/27
 * @Description View动画辅助类
 **/
public class AnimationHelper {

    //向上平移进入动画
    public static TranslateAnimation getTranslateUpVisible(long durationMillis) {
        TranslateAnimation mShowAction1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 3.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction1.setDuration(durationMillis);
        return mShowAction1;
    }

    public static TranslateAnimation getTranslateUpHide(long durationMillis) {
        TranslateAnimation mShowAction1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -3.0f);
        mShowAction1.setDuration(durationMillis);
        return mShowAction1;
    }

    public static AlphaAnimation getAlphaAnimationShow(int durationMillis) {
        AlphaAnimation alphaShow = new AlphaAnimation(0, 1);
        alphaShow.setDuration(durationMillis);
        return alphaShow;
    }

    public static AlphaAnimation getAlphaAnimationShow(int durationMillis, boolean fillAfter) {
        AlphaAnimation alphaAnimation = getAlphaAnimationShow(durationMillis);
        alphaAnimation.setFillAfter(fillAfter);
        return alphaAnimation;
    }

    public static AlphaAnimation getAlphaAnimationHide(int durationMillis, boolean fillAfter) {
        AlphaAnimation alphaAnimation = getAlphaAnimationHide(durationMillis);
        alphaAnimation.setFillAfter(fillAfter);
        return alphaAnimation;
    }

    //淡出动画
    public static AlphaAnimation getAlphaAnimationHide(long durationMillis) {
        AlphaAnimation alphaHide = new AlphaAnimation(1, 0);
        alphaHide.setDuration(durationMillis);
        return alphaHide;
    }

}
