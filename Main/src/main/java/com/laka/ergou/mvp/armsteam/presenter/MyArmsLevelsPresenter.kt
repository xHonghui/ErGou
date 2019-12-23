package com.laka.ergou.mvp.armsteam.presenter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.androidlib.util.rx.constant.RequestCommonCode
import com.laka.androidlib.util.rx.exception.BaseException
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.androidlib.widget.refresh.BaseListBean
import com.laka.ergou.mvp.armsteam.constant.MyArmsLevelsConstant
import com.laka.ergou.mvp.armsteam.contract.MyArmsLevelsContract
import com.laka.ergou.mvp.armsteam.model.bean.MyArmsLevelsBean
import com.laka.ergou.mvp.armsteam.model.bean.MyArmsResponse
import com.laka.ergou.mvp.armsteam.model.repository.MyArmsLevelsModel

/**
 * @Author:summer
 * @Date:2019/3/8
 * @Description:
 */
class MyArmsLevelsPresenter : MyArmsLevelsContract.IMyLowerLevelsPresenter {

    private lateinit var mView: MyArmsLevelsContract.IMyLowerLevelsView
    private var mModel: MyArmsLevelsContract.IMyLowerLevelsModel = MyArmsLevelsModel()

    override fun onViewCreate() {

    }

    override fun onLifeCycleChange(owner: LifecycleOwner, event: Lifecycle.Event) {

    }

    override fun setView(view: MyArmsLevelsContract.IMyLowerLevelsView) {
        this.mView = view
        mModel.setView(view)
    }

    override fun onViewDestroy() {
        mModel.onViewDestory()
    }

    //===================================== 上级接口实现 ===========================================
    override fun onLoadMyArmsLevelsData(page: Int, type: Int, comradeArmsLevelsType: Int) {
        val params = HashMap<String, String>()
        params[MyArmsLevelsConstant.TYPE_LOWER_PAGE] = "$type"
        params[MyArmsLevelsConstant.ARMS_LEVELS_TYPE] = "$comradeArmsLevelsType"
        params[MyArmsLevelsConstant.PAGE_SIZE_KEY] = MyArmsLevelsConstant.PAGE_SIZE
        params[MyArmsLevelsConstant.PAGE_KEY] = "$page"
        mModel.onLoadMyArmsLevelsData(params, object : ResponseCallBack<MyArmsResponse> {
            override fun onSuccess(t: MyArmsResponse) {
                mView.onLoadMyArmsLevelsDataSuccess(object : BaseListBean<MyArmsLevelsBean>() {
                    override fun getList(): MutableList<MyArmsLevelsBean> {
                        return t.data
                    }

                    override fun getPageTotalCount(): Int {
                        LogUtils.error("t.data.total：" + t.toString())
                        val div = t.total / MyArmsLevelsConstant.PAGE_SIZE.toInt()
                        val mod = t.total % MyArmsLevelsConstant.PAGE_SIZE.toInt()
                        return if (mod == 0) div else div + 1
                    }
                }, t.total)
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