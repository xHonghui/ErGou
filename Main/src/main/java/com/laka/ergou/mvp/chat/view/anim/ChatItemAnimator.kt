package com.laka.ergou.mvp.chat.view.anim

import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView

/**
 * @Author:summer
 * @Date:2019/2/28
 * @Description:聊天列表 item 动画
 */
class ChatItemAnimator: DefaultItemAnimator(){
    /**
     * 添加item是调用
     * */
    override fun animateAdd(holder: RecyclerView.ViewHolder?): Boolean {
        return super.animateAdd(holder)
    }

    /**
     * 删除item时调用
     * */
    override fun animateRemove(holder: RecyclerView.ViewHolder?): Boolean {
        return super.animateRemove(holder)
    }

    /**
     * 移动item时调用
     * */
    override fun animateMove(holder: RecyclerView.ViewHolder?, fromX: Int, fromY: Int, toX: Int, toY: Int): Boolean {
        return super.animateMove(holder, fromX, fromY, toX, toY)
    }

    /**
     * 改变item时调用
     * */
    override fun animateChange(oldHolder: RecyclerView.ViewHolder, newHolder: RecyclerView.ViewHolder, preInfo: ItemHolderInfo, postInfo: ItemHolderInfo): Boolean {
        return super.animateChange(oldHolder, newHolder, preInfo, postInfo)
    }

    /**
     * 改变item时调用
     * */
    override fun animateChange(oldHolder: RecyclerView.ViewHolder?, newHolder: RecyclerView.ViewHolder?, fromX: Int, fromY: Int, toX: Int, toY: Int): Boolean {
        return super.animateChange(oldHolder, newHolder, fromX, fromY, toX, toY)
    }

}