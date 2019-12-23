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
interface IUserGenderEdit {

    interface IUserGenderEditView : IBaseLoadingView<CommonData> {
        fun onUserGenderEditSuccess(resultBean: CommonData)
        fun authorInvalid()
    }

    interface IUserGenderEditPresenter : IBasePresenter<IUserGenderEditView> {
        fun onUserGenderEdit(gender: String)
    }

    interface IUserGenderEditModel:IBaseModel<IUserGenderEditView> {
        //fun onUserGenderEdit(params: HashMap<String, String?>): Observable<BaseResponse<CommonData>>
        fun onUserGenderEdit(params:HashMap<String,String?>,callBack: ResponseCallBack<CommonData>)
    }

}