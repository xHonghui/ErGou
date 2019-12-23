package com.laka.ergou.mvp.user.presenter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.androidlib.util.rx.constant.RequestCommonCode
import com.laka.androidlib.util.rx.exception.BaseException
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.androidlib.widget.refresh.BaseListBean
import com.laka.ergou.mvp.user.constant.UserConstant
import com.laka.ergou.mvp.user.contract.IRewardListConstract
import com.laka.ergou.mvp.user.model.bean.RewardListBean
import com.laka.ergou.mvp.user.model.bean.RewardListResponse
import com.laka.ergou.mvp.user.model.responsitory.RewardListModel

/**
 * @Author:summer
 * @Date:2019/6/26
 * @Description:
 */
class RewardListPresenter : IRewardListConstract.IRewardListPresenter {

    private lateinit var mView: IRewardListConstract.IRewardListView
    private var mModel: IRewardListConstract.IRewardListModel = RewardListModel()

    override fun setView(view: IRewardListConstract.IRewardListView) {
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

    override fun onLoadRewardListData(page: Int, type: Int) {
        val params: HashMap<String, String> = HashMap()
        params[UserConstant.SOURCE_TYPE] = "$type"
        params[UserConstant.PAGE_NUMBER] = "$page"
        mModel.onLoadRewardListData(params, object : ResponseCallBack<RewardListResponse> {
            override fun onSuccess(t: RewardListResponse) {
                mView.onLoadRewardListSuccess(object : BaseListBean<RewardListBean>() {
                    override fun getList(): MutableList<RewardListBean> {
                        return t.data
                    }

                    override fun getPageTotalCount(): Int {
                        return if (t.total % UserConstant.PAGE_SIZE_VALUE == 0) {
                            t.total / UserConstant.PAGE_SIZE_VALUE
                        } else {
                            t.total / UserConstant.PAGE_SIZE_VALUE + 1
                        }
                    }
                })
            }

            override fun onFail(e: BaseException?) {
                if (e?.code == RequestCommonCode.LK_WRONG_USER_TOKEN
                        || e?.code == RequestCommonCode.LK_NOT_LOGIN) {
                    mView.onAuthorFail()
                }
                ToastHelper.showCenterToast(e?.errorMsg)
                mView.onLoadError(page)
            }
        })
    }


}