package com.laka.ergou.mvp.login.contract

import com.laka.androidlib.mvp.IBaseLoadingView
import com.laka.androidlib.mvp.IBaseModel
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.ergou.mvp.login.model.bean.AgentSimpleInfo
import org.json.JSONObject

/**
 * @Author:summer
 * @Date:2019/3/9
 * @Description:绑定邀请码
 */
interface IBindInvitationCodeConstract {

    interface IBindInvitationCodeView : IBaseLoadingView<AgentSimpleInfo> {
        fun onBindSuperAgentSuccess()
        fun onBindSuperAgentFail(msg:String)
        fun onLoadUserAgentInfoSuccess(agentSimpleInfo: AgentSimpleInfo)
    }

    interface IBindInvitationCodePresenter : IBasePresenter<IBindInvitationCodeView> {
        fun onLoadUserAgentInfo(agentCode: String)
        fun onBindSuperAgent(agentCode: String)
    }

    interface IBindInvitationCodeModel : IBaseModel<IBindInvitationCodeView> {
        fun onLoadUserAgentInfo(params: HashMap<String, String>, callBack: ResponseCallBack<AgentSimpleInfo>)
        fun onBindSuperAgent(params: HashMap<String, String>, callBack: ResponseCallBack<JSONObject>)
    }
}