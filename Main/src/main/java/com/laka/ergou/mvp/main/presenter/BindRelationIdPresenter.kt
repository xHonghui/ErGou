package com.laka.ergou.mvp.main.presenter

import android.app.Activity
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.text.TextUtils
import com.alibaba.baichuan.trade.biz.login.AlibcLogin
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.androidlib.util.rx.exception.BaseException
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.ergou.common.util.ClipBoardManagerHelper
import com.laka.ergou.mvp.main.contract.IBindRelationIdContract
import com.laka.ergou.mvp.main.model.repository.IBindRelationIdModel
import com.laka.ergou.mvp.user.UserUtils.UserUtils
import com.laka.ergou.mvp.user.constant.UserConstant
import com.laka.ergou.mvp.user.model.bean.RelationResponse
import com.laka.ergou.mvp.user.model.bean.UrlResponse

/**
 * @Author:summer
 * @Date:2019/8/14
 * @Description:
 */
class BindRelationIdPresenter : IBindRelationIdContract.IBindRelationIdPresenter {

    private lateinit var mView: IBindRelationIdContract.IBindRelationIdView
    private var mModel: IBindRelationIdModel = IBindRelationIdModel()

    override fun setView(view: IBindRelationIdContract.IBindRelationIdView) {
        this.mView = view
        mModel.setView(mView)
    }

    override fun onViewCreate() {

    }

    override fun onViewDestroy() {
        mModel.onViewDestory()
    }

    override fun onLifeCycleChange(owner: LifecycleOwner, event: Lifecycle.Event) {

    }

    override fun onTaoBaoAuthor(activity: Activity) {
        if (!AlibcLogin.getInstance().isLogin) {
            mView.showLoading()
            //进入淘宝授权前，清空本地粘贴板内容，防止粘贴板含有淘口令从而引起淘宝弹窗搜索弹窗
            ClipBoardManagerHelper.getInstance.clearClipBoardContentForHasTkl()
            AlibcLogin.getInstance().showLogin(object : AlibcLoginCallback {
                override fun onSuccess(p0: Int, p1: String?, p2: String?) {
                    mView.dismissLoading()
                    val userInfoBean = UserUtils.getUserInfoBean()
                    userInfoBean.session = AlibcLogin.getInstance().session
                    UserUtils.updateUserInfo(userInfoBean)
                    mView?.onTaoBaoAuthorSuccess()
                }

                override fun onFailure(p0: Int, p1: String?) {
                    mView.dismissLoading()
                    ToastHelper.showToast("授权失败 ")
                }
            })
        } else {
            mView?.onTaoBaoAuthorSuccess()
        }
    }

    override fun getUnionCodeUrl() {
        mView.showLoading()
        val params = HashMap<String, String>()
        mModel.getUnionCodeUrl(params, object : ResponseCallBack<UrlResponse> {
            override fun onSuccess(t: UrlResponse) {
                LogUtils.info("--json:" + t.toString())
                if (!TextUtils.isEmpty(t.url)) {
                    mView.getUnionCodeUrlSuccess(t.url)
                } else {
                    ToastHelper.showCenterToast("获取联盟授权url失败")
                }
            }

            override fun onFail(e: BaseException?) {
                ToastHelper.showCenterToast(e?.errorMsg)
            }
        })
    }

    override fun handleUnionCode(code: String, state: String) {
        mView.showLoading()
        val params = HashMap<String, String>()
        params[UserConstant.UNION_CODE] = "$code"
        params[UserConstant.UNION_STATE] = "$state"
        mModel.handleUnionCode(params, object : ResponseCallBack<RelationResponse> {
            override fun onSuccess(t: RelationResponse) {
                if (!TextUtils.isEmpty(t.relation_id)) {
                    UserUtils.updateRelationId(t.relation_id)
                    mView.handleUnionCodeSuccess()
                }
            }

            override fun onFail(e: BaseException?) {
                ToastHelper.showCenterToast(e?.errorMsg)
                mView.handleUnionCodeFail()
            }
        })
    }
}