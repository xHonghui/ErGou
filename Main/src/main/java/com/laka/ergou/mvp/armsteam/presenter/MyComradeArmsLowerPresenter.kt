package com.laka.ergou.mvp.armsteam.presenter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.androidlib.util.rx.constant.RequestCommonCode
import com.laka.androidlib.util.rx.exception.BaseException
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.androidlib.widget.refresh.BaseListBean
import com.laka.ergou.mvp.armsteam.constant.MyArmsLevelsConstant
import com.laka.ergou.mvp.armsteam.contract.MyComradeArmsLowerConstract
import com.laka.ergou.mvp.armsteam.model.bean.MyArmsLevelsBean
import com.laka.ergou.mvp.armsteam.model.bean.MyArmsResponse
import com.laka.ergou.mvp.armsteam.model.repository.MyComradeArmsLowerModel

/**
 * @Author:summer
 * @Date:2019/5/24
 * @Description:
 */
class MyComradeArmsLowerPresenter : MyComradeArmsLowerConstract.IMyComradeArmsLowerPresenter {

    private lateinit var mView: MyComradeArmsLowerConstract.IMyComradeArmsLowerView
    private var mModel: MyComradeArmsLowerConstract.IMyComradeArmsLowerModel = MyComradeArmsLowerModel()

    override fun setView(view: MyComradeArmsLowerConstract.IMyComradeArmsLowerView) {
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

    override fun onLoadComradeArmsLowerData(id: String, page: Int, pageType: Int, classType: Int) {
        val params = HashMap<String, String>()
        params[MyArmsLevelsConstant.KEY_SUB_USER_ID] = id
        params[MyArmsLevelsConstant.ARMS_LEVELS_TYPE] = "$classType"
        params[MyArmsLevelsConstant.TYPE_LOWER_PAGE] = "$pageType"
        params[MyArmsLevelsConstant.PAGE_KEY] = "$page"
        mModel.onLoadComradeArmsLowerData(params, object : ResponseCallBack<MyArmsResponse> {
            override fun onSuccess(t: MyArmsResponse) {
                mView.onLoadMyComradeArmsLowerDataSuccess(object : BaseListBean<MyArmsLevelsBean>() {
                    override fun getList(): MutableList<MyArmsLevelsBean> {
                        return t.data
                    }

                    override fun getPageTotalCount(): Int {
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
                mView.onLoadMyComradeArmsLowerDataFaild("${e?.errorMsg}", page)
            }
        })
    }
}