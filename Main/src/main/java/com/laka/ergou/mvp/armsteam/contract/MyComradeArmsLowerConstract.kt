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
 * @Date:2019/5/24
 * @Description:我的战友
 */
interface MyComradeArmsLowerConstract {

    interface IMyComradeArmsLowerView : IBaseLoadingView<MyArmsLevelsBean> {
        fun onLoadMyComradeArmsLowerDataSuccess(list: BaseListBean<MyArmsLevelsBean>)
        fun onLoadMyComradeArmsLowerDataFaild(msg: String, page: Int)
        fun onAuthorFail()
    }

    interface IMyComradeArmsLowerPresenter : IBasePresenter<IMyComradeArmsLowerView> {
        fun onLoadComradeArmsLowerData(id: String, page: Int, pageType: Int, classType: Int)
    }

    interface IMyComradeArmsLowerModel : IBaseModel<IMyComradeArmsLowerView> {
        fun onLoadComradeArmsLowerData(params: HashMap<String, String>, callback: ResponseCallBack<MyArmsResponse>)
    }

}