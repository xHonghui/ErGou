package com.laka.ergou.mvp.user.presenter

import android.app.Activity
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.text.TextUtils
import com.alibaba.baichuan.trade.biz.login.AlibcLogin
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback
import com.laka.androidlib.eventbus.EventBusManager
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.SPHelper
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.androidlib.util.rx.constant.RequestCommonCode
import com.laka.androidlib.util.rx.exception.BaseException
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.ergou.common.util.ClipBoardManagerHelper
import com.laka.ergou.common.util.rx.RxSubscriber
import com.laka.ergou.mvp.login.constant.LoginConstant
import com.laka.ergou.mvp.login.model.event.UserEvent
import com.laka.ergou.mvp.login.model.bean.UserBean
import com.laka.ergou.mvp.login.model.bean.UserInfoBean
import com.laka.ergou.mvp.shopping.center.constant.ShoppingApiConstant
import com.laka.ergou.mvp.user.UserUtils.UserUtils
import com.laka.ergou.mvp.user.constant.UserApiConstant
import com.laka.ergou.mvp.user.constant.UserConstant
import com.laka.ergou.mvp.user.contract.IUserInfoContract
import com.laka.ergou.mvp.user.model.bean.CommonData
import com.laka.ergou.mvp.user.model.bean.RelationResponse
import com.laka.ergou.mvp.user.model.bean.StsTokenBean
import com.laka.ergou.mvp.user.model.bean.UrlResponse
import com.laka.ergou.mvp.user.model.responsitory.UserInfoModel
import io.reactivex.disposables.Disposable
import org.json.JSONObject

/**
 * @Author:summer
 * @Date:2019/1/10
 * @Description:
 */
class UserInfoPresenter : IUserInfoContract.IUserInfoPresenter {

    private lateinit var mView: IUserInfoContract.IUserInfoView
    private var mModel: IUserInfoContract.IUserInfoModel = UserInfoModel()
    private var mDisposableList = ArrayList<Disposable>()

    override fun onViewCreate() {

    }

    override fun onViewDestroy() {
        mDisposableList.forEach {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
        mDisposableList.clear()
        mModel.onViewDestory()
    }

    override fun onLifeCycleChange(owner: LifecycleOwner, event: Lifecycle.Event) {

    }

    override fun setView(view: IUserInfoContract.IUserInfoView) {
        this.mView = view
        mModel.setView(mView)
    }

    override fun onUploadUserAvatar(objectKey: String) {
        mView.showLoading()
        LogUtils.info("上传头像的地址:$objectKey")
        mModel.onUploadUserAvatar(objectKey, object : ResponseCallBack<CommonData> {
            override fun onSuccess(t: CommonData) {
                mView.onUploadUserAvatarSuccess(t)
            }

            override fun onFail(e: BaseException?) {
                ToastHelper.showCenterToast("${e?.errorMsg}")
            }
        })
    }

    /**
     * 获取sts token
     * */
    override fun onGetStsToken() {
        mView.showLoading()
        val params = HashMap<String, String?>()
        mModel.onGetStsToken(params, object : ResponseCallBack<StsTokenBean> {
            override fun onSuccess(t: StsTokenBean) {
                mView.onGetStsTokenSuccess(t)
            }

            override fun onFail(e: BaseException?) {
                ToastHelper.showCenterToast("${e?.errorMsg}")
            }
        })
    }

    override fun onLogout(activity: Activity) {
        // 退出的时候，清除用户信息
        UserUtils.clearUserInfo()
        AlibcLogin.getInstance().logout(object :AlibcLoginCallback{
            override fun onSuccess(p0: Int, p1: String?, p2: String?) {
                EventBusManager.postEvent(UserEvent(UserConstant.LOGOUT_EVENT))
                ToastHelper.showCenterToast("退出登录")
                mView.onLogoutSuccess()
                mView.dismissLoading()
            }

            override fun onFailure(p0: Int, p1: String?) {
                ToastHelper.showCenterToast("退出登录失败，请稍后重试！")
                mView.dismissLoading()
            }
        })
    }

    override fun unbindTaoBaoAccount() {
        mView?.showLoading()
        mModel?.unbindTaoBaoAccount()
                .subscribe(object : RxSubscriber<JSONObject, IUserInfoContract.IUserInfoView>(mView) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }

                    override fun onNext(t: JSONObject) {
                        super.onNext(t)
                        mView?.unbindTaoBaoAccountCallBack(true)
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        mView?.unbindTaoBaoAccountCallBack(false)
                    }
                })
    }

    //加载用户信息
    override fun onLoadUserInfo(userId: String) {
        if (!UserUtils.isLogin()) {
            mView.authorInvalid()
            return
        }
        val params = HashMap<String, String?>()
        mModel.onLoadUserInfo(params, object : ResponseCallBack<UserBean> {
            override fun onSuccess(t: UserBean) {
                // 自动刷新Token，更新到SP和静态内存
                UserUtils.updateUserInfo(UserInfoBean(t, null))
            }

            override fun onFail(e: BaseException?) {
                if (e?.code == RequestCommonCode.LK_NOT_LOGIN
                        || e?.code == RequestCommonCode.LK_WRONG_USER_TOKEN) {
                    mView.authorInvalid()
                } else {
                    ToastHelper.showCenterToast("${e?.errorMsg}")
                }
            }
        })
    }

    override fun bindWxAccount(wxAuthorizedCode: String) {
        mModel.bindWxAccount(wxAuthorizedCode)
                .subscribe(object : RxSubscriber<UserBean, IUserInfoContract.IUserInfoView>(mView) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }

                    override fun onNext(t: UserBean) {
                        super.onNext(t)
                        t.wxAccount?.let {
                            UserUtils.updateWeChatInfo(it)
                        }
                        mView.operateWxAccount(true, true)
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        mView.operateWxAccount(false, false)
                    }
                })
    }

    override fun unbindWxAccount() {
        mModel.unbindWxAccount()
                .subscribe(object : RxSubscriber<JSONObject, IUserInfoContract.IUserInfoView>(mView) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }

                    override fun onNext(t: JSONObject) {
                        super.onNext(t)
                        UserUtils.updateWeChatInfo("")
                        mView.operateWxAccount(false, true)
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        mView.operateWxAccount(false, false)
                    }
                })
    }

    //淘宝授权
    override fun onTaoBaoAuthor(activity: Activity) {
        if (!AlibcLogin.getInstance().isLogin) {
            mView.showLoading()
            //进入淘宝授权前，清空本地粘贴板内容，防止粘贴板含有淘口令从而引起淘宝弹窗搜索弹窗
            ClipBoardManagerHelper.getInstance.clearClipBoardContentForHasTkl()
            AlibcLogin.getInstance().showLogin(object : AlibcLoginCallback {
                override fun onSuccess(p0: Int, p1: String?, p2: String?) {
                    mView.dismissLoading()
                    LogUtils.error("获取手掏用户信息: " + AlibcLogin.getInstance().session)
                    val userInfoBean = UserUtils.getUserInfoBean()
                    userInfoBean.session = AlibcLogin.getInstance().session
                    UserUtils.updateUserInfo(userInfoBean)
                    mView?.onTaoBaoAuthorSuccess()
                }

                override fun onFailure(p0: Int, p1: String?) {
                    mView.dismissLoading()
                    ToastHelper.showToast("授权失败 ")
                }
            })
        } else {
            mView?.onTaoBaoAuthorSuccess()
        }
    }

    /**获取联盟授权codeUrl*/
    override fun getUnionCodeUrl() {
        mView.showLoading()
        val params = HashMap<String, String>()
        mModel.getUnionCodeUrl(params, object : ResponseCallBack<UrlResponse> {
            override fun onSuccess(t: UrlResponse) {
                LogUtils.info("--json:" + t.toString())
                if (!TextUtils.isEmpty(t.url)) {
                    mView.getUnionCodeUrlSuccess(t.url)
                } else {
                    ToastHelper.showCenterToast("获取联盟授权url失败")
                }
            }

            override fun onFail(e: BaseException?) {
                ToastHelper.showCenterToast(e?.errorMsg)
            }
        })
    }

    override fun handleUnionCode(code: String, state: String) {
        mView.showLoading()
        val params = HashMap<String, String>()
        params.put(UserConstant.UNION_CODE, "$code")
        params.put(UserConstant.UNION_STATE, "$state")
        mModel.handleUnionCode(params, object : ResponseCallBack<RelationResponse> {
            override fun onSuccess(t: RelationResponse) {
                if (!TextUtils.isEmpty(t.relation_id)) {
                    UserUtils.updateRelationId(t.relation_id)
                    mView.handleUnionCodeSuccess()
                }
            }

            override fun onFail(e: BaseException?) {
                ToastHelper.showCenterToast(e?.errorMsg)
            }
        })
    }
}