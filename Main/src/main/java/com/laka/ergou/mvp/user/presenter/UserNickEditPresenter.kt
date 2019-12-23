package com.laka.ergou.mvp.user.presenter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import com.laka.androidlib.util.SPHelper
import com.laka.androidlib.net.response.BaseResponse
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.androidlib.util.rx.constant.RequestCommonCode
import com.laka.androidlib.util.rx.exception.BaseException
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.ergou.common.util.rx.RxSubscriber
import com.laka.ergou.mvp.login.constant.LoginConstant
import com.laka.ergou.mvp.login.model.bean.UserInfoBean
import com.laka.ergou.mvp.user.constant.UserApiConstant
import com.laka.ergou.mvp.user.constant.UserConstant
import com.laka.ergou.mvp.user.contract.IUserNickEditConstract
import com.laka.ergou.mvp.user.model.bean.CommonData
import com.laka.ergou.mvp.user.model.responsitory.UserNickEditModel
import io.reactivex.disposables.Disposable

/**
 * @Author:summer
 * @Date:2019/1/11
 * @Description:
 */
class UserNickEditPresenter : IUserNickEditConstract.IUserNickEditPresenter {

    private var mDisposableList = ArrayList<Disposable>()

    private lateinit var mView: IUserNickEditConstract.IUserNickEditView
    private val mModel: IUserNickEditConstract.IUserNickEditModel = UserNickEditModel()

    override fun onViewCreate() {
    }

    override fun onViewDestroy() {
        mDisposableList.forEach {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
        mDisposableList.clear()
        mModel.onViewDestory()
    }

    override fun onLifeCycleChange(owner: LifecycleOwner, event: Lifecycle.Event) {
    }

    override fun setView(view: IUserNickEditConstract.IUserNickEditView) {
        this.mView = view
        mModel.setView(view)
    }

    override fun onUserNickEdit(nickname: String) {
        mView.showLoading()
        val params = HashMap<String, String?>()
        params.put(UserConstant.NICKNAME, nickname)
        mModel.onUserNickEdit(params, object : ResponseCallBack<CommonData> {
            override fun onSuccess(t: CommonData) {
                mView.onUserNickEditSuccess(t)
            }

            override fun onFail(e: BaseException?) {
                if (e?.code == RequestCommonCode.LK_NOT_LOGIN
                        || e?.code == RequestCommonCode.LK_WRONG_USER_TOKEN) {
                    mView.authorInvalid()
                }
                mView.onUserNickEditFail("${e?.errorMsg}")
            }
        })
    }
}