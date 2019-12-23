package com.laka.ergou.mvp.user.presenter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import com.laka.androidlib.net.response.BaseResponse
import com.laka.androidlib.util.SPHelper
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.androidlib.util.rx.constant.RequestCommonCode
import com.laka.androidlib.util.rx.exception.BaseException
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.ergou.common.util.rx.RxSubscriber
import com.laka.ergou.mvp.login.constant.LoginConstant
import com.laka.ergou.mvp.login.model.bean.UserInfoBean
import com.laka.ergou.mvp.user.constant.UserApiConstant
import com.laka.ergou.mvp.user.constant.UserConstant
import com.laka.ergou.mvp.user.contract.IUserGenderEdit
import com.laka.ergou.mvp.user.model.bean.CommonData
import com.laka.ergou.mvp.user.model.responsitory.UserGenderEditModel
import io.reactivex.disposables.Disposable

/**
 * @Author:summer
 * @Date:2019/1/11
 * @Description:
 */
class UserGenderEditPresenter : IUserGenderEdit.IUserGenderEditPresenter {

    private lateinit var mView: IUserGenderEdit.IUserGenderEditView
    private val mModel: IUserGenderEdit.IUserGenderEditModel = UserGenderEditModel()
    private val mDisposable = ArrayList<Disposable>()

    override fun onViewCreate() {
    }

    override fun onViewDestroy() {
        mDisposable.forEach {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
        mDisposable.clear()
        mModel.onViewDestory()
    }

    override fun onLifeCycleChange(owner: LifecycleOwner, event: Lifecycle.Event) {
    }

    override fun setView(view: IUserGenderEdit.IUserGenderEditView) {
        this.mView = view
        mModel.setView(view)
    }

    override fun onUserGenderEdit(gender: String) {
        mView.showLoading()
        val params = HashMap<String, String?>()
        params.put(UserConstant.GENDER, gender)
        mModel.onUserGenderEdit(params, object : ResponseCallBack<CommonData> {
            override fun onSuccess(t: CommonData) {
                mView.onUserGenderEditSuccess(t)
            }

            override fun onFail(e: BaseException?) {
                if (e?.code == RequestCommonCode.LK_NOT_LOGIN
                        || e?.code == RequestCommonCode.LK_WRONG_USER_TOKEN) {
                    mView.authorInvalid()
                } else {
                    ToastHelper.showCenterToast("${e?.errorMsg}")
                }
            }
        })
    }
}