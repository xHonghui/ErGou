package com.laka.ergou.mvp.user.presenter

import android.app.Activity
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.text.TextUtils
import cn.jpush.android.api.JPushInterface
import com.alibaba.baichuan.trade.biz.login.AlibcLogin
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.androidlib.util.rx.constant.RequestCommonCode
import com.laka.androidlib.util.rx.exception.BaseException
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.ergou.common.util.ClipBoardManagerHelper
import com.laka.ergou.common.util.MetaDataUtils
import com.laka.ergou.common.util.rx.RxSubscriber
import com.laka.ergou.mvp.advert.constant.AdvertConstant
import com.laka.ergou.mvp.advertbanner.constant.AdvertBannerConstant
import com.laka.ergou.mvp.commission.model.bean.CommissionBean
import com.laka.ergou.mvp.login.constant.LoginConstant
import com.laka.ergou.mvp.login.model.bean.UserBean
import com.laka.ergou.mvp.login.model.bean.UserInfoBean
import com.laka.ergou.mvp.user.UserUtils.UserUtils
import com.laka.ergou.mvp.user.constant.UserApiConstant
import com.laka.ergou.mvp.user.constant.UserConstant
import com.laka.ergou.mvp.user.contract.IUserHomeConstract
import com.laka.ergou.mvp.user.model.bean.BannerAdvBean
import com.laka.ergou.mvp.user.model.bean.RelationResponse
import com.laka.ergou.mvp.user.model.bean.UrlResponse
import com.laka.ergou.mvp.user.model.responsitory.UserHomeModel
import io.reactivex.disposables.Disposable

/**
 * @Author:summer
 * @Date:2019/1/14
 * @Description:
 */
class UserHomePresenter : IUserHomeConstract.IUserHomePresenter {

    private val mDisposableList = ArrayList<Disposable>()
    private lateinit var mView: IUserHomeConstract.IUserHomeView
    private var mModel: IUserHomeConstract.IUserHomeModel = UserHomeModel()

    override fun onViewCreate() {}
    override fun onLifeCycleChange(owner: LifecycleOwner, event: Lifecycle.Event) {}

    override fun onViewDestroy() {
        mDisposableList.forEach {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
        mDisposableList.clear()
        mModel.onViewDestory()
    }

    override fun setView(view: IUserHomeConstract.IUserHomeView) {
        this.mView = view
        mModel.setView(view)
    }

    override fun onLoadUserInfo(context: Context) {
        if (!UserUtils.isLogin()) {
            return
        }
        val jpushId = JPushInterface.getRegistrationID(context)
        val channel = MetaDataUtils.getMateDataForApplicationInfo("UMENG_CHANNEL")
        val params = HashMap<String, String?>()
        if (!TextUtils.isEmpty(jpushId)) {
            params[UserApiConstant.JPUSH_REGISTER_ID] = jpushId
        }
        params[UserApiConstant.JPUSH_REGISTER_CHANNEL] = channel
        params[LoginConstant.PLATFORM] = LoginConstant.ANDROID_PLATFROM
        mModel.onLoadUserInfo(params, object : ResponseCallBack<UserBean> {
            override fun onSuccess(t: UserBean) {
                UserUtils.updateUserInfo(UserInfoBean(t, null))
                mView.onLoadUserInfoSuccess(t)
            }

            override fun onFail(e: BaseException?) {
                if (e?.code == RequestCommonCode.LK_NOT_LOGIN
                        || e?.code == RequestCommonCode.LK_WRONG_USER_TOKEN) {
                    LogUtils.info("用户未登录或登录信息已过期")
                } else {
                    ToastHelper.showCenterToast("${e?.errorMsg}")
                }
            }
        })
    }

    /**
     * description:获取用户补贴信息
     **/
    override fun getUserCommissionData() {
        mModel.getUserCommissionData()
                .subscribe(object : RxSubscriber<CommissionBean, IUserHomeConstract.IUserHomeView>(mView) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }

                    override fun onNext(t: CommissionBean) {
                        super.onNext(t)
                        mView.loadUserCommissionSuccess(t)
                    }
                })

    }

    /**
     * description:获取推广Banner
     **/
    override fun getBannerAdv() {
        val params = HashMap<String, String>()
        params["class_id"] = AdvertBannerConstant.TYPE_BANNER_CLASSID_USERCENTER
        mModel.getBannerAdv(params)
                .subscribe(object : RxSubscriber<ArrayList<BannerAdvBean>, IUserHomeConstract.IUserHomeView>(mView) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }

                    override fun onNext(t: ArrayList<BannerAdvBean>) {
                        super.onNext(t)
                        mView.getBannerDataSuccess(t)
                    }
                })
    }

    /**淘宝授权*/
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

    /**获取淘宝联盟codeUrl*/
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
        params[UserConstant.UNION_CODE] = "$code"
        params[UserConstant.UNION_STATE] = "$state"
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