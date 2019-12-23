package com.laka.ergou.mvp.user.contract

import com.laka.androidlib.mvp.IBaseLoadingView
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.widget.refresh.BaseListBean
import com.laka.ergou.mvp.user.constant.UserApiConstant
import com.laka.ergou.mvp.user.model.bean.RobotDataResponse
import com.laka.ergou.mvp.user.model.bean.RobotInfo
import com.laka.ergou.mvp.user.model.bean.RobotListResponse
import io.reactivex.Observable

/**
 * @Author:Rayman
 * @Date:2019/1/17
 * @Description:用户模块---我的机器人Contract
 */
interface IUserRobotContract {

    interface IUserRobotModel {

        /**
         * description:获取当前用户RobotList
         **/
        fun getRobotList(page: Int = 1, pageSize: Int = UserApiConstant.PARAM_DEFAULT_PAGE_SIZE): Observable<RobotDataResponse<RobotInfo>>
    }

    interface IUserRobotPresenter : IBasePresenter<IUserRobotView> {

        fun getRobotList(page: Int = 1, pageSize: Int = UserApiConstant.PARAM_DEFAULT_PAGE_SIZE)
    }

    interface IUserRobotView : IBaseLoadingView<RobotInfo> {

        fun showRobotList(robotList: BaseListBean<RobotInfo>)

        fun showErrorView()
    }
}