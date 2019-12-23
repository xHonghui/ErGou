package com.laka.ergou.mvp.shopping.search.view.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.laka.androidlib.eventbus.Event
import com.laka.androidlib.eventbus.EventBusManager
import com.laka.ergou.R
import com.laka.ergou.mvp.main.constant.HomeEventConstant

/**
 * @Author:Rayman
 * @Date:2019/1/11
 * @Description:搜索---热门Adapter
 */
class SearchHotAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_search_hot) {

    override fun convert(helper: BaseViewHolder?, item: String?) {
        helper?.setText(R.id.tv_search_hot_title, item)
        helper?.getView<TextView>(R.id.tv_search_hot_title)?.setOnClickListener {
            // EventBus回调给SearchActivity
            EventBusManager.postEvent(Event(HomeEventConstant.EVENT_SEARCH_KEY_WORD, item))
        }
    }
}