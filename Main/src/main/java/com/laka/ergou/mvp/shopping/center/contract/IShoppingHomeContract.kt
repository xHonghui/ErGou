package com.laka.ergou.mvp.shopping.center.contract

import android.content.Context
import com.laka.androidlib.mvp.IBaseModel
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.mvp.IBaseView
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.ergou.mvp.shopping.center.model.bean.*
import io.reactivex.Observable
import org.json.JSONObject

/**
 * @Author:Rayman
 * @Date:2018/12/24
 * @Description:商品主页Contract
 */
interface IShoppingHomeContract {

    interface IShoppingHomeModel : IBaseModel<IShoppingHomeView> {
        fun getShoppingParentType(callBack: ResponseCallBack<ShoppingFavoriteResponse>)
        fun getHomePageData(params: HashMap<String, String>, callBack: ResponseCallBack<HomePageResponse>)
        fun onGetAdvert(params: HashMap<String, String>, callBack: ResponseCallBack<List<AdvertBean>>)
        fun onGetH5Url(callBack: ResponseCallBack<HomeUrlBean>)
    }

    interface IShoppingHomePresenter : IBasePresenter<IShoppingHomeView> {
        fun refreshHomePageData()
        fun getHomePageDataFirst()
        fun getAdvert(context: Context)
        fun getH5Url()
    }

    interface IShoppingHomeView : IBaseView<ArrayList<ProductParentType>> {
        fun showNetWorkErrorView()
        fun onGetHomePageDataSuccess(response: HomePageResponse)
        fun onGetHomePageDataFinish()
        fun onRefreshFragmentData()
        fun onGetH5UrlSuccess(response:HomeUrlBean)
        fun onLoadPopupDataSuccess(popupBean: HomePopupBean?)
    }

}