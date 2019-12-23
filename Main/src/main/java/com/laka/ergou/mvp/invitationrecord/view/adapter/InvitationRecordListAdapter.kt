package com.laka.ergou.mvp.invitationrecord.view.adapter

import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.laka.androidlib.util.GlideUtil
import com.laka.androidlib.util.screen.ScreenUtils
import com.laka.ergou.R
import com.laka.ergou.mvp.invitationrecord.model.bean.InvitationRecord

/**
 * @Author:summer
 * @Date:2019/3/13
 * @Description:
 */
class InvitationRecordListAdapter : BaseQuickAdapter<InvitationRecord, BaseViewHolder> {

    constructor(layoutResId: Int, data: MutableList<InvitationRecord>?) : super(layoutResId, data)

    override fun convert(helper: BaseViewHolder?, item: InvitationRecord?) {
        R.layout.item_invitation_record
        val layoutParams = helper?.itemView?.layoutParams as? RecyclerView.LayoutParams
        if (helper?.position == 0) {
            layoutParams?.topMargin = ScreenUtils.dp2px(10f)
        } else {
            layoutParams?.topMargin = ScreenUtils.dp2px(0f)
        }
        item?.let {
            helper?.let {
                normalConvert(helper, item)
            }
        }
    }

    private fun normalConvert(helper: BaseViewHolder, item: InvitationRecord) {
        val ivAvatar = helper.getView<ImageView>(R.id.iv_avatar)
        val tvUserName = helper.getView<TextView>(R.id.tv_user_name)
        val tvTime = helper.getView<TextView>(R.id.tv_des)
        val tvCommission = helper.getView<TextView>(R.id.tv_royalty_commission)
        val tvCommissionTxt = helper.getView<TextView>(R.id.tv_royalty_txt)
        GlideUtil.loadImage(mContext, item.avatar, R.drawable.default_img, R.drawable.default_img, ivAvatar)
        tvUserName.text = item.nickname
        tvTime.text = item.create_time
        tvCommission.text = item.earn
        tvCommissionTxt.text = item.word
    }
}