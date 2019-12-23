package com.laka.androidlib.base.activity;

import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.laka.androidlib.R;
import com.laka.androidlib.widget.titlebar.TitleBarView;

/**
 * @Author Lyf
 * @CreateTime 2018/8/10
 * @Description 基类Fragment页面Activity
 **/
public abstract class BaseFragmentActivity extends BaseActivity {

    private FragmentManager mFragmentManager;
    protected TitleBarView mTitleBar;
    protected ConstraintLayout mRootView;

    @Override
    public int setContentView() {
        return R.layout.fragment_container;
    }

    /**
     * description:设计初衷之初initViews是为了setFragment之后的扩展函数，可以在initViews再对Fragment做一些操作
     * 例如当前页面就直接调用webFragment.loadUrl(url)。
     * 但是实际上呢，虽然initViews的时间是在setFragment之前就调用了。但是这时候执行的Fragment还没走完初始化的流程，
     * 例如继承了BaseFragment的类，可能在某个时间点内还没走fragment.initViews这个函数。所以不能在initViews的函数里面调用
     * fragment的子类，假若真的需要使用，可以在Fragment调用initViews之后发送EventBus事件回调
     **/
    @Override
    protected void initViews() {
        mTitleBar = findViewById(R.id.title_bar);
        mRootView = findViewById(R.id.cl_root_view);
        initFragment();
    }

    private void initFragment() {
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (mFragmentManager.getBackStackEntryCount() == 1) {
                    setSwipeBackEnable(true);
                }
            }
        });
        openFragment();
    }


    @Override
    public void onBackPressed() {

        if (mFragmentManager.getBackStackEntryCount() == 1) {
            finish();
            return;
        }
        super.onBackPressed();
    }

    /**
     * 打开
     */
    private String openFragment() {

        String fragmentTag = setFragmentTag();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        if (isPlayAnimation()) {
            transaction.setCustomAnimations(0, 0, 0, 0);
        } else {
            transaction.setCustomAnimations(R.anim.slide_right_in, 0, 0, R.anim.slide_right_out);
        }

        transaction.addToBackStack(fragmentTag);
        transaction.add(R.id.content, setFragment(), fragmentTag);
        transaction.commit();

        return fragmentTag;
    }

    /**
     * 设置Fragment
     */
    public abstract Fragment setFragment();

    /**
     * 设置FragmentTag
     */
    public abstract String setFragmentTag();

    /**
     * 是否需要进场动画
     */
    public abstract boolean isPlayAnimation();
}
