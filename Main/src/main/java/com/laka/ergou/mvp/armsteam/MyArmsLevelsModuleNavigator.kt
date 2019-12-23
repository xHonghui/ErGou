package com.laka.ergou.mvp.armsteam

import android.content.Context
import android.os.Bundle
import com.laka.androidlib.util.BaseActivityNavigator
import com.laka.ergou.mvp.armsteam.constant.MyArmsLevelsConstant
import com.laka.ergou.mvp.armsteam.view.activity.MyComradeArmsLowerActivity

/**
 * @Author:summer
 * @Date:2019/3/7
 * @Description:
 */
object MyArmsLevelsModuleNavigator {

    fun startMyComradeArmsLowerActivity(context: Context, id: String, mPageTyle: Int, mClassType: Int) {
        val bundle = Bundle()
        bundle.putString(MyArmsLevelsConstant.KEY_MY_ARMS_ID, id)
        bundle.putInt(MyArmsLevelsConstant.KEY_MY_ARMS_PAGE_TYPE, mPageTyle)
        bundle.putInt(MyArmsLevelsConstant.KEY_MY_ARMS_CLASS_TYPE, mClassType)
        BaseActivityNavigator.startActivity(context, MyComradeArmsLowerActivity::class.java, bundle)
    }


}