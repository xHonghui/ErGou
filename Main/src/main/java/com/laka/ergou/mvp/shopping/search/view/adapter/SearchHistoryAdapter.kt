package com.laka.ergou.mvp.shopping.search.view.adapter

import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.laka.androidlib.eventbus.EventBusManager
import com.laka.ergou.R
import com.laka.ergou.mvp.main.constant.HomeEventConstant
import com.laka.ergou.mvp.shopping.search.model.bean.SearchHistoryBean

/**
 * @Author:Rayman
 * @Date:2019/1/11
 * @Description:搜索主页---搜索历史Adapter
 */
class SearchHistoryAdapter : BaseQuickAdapter<SearchHistoryBean, BaseViewHolder>(R.layout.item_search_history) {

    private var mPreClickTime: Long = 0

    override fun convert(helper: BaseViewHolder?, item: SearchHistoryBean?) {
        helper?.setText(R.id.tv_search_history_title, item?.name)
        helper?.getView<TextView>(R.id.tv_search_history_title)
                ?.setOnClickListener {
                    // 发送KeyWord到SearchActivity
                    EventBusManager.postEvent(HomeEventConstant.EVENT_SEARCH_KEY_WORD, item?.name)
                }
        helper?.getView<ImageView>(R.id.iv_search_history_delete)
                ?.setOnClickListener {
                    if (System.currentTimeMillis() - mPreClickTime > 800) {
                        // 删除当前条目
                        mData.remove(item)
                        notifyDataSetChanged()
                        EventBusManager.postEvent(HomeEventConstant.EVENT_DELETE_SEARCH_HISTORY, item?.name)
                        mPreClickTime = System.currentTimeMillis()
                    }
                }
    }

    open fun clearAll() {
        mData?.clear()
        notifyDataSetChanged()
    }
}