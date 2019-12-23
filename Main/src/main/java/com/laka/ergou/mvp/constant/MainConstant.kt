package com.laka.ergou.mvp.constant

import android.support.annotation.IntDef

/**
 * @Author:Rayman
 * @Date:2018/12/21
 * @Description:主程常量类
 */
object MainConstant {

    /**
     * description:主页Fragment类型
     **/
    const val HOMEPAGE_FRAGMENT_INDEX = "HOMEPAGE_FRAGMENT_INDEX"
    const val HOMEPAGE_SHOPPING = 0
    const val HOMEPAGE_MINE = 1
    const val HOMEPAGE_CHAT = 3
    const val HOMEPAGE_CIRCLE = 4
    const val HOMEPAGE_ADVERT = 2

    @IntDef(HOMEPAGE_SHOPPING, HOMEPAGE_MINE, HOMEPAGE_CHAT,HOMEPAGE_ADVERT)
    annotation class HomePageFragmentType

    // Setting Sp中购小二信息KeyWord
    const val IS_OPEN_GO_XIAO_ER: String = "is_open_gou_xiao_er"
}