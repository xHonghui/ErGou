package com.laka.ergou.mvp.invitationrecord

import android.content.Context
import com.laka.androidlib.util.BaseActivityNavigator
import com.laka.ergou.mvp.invitationrecord.view.activity.InvitationRecordActivity
import com.laka.ergou.mvp.login.LoginModuleNavigator
import com.laka.ergou.mvp.user.UserUtils.UserUtils

/**
 * @Author:summer
 * @Date:2019/3/13
 * @Description:邀请记录
 */
object InvitationRecordModuleNavigator {

    fun startInvitationRecordActivity(context: Context) {
        BaseActivityNavigator.startActivity(context, InvitationRecordActivity::class.java)
    }

    private fun loginHandle(context: Context): Boolean {
        return if (!UserUtils.isLogin()) {
            LoginModuleNavigator.startLoginActivity(context)
            false
        } else {
            true
        }
    }

}