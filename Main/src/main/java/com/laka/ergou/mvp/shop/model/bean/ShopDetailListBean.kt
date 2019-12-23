package com.laka.ergou.mvp.shop.model.bean

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.laka.androidlib.widget.refresh.BaseListBean

/**
 * @Author:summer
 * @Date:2018/12/25
 * @Description: 商品详情列表bean
 */
class ShopDetailListBean : BaseListBean<MultiItemEntity>() {

    private var mDataList: ArrayList<MultiItemEntity> = ArrayList()

    override fun getList(): ArrayList<MultiItemEntity> {
        return mDataList
    }

    fun setDataList(data: ArrayList<MultiItemEntity>) {
        this.mDataList = data
    }
}