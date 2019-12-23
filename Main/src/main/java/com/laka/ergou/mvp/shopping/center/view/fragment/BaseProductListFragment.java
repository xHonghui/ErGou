package com.laka.ergou.mvp.shopping.center.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laka.androidlib.base.fragment.BaseLazyLoadFragment;
import com.laka.androidlib.mvp.IBasePresenter;
import com.laka.androidlib.mvp.IBaseView;

/**
 * @Author:Rayman
 * @Date:2018/12/24
 * @Description:主页商品列表基类Fragment .
 * 测试之后发现，假若当前的类使用Kotlin的话，在泛型转换的时候。会强制要求全部泛型都必须指定
 */

public abstract class BaseProductListFragment<D> extends BaseLazyLoadFragment implements IBaseView<D> {

    protected IBasePresenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

}
