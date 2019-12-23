package com.laka.ergou.mvp.test.preseter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.androidlib.util.rx.exception.BaseException
import com.laka.ergou.mvp.shop.constant.ShopDetailConstant
import com.laka.ergou.mvp.shop.model.bean.ImageDetail
import com.laka.ergou.mvp.test.constract.ITestContract
import com.laka.ergou.mvp.test.model.responsitory.TestModel
import io.reactivex.disposables.Disposable

/**
 * @Author:summer
 * @Date:2018/12/20
 * @Description:
 */
class TestPresenter : ITestContract.ITestPresenter {

    private var mDisposableList: ArrayList<Disposable> = ArrayList()
    private lateinit var mView: ITestContract.ITestView
    private var mModel: ITestContract.ITestModel = TestModel()

    override fun onViewCreate() {
    }

    override fun onViewDestroy() {
        mDisposableList?.forEach {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
        mDisposableList.clear()
        mModel.onViewDestory()
    }

    override fun onLifeCycleChange(owner: LifecycleOwner, event: Lifecycle.Event) {
    }

    override fun setView(view: ITestContract.ITestView) {
        this.mView = view
        mModel.setView(view)
    }

    override fun onLoadShopDetailImage() {
//        val params = HashMap<String, String>()
//        params.put(ShopDetailConstant.PRODUCT_ID, "5833323535")//
//        mModel.onLoadShopDetailImage(params, object : ResponseCallBack<ArrayList<ImageDetail>> {
//            override fun onSuccess(t: ArrayList<ImageDetail>) {
//                LogUtils.info(t.toString())
//            }
//
//            override fun onFail(e: BaseException?) {
//                LogUtils.info("${e?.errorMsg}")
//            }
//        })
    }
}

