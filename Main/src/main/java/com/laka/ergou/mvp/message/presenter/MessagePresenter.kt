package com.laka.ergou.mvp.message.presenter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.androidlib.util.rx.constant.RequestCommonCode
import com.laka.androidlib.util.rx.exception.BaseException
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.androidlib.widget.refresh.BaseListBean
import com.laka.ergou.mvp.message.constant.MessageConstant
import com.laka.ergou.mvp.message.constract.MessageConstract
import com.laka.ergou.mvp.message.model.bean.MessageResponse
import com.laka.ergou.mvp.message.model.bean.Msg
import com.laka.ergou.mvp.message.model.respository.MessageModel
import com.laka.ergou.mvp.user.UserUtils.UserUtils

/**
 * @Author:summer
 * @Date:2019/3/8
 * @Description:
 */
class MessagePresenter : MessageConstract.IMessagePresenter {

    private lateinit var mView: MessageConstract.IMessageView
    private var mModel: MessageConstract.IMessageModel = MessageModel()

    override fun setView(view: MessageConstract.IMessageView) {
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

    override fun onLoadMessage(page: Int, type: Int) {
        val params = HashMap<String, String>()
        params[MessageConstant.MESSAGE_TYPE] = "$type"
        params[MessageConstant.MESSAGE_PAGE_KEY] = "$page"
        params[MessageConstant.MESSAGE_PAGE_SIZE_KEY] = MessageConstant.PAGE_SIZE
        mModel.onLoadMessage(params, object : ResponseCallBack<MessageResponse> {
            override fun onSuccess(t: MessageResponse) {
                mView.onLoadMessageSuccess(object : BaseListBean<MultiItemEntity>() {
                    override fun getList(): MutableList<Msg> {
                        return t.msgs
                    }

                    override fun getPageTotalCount(): Int {
                        val div = t.total / MessageConstant.PAGE_SIZE.toInt()
                        val mod = t.total % MessageConstant.PAGE_SIZE.toInt()
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