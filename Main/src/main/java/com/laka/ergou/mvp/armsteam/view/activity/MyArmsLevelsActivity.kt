package com.laka.ergou.mvp.armsteam.view.activity

import android.support.v4.view.PagerAdapter
import com.laka.androidlib.base.fragment.BaseLazyLoadFragment
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.ergou.R
import com.laka.ergou.mvp.base.view.activity.BasePagerListMvpActivity
import com.laka.ergou.mvp.base.view.adapter.BaseCustomFragmentPagerAdapter
import com.laka.ergou.mvp.armsteam.constant.MyArmsLevelsConstant
import com.laka.ergou.mvp.armsteam.view.fragment.MyArmsPageFragment
import com.laka.ergou.mvp.main.RouterNavigator
import com.laka.ergou.mvp.main.constant.HomeApiConstant
import com.laka.ergou.mvp.main.constant.HomeConstant
import kotlinx.android.synthetic.main.activity_base_pager_list.*

/**
 * @Author:summer
 * @Date:2019/3/7
 * @Description:我的战队
 */
class MyArmsLevelsActivity : BasePagerListMvpActivity<String>() {

    private var mFragList: ArrayList<BaseLazyLoadFragment> = arrayListOf(MyArmsPageFragment.getInstance(MyArmsLevelsConstant.TYPE_APP), MyArmsPageFragment.getInstance(MyArmsLevelsConstant.TYPE_WECHAT))

    override fun showData(data: String) {

    }

    override fun showErrorMsg(msg: String?) {
    }

    override fun createPresenter(): IBasePresenter<*>? {
        return null
    }

    override fun getPagerAdapter(): PagerAdapter {
        return BaseCustomFragmentPagerAdapter(supportFragmentManager, mFragList)
    }

    override fun initIntent() {
    }

    override fun initViews() {
        super.initViews()
        title_bar.setTitle(getString(R.string.my_lower_levels)).setRightText("战队说明").setOnRightClickListener { openWarteamInfoActivity() }
        setLeftTabText(getString(R.string.app_lower_levels))
        setRightTabText(getString(R.string.wechat_lower_levels))
    }

    private fun openWarteamInfoActivity() {
        val target = RouterNavigator.bannerRouterReflectMap[5]
        val params = HashMap<String,String>()
        params[HomeConstant.WEB_TITLE] = "战队说明"
        params[HomeConstant.WEB_URL] = HomeApiConstant.URL_WARTEAM_INFO
        RouterNavigator.handleAppInternalNavigator(this,"$target",params)
    }

    override fun initData() {
    }

    override fun initEvent() {
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun onMyLowerLevelsEvent(event: MyArmsLevelsEvent) {
//        when (event.eventType) {
//            MyArmsLevelsConstant.TYPE_APP -> {
//                setLeftTabText("APP战队（${event.data}）")
//            }
//            MyArmsLevelsConstant.TYPE_WECHAT -> {
//                setRightTabText("微信战队（${event.data}）")
//            }
//        }
//    }


}