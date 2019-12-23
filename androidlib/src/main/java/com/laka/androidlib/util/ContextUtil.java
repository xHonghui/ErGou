package com.laka.androidlib.util;

import android.app.Activity;
import android.content.Context;

/**
 * @Author:Rayman
 * @Date:2018/7/10
 * @Description:监测Context有效性
 */

public class ContextUtil {

    private ContextUtil() {
    }

    /**
     * 判断当前Context的有效性（仅针对Activity类型Context的生命周期正常判断，其他Context是否为空的判断）
     *
     * @param context
     * @return
     */
    public final static boolean isValidContext(final Context context) {
        if (context == null) {
            return false;
        }
        if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            if (activity.isDestroyed() || activity.isFinishing()) {
                return false;
            }
        }
        return true;
    }
}
