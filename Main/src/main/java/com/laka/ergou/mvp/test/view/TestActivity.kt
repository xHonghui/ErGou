package com.laka.ergou.mvp.test.view

import android.view.View
import com.laka.androidlib.base.activity.BaseMvpActivity
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.LogUtils
import com.laka.ergou.R
import com.laka.ergou.mvp.advertbanner.model.bean.AdvertBannerBean
import com.laka.ergou.mvp.advertbanner.presenter.AdvertBannerPresenter
import com.laka.ergou.mvp.main.contract.IAdvertBannerConstract
import kotlinx.android.synthetic.main.activity_test.*


/**
 * @Author:summer
 * @Date:2019/1/17
 * @Description:测试 Activity
 */
class TestActivity : BaseMvpActivity<ArrayList<AdvertBannerBean>>(), IAdvertBannerConstract.IAdvertBannerView, View.OnClickListener {

    private lateinit var mPresenter: IAdvertBannerConstract.IAdvertBannerPresenter

    override fun showErrorMsg(msg: String?) {
    }

    override fun createPresenter(): IBasePresenter<*> {
        mPresenter = AdvertBannerPresenter()
        return mPresenter
    }

    override fun setContentView(): Int {
        return R.layout.activity_test
    }

    override fun initIntent() {
        LogUtils.info("initIntent----测试")
    }

    fun switchTab(view: View) {
        plbv.switchTab()
    }

    fun changeText(view: View) {
        plbv.setLeftTabText("${System.currentTimeMillis()}")
    }

    override fun initViews() {

    }

    override fun initData() {

    }

    override fun initEvent() {

    }

    override fun onClick(v: View?) {

    }

    override fun showData(data: ArrayList<AdvertBannerBean>) {

    }

    override fun onLoadAdvertBannerDataSuccess(response: ArrayList<AdvertBannerBean>) {

    }

    override fun onLoadAdvertBannerDataFail(msg: String) {

    }
}