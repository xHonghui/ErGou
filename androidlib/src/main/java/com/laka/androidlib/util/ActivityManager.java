package com.laka.androidlib.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.ArrayMap;

import com.laka.androidlib.constant.AppConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Lyf
 * @CreateTime 2018/3/7
 * @Description Activity栈管理
 **/
public class ActivityManager {

    private List<Activity> mActivities = new ArrayList<>();

    private ArrayMap<String, Activity> activityArrayMap = new ArrayMap<>();

    private final static ActivityManager sActivityManager = new ActivityManager();

    private Intent intent;

    public static ActivityManager getInstance() {
        return sActivityManager;
    }

    private ActivityManager() {
        intent = new Intent();
    }

    /**
     * 添加一个Activity
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        mActivities.add(activity);
        activityArrayMap.put(activity.getClass().getSimpleName(), activity);
    }

    /**
     * 删除一个Activity
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {
        mActivities.remove(activity);
        activityArrayMap.remove(activity.getClass().getSimpleName());
    }

    /**
     * 完全退出
     */
    public void exitApp() {
        while (mActivities.size() > 0) {
            popActivity(mActivities.get(mActivities.size() - 1));
        }
    }

    /**
     * 根据class name获取activity
     *
     * @return 如果Activity不存在，会返回null。
     */
    @Nullable
    public Activity getActivityByClassName(String name) {
        if (TextUtils.isEmpty(name)) {
            return null;
        }
        return activityArrayMap.get(name);
    }

    /**
     * description:是否包含当前Activity
     *
     * @param activity Activity名
     **/
    public boolean isContainActivity(Activity activity) {
        if (activity == null) {
            return false;
        }
        return activityArrayMap.get(activity.getClass().getSimpleName()) != null;
    }

    /**
     * description:是否包含当前Activity
     *
     * @param activity Activity名
     **/
    public boolean isContainActivity(Class activity) {
        if (activity == null) {
            return false;
        }
        return activityArrayMap.get(activity.getSimpleName()) != null;
    }


    /**
     * 根据Class类型获取Activity
     *
     * @return 如果Activity不存在，会返回null。
     */
    @Nullable
    public Activity getActivityByClass(Class cs) {
        for (Activity ac : mActivities) {
            if (ac.getClass().equals(cs)) {
                return ac;
            }
        }
        return null;
    }


    /**
     * @param index
     * @return return false, 如果index，大于等于当前的Activity的个数，或者index小于0.
     */
    public boolean finishActivity(int index) {

        if (index >= mActivities.size() || index < 0) {
            return false;
        }

        int beforeRemoveCount = mActivities.size();
        Activity activity = mActivities.get(index);
        if (activity != null) {
            popActivity(activity);
        }
        // 删除后数量比删除前的小,就代表删除成功
        return mActivities.size() < beforeRemoveCount;
    }

    /**
     * 关掉指定的页面
     *
     * @param cs 类名.class
     * @return 如果Activity不存在，会返回null。
     */
    public boolean finishActivity(Class cs) {

        int beforeRemoveCount = mActivities.size();
        Activity activity = getActivityByClass(cs);
        if (activity != null) {
            popActivity(activity);
        }
        // 删除后数量比删除前的小,就代表删除成功
        return mActivities.size() < beforeRemoveCount;
    }

    /**
     * 弹出activity
     *
     * @param activity
     */
    private void popActivity(Activity activity) {
        removeActivity(activity);
        activity.finish();
    }


    /**
     * 弹出activity到
     *
     * @param cs
     */
    public void popUntilActivity(Class... cs) {

        List<Activity> list = new ArrayList<>();

        for (int i = mActivities.size() - 1; i >= 0; i--) {
            Activity ac = mActivities.get(i);
            boolean isTop = false;
            for (Class c : cs) {
                if (ac.getClass().equals(c)) {
                    isTop = true;
                    break;
                }
            }
            if (!isTop) {
                list.add(ac);
            } else break;
        }
        for (Activity activity : list) {
            popActivity(activity);
        }
    }

    /**
     * @return 返回当前的Activity
     */
    @Nullable
    public Activity getCurrentActivity() {

        if (isAppRunning()) {
            return mActivities.get(mActivities.size() - 1);
        } else {
            return null;
        }
    }

    /**
     * 判断APP是否在运行
     */
    public boolean isAppRunning() {
        return mActivities.size() > 0;
    }

    /**
     * 是否在栈顶
     *
     * @param className
     */
    public static boolean isActivityOnStackTop(Class className) {

        if (className == null) {
            throw new RuntimeException("className should not be null!");
        }

        Activity lastActivity = getInstance().getCurrentActivity();
        return lastActivity != null && className.getSimpleName()
                .equals(lastActivity.getClass().getSimpleName());
    }

    /**
     * @return 当前Activity的个数
     */
    public int getActivitySize() {
        return mActivities.size();
    }

    public List<Activity> getActivities() {
        return mActivities;
    }

    public void clearActivities() {

    }
}
