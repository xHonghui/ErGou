package com.laka.ergou.mvp.armsteam.view.activity

import com.laka.androidlib.base.activity.BaseMvpActivity
import com.laka.androidlib.features.login.OnRequestListener
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.androidlib.widget.refresh.BaseListBean
import com.laka.androidlib.widget.refresh.OnResultListener
import com.laka.ergou.R
import com.laka.ergou.mvp.armsteam.constant.MyArmsLevelsConstant
import com.laka.ergou.mvp.armsteam.contract.MyComradeArmsLowerConstract
import com.laka.ergou.mvp.armsteam.model.bean.MyArmsLevelsBean
import com.laka.ergou.mvp.armsteam.presenter.MyComradeArmsLowerPresenter
import com.laka.ergou.mvp.armsteam.view.adapter.MyComradeArmsLowerAdapter
import com.laka.ergou.mvp.login.LoginModuleNavigator
import kotlinx.android.synthetic.main.activity_my_comrade_arms_lower.*

/**
 * @Author:summer
 * @Date:2019/5/24
 * @Description:我的战友的战友
 */
class MyComradeArmsLowerActivity : BaseMvpActivity<MyArmsLevelsBean>(), OnRequestListener<OnResultListener>, MyComradeArmsLowerConstract.IMyComradeArmsLowerView {

    private lateinit var mPresenter: MyComradeArmsLowerPresenter
    private var mResultListener: OnResultListener? = null
    private var mId: String = ""
    private var mPageType: Int = 0
    private var mClassType: Int = 0
    private var mDataList: ArrayList<MyArmsLevelsBean> = ArrayList()

    override fun createPresenter(): IBasePresenter<*> {
        mPresenter = MyComradeArmsLowerPresenter()
        return mPresenter
    }

    override fun setContentView(): Int {
        return R.layout.activity_my_comrade_arms_lower
    }

    override fun initIntent() {
        mId = intent.getStringExtra(MyArmsLevelsConstant.KEY_MY_ARMS_ID)
        mPageType = intent.getIntExtra(MyArmsLevelsConstant.KEY_MY_ARMS_PAGE_TYPE, -1)
        mClassType = intent.getIntExtra(MyArmsLevelsConstant.KEY_MY_ARMS_CLASS_TYPE, -1)
    }

    override fun initViews() {
        title_bar?.setLeftIcon(R.drawable.nav_btn_back_n)
                ?.setTitleTextColor(R.color.black)
                ?.setTitle(getString(R.string.my_comrade_arms_lower_title))
        rv_list.setOnRequestListener(this)
        rv_list.adapter = MyComradeArmsLowerAdapter(this, R.layout.item_my_lower_levels, mDataList, mPageType, mClassType)
        rv_list.refresh()
    }

    override fun initData() {

    }

    override fun initEvent() {

    }

    override fun onRequest(page: Int, resultListener: OnResultListener?): String {
        this.mResultListener = resultListener
        mPresenter.onLoadComradeArmsLowerData(mId, page, mPageType, mClassType)
        return ""
    }

    override fun onLoadMyComradeArmsLowerDataSuccess(list: BaseListBean<MyArmsLevelsBean>) {
        mResultListener?.onResponse(list)
    }

    override fun onLoadMyComradeArmsLowerDataFaild(msg: String, page: Int) {
        mResultListener?.onFailure(-1, msg)
    }

    override fun onAuthorFail() {
        if (isFinishing || isDestroyed) {
            return
        }
        LoginModuleNavigator.startLoginActivity(this)
    }

    override fun showData(data: MyArmsLevelsBean) {

    }

    override fun showErrorMsg(msg: String?) {
        ToastHelper.showToast("$msg")
    }

}