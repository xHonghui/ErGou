package com.laka.ergou.mvp.login.presenter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.androidlib.util.rx.exception.BaseException
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.ergou.mvp.login.constant.LoginConstant
import com.laka.ergou.mvp.login.contract.IBindInvitationCodeConstract
import com.laka.ergou.mvp.login.model.bean.AgentSimpleInfo
import com.laka.ergou.mvp.login.model.repository.BindInvitationCodeModel
import com.laka.ergou.mvp.user.UserUtils.UserUtils
import org.json.JSONObject

/**
 * @Author:summer
 * @Date:2019/3/9
 * @Description:
 */
class BindInvitationCodePresenter : IBindInvitationCodeConstract.IBindInvitationCodePresenter {

    private lateinit var mView: IBindInvitationCodeConstract.IBindInvitationCodeView
    private var mModel: IBindInvitationCodeConstract.IBindInvitationCodeModel = BindInvitationCodeModel()

    override fun setView(view: IBindInvitationCodeConstract.IBindInvitationCodeView) {
        this.mView = view
        mModel.setView(view)
    }

    override fun onViewCreate() {
    }

    override fun onViewDestroy() {
        mModel.onViewDestory()
    }

    override fun onLifeCycleChange(owner: LifecycleOwner, event: Lifecycle.Event) {
    }

    override fun onBindSuperAgent(agentCode: String) {
        mView.showLoading()
        val params = HashMap<String, String>()
        params.put(LoginConstant.AGENT_CODE, agentCode)
        mModel.onBindSuperAgent(params, object : ResponseCallBack<JSONObject> {
            override fun onSuccess(t: JSONObject) {
                ToastHelper.showCenterToast("绑定成功")
                mView.dismissLoading()
                mView.onBindSuperAgentSuccess()
            }

            override fun onFail(e: BaseException?) {
                //ToastHelper.showCenterToast("${e?.errorMsg}")
                mView.onBindSuperAgentFail("${e?.errorMsg}")
            }
        })
    }

    override fun onLoadUserAgentInfo(agentCode: String) {
        mView.showLoading()
        val params = HashMap<String, String>()
        params.put(LoginConstant.AGENT_CODE, agentCode)
        mModel.onLoadUserAgentInfo(params, object : ResponseCallBack<AgentSimpleInfo> {
            override fun onSuccess(t: AgentSimpleInfo) {
                mView.dismissLoading()
                t.agent_code = agentCode
                mView.onLoadUserAgentInfoSuccess(t)
            }

            override fun onFail(e: BaseException?) {
                //ToastHelper.showCenterToast("${e?.errorMsg}")
                mView.onBindSuperAgentFail("${e?.errorMsg}")
            }
        })
    }


}