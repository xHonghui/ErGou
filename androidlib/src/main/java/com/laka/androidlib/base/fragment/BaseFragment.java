package com.laka.androidlib.base.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laka.androidlib.R;
import com.laka.androidlib.eventbus.Event;
import com.laka.androidlib.eventbus.EventBusManager;
import com.laka.androidlib.listener.EventListener;
import com.laka.androidlib.util.LogUtils;
import com.laka.androidlib.util.network.NetWorkHelper;
import com.laka.androidlib.widget.dialog.LoadingDialog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @Author:Rayman
 * @Date:2018/5/7
 * @Description:基类Fragment
 **/
public abstract class BaseFragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();

    protected ViewGroup rootView;
    protected Activity mActivity;
    protected Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            if (setContentView() == 0) {
                throw new IllegalArgumentException("layoutId can't no be null");
            }
            rootView = (ViewGroup) inflater.inflate(R.layout.fragment_root_view, container, false);
            inflater.inflate(setContentView(), rootView, true);
        }
        unbinder = ButterKnife.bind(this, rootView);
        mActivity = getActivity();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initLoadingDialog();
        initArgumentsData(getArguments());
        initView(rootView, savedInstanceState);
        initData();
        initEvent();
        EventBusManager.register(this);
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
    protected abstract void initData();

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
    public void onDestroyView() {
        dismissLoading();
        mLoadingDialog = null;
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        EventBusManager.unRegister(this);
        if (unbinder != null) {
            unbinder.unbind();
        }
        super.onDestroy();
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
