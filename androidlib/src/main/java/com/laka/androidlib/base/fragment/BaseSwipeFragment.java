package com.laka.androidlib.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laka.androidlib.eventbus.Event;
import com.laka.androidlib.eventbus.EventBusManager;
import com.laka.androidlib.listener.EventListener;
import com.laka.androidlib.widget.dialog.LoadingDialog;
import com.laka.androidlib.widget.swipebacklayout.SwipeBackActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.swipebackfragment.SwipeBackFragment;

/**
 * @Author Lyf
 * @CreateTime 2018/6/28
 * @Description 可侧滑Fragment
 **/
public abstract class BaseSwipeFragment extends SwipeBackFragment {

    private final String TAG = this.getClass().getSimpleName();

    protected View rootView;
    protected FragmentActivity mActivity;
    protected Unbinder unbinder;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            if (setContentView() == 0) {
                throw new IllegalArgumentException("layoutId can't no be null");
            }
            rootView = inflater.inflate(setContentView(), container, false);
        }
        unbinder = ButterKnife.bind(this, rootView);
        mActivity = getActivity();
        initLoadingDialog();
        initView(rootView, savedInstanceState);
        EventBusManager.register(this);
        initData();
        initEvent();
        return attachToSwipeBack(rootView);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 禁掉Activity的侧滑, 至于何时重启，得根据不同的业务逻辑去处理了。
        if (mActivity instanceof SwipeBackActivity) {
            ((SwipeBackActivity) mActivity).setSwipeBackEnable(false);
        }
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


    /**
     * 配置当前LayoutId
     *
     * @return
     */
    protected abstract int setContentView();

    /**
     * 初始化控件，考虑BufferKnife使用
     *
     * @param rootView
     */
    protected abstract void initView(View rootView, Bundle savedInstanceState);

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 初始化事件
     */
    protected abstract void initEvent();


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event event) {
    }

    @Override
    public void onDestroyView() {
        dismissLoading();
        mLoadingDialog = null;
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        EventBusManager.unRegister(this);
        super.onDestroy();
    }

    protected boolean checkStringValidate(String str) {
        if (str == null) {
            return false;
        }
        if (str.equals("")) {
            return false;
        }
        return true;
    }

    /**
     * find的出来的View, 带有防抖功能。
     */
    public <T extends View> T findClickView(int id) {

        T view = rootView.findViewById(id);
        view.setOnClickListener(new EventListener(this));
        return view;
    }

    public <T extends View> T findViewById(int id) {
        return rootView.findViewById(id);
    }

    public void onClick(View view) {
    }
}
