package com.laka.ergou.mvp.main.contract

import com.laka.androidlib.mvp.IBaseLoadingView
import com.laka.androidlib.mvp.IBaseModel
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.ergou.mvp.advertbanner.model.bean.AdvertBannerBean

/**
 * @Author:summer
 * @Date:2019/7/27
 * @Description:广告banner协议
 */
interface IAdvertBannerConstract {

    interface IAdvertBannerView : IBaseLoadingView<ArrayList<AdvertBannerBean>> {
        fun onLoadAdvertBannerDataSuccess(response: ArrayList<AdvertBannerBean>)
        fun onLoadAdvertBannerDataFail(msg:String)
    }

    interface IAdvertBannerPresenter : IBasePresenter<IAdvertBannerView> {
        fun onLoadAdvertBannerData(classId:String)
    }

    interface IAdvertBannerModel : IBaseModel<IAdvertBannerView> {
        fun onLoadAdvertBannerData(params: HashMap<String, String>, callBack: ResponseCallBack<ArrayList<AdvertBannerBean>>)
    }

}