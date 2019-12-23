package com.laka.ergou.mvp.user.presenter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.androidlib.util.rx.exception.BaseException
import com.laka.ergou.mvp.invitationrecord.constract.InvitationRecordConstract
import com.laka.ergou.mvp.user.constant.UserConstant
import com.laka.ergou.mvp.user.contract.IInvitationPlaybillConstract
import com.laka.ergou.mvp.user.model.responsitory.InvitationPlaybillModel

/**
 * @Author:summer
 * @Date:2019/6/4
 * @Description:
 */
class InvitationPlaybillPresenter : IInvitationPlaybillConstract.IInvitationPlaybillPresenter {

    private lateinit var mView: IInvitationPlaybillConstract.IInvitationPlaybillView
    private var mModel: IInvitationPlaybillConstract.IInvitationPlaybillModel = InvitationPlaybillModel()

    override fun setView(view: IInvitationPlaybillConstract.IInvitationPlaybillView) {
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

    override fun onLoadSharePosterData(postId: Int) {
        val params = HashMap<String, String>()
        if (postId != -1) { //有postId，则抽奖入口进入
            params[UserConstant.POST_ID] = "$postId"
        }
        mModel.onLoadSharePosterData(params, object : ResponseCallBack<ArrayList<String>> {
            override fun onSuccess(list: ArrayList<String>) {
                mView.onLoadSharePosterDataSuccess(list)
            }

            override fun onFail(e: BaseException?) {
                mView.onLoadSharePosterDataFail("${e?.errorMsg}")
            }
        })
    }


}