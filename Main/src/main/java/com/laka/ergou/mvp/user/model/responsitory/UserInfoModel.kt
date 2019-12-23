package com.laka.ergou.mvp.user.model.responsitory

import com.laka.androidlib.util.rx.RxResponseComposer
import com.laka.androidlib.util.rx.RxSchedulerComposer
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.ergou.common.util.rx.RxCustomSubscriber
import com.laka.ergou.mvp.login.model.bean.UserBean
import com.laka.ergou.mvp.user.constant.UserConstant
import com.laka.ergou.mvp.user.contract.IUserInfoContract
import com.laka.ergou.mvp.user.model.bean.CommonData
import com.laka.ergou.mvp.user.model.bean.RelationResponse
import com.laka.ergou.mvp.user.model.bean.StsTokenBean
import com.laka.ergou.mvp.user.model.bean.UrlResponse
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import org.json.JSONObject
import java.util.ArrayList

/**
 * @Author:summer
 * @Date:2019/1/11
 * @Description:
 */
class UserInfoModel : IUserInfoContract.IUserInfoModel {

    private lateinit var mView: IUserInfoContract.IUserInfoView
    private val mDisposableList = ArrayList<Disposable>()

    override fun setView(v: IUserInfoContract.IUserInfoView) {
        this.mView = v
    }

    override fun onViewDestory() {
        mDisposableList.forEach {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
        mDisposableList.clear()
    }

    override fun onUploadUserAvatar(path:String, callback: ResponseCallBack<CommonData>) {
        UserCustomRetrofitHelper.instance
                .onUpdateUserAvatarData(path)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<CommonData, IUserInfoContract.IUserInfoView>(mView, callback) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }

    override fun onGetStsToken(params: HashMap<String, String?>, callback: ResponseCallBack<StsTokenBean>) {
        UserCustomRetrofitHelper.instance
                .onGetStsToken(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<StsTokenBean, IUserInfoContract.IUserInfoView>(mView, callback) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }

    override fun unbindTaoBaoAccount(): Observable<JSONObject> {
        var params = HashMap<String, String?>()
        //params[UserApiConstant.USER_TOKEN] = UserUtils.getUserToken()
        return UserRetrofitHelper.instance.unbindTaoBaoAccount(params)
                .compose(RxResponseComposer.flatResponse())
    }

    override fun updateUserOpenChatStatus(isOpenGouXiaoEr: Boolean): Observable<CommonData> {
        val params = HashMap<String, String?>()
        params[UserConstant.OPEN_GOU_XIAO_ER] = if (isOpenGouXiaoEr) "1" else "0"
        //params[UserConstant.TOKEN] = UserUtils.getUserToken()
        return UserRetrofitHelper.instance.onUpdateUserInfoData(params)
                .compose(RxResponseComposer.flatResponse())
    }

    //加载个人信息
    override fun onLoadUserInfo(params: HashMap<String, String?>, callback: ResponseCallBack<UserBean>) {
        UserCustomRetrofitHelper.instance
                .onLoadUserInfo(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<UserBean, IUserInfoContract.IUserInfoView>(mView, callback) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }

    override fun bindWxAccount(wxAuthorizedCode: String): Observable<UserBean> {
        val params = HashMap<String, String?>()
        //params[UserConstant.TOKEN] = UserUtils.getUserToken()
        params[UserConstant.CODE] = wxAuthorizedCode
        return UserRetrofitHelper.instance
                .bindWxAccount(params)
                .compose(RxResponseComposer.flatResponse())
    }

    override fun unbindWxAccount(): Observable<JSONObject> {
        val params = HashMap<String, String?>()
        //添加了token公共参数拦截器，无需再手动添加
        //params[UserConstant.TOKEN] = UserUtils.getUserToken()
        return UserRetrofitHelper.instance
                .unbindWxAccount(params)
                .compose(RxResponseComposer.flatResponse())
    }

    /**获取联盟授权codeUrl*/
    override fun getUnionCodeUrl(params: HashMap<String, String>, callback: ResponseCallBack<UrlResponse>) {
        UserCustomRetrofitHelper.instance
                .getUnionCodeUrl(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<UrlResponse, IUserInfoContract.IUserInfoView>(mView, callback) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }

    override fun handleUnionCode(params: HashMap<String, String>, callback: ResponseCallBack<RelationResponse>) {
        UserCustomRetrofitHelper.instance
                .handleUnionCode(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<RelationResponse, IUserInfoContract.IUserInfoView>(mView, callback) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }
}