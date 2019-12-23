package com.laka.ergou.common.fragmentstack;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.laka.androidlib.base.fragment.BaseFragment;
import com.laka.ergou.R;

/**
 * @Author:summer
 * @Date:2018/12/27
 * @Description:Fragment工具类,替换/弹起/添加等Fragment操作都可以使用该工具类完成
 */
public class FragmentTools {


    public static void pushScreen(Class<? extends BaseFragment> baseFragment) {
        pushScreen(baseFragment, new Bundle());
    }

    /**
     * 弹起一个Fragment
     *
     * @param baseFragment
     * @param args
     */
    public static void pushScreen(Class<? extends BaseFragment> baseFragment, Bundle args) {
        try {
            if (args == null) {
                args = new Bundle();
            }
            BaseFragment currentFragment = baseFragment.newInstance();
            currentFragment.setArguments(args);
            FragmentConfig.mCurrentFragment = currentFragment;
            // v4FragmentManager.beginTransaction() 其实就是创建一个BackStackRecord 对象，该对象实现
            // FragmentTransaction 接口，BackStackRecord 表示 Fragment的一次操作。
            FragmentTransaction transaction = FragmentConfig.v4FragmentManager.beginTransaction();
            // 设置Fragment转场方式
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            // 将新的Fragment添加到指定ID的布局中区，使用Fragment的activity需要提供一个相同ID的layout出来
            transaction.add(R.id.fragment_root_view, currentFragment, baseFragment.getName());
            // 判断当前fragment是否是v4FragmentManager的第一个fragment，如果是，则不添加到回退栈
            int fragmentCount = FragmentConfig.v4FragmentManager.getFragments().size();
            if (fragmentCount != 0) {
                // 添加到回退栈中,这样fragment就可以像activity一样回退了(点击后退键fragment栈会一个个回退出来)
                transaction.addToBackStack(baseFragment.getName());
            }
            transaction.commit();
            setRootScreen(baseFragment.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 切换根fragment
     *
     * @param baseFragment
     * @param args
     */
    public static final void replaceRootPushScreen(Class<? extends BaseFragment> baseFragment, Bundle args) {
        try {
            if (args == null) {
                args = new Bundle();
            }
            BaseFragment currentFragment = baseFragment.newInstance();
            currentFragment.setArguments(args);
            FragmentConfig.mCurrentFragment = currentFragment;
            // 弹出指定屏幕之上的包括自己的所有fragment
            FragmentConfig.v4FragmentManager.popBackStackImmediate(FragmentConfig.CurrentRootScreenName, FragmentConfig.v4FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction transaction = FragmentConfig.v4FragmentManager.beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.replace(R.id.fragment_root_view, currentFragment, baseFragment.getName());
            transaction.addToBackStack(baseFragment.getName());
            transaction.commit();
            FragmentConfig.v4FragmentManager.executePendingTransactions();
            FragmentConfig.mCurrentFragment.onResume();
            setRootScreen(baseFragment.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 弹出到指定屏幕
     *
     * @param assignFragment 要保留的堆栈里的屏幕
     * @param args           要传递的参数
     */
    public static final void pullToAssignScreen(Class<? extends BaseFragment> assignFragment, Bundle args) {
        try {
            // 0表示弹出的屏幕不包括当前屏幕
            FragmentConfig.v4FragmentManager.popBackStackImmediate(assignFragment.getName(), 0);
            FragmentConfig.mCurrentFragment = (BaseFragment) FragmentConfig.v4FragmentManager.findFragmentByTag(assignFragment.getName());
            if (FragmentConfig.mCurrentFragment != null && FragmentConfig.mCurrentFragment.getArguments() != null) {
                if (args != null) {
                    FragmentConfig.mCurrentFragment.getArguments().putAll(args);
                }
            } else {
                pushScreen(assignFragment, args);
            }
            FragmentConfig.mCurrentFragment.onResume();
            setRootScreen(assignFragment.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static final boolean pullScreen() {
        boolean isRootStackFragment = false; //是否是根部Fragment
        FragmentConfig.v4FragmentManager.popBackStackImmediate();
        if (FragmentConfig.v4FragmentManager.getBackStackEntryCount() > 1) {
            if (FragmentConfig.v4FragmentManager.findFragmentByTag(FragmentConfig.v4FragmentManager
                    .getBackStackEntryAt(FragmentConfig.v4FragmentManager.getBackStackEntryCount() - 1).getName()) instanceof BaseFragment) {
                FragmentConfig.mCurrentFragment = (BaseFragment) FragmentConfig.v4FragmentManager
                        .findFragmentByTag(FragmentConfig.v4FragmentManager
                                .getBackStackEntryAt(FragmentConfig.v4FragmentManager.getBackStackEntryCount() - 1).getName());
            }
        } else if (FragmentConfig.v4FragmentManager.getBackStackEntryCount() == 1) {
            if (FragmentConfig.v4FragmentManager
                    .findFragmentByTag(FragmentConfig.v4FragmentManager
                            .getBackStackEntryAt(0).getName()) instanceof BaseFragment) {
                FragmentConfig.mCurrentFragment = (BaseFragment) FragmentConfig.v4FragmentManager
                        .findFragmentByTag(FragmentConfig.v4FragmentManager
                                .getBackStackEntryAt(0).getName());
            }
        } else {
            Log.i("pullScreen", "当前回退栈为空，无法执行pullScreen操作");
            isRootStackFragment = true;
        }
        FragmentConfig.mCurrentFragment.onResume();
        setRootScreen(FragmentConfig.mCurrentFragment.getClass().getName());
        return isRootStackFragment;
    }


    /**
     * 在指定fragment之上显示当前fragment(指定fragmnet之上的fragment全部清除)
     *
     * @param baseFragment   要跳转的fragment
     * @param args           参数
     * @param assignFragment 指定的fragment
     */
    public static final void pushOnAssignScreen(Class<? extends BaseFragment> baseFragment, Bundle args, Class<? extends BaseFragment> assignFragment) {
        try {
            if (args == null) {
                args = new Bundle();
            }
            BaseFragment currentFragment = baseFragment.newInstance();
            currentFragment.setArguments(args);
            FragmentConfig.mCurrentFragment = currentFragment;
            // 指定fragment上的fragment全部清除
            FragmentConfig.v4FragmentManager.popBackStack(assignFragment.getName(), 0);// 0表示弹出的屏幕不包括当前屏幕
            FragmentTransaction transaction = FragmentConfig.v4FragmentManager.beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE); // 转场动画
            transaction.add(R.id.fragment_root_view, currentFragment, baseFragment.getName());
            transaction.addToBackStack(baseFragment.getName());
            transaction.commit();
            setRootScreen(baseFragment.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 替换当前fragment
     *
     * @param baseFragment
     * @param args
     */
    public static final void replacemCurrentFragment(Class<? extends BaseFragment> baseFragment, Bundle args) {
        try {
            if (args == null) {
                args = new Bundle();
            }
            BaseFragment currentFragment = baseFragment.newInstance();
            currentFragment.setArguments(args);
            FragmentConfig.mCurrentFragment = currentFragment;
            FragmentTransaction transaction = FragmentConfig.v4FragmentManager.beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            FragmentConfig.v4FragmentManager.popBackStack(); // 弹出顶部fragment
            transaction.add(R.id.fragment_root_view, currentFragment, baseFragment.getName());
            transaction.addToBackStack(baseFragment.getName());
            transaction.commit();
            setRootScreen(baseFragment.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 记录当前根部Fragment的名称
     *
     * @param name
     */
    public static void setRootScreen(final String name) {
        FragmentConfig.v4FragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                // 获取Fragment栈中除了当前显示Fragment外的个数,如果为0,则当前Fragment为前台fragmnet
                if (FragmentConfig.v4FragmentManager.getBackStackEntryCount() == 0) {
                    FragmentConfig.CurrentRootScreenName = name;
                }
                if (FragmentConfig.v4FragmentManager.getBackStackEntryCount() == 1) {
                    FragmentConfig.CurrentRootScreenName = FragmentConfig.v4FragmentManager.getBackStackEntryAt(0).getName();
                }
            }
        });
    }
}
