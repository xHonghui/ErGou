package com.laka.ergou.mvp.test.constract

import com.laka.androidlib.mvp.IBaseLoadingView
import com.laka.androidlib.mvp.IBaseModel
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.ergou.mvp.shop.model.bean.ImageDetail

/**
 * @Author:summer
 * @Date:2018/12/20
 * @Description:
 */
interface ITestContract {

    interface ITestView : IBaseLoadingView<ArrayList<ImageDetail>> {
        fun onLoadShopDetailImage(params: HashMap<String, String>, callBack: ResponseCallBack<ArrayList<ImageDetail>>)
    }

    interface ITestPresenter : IBasePresenter<ITestView> {
        fun onLoadShopDetailImage()
    }

    interface ITestModel : IBaseModel<ITestView> {
        fun onLoadShopDetailImage(params: HashMap<String, String>, callBack: ResponseCallBack<ArrayList<ImageDetail>>)
    }

}