package com.laka.ergou.mvp.user.contract

import com.laka.androidlib.mvp.IBaseLoadingView
import com.laka.androidlib.mvp.IBaseModel
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.net.response.BaseResponse
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.ergou.mvp.user.model.bean.CommonData
import io.reactivex.Observable

/**
 * @Author:summer
 * @Date:2019/1/11
 * @Description:
 */
interface IUserNickEditConstract {

    interface IUserNickEditView : IBaseLoadingView<CommonData> {
        fun onUserNickEditSuccess(resultBean: CommonData)
        fun authorInvalid()
        fun onUserNickEditFail(result: String)
    }

    interface IUserNickEditPresenter : IBasePresenter<IUserNickEditView> {
        fun onUserNickEdit(nickname: String)
    }

    interface IUserNickEditModel:IBaseModel<IUserNickEditView> {
        fun onUserNickEdit(params: HashMap<String, String?>,callback:ResponseCallBack<CommonData>)
    }

}