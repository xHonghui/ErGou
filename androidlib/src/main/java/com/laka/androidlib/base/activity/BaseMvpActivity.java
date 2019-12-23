package com.laka.androidlib.base.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.laka.androidlib.mvp.IBasePresenter;
import com.laka.androidlib.mvp.IBaseView;

/**
 * @Author:Rayman
 * @Date:2018/12/17
 * @Description:继承BaseActivity，整合MVP架构
 */

public abstract class BaseMvpActivity<D> extends BaseActivity implements IBaseView<D> {

    /**
     * description:MVP层关联
     **/
    private IBasePresenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        initMvp();
        super.onCreate(savedInstanceState);
    }

    /**
     * 初始化MVP架构
     */
    private void initMvp() {
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.setView(this);

            // 初始化MVP的时候，添加生命周期监听
            getLifecycle().addObserver(mPresenter);
        }
    }

    /**
     * 设置当前模块的P层，需要在initView之前调用
     *
     * @return
     */
    protected abstract IBasePresenter createPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onViewDestroy();
        }
    }
}

