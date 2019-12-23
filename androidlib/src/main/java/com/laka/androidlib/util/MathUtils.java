package com.laka.androidlib.util;

/**
 * @ClassName:
 * @Description:
 * @Author: chuan
 * @Date: 07/03/2018
 */

public class MathUtils {
    private MathUtils() {
        throw new UnsupportedOperationException("do not instantiate me , please.");
    }

    public static int maxNum(int[] num) {
        if (num == null || num.length == 0) {
            return 0;
        }

        if (num.length == 1) {
            return num[0];
        }

        int max = num[0];

        for (int i : num) {
            if (i > max) {
                max = i;
            }
        }

        return max;
    }

    public static int minNum(int[] num) {
        if (num == null || num.length == 0) {
            return 0;
        }

        if (num.length == 1) {
            return num[0];
        }

        int min = num[0];

        for (int i : num) {
            if (i < min) {
                min = i;
            }
        }

        return min;
    }
}
