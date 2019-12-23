package com.laka.ergou.mvp.message.constract

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.laka.androidlib.mvp.IBaseLoadingView
import com.laka.androidlib.mvp.IBaseModel
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.androidlib.widget.refresh.BaseListBean
import com.laka.ergou.mvp.message.model.bean.MessageResponse
import com.laka.ergou.mvp.message.model.bean.Msg

/**
 * @Author:summer
 * @Date:2019/3/8
 * @Description:
 */
interface MessageConstract {

    interface IMessageView : IBaseLoadingView<MessageResponse> {
        fun onLoadMessageSuccess(result:BaseListBean<MultiItemEntity>)
        fun onLoadError(page:Int)
        fun onAuthorFail()
    }

    interface IMessagePresenter : IBasePresenter<IMessageView> {
        fun onLoadMessage(page: Int,type:Int)
    }

    interface IMessageModel : IBaseModel<IMessageView> {
        fun onLoadMessage(params: HashMap<String, String>, callBack: ResponseCallBack<MessageResponse>)
    }
}