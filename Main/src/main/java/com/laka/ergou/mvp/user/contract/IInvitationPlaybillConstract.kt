package com.laka.ergou.mvp.user.contract

import com.laka.androidlib.mvp.IBaseLoadingView
import com.laka.androidlib.mvp.IBaseModel
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.rx.callback.ResponseCallBack

/**
 * @Author:summer
 * @Date:2019/6/4
 * @Description:
 */
interface IInvitationPlaybillConstract {

    interface IInvitationPlaybillView : IBaseLoadingView<String> {
        fun onLoadSharePosterDataSuccess(list: ArrayList<String>)
        fun onLoadSharePosterDataFail(msg: String)
    }

    interface IInvitationPlaybillPresenter : IBasePresenter<IInvitationPlaybillView> {
        fun onLoadSharePosterData(postId:Int)
    }

    interface IInvitationPlaybillModel : IBaseModel<IInvitationPlaybillView> {
        fun onLoadSharePosterData(params: HashMap<String, String>, callBack: ResponseCallBack<ArrayList<String>>)
    }
}