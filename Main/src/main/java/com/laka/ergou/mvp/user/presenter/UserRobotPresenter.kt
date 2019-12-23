package com.laka.ergou.mvp.user.presenter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.rx.BaseRxSubscriber
import com.laka.androidlib.widget.refresh.BaseListBean
import com.laka.ergou.common.util.rx.RxSubscriber
import com.laka.ergou.mvp.user.contract.IUserRobotContract
import com.laka.ergou.mvp.user.model.bean.RobotDataResponse
import com.laka.ergou.mvp.user.model.bean.RobotInfo
import com.laka.ergou.mvp.user.model.bean.RobotListResponse
import com.laka.ergou.mvp.user.model.responsitory.UserRobotModel
import io.reactivex.disposables.Disposable

/**
 * @Author:Rayman
 * @Date:2019/1/17
 * @Description:用户模块---我的机器人Presenter层
 */
class UserRobotPresenter : IUserRobotContract.IUserRobotPresenter {

    private var mView: IUserRobotContract.IUserRobotView? = null
    private val mModel: IUserRobotContract.IUserRobotModel = UserRobotModel()
    private var mDisposableList: ArrayList<Disposable> = ArrayList()

    override fun setView(view: IUserRobotContract.IUserRobotView) {
        this.mView = view
    }

    override fun onViewCreate() {
    }

    override fun onViewDestroy() {
        mDisposableList.forEach {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
        mDisposableList.clear()
    }

    override fun onLifeCycleChange(owner: LifecycleOwner, event: Lifecycle.Event) {
    }

    override fun getRobotList(page: Int, pageSize: Int) {
        mModel?.getRobotList(page)
                .subscribe(object : RxSubscriber<RobotDataResponse<RobotInfo>
                        , IUserRobotContract.IUserRobotView>(mView) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }

                    override fun onNext(response: RobotDataResponse<RobotInfo>) {
                        super.onNext(response)
//                        response.haveRobot = 1
//                        var testList: ArrayList<RobotInfo> = ArrayList()
//                        var testData = RobotInfo()
//                        testData.robotName = "测试机器人1"
//                        testData.robotWxId = "测试ID"
//                        testData.robotAvatar = ""
//                        testData.robotStatus = 1
//                        testList.add(testData)
//
//                        testData = RobotInfo()
//                        testData.robotName = "测试机器人2"
//                        testData.robotWxId = "测试ID1"
//                        testData.robotAvatar = ""
//                        testData.robotStatus = 2
//                        testList.add(testData)

                        if (response.haveRobot == 0) {
                            mView?.showData(response?.defaultRobot)
                        } else {
                            // 现在机器人是没有分页的，暂时留分页功能
                            var baseListBean = object : BaseListBean<RobotInfo>() {
                                override fun getList(): ArrayList<RobotInfo> {
                                    return response?.robotData?.robotList
//                                    return testList
                                }
                            }
//                            baseListBean.pageTotalCount = 2
                            val totalPage = response?.robotData?.totalPage / pageSize
                            baseListBean.pageTotalCount = if (response?.robotData?.totalPage % pageSize != 0) {
                                totalPage + 1
                            } else {
                                totalPage
                            }
                            mView?.showRobotList(baseListBean)
                        }
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        mView?.showErrorView()
                    }
                })
    }
}