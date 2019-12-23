package com.laka.ergou.mvp.user

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import com.alibaba.baichuan.trade.biz.login.AlibcLogin
import com.laka.androidlib.util.BaseActivityNavigator
import com.laka.androidlib.util.ResourceUtils
import com.laka.ergou.R
import com.laka.ergou.mvp.armsteam.view.activity.MyArmsLevelsActivity
import com.laka.ergou.mvp.commission.view.activity.CommissionActivity
import com.laka.ergou.mvp.invitationrecord.view.activity.InvitationRecordActivity
import com.laka.ergou.mvp.login.LoginModuleNavigator
import com.laka.ergou.mvp.main.constant.HomeApiConstant
import com.laka.ergou.mvp.main.constant.HomeConstant
import com.laka.ergou.mvp.main.util.AlibcNavigatorUtil
import com.laka.ergou.mvp.message.view.activity.MessageCenterActivity
import com.laka.ergou.mvp.order.view.activity.MyAllOrderActivity
import com.laka.ergou.mvp.teamaward.view.activity.TeamAwardActivity
import com.laka.ergou.mvp.user.UserUtils.UserUtils
import com.laka.ergou.mvp.user.constant.UserConstant
import com.laka.ergou.mvp.user.view.activity.*


/**
 * @Author:Rayman
 * @Date:2019/1/7
 * @Description:用户模块跳转工具类
 */
object UserModuleNavigator {

    private fun loginHandle(context: Context): Boolean {
        return if (!UserUtils.isLogin()) {
            LoginModuleNavigator.startLoginActivity(context)
            false
        } else {
            true
        }
    }

    fun startMyOrderActivity(activity: Context) {
        if (loginHandle(activity)) {
            BaseActivityNavigator.startActivity(activity, MyAllOrderActivity::class.java)
        }
    }

    fun startMyOrderActivityForResult(activity: Activity, requestCode: Int) {
        if (loginHandle(activity)) {
            BaseActivityNavigator.startActivityForResult(activity, MyAllOrderActivity::class.java, requestCode)
        }
    }

    fun startMyShopCartActivity(context: Activity) {
        if (loginHandle(context)) {
            AlibcNavigatorUtil.showAlibcMyShopCart(context)
        }
    }

    fun startMySettingActivity(context: Context) {
        if (loginHandle(context)) {
            BaseActivityNavigator.startActivity(context, SettingActivity::class.java)
        }
    }

    fun startMySettingActivityForResult(context: Activity, requestCode: Int) {
        if (loginHandle(context)) {
            BaseActivityNavigator.startActivityForResult(context, SettingActivity::class.java, requestCode)
        }
    }

    fun startUserInfoActivity(context: Context) {
        if (loginHandle(context)) {
            BaseActivityNavigator.startActivity(context, UserInfoActivity::class.java)
        }
    }

    fun startUserNickEditActivity(context: Context) {
        if (loginHandle(context)) {
            BaseActivityNavigator.startActivity(context, UserNickEditActivity::class.java)
        }
    }

    fun startUserGenderEditActivity(context: Context) {
        if (loginHandle(context)) {
            BaseActivityNavigator.startActivity(context, UserGenderEditActivity::class.java)
        }
    }

    fun startChangeUserPhoneActivity(context: Context) {
        if (loginHandle(context)) {
            BaseActivityNavigator.startActivity(context, ChangePhoneActivity::class.java)
        }
    }

    fun startChangePhoneVerifyCodeActivity(context: Context, phone: String) {
        if (loginHandle(context)) {
            val bundle = Bundle()
            bundle.putString(UserConstant.CHANG_PHONE_NUMBER, phone)
            BaseActivityNavigator.startActivity(context, VerifyCodeActivity::class.java, bundle)
        }
    }

    fun startBindAliAccountActivity(context: Context) {
        if (loginHandle(context)) {
            BaseActivityNavigator.startActivity(context, BindAliAccountActivity::class.java)
        }
    }

    fun startBindAliAccountActivityForResult(activity: Activity, requestCode: Int) {
        if (loginHandle(activity)) {
            BaseActivityNavigator.startActivityForResult(activity, BindAliAccountActivity::class.java, requestCode)
        }
    }

    fun startEarnCommissionActivity(context: Context, robotId: String, robotName: String) {
        if (loginHandle(context)) {
            val bundle = Bundle()
            bundle.putString(UserConstant.ROBOT_ID, robotId)
            bundle.putString(UserConstant.ROBOT_NAME, robotName)
            BaseActivityNavigator.startActivity(context, EarnCommissionActivity::class.java, bundle)
        }
    }

    fun startAboutUsActivity(context: Context) {
        if (loginHandle(context)) {
            BaseActivityNavigator.startActivity(context, AboutUsActivity::class.java)
        }
    }

    fun startMyCommissionActivity(context: Context) {
        if (loginHandle(context)) {
            BaseActivityNavigator.startActivity(context, CommissionActivity::class.java)
        }
    }

    fun startMyCommissionActivityForResult(context: Activity, requestCode: Int) {
        if (loginHandle(context)) {
            BaseActivityNavigator.startActivityForResult(context, CommissionActivity::class.java, requestCode)
        }
    }

    fun startBindOrderActivity(context: Context) {
        if (loginHandle(context)) {
            BaseActivityNavigator.startActivity(context, BindOrderActivity::class.java)
        }
    }

    fun startBindOrderActivityForResult(context: Activity, requestCode: Int) {
        if (loginHandle(context)) {
            BaseActivityNavigator.startActivityForResult(context, BindOrderActivity::class.java, requestCode)
        }
    }

    fun startMyRobotActivity(context: Context) {
        if (loginHandle(context)) {
            BaseActivityNavigator.startActivity(context, MyRobotActivity::class.java)
        }
    }

    fun startMyRobotActivityForResult(context: Activity, requestCode: Int) {
        if (loginHandle(context)) {
            BaseActivityNavigator.startActivityForResult(context, MyRobotActivity::class.java, requestCode)
        }
    }

    fun startAddRobotActivity(context: Context) {
        if (loginHandle(context)) {
            BaseActivityNavigator.startActivity(context, AddRobotActivity::class.java)
        }
    }

    fun startInvitationWebActivity(context: Context) {
        if (loginHandle(context)) {
            val bundle = Bundle()
            bundle.putString(HomeConstant.WEB_TITLE, ResourceUtils.getString(R.string.util_invited_friend))
            bundle.putString(HomeConstant.WEB_URL, HomeApiConstant.URL_TEAM_INVITATION)
            BaseActivityNavigator.startActivity(context, InvitationWebActivity::class.java, bundle)
        }
    }

    fun startInvitationWebActivityForResult(context: Activity, resultCode: Int) {
        if (loginHandle(context)) {
            val bundle = Bundle()
            bundle.putString(HomeConstant.WEB_TITLE, ResourceUtils.getString(R.string.util_invited_friend))
            bundle.putString(HomeConstant.WEB_URL, HomeApiConstant.URL_TEAM_INVITATION)
            BaseActivityNavigator.startActivityForResult(context, InvitationWebActivity::class.java, bundle, resultCode)
        }
    }

    fun startInvitationPlaybillActivity(context: Context) {
        if (loginHandle(context)) {
            BaseActivityNavigator.startActivity(context, InvitationPlaybillActivity::class.java)
        }
    }

    fun startInvitationPlaybillActivityForResult(context: Activity, requestCode: Int) {
        if (loginHandle(context)) {
            BaseActivityNavigator.startActivityForResult(context, InvitationPlaybillActivity::class.java, requestCode)
        }
    }

    fun startInvitationPlaybillActivity(context: Context, postId: Int, resultCode: Int) {
        if (loginHandle(context)) {
            var bundle = Bundle()
            bundle.putInt(UserConstant.POST_ID, postId)
            BaseActivityNavigator.startActivityForResult(context as Activity, InvitationPlaybillActivity::class.java, bundle, resultCode)
        }
    }

    fun startMyLowerLevelActivity(context: Context) {
        if (loginHandle(context)) {
            BaseActivityNavigator.startActivity(context, MyArmsLevelsActivity::class.java)
        }
    }

    fun startMyLowerLevelActivityForResult(context: Activity, requestCode: Int) {
        if (loginHandle(context)) {
            BaseActivityNavigator.startActivityForResult(context, MyArmsLevelsActivity::class.java, requestCode)
        }
    }

    fun startMessageCenterActivity(context: Context) {
        if (loginHandle(context)) {
            BaseActivityNavigator.startActivity(context, MessageCenterActivity::class.java)
        }
    }

    fun startTearmAwardActivity(context: Context) {
        if (loginHandle(context)) {
            BaseActivityNavigator.startActivity(context, TeamAwardActivity::class.java)
        }
    }

    fun startTearmAwardActivityForResult(context: Activity, requestCode: Int) {
        if (loginHandle(context)) {
            BaseActivityNavigator.startActivityForResult(context, TeamAwardActivity::class.java, requestCode)
        }
    }

    fun startMessageCenterActivity(context: Context, fragmentIndex: Int) {
        if (loginHandle(context)) {
            val bundle = Bundle()
            bundle.putInt(UserConstant.MESSAGE_TYPE, fragmentIndex)
            BaseActivityNavigator.startActivity(context, MessageCenterActivity::class.java, bundle)
        }
    }

    fun startMessageCenterActivityForResult(context: Activity, fragmentIndex: Int, requestCode: Int) {
        if (loginHandle(context)) {
            val bundle = Bundle()
            bundle.putInt(UserConstant.MESSAGE_TYPE, fragmentIndex)
            BaseActivityNavigator.startActivityForResult(context, MessageCenterActivity::class.java, bundle, requestCode)
        }
    }

    fun startLoginActivity(context: Context) {
        loginHandle(context)
    }

    fun startLoginActivityFinish(activity: Activity) {
        loginHandle(activity)
        activity.finish()
    }

    fun startInvitationRecordActivity(context: Context) {
        if (loginHandle(context)) {
            BaseActivityNavigator.startActivity(context, InvitationRecordActivity::class.java)
        }
    }

    fun startInvitationRecordActivityForResult(context: Activity, requestCode: Int) {
        if (loginHandle(context)) {
            BaseActivityNavigator.startActivityForResult(context, InvitationRecordActivity::class.java, requestCode)
        }
    }

    fun startRewardListActivity(context: Context) {
        if (loginHandle(context)) {
            BaseActivityNavigator.startActivity(context, RewardActivity::class.java)
        }
    }

    fun startRewardListActivityForResult(context: Activity, requestCode: Int) {
        if (loginHandle(context)) {
            BaseActivityNavigator.startActivityForResult(context, RewardActivity::class.java, requestCode)
        }
    }

    /**进入淘宝联盟授权url页面*/
    fun startBindUnionCodeActivityForResult(context: Activity, url: String, code: Int) {
        if (loginHandle(context) && AlibcLogin.getInstance().isLogin) {
            val bundle = Bundle()
            bundle.putString(HomeConstant.WEB_URL, url)
            BaseActivityNavigator.startActivityForResult(context, BindUnionCodeWebActivity::class.java, bundle, code)
        }
    }

    /**在Fragment 中使用startActivityForResult ，不能用activity来调用，否则无法收到相应的回调，为了兼容这个问题，使用
     * 当前的fragment 来调用，所以扩展了此方法*/
    fun startBindUnionCodeActivityForResultOnFragment(context: Activity, fragment: Fragment, url: String, code: Int) {
        if (loginHandle(context) && AlibcLogin.getInstance().isLogin) {
            val bundle = Bundle()
            bundle.putString(HomeConstant.WEB_URL, url)
            BaseActivityNavigator.startActivityForResultOnFragment(context, fragment, BindUnionCodeWebActivity::class.java, bundle, code)
        }
    }

}