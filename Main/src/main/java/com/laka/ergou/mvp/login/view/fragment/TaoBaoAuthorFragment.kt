package com.laka.ergou.mvp.login.view.fragment

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.laka.androidlib.base.fragment.BaseMvpFragment
import com.laka.androidlib.eventbus.EventBusManager
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.ergou.R
import com.laka.ergou.mvp.login.contract.ITaoBaoLoginContract
import com.laka.ergou.mvp.login.model.event.UserEvent
import com.laka.ergou.mvp.login.model.bean.UserInfoBean
import com.laka.ergou.mvp.login.presenter.TaoBaoAuthorPresenter
import com.laka.ergou.mvp.user.constant.UserConstant

/**
 * @Author:summer
 * @Date:2019/1/7
 * @Description: 淘宝登录 fragment
 */
class TaoBaoAuthorFragment : BaseMvpFragment<UserInfoBean>(), ITaoBaoLoginContract.ILoginView {

    private lateinit var mLoginPresenter: ITaoBaoLoginContract.ILoginPresenter
    private lateinit var mFlBack: FrameLayout
    private lateinit var mIvBack: ImageView
    private lateinit var mTextViewLogin: TextView

    override fun setContentView(): Int {
        return R.layout.fragment_login_taobao
    }

    override fun initArgumentsData(arguments: Bundle?) {

    }

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
        mIvBack = findViewById(R.id.iv_back)
        mFlBack = findViewById(R.id.fl_back)
        mTextViewLogin = findViewById(R.id.text_view_login)
    }

    override fun initData() {

    }

    override fun initEvent() {
        mTextViewLogin.setOnClickListener {
            mLoginPresenter?.onTaoBaoAuthor(activity)
        }
        mFlBack.setOnClickListener {
            activity.finish()
        }
    }

    override fun createPresenter(): IBasePresenter<*> {
        mLoginPresenter = TaoBaoAuthorPresenter()
        return mLoginPresenter
    }

    override fun onTaoBaoAuthorSuccess() {
        if (activity != null && !activity.isFinishing && !activity.isDestroyed) {
            // 更新淘宝绑定状态

            EventBusManager.postEvent(UserEvent(UserConstant.TAOBAO_AUTHOR_SUCCESS_EVENT))
            ToastHelper.showToast("授权成功 ")
            activity.finish()
        }
    }

    override fun showLoading() {
    }

    override fun dismissLoading() {
    }

    override fun showData(data: UserInfoBean) {
    }

    override fun showErrorMsg(msg: String?) {
    }
}