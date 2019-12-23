package com.laka.ergou.mvp.teamaward

import android.content.Context
import com.laka.androidlib.util.BaseActivityNavigator
import com.laka.ergou.mvp.login.LoginModuleNavigator
import com.laka.ergou.mvp.teamaward.view.activity.TeamAwardActivity

/**
 * @Author:summer
 * @Date:2019/6/28
 * @Description:
 */
object TeamAwardModelNavigator {

    fun startTeamAwardActivity(context: Context) {
        if (LoginModuleNavigator.loginHandle(context)) {
            BaseActivityNavigator.startActivity(context, TeamAwardActivity::class.java)
        }
    }

}