package com.laka.ergou.mvp.user.model.responsitory

import com.laka.androidlib.util.rx.RxResponseComposer
import com.laka.ergou.mvp.user.UserUtils.UserUtils
import com.laka.ergou.mvp.user.constant.UserApiConstant
import com.laka.ergou.mvp.user.contract.IUserBindAliAccountContract
import com.laka.ergou.mvp.user.model.bean.CommonData
import io.reactivex.Observable

/**
 * @Author:Rayman
 * @Date:2019/1/21
 * @Description:用户模块---绑定阿里账号Model实现层
 */
class UserBindAliAccountModel : IUserBindAliAccountContract.IUserBindAliAccountModel {

    override fun bindAliAccount(realName: String, aliAccount: String): Observable<CommonData> {

        val params = HashMap<String, String?>()
        params[UserApiConstant.ALI_REAL_NAME] = realName
        params[UserApiConstant.ALI_ACCOUNT] = aliAccount

        return UserRetrofitHelper.instance.onUpdateUserInfoData(params)
                .compose(RxResponseComposer.flatResponse())
    }
}