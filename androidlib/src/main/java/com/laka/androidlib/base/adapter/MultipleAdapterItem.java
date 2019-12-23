package com.laka.androidlib.base.adapter;

import com.chad.library.adapter.base.BaseViewHolder;

/**
 * @Author:Rayman
 * @Date:2018/5/22
 * @Description:多种类AdapterItem，根据BaseRecyclerView
 */

public interface MultipleAdapterItem<T> {

    /**
     * 子类重写当前方法
     *
     * @param helper
     * @param item
     * @param type 类型，用于区分相同item，不同情况下展示不同UI的区分
     */
    void convert(BaseViewHolder helper, T item);

}
