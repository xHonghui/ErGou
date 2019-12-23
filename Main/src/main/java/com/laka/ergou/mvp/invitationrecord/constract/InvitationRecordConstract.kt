package com.laka.ergou.mvp.invitationrecord.constract

import com.laka.androidlib.mvp.IBaseLoadingView
import com.laka.androidlib.mvp.IBaseModel
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.androidlib.widget.refresh.BaseListBean
import com.laka.ergou.mvp.invitationrecord.model.bean.InvitationRecord
import com.laka.ergou.mvp.invitationrecord.model.bean.InvitationRecordResponse

/**
 * @Author:summer
 * @Date:2019/3/13
 * @Description:
 */
interface InvitationRecordConstract {

    interface IInvitationRecordView : IBaseLoadingView<InvitationRecordResponse> {
        fun onLoadInvitationRecordSuccess(list: BaseListBean<InvitationRecord>)
        fun onLoadError(page:Int)
        fun onAuthorFail()
    }

    interface IInvitationRecordPresenter: IBasePresenter<IInvitationRecordView> {
        fun onLoadInvitationRecord(type: Int,sourceType:Int, page: Int)
    }

    interface IInvitationRecordModel : IBaseModel<IInvitationRecordView> {
        fun onLoadInvitationRecord(params: HashMap<String, String>,callBack: ResponseCallBack<InvitationRecordResponse>)
    }

}