package com.laka.ergou.mvp.user.view.activity

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import com.laka.androidlib.base.activity.BaseMvpActivity
import com.laka.androidlib.eventbus.EventBusManager
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.ResourceUtils
import com.laka.androidlib.util.SPHelper
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.ergou.R
import com.laka.ergou.mvp.login.LoginModuleNavigator
import com.laka.ergou.mvp.login.constant.LoginConstant
import com.laka.ergou.mvp.login.model.event.UserEvent
import com.laka.ergou.mvp.login.model.bean.UserInfoBean
import com.laka.ergou.mvp.user.UserUtils.UserUtils
import com.laka.ergou.mvp.user.constant.UserConstant
import com.laka.ergou.mvp.user.contract.IUserNickEditConstract
import com.laka.ergou.mvp.user.model.bean.CommonData
import com.laka.ergou.mvp.user.presenter.UserNickEditPresenter
import kotlinx.android.synthetic.main.activity_nick_edit.*

/**
 * @Author:summer
 * @Date:2019/1/11
 * @Description: 用户昵称编辑
 */
class UserNickEditActivity : BaseMvpActivity<CommonData>(), IUserNickEditConstract.IUserNickEditView, TextWatcher {

    private lateinit var mPresenter: IUserNickEditConstract.IUserNickEditPresenter

    override fun showErrorMsg(msg: String?) {
        ToastHelper.showCenterToast(msg)
    }

    override fun createPresenter(): IBasePresenter<*> {
        mPresenter = UserNickEditPresenter()
        return mPresenter
    }

    override fun setContentView(): Int {
        return R.layout.activity_nick_edit
    }

    override fun initIntent() {
    }

    override fun initViews() {
        title_bar.showDivider(false)
                .setTitle("昵称")
                .setLeftIcon(R.drawable.seletor_nav_btn_back)
                .setTitleTextColor(R.color.black)
                .setTitleTextSize(16)
        val userInfoBean = SPHelper.getObject(LoginConstant.USER_INFO_FILENAME, LoginConstant.USER_LOGIN_INFO, UserInfoBean::class.java)
        et_nickname.setText(userInfoBean?.getNickName())
        et_nickname.setSelection(et_nickname.text.toString().length)
    }

    override fun initData() {
    }

    override fun initEvent() {
        sb_sure.setOnClickListener {
            onSave()
        }
        iv_delete.setOnClickListener {
            if (et_nickname.text.toString().isNotEmpty()) {
                et_nickname.setText("")
            }
        }
        et_nickname.addTextChangedListener(this)
    }

    override fun afterTextChanged(s: Editable?) {

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (TextUtils.isEmpty(et_nickname.text.toString())) {
            iv_delete.visibility = View.GONE
        } else {
            iv_delete.visibility = View.VISIBLE
        }
    }

    private fun onSave() {
        val nickName = et_nickname.text.toString()
        if (TextUtils.isEmpty(nickName)) {
            ToastHelper.showCenterToast("请输入昵称")
            return
        }
        if (nickName.length > UserConstant.NICKNAME_EDIT_LENGTH) {
            ToastHelper.showCenterToast("昵称必须在${UserConstant.NICKNAME_EDIT_LENGTH}个字符内")
            return
        }
        mPresenter.onUserNickEdit(nickName)
    }

    override fun showData(data: CommonData) {
    }

    override fun onUserNickEditSuccess(resultBean: CommonData) {
        ToastHelper.showCenterToast("修改昵称成功")
        UserUtils.updateUserName(resultBean.nickname)
        EventBusManager.postEvent(UserEvent(UserConstant.EDIT_USER_INFO))
        finish()
    }

    override fun onUserNickEditFail(result: String) {
        tv_tips.text = "$result"
        ToastHelper.showCenterToast(result)
    }

    override fun authorInvalid() {
        LoginModuleNavigator.startLoginActivity(this)
    }
}