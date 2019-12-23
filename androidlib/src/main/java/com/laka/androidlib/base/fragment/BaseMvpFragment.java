package com.laka.androidlib.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laka.androidlib.mvp.IBasePresenter;
import com.laka.androidlib.mvp.IBaseView;

/**
 * @Author:Rayman
 * @Date:2019/1/8
 * @Description:基于BaseFragment结合MVP架构的Fragment
 */

public abstract class BaseMvpFragment<D> extends BaseFragment implements IBaseView<D> {

    private IBasePresenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initMvp();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initMvp() {
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.setView(this);
        }

        // 初始化MVP的时候，添加生命周期监听
        getLifecycle().addObserver(mPresenter);
    }

    /**
     * 创建P层对象
     *
     * @return
     */
    protected abstract IBasePresenter createPresenter();

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onViewDestroy();
        }
    }
}
