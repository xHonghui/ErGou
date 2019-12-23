package com.laka.ergou.mvp.user.view.adapter

import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.laka.androidlib.callback.ImageLoaderCallBack
import com.laka.androidlib.util.ResourceUtils
import com.laka.androidlib.util.TextViewHelper
import com.laka.androidlib.util.image.ImageLoader
import com.laka.ergou.R
import com.laka.ergou.mvp.user.model.bean.RobotInfo
import java.lang.Exception

/**
 * @Author:Rayman
 * @Date:2019/1/18
 * @Description:我的模块---我的机器人页面Adapter
 */
class RobotListAdapter : BaseQuickAdapter<RobotInfo, BaseViewHolder>(R.layout.item_my_robot) {

    override fun convert(helper: BaseViewHolder?, item: RobotInfo?) {
        val mIvRobotAvatar = helper?.getView<ImageView>(R.id.iv_robot_avatar)
        ImageLoader.getInstance().with(helper?.convertView?.context)
                .load(item?.robotAvatar)
                .placeholder(R.drawable.ic_robot_place_holder)
                .callBack(object : ImageLoaderCallBack {
                    override fun onBitmapLoaded(drawable: Drawable?) {
                        mIvRobotAvatar?.setImageDrawable(drawable)
                    }

                    override fun onBitmapFailed(e: Exception?) {
                    }

                })
        helper?.setText(R.id.tv_robot_name, item?.robotName)
        helper?.setText(R.id.tv_robot_id, "微信号：${item?.robotWxId}")

        val mTvCopyWeChatId = helper?.getView<TextView>(R.id.tv_robot_id_copy)
        TextViewHelper.setSpan(TextViewHelper.Builder()
                .setText(ResourceUtils.getString(R.string.robot_copy_id))
                .setTextView(mTvCopyWeChatId)
                .setUnderLine(true))

        val onlineStatus = helper?.getView<ImageView>(R.id.iv_robot_status)
        val onlineStatusText = helper?.getView<TextView>(R.id.tv_robot_status)
        val robotOperation = helper?.getView<TextView>(R.id.tv_robot_operation)
        if (item?.isOnline()!!) {
            onlineStatus?.setImageResource(R.drawable.shape_on_line_circle)
            onlineStatusText?.text = ResourceUtils.getString(R.string.on_line)
            robotOperation?.text = ResourceUtils.getString(R.string.robot_earn_commission)
            robotOperation?.setTextColor(ResourceUtils.getColor(R.color.color_on_line_text))
            robotOperation?.setBackgroundResource(R.drawable.btn_on_line_rect)
        } else {
            onlineStatus?.setImageResource(R.drawable.shape_off_line_circle)
            onlineStatusText?.text = ResourceUtils.getString(R.string.off_line)
            robotOperation?.text = ResourceUtils.getString(R.string.robot_call_server)
            robotOperation?.setTextColor(ResourceUtils.getColor(R.color.color_off_line_text))
            robotOperation?.setBackgroundResource(R.drawable.btn_off_line_rect)
        }

        helper?.addOnClickListener(R.id.tv_robot_id_copy)
        helper?.addOnClickListener(R.id.tv_robot_operation)
    }
}