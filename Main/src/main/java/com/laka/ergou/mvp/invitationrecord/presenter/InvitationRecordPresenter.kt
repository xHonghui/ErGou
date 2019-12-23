package com.laka.ergou.mvp.invitationrecord.presenter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import com.laka.androidlib.eventbus.EventBusManager
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.androidlib.util.rx.constant.RequestCommonCode
import com.laka.androidlib.util.rx.exception.BaseException
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.androidlib.widget.refresh.BaseListBean
import com.laka.ergou.mvp.invitationrecord.constant.InvitationRecordConstant
import com.laka.ergou.mvp.invitationrecord.constract.InvitationRecordConstract
import com.laka.ergou.mvp.invitationrecord.model.bean.InvitationRecord
import com.laka.ergou.mvp.invitationrecord.model.bean.InvitationRecordResponse
import com.laka.ergou.mvp.invitationrecord.model.event.InvitationRecordEvent
import com.laka.ergou.mvp.invitationrecord.model.repository.InvitationRecordModel
import com.laka.ergou.mvp.armsteam.constant.MyArmsLevelsConstant

/**
 * @Author:summer
 * @Date:2019/3/13
 * @Description:
 */
class InvitationRecordPresenter : InvitationRecordConstract.IInvitationRecordPresenter {

    private lateinit var mView: InvitationRecordConstract.IInvitationRecordView
    private var mModel: InvitationRecordConstract.IInvitationRecordModel = InvitationRecordModel()

    override fun setView(view: InvitationRecordConstract.IInvitationRecordView) {
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

    override fun onLoadInvitationRecord(type: Int, sourceType: Int, page: Int) {
        val params = HashMap<String, String>()
        params[InvitationRecordConstant.INVITATION_TYPE] = "$type"
        params[InvitationRecordConstant.SOURCE_TYPE] = "$sourceType"
        params[InvitationRecordConstant.PAGE_NUMBER] = "$page"
        params[InvitationRecordConstant.PAGE_SIZE_KEY] = InvitationRecordConstant.PAGE_SIZE
        mModel.onLoadInvitationRecord(params, object : ResponseCallBack<InvitationRecordResponse> {
            override fun onSuccess(t: InvitationRecordResponse) {
                //EventBusManager.postEvent(InvitationRecordEvent(type, t.total.toString()))
                mView.onLoadInvitationRecordSuccess(object : BaseListBean<InvitationRecord>() {
                    override fun getList(): MutableList<InvitationRecord> {
                        return t.records
                    }

                    override fun getPageTotalCount(): Int {
                        LogUtils.error("t.data.total：" + t.toString())
                        val div = t.total / MyArmsLevelsConstant.PAGE_SIZE.toInt()
                        val mod = t.total % MyArmsLevelsConstant.PAGE_SIZE.toInt()
                        return if (mod == 0) div else div + 1
                    }
                })
            }

            override fun onFail(e: BaseException?) {
                if (e?.code == RequestCommonCode.LK_WRONG_USER_TOKEN
                        || e?.code == RequestCommonCode.LK_NOT_LOGIN) {
                    mView.onAuthorFail()
                }
                ToastHelper.showCenterToast(e?.errorMsg)
                mView.onLoadError(page)
            }
        })
    }


}