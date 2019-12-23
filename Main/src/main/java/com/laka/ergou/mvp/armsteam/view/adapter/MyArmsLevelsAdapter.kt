package com.laka.ergou.mvp.armsteam.view.adapter

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.laka.androidlib.util.GlideUtil
import com.laka.androidlib.util.screen.ScreenUtils
import com.laka.ergou.R
import com.laka.ergou.mvp.armsteam.MyArmsLevelsModuleNavigator
import com.laka.ergou.mvp.armsteam.constant.MyArmsLevelsConstant
import com.laka.ergou.mvp.armsteam.model.bean.MyArmsLevelsBean
import com.laka.ergou.mvp.main.RouterNavigator
import com.laka.ergou.mvp.main.constant.HomeApiConstant
import com.laka.ergou.mvp.main.constant.HomeConstant
import com.laka.ergou.mvp.user.UserUtils.UserUtils

/**
 * @Author:summer
 * @Date:2019/3/7
 * @Description:我的战友
 */
class MyArmsLevelsAdapter : BaseQuickAdapter<MyArmsLevelsBean, MyArmsLevelsAdapter.MyLowerLevelsViewHolder> {

    private var activity: Context
    private var mPageTyle: Int
    private var mClassType: Int

    constructor(context: Context, layoutResId: Int, data: MutableList<MyArmsLevelsBean>?, pageType: Int, classType: Int) : super(layoutResId, data) {
        this.activity = context
        this.mPageTyle = pageType
        this.mClassType = classType
    }

    override fun convert(helper: MyLowerLevelsViewHolder?, item: MyArmsLevelsBean?) {
        R.layout.item_my_lower_levels
        item?.let {
            helper?.showData(activity, item)
            val layoutParams = helper?.itemView?.layoutParams as? RecyclerView.LayoutParams
            if (helper?.position == 0) {
                layoutParams?.topMargin = ScreenUtils.dp2px(10f)
            } else {
                layoutParams?.topMargin = ScreenUtils.dp2px(0f)
            }
        }
    }

    inner class MyLowerLevelsViewHolder : BaseViewHolder {
        private var ivAvatar: ImageView?
        private var tvUserName: TextView?
        private var tvDate: TextView?
        private var tvRoyaCommission: TextView?
        private var tvRonaText: TextView?
        private var ivVipGrade: ImageView?
        private var ivMore: ImageView?
        private var llAlertMsg: LinearLayout?
        private var clHead: ConstraintLayout?
        private var tvInfo: TextView?

        constructor(view: View?) : super(view) {
            ivAvatar = view?.findViewById(R.id.iv_avatar)
            tvUserName = view?.findViewById(R.id.tv_user_name)
            tvDate = view?.findViewById(R.id.tv_date)
            tvRoyaCommission = view?.findViewById(R.id.tv_royalty_commission)
            tvRonaText = view?.findViewById(R.id.tv_royalty_txt)
            ivVipGrade = view?.findViewById(R.id.iv_vip_grade)
            ivMore = view?.findViewById(R.id.iv_more)
            llAlertMsg = view?.findViewById(R.id.ll_alert_msg)
            clHead = view?.findViewById(R.id.cl_head)
            tvInfo = view?.findViewById(R.id.tv_info)
        }

        fun showData(context: Context, item: MyArmsLevelsBean) {
            //头部显示
            if (position == 0) {
                clHead?.visibility = View.VISIBLE
                when (mClassType) {
                    MyArmsLevelsConstant.MY_LOWER_PAGE_FIRST -> {
                        tvInfo?.text = "您可以获得一级战队购物补贴的${UserUtils.getAgentInfo().agentRate}为您提成"
                    }
                    MyArmsLevelsConstant.MY_LOWER_PAGE_SECOND -> {
                        tvInfo?.text = "一级战友获得二级战友补贴提成的${UserUtils.getAgentInfo().agentRate}为您的提成"
                    }
                    MyArmsLevelsConstant.MY_LOWER_PAGE_THIRD -> {
                        tvInfo?.text = "一级战友获得军团战友补贴提成的${UserUtils.getAgentInfo().agentRate}为您的提成"
                    }
                }
            } else {
                clHead?.visibility = View.GONE
            }
            tvUserName?.text = "${item?.nickname}"
            tvRoyaCommission?.text = "${item?.commonValue}"
            tvDate?.text = item?.createdTime
            GlideUtil.loadFilletImage(context, item.avatar, R.drawable.default_img, R.drawable.default_img, ivAvatar)
            when (item?.level) {
                MyArmsLevelsConstant.MY_ARMS_LEVELS_FIRST -> { //10：超级队长，
                    ivVipGrade?.setImageResource(R.drawable.mine_img_super)
                }
                MyArmsLevelsConstant.MY_ARMS_LEVELS_SECOND -> { //20：金牌团长，
                    ivVipGrade?.setImageResource(R.drawable.mine_img_partner)
                }
                MyArmsLevelsConstant.MY_ARMS_LEVELS_THIRD -> {  //30：荣耀总司令
                    ivVipGrade?.setImageResource(R.drawable.mine_img_brand)
                }
            }
            if (item?.personCount <= 0) {
                tvRonaText?.text = "提成补贴"
                ivMore?.visibility = View.GONE
            } else {
                tvRonaText?.text = "战友${item?.personCount}人"
                ivMore?.visibility = View.VISIBLE
            }

            llAlertMsg?.setOnClickListener {
                if (item?.personCount > 0 && mClassType == MyArmsLevelsConstant.MY_LOWER_PAGE_FIRST) {
                    MyArmsLevelsModuleNavigator.startMyComradeArmsLowerActivity(mContext, item.mId, mPageTyle, mClassType)
                }
            }
            tvInfo?.setOnClickListener {
                openWarteamInfoActivity()
            }
        }
    }

    private fun openWarteamInfoActivity() {
        val target = RouterNavigator.bannerRouterReflectMap[5]
        val params = HashMap<String, String>()
        params[HomeConstant.WEB_TITLE] = "战队说明"
        params[HomeConstant.WEB_URL] = HomeApiConstant.URL_WARTEAM_INFO
        RouterNavigator.handleAppInternalNavigator(mContext, "$target", params)
    }

}