package com.laka.ergou.mvp.launch

import android.content.Context
import android.os.Bundle
import com.laka.androidlib.util.BaseActivityNavigator
import com.laka.ergou.mvp.launch.constant.LaunchConstant
import com.laka.ergou.mvp.main.view.activity.HomeActivity

/**
 * @Author:Rayman
 * @Date:2018/12/19
 * @Description:
 */
object LaunchNavigator {

    fun startMainActivity(activity: Context) {
        BaseActivityNavigator.startActivity(activity, HomeActivity::class.java)
    }
    fun startMainActivity(activity: Context,isShow:Boolean) {
        val bundle = Bundle()
        bundle.putBoolean(LaunchConstant.ADVERT_IS_SHOW, isShow)
        BaseActivityNavigator.startActivity(activity, HomeActivity::class.java,bundle)
    }
}