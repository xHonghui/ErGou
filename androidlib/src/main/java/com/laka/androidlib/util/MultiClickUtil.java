package com.laka.androidlib.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author:Rayman
 * @Date:2018/6/28
 * @Description:防抖工具类
 */

public class MultiClickUtil {

    protected static final int CLICK_INTERVAL = 500;
    private static long firClickTime = 0;
    private static Map<Integer, Long> clickTimeMap = new HashMap<>();

    /**
     * 针对Id进行判断
     *
     * @param viewId
     * @return
     */
    public static boolean checkClickValid(int viewId) {
        boolean isValid;
        firClickTime = clickTimeMap.get(viewId) == null ? firClickTime : clickTimeMap.get(viewId);
        if (System.currentTimeMillis() - firClickTime > CLICK_INTERVAL) {
            firClickTime = System.currentTimeMillis();
            isValid = true;
        } else {
            firClickTime = System.currentTimeMillis();
            isValid = false;
        }
        clickTimeMap.put(viewId, firClickTime);
        return isValid;
    }

    public static boolean checkClickValid() {
        boolean isValid;
        if (System.currentTimeMillis() - firClickTime > CLICK_INTERVAL) {
            firClickTime = System.currentTimeMillis();
            isValid = true;
        } else {
            firClickTime = System.currentTimeMillis();
            isValid = false;
        }
        return isValid;
    }
}
