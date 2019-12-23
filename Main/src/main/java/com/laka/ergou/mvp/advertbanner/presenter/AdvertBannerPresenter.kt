package com.laka.ergou.mvp.advertbanner.presenter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.androidlib.util.rx.exception.BaseException
import com.laka.ergou.mvp.advertbanner.model.bean.AdvertBannerBean
import com.laka.ergou.mvp.advertbanner.model.respositroy.AdvertBannerModel
import com.laka.ergou.mvp.main.contract.IAdvertBannerConstract

/**
 * @Author:summer
 * @Date:2019/7/27
 * @Description:
 */
class AdvertBannerPresenter : IAdvertBannerConstract.IAdvertBannerPresenter {

    private var mModel: IAdvertBannerConstract.IAdvertBannerModel = AdvertBannerModel()
    private lateinit var mView: IAdvertBannerConstract.IAdvertBannerView

    override fun setView(view: IAdvertBannerConstract.IAdvertBannerView) {
        this.mView = view
        mModel.setView(view)
    }

    override fun onViewCreate() {

    }

    override fun onViewDestroy() {
        this.mModel.onViewDestory()
    }

    override fun onLifeCycleChange(owner: LifecycleOwner, event: Lifecycle.Event) {

    }

    override fun onLoadAdvertBannerData(classId: String) {
        val params = HashMap<String, String>()
        params["class_id"] = classId
        mModel.onLoadAdvertBannerData(params, object : ResponseCallBack<ArrayList<AdvertBannerBean>> {
            override fun onSuccess(t: ArrayList<AdvertBannerBean>) {
                mView.onLoadAdvertBannerDataSuccess(t)
            }

            override fun onFail(e: BaseException?) {
                mView.onLoadAdvertBannerDataFail("${e?.errorMsg}")
            }
        })
    }
}