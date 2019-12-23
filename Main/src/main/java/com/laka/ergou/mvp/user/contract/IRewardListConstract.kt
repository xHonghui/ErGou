package com.laka.ergou.mvp.user.contract

import com.laka.androidlib.mvp.IBaseLoadingView
import com.laka.androidlib.mvp.IBaseModel
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.androidlib.widget.refresh.BaseListBean
import com.laka.ergou.mvp.user.model.bean.RewardListBean
import com.laka.ergou.mvp.user.model.bean.RewardListResponse

/**
 * @Author:summer
 * @Date:2019/6/26
 * @Description:
 */
interface IRewardListConstract {

    interface IRewardListView : IBaseLoadingView<RewardListBean> {
        fun onLoadRewardListSuccess(result: BaseListBean<RewardListBean>)
        fun onLoadError(page:Int)
        fun onAuthorFail()
    }

    interface IRewardListPresenter : IBasePresenter<IRewardListView> {
        fun onLoadRewardListData(page: Int,type:Int)
    }

    interface IRewardListModel : IBaseModel<IRewardListView> {
        fun onLoadRewardListData(params:HashMap<String,String>,callBack: ResponseCallBack<RewardListResponse>)
    }
}