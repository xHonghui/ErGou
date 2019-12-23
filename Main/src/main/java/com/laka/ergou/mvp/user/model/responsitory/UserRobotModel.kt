package com.laka.ergou.mvp.user.model.responsitory

import com.laka.androidlib.util.SPHelper
import com.laka.androidlib.util.rx.RxResponseComposer
import com.laka.ergou.mvp.login.constant.LoginConstant
import com.laka.ergou.mvp.login.model.bean.UserInfoBean
import com.laka.ergou.mvp.user.constant.UserApiConstant
import com.laka.ergou.mvp.user.contract.IUserRobotContract
import com.laka.ergou.mvp.user.model.bean.RobotDataResponse
import com.laka.ergou.mvp.user.model.bean.RobotInfo
import com.laka.ergou.mvp.user.model.bean.RobotListResponse
import io.reactivex.Observable

/**
 * @Author:Rayman
 * @Date:2019/1/17
 * @Description:用户模块---我的机器人Model实现层
 */
class UserRobotModel : IUserRobotContract.IUserRobotModel {

    override fun getRobotList(page: Int, pageSize: Int): Observable<RobotDataResponse<RobotInfo>> {
        val params = HashMap<String, String?>()
        val defaultPageSize = UserApiConstant.PARAM_DEFAULT_PAGE_SIZE
        params[UserApiConstant.PARAM_PAGE] = "$page"
        params[UserApiConstant.PARAM_PAGE_SIZE] = "$defaultPageSize"

        return UserRetrofitHelper.instance.getUserRobotList(params = params)
                .compose(RxResponseComposer.flatResponse<RobotDataResponse<RobotInfo>>())
    }
}