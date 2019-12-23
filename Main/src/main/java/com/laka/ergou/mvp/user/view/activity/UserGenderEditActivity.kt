package com.laka.ergou.mvp.user.view.activity

import android.view.View
import com.laka.androidlib.base.activity.BaseMvpActivity
import com.laka.androidlib.eventbus.EventBusManager
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.SPHelper
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.ergou.R
import com.laka.ergou.mvp.login.LoginModuleNavigator
import com.laka.ergou.mvp.login.constant.LoginConstant
import com.laka.ergou.mvp.login.model.event.UserEvent
import com.laka.ergou.mvp.login.model.bean.UserInfoBean
import com.laka.ergou.mvp.user.UserUtils.UserUtils
import com.laka.ergou.mvp.user.constant.UserConstant
import com.laka.ergou.mvp.user.contract.IUserGenderEdit
import com.laka.ergou.mvp.user.model.bean.CommonData
import com.laka.ergou.mvp.user.presenter.UserGenderEditPresenter
import kotlinx.android.synthetic.main.activity_user_gender_edit.*

/**
 * @Author:summer
 * @Date:2019/1/11
 * @Description: 用户性别编辑
 */
class UserGenderEditActivity : BaseMvpActivity<CommonData>(), IUserGenderEdit.IUserGenderEditView {

    private lateinit var mPresenter: IUserGenderEdit.IUserGenderEditPresenter
    private var mGender: String = ""

    override fun showData(data: CommonData) {

    }

    override fun showErrorMsg(msg: String?) {
        ToastHelper.showCenterToast(msg)
    }

    override fun createPresenter(): IBasePresenter<*> {
        mPresenter = UserGenderEditPresenter()
        return mPresenter
    }

    override fun setContentView(): Int {
        return R.layout.activity_user_gender_edit
    }

    override fun initIntent() {
    }

    override fun initViews() {
        title_bar.setTitle("性别")
                .showDivider(false)
                .setLeftIcon(R.drawable.seletor_nav_btn_back)
                .setTitleTextColor(R.color.black)
                .setTitleTextSize(16)
        val userInfoBean = SPHelper.getObject(LoginConstant.USER_INFO_FILENAME, LoginConstant.USER_LOGIN_INFO, UserInfoBean::class.java)
        mGender = "${userInfoBean?.getGender()}"
        if ("1" == userInfoBean?.getGender()) { // 男
            iv_man_select_icon.visibility = View.VISIBLE
            iv_women_select_icon.visibility = View.GONE
        } else if ("2" == userInfoBean?.getGender()) { // 女
            iv_man_select_icon.visibility = View.GONE
            iv_women_select_icon.visibility = View.VISIBLE
        } else { // 未知
            iv_man_select_icon.visibility = View.GONE
            iv_women_select_icon.visibility = View.GONE
        }
    }

    override fun initData() {
    }

    override fun initEvent() {
        cl_gender_man.setOnClickListener {
            mGender = "1"
            iv_man_select_icon.visibility = View.VISIBLE
            iv_women_select_icon.visibility = View.GONE
        }
        cl_gender_women.setOnClickListener {
            mGender = "2"
            iv_man_select_icon.visibility = View.GONE
            iv_women_select_icon.visibility = View.VISIBLE
        }
        sb_sure.setOnClickListener {
            onEditGender(mGender)
        }
    }

    private fun onEditGender(gender: String) {
        mPresenter.onUserGenderEdit(gender)
    }


    override fun onUserGenderEditSuccess(resultBean: CommonData) {
        ToastHelper.showCenterToast("修改成功")
        UserUtils.updateUserGender(resultBean?.gender)
        EventBusManager.postEvent(UserEvent(UserConstant.EDIT_USER_INFO))
        finish()
    }

    override fun authorInvalid() {
        LoginModuleNavigator.startLoginActivity(this)
    }

}