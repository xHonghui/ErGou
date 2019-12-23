package com.laka.ergou.mvp.armsteam.contract

import com.laka.androidlib.mvp.IBaseLoadingView
import com.laka.androidlib.mvp.IBaseModel
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.androidlib.widget.refresh.BaseListBean
import com.laka.ergou.mvp.armsteam.model.bean.MyArmsLevelsBean
import com.laka.ergou.mvp.armsteam.model.bean.MyArmsResponse

/**
 * @Author:summer
 * @Date:2019/3/8
 * @Description:
 */
interface MyArmsLevelsContract {

    interface IMyLowerLevelsView : IBaseLoadingView<BaseListBean<MyArmsLevelsBean>> {
        fun onLoadMyArmsLevelsDataSuccess(list: BaseListBean<MyArmsLevelsBean>, total: Int)
        fun onLoadError(page: Int)
        fun onAuthorFail()
    }

    interface IMyLowerLevelsPresenter : IBasePresenter<IMyLowerLevelsView> {
        fun onLoadMyArmsLevelsData(page: Int, type: Int, mComradeArmsLevelsType: Int)
    }

    interface IMyLowerLevelsModel : IBaseModel<IMyLowerLevelsView> {
        fun onLoadMyArmsLevelsData(params: HashMap<String, String>, callBack: ResponseCallBack<MyArmsResponse>)
    }

}