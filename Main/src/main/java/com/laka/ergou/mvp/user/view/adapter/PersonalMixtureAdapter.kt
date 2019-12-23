package com.laka.ergou.mvp.user.view.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.laka.ergou.R
import com.laka.ergou.mvp.main.RouterNavigator
import com.laka.ergou.mvp.user.UserUtils.UserUtils
import com.laka.ergou.mvp.user.constant.UserConstant
import com.laka.ergou.mvp.user.model.bean.PersonalUtilBean

/**
 * @Author:Rayman
 * @Date:2019/3/7
 * @Description:用户主页---水平工具表，单个小工具Adapter
 */
class PersonalMixtureAdapter : BaseQuickAdapter<PersonalUtilBean, BaseViewHolder> {

    private var mItemClickTime = 0L
    private var mTimeInterval = 800

    constructor(list: MutableList<PersonalUtilBean>) : super(R.layout.item_personal_util_vertical, list)

    override fun convert(helper: BaseViewHolder?, item: PersonalUtilBean?) {
        var mTvUtil = helper?.getView<TextView>(R.id.tv_personal_util)
        helper?.setImageResource(R.id.iv_personal_util, item?.utilIcon!!)
        mTvUtil?.text = item?.utilName
        helper?.convertView?.setOnClickListener {
            if (System.currentTimeMillis() - mItemClickTime > mTimeInterval) {
                RouterNavigator.handleAppInternalNavigator(helper?.convertView!!.context!!,
                        item!!.utilNavigation,
                        HashMap())
                mItemClickTime = System.currentTimeMillis()
            }
        }
    }
}