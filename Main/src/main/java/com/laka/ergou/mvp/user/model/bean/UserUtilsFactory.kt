package com.laka.ergou.mvp.user.model.bean

import com.laka.androidlib.util.ResourceUtils
import com.laka.ergou.R
import com.laka.ergou.mvp.advertbanner.model.bean.AdvertBannerBean
import com.laka.ergou.mvp.main.constant.HomeNavigatorConstant
import com.laka.ergou.mvp.user.view.adapter.PersonalHintItem

/**
 * @Author:Rayman
 * @Date:2019/3/7
 * @Description:测试类---创建测试数据l
 */
object UserUtilsFactory {

    /**用户防丢单item*/
    fun createPreventOrderLostUtils(): PersonalHintBean {
        return PersonalHintBean()
    }

    /**
     * description:创建多工具面板
     **/
    fun createMixtureUtils(): PersonalMixtureBean {
        val mUtils = ArrayList<PersonalUtilBean>()
        mUtils.add(PersonalUtilBean(R.drawable.ic_mine_commission, ResourceUtils.getString(R.string.util_my_commission), HomeNavigatorConstant.NAV_MY_COMMISSION))
        mUtils.add(PersonalUtilBean(R.drawable.ic_mine_order, ResourceUtils.getString(R.string.util_my_order), HomeNavigatorConstant.NAV_MY_ORDER))
        mUtils.add(PersonalUtilBean(R.drawable.ic_my_subordinate, ResourceUtils.getString(R.string.util_subordinate), HomeNavigatorConstant.NAV_MY_SUBORDINATE))
        mUtils.add(PersonalUtilBean(R.drawable.ic_mine_invitation, ResourceUtils.getString(R.string.util_invitation), HomeNavigatorConstant.NAV_INVITATION))

        mUtils.add(PersonalUtilBean(R.drawable.mine_icon_yqjl, ResourceUtils.getString(R.string.invitation_record), HomeNavigatorConstant.NAV_INVITATION_RECORD))
        mUtils.add(PersonalUtilBean(R.drawable.mine_icon_zytc, ResourceUtils.getString(R.string.util_my_tearm_award), HomeNavigatorConstant.NAV_ACTIVITY_TEARM_AWARD))
        mUtils.add(PersonalUtilBean(R.drawable.mine_icon_qtjl, ResourceUtils.getString(R.string.other_reward), HomeNavigatorConstant.NAV_MY_OTHER_REWARD))
        mUtils.add(PersonalUtilBean(R.drawable.mine_icon_hbfx, ResourceUtils.getString(R.string.share_poster), HomeNavigatorConstant.NAV_MY_POSTER_SHARE))
        return PersonalMixtureBean(mUtils)
    }

    /**
     * description:创建单工具数据
     **/
    fun createSingleUtils(): ArrayList<PersonalUtilBean> {
        val mUtils = ArrayList<PersonalUtilBean>()
//        mUtils.add(PersonalUtilBean(R.mipmap.ic_mine_helper, ResourceUtils.getString(R.string.util_my_helper), HomeNavigatorConstant.NAV_MY_HELPER))
        mUtils.add(PersonalUtilBean(R.drawable.ic_bind_account, ResourceUtils.getString(R.string.util_bind_order), HomeNavigatorConstant.NAV_MY_BIND_ORDER))
        //mUtils.add(PersonalUtilBean(R.drawable.mine_icon_yqjl, ResourceUtils.getString(R.string.invitation_record), HomeNavigatorConstant.NAV_INVITATION_RECORD))
        //mUtils.add(PersonalUtilBean(R.drawable.ic_bind_account, ResourceUtils.getString(R.string.util_my_tearm_award), HomeNavigatorConstant.NAV_ACTIVITY_TEARM_AWARD))
        //mUtils.add(PersonalUtilBean(R.drawable.mine_icon_qtjl, ResourceUtils.getString(R.string.other_reward), HomeNavigatorConstant.NAV_MY_OTHER_REWARD))
        //mUtils.add(PersonalUtilBean(R.drawable.mine_icon_hbfx, ResourceUtils.getString(R.string.share_poster), HomeNavigatorConstant.NAV_MY_POSTER_SHARE))
        //我的机器人
        //mUtils.add(PersonalUtilBean(R.drawable.mine_icon_yqjl, ResourceUtils.getString(R.string.util_my_robot), HomeNavigatorConstant.NAV_MY_ROBOT))
        return mUtils
    }
}