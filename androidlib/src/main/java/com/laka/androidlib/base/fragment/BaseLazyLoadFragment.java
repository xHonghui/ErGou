package com.laka.androidlib.base.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laka.androidlib.eventbus.Event;
import com.laka.androidlib.eventbus.EventBusManager;
import com.laka.androidlib.listener.EventListener;
import com.laka.androidlib.mvp.IBasePresenter;
import com.laka.androidlib.util.LogUtils;
import com.laka.androidlib.widget.dialog.LoadingDialog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @Author:Rayman
 * @Date:2018/5/17
 * @Description:懒加载Fragment(只适用于与ViewPager的方式实现) 在常规的replace-add-hide里面，setUserVisibleHint这个函数是不调用的。所以不会执行下面的initData函数
 */

public abstract class BaseLazyLoadFragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();

    /**
     * description:
     * 判断是否可见，是否加载完毕，是否第一次加载
     **/
    private boolean isVisible;
    private boolean isPrepared;
    private boolean isFirst = true;

    protected View rootView;
    protected Activity mActivity;
    protected Unbinder unbinder;
    private IBasePresenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initMvp();
        if (rootView == null) {
            if (setContentView() == 0) {
                throw new IllegalArgumentException("layoutId can't no be null");
            }
            rootView = inflater.inflate(setContentView(), container, false);
        }
        isPrepared = true;
        mActivity = getActivity();
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initLoadingDialog();
        initArgumentsData(getArguments());
        initView(rootView, savedInstanceState);
        initSecondView(rootView, savedInstanceState);
        lazyLoad();
        initEvent();
        EventBusManager.register(this);
    }

    /**
     * 第二初始化view 方法，提供给中间层 BaseFragment 使用
     */
    protected void initSecondView(View rootView, Bundle savedInstanceState) {
    }

    private LoadingDialog mLoadingDialog;

    private void initLoadingDialog() {
        if (getActivity() == null || getActivity().isFinishing() || getActivity().isDestroyed()) {
            return;
        }
        mLoadingDialog = new LoadingDialog(getActivity());
    }

    public void showLoading() {
        if (getActivity() == null || getActivity().isFinishing() || getActivity().isDestroyed()) {
            return;
        }
        if (!mLoadingDialog.isShowing() && mLoadingDialog != null) {
            mLoadingDialog.show();
        }
    }

    public void dismissLoading() {
        if (getActivity() == null || getActivity().isFinishing() || getActivity().isDestroyed()) {
            return;
        }
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void onDestroyView() {
        EventBusManager.unRegister(this);
        dismissLoading();
        mLoadingDialog = null;
        super.onDestroyView();
        if (mPresenter != null) {
            mPresenter.onViewDestroy();
        }
    }


    private void initMvp() {
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.setView(this);
        }
    }

    /**
     * 创建P层对象
     *
     * @return
     */
    protected abstract IBasePresenter createPresenter();

    /**
     * 配置当前LayoutId
     *
     * @return
     */
    @LayoutRes
    protected abstract int setContentView();

    /**
     * 初始化Arguments数据
     *
     * @param arguments
     */
    protected abstract void initArgumentsData(Bundle arguments);

    /**
     * 初始化控件，考虑BufferKnife使用
     *
     * @param rootView
     */
    protected abstract void initView(View rootView, Bundle savedInstanceState);

    /**
     * 初始化数据
     */
    protected abstract void initDataLazy();

    /**
     * 初始化事件
     */
    protected abstract void initEvent();

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String action) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event event) {
    }

    @Override
    public void onDestroy() {
        if (unbinder != null) {
            unbinder.unbind();
        }
        super.onDestroy();
    }

    /**
     * 判断当前Fragment是否显示（在onCreate之前调用即UI还没初始化之前）
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //当UI初始化完毕之后
        if (getUserVisibleHint()) {
            isVisible = true;
            lazyLoad();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    /**
     * 懒加载的方法，主要是使initData实现延迟加载数据
     * 因为需要根据生命周期判断三次状态，所以该方法至少需要调用两次
     */
    private void lazyLoad() {
        LogUtils.info("lazyLoad-----" + this.getClass().getSimpleName());
        if (!isPrepared || !isVisible || !isFirst) {
            return;
        }
        LogUtils.info("lazyLoad-----isPrepared=" + isPrepared + "-----isVisible=" + isVisible + "------isFirst=" + isFirst);
        initDataLazy();
        isFirst = false;
    }

    protected void onInvisible() {

    }

    public <T extends View> T findViewById(int id) {
        return rootView.findViewById(id);
    }

    /**
     * Find出来的View，自带防抖点击功能
     */
    public <T extends View> T setClickView(int id) {
        T view = rootView.findViewById(id);
        view.setOnClickListener(new EventListener(this));
        return view;
    }
}
