package com.laka.ergou.mvp.user.view.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseViewHolder
import com.laka.androidlib.base.adapter.MultipleAdapterItem
import com.laka.ergou.R
import com.laka.ergou.mvp.circle.constant.CircleConstant
import com.laka.ergou.mvp.main.RouterNavigator
import com.laka.ergou.mvp.main.constant.HomeConstant
import com.laka.ergou.mvp.main.constant.HomeNavigatorConstant
import com.laka.ergou.mvp.user.model.bean.PersonalUtilBean

/**
 * @Author:Rayman
 * @Date:2019/3/7
 * @Description:用户主页---单个工具
 */
class PersonalSingleItem : MultipleAdapterItem<PersonalUtilBean> {

    private var mItemClickTime = 0L
    private var mTimeInterval = 800

    override fun convert(helper: BaseViewHolder?, item: PersonalUtilBean?) {
        var mTvUtil = helper?.getView<TextView>(R.id.tv_personal_util)
        helper?.setImageResource(R.id.iv_personal_util, item?.utilIcon!!)
        mTvUtil?.text = item?.utilName
        helper?.convertView?.setOnClickListener {
            if (System.currentTimeMillis() - mItemClickTime > mTimeInterval) {
                if (item?.utilNavigation == HomeNavigatorConstant.NAV_MY_HELPER) {
                    //大管家页面
                    RouterNavigator.handleAppInternalNavigator(helper?.convertView!!.context!!,
                            item!!.utilNavigation,
                            HashMap(),
                            CircleConstant.BUTLER_REQUEST_CODE_FOR_USERCENTER)
                } else {
                    RouterNavigator.handleAppInternalNavigator(helper?.convertView!!.context!!,
                            item!!.utilNavigation,
                            HashMap())
                }
                mItemClickTime = System.currentTimeMillis()
            }
        }
    }
}