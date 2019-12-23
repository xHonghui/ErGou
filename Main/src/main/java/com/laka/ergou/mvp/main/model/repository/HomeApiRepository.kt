package com.laka.ergou.mvp.main.model.repository

import com.laka.androidlib.mvp.IBaseView
import com.laka.androidlib.util.rx.RxResponseComposer
import com.laka.ergou.common.util.MetaDataUtils
import com.laka.ergou.common.util.rx.RxSubscriber
import com.laka.ergou.mvp.main.constant.HomeApiConstant
import com.laka.ergou.mvp.main.model.bean.AddLetteryTimeResponse
import com.laka.ergou.mvp.main.model.bean.AppUpdateInfo
import com.laka.ergou.mvp.main.model.bean.AppUrlsInfo
import com.laka.ergou.mvp.main.model.bean.ShareInfo
import io.reactivex.Observable
import okhttp3.ResponseBody

/**
 * @Author:Rayman
 * @Date:2019/1/22
 * @Description:主页API请求类---单独请求类。可供全局使用
 */
object HomeApiRepository {

    fun getAppUpdateInfo(): Observable<AppUpdateInfo> {
        val channel = MetaDataUtils.getMateDataForApplicationInfo("UMENG_CHANNEL")
        val params = HashMap<String, String?>()
        params[HomeApiConstant.APP_CHANNEL] = channel
        //params[HomeApiConstant.VERSION] = SystemUtils.getVersionName()
        //params[HomeApiConstant.BUILD] = "${SystemUtils.getVersionCode()}"
        //params[HomeApiConstant.APP_PLAT_FORM] = "Android"
        return HomeRetrofitHelper.newInstance().getAppUpdateInfo(params)
                .compose(RxResponseComposer.flatResponse())
    }

    fun getWxShareOfficial(): Observable<ShareInfo> {
        val params = HashMap<String, String?>()
        return HomeRetrofitHelper.newInstance().getShareOfficial(params)
                .compose(RxResponseComposer.flatResponse())
    }

    fun getWxSharePromotion(): Observable<ShareInfo> {
        val params = HashMap<String, String?>()
        return HomeRetrofitHelper.newInstance().getSharePromotion(params)
                .compose(RxResponseComposer.flatResponse())
    }

    fun addLotteryTime(): Observable<AddLetteryTimeResponse> {
        val params = HashMap<String, String?>()
        return HomeRetrofitHelper.newInstance().addLotteryTime(params)
                .compose(RxResponseComposer.flatResponse())
    }

    fun downloadAPK(downLoadUrl: String): Observable<ResponseBody> {
        return HomeRetrofitHelper.newDownLoadInstance().downloadApk(downLoadUrl)
    }

    fun getAppUrls() {
        HomeRetrofitHelper.newInstance().getAppUrls()
                .compose(RxResponseComposer.flatResponse())
                .subscribe(object : RxSubscriber<AppUrlsInfo, IBaseView<AppUrlsInfo>>(null) {
                    override fun onNext(it: AppUrlsInfo) {
                        super.onNext(it)
                        it.privacyUrl?.let {
                            HomeApiConstant.ABOUT_PRIVACY = it
                        }
                        it.aboutLaka?.let {
                            HomeApiConstant.ABOUT_LAKA = it
                        }
                        it.earnCommissionUrl?.let {
                            HomeApiConstant.EARN_COMMISSION = it
                        }
                        it.invitationUrl?.let {
                            HomeApiConstant.URL_INVITATION = it
                        }
                        it.invitationShareUrl?.let {
                            HomeApiConstant.URL_INVITATION_SHARE = it
                        }
                        it.invitationPosterUrl?.let {
                            HomeApiConstant.URL_INVITATION_POSTER = it
                        }
                        it.taobaoLogoutUrl?.let {
                            HomeApiConstant.URL_TAOBAO_LOGOUT = it
                        }
                        it.userTutorialUrl?.let {
                            HomeApiConstant.URL_USER_TUTORIAL = it
                        }
                        it.scanDownloadUrl?.let {
                            HomeApiConstant.URL_SCAN_DOWNLOAD = it
                        }

//                        HomeApiConstant.URL_TMALL_PREFIX = it.tmallPrefix
                        it.tmallPrefixDetailUrl?.let {
                            HomeApiConstant.URL_TMALL_PREFIX_LIST = it
                        }
                        it.wechatMoment?.let {
                            HomeApiConstant.URL_WECHAT_MOMENT = it
                        }
                        it.warTeam?.let {
                            HomeApiConstant.URL_WARTEAM_INFO = it
                        }
                        it.teamInvite?.let {
                            HomeApiConstant.URL_TEAM_INVITATION = it
                        }
                        it.customerUrl?.let {
                            HomeApiConstant.URL_CUSTOMER = it
                        }
                        it.zeroBuy?.let {
                            HomeApiConstant.URL_AERO_BUY = it
                        }
                        it.majordomoService?.let {
                            HomeApiConstant.URL_SEND_CIRCLE = it
                        }
                        it.wxPayReferer?.let {
                            HomeApiConstant.URL_WX_PAY_REFERER = it
                        }
                        it.wxPayUrl?.let {
                            HomeApiConstant.URL_WX_PAY = it
                        }
                        //EventBusManager.postEvent(PromotionEvent(it.promotion))
                    }
                })
    }
}