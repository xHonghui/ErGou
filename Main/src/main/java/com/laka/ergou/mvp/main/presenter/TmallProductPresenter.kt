package com.laka.ergou.mvp.main.presenter

import android.app.Activity
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.text.TextUtils
import com.alibaba.baichuan.trade.biz.login.AlibcLogin
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.androidlib.util.rx.exception.BaseException
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.ergou.common.util.ClipBoardManagerHelper
import com.laka.ergou.mvp.main.constant.HomeApiConstant
import com.laka.ergou.mvp.main.contract.ITmallProductContract
import com.laka.ergou.mvp.main.model.repository.TmallProductModel
import com.laka.ergou.mvp.shop.constant.ShopDetailConstant
import com.laka.ergou.mvp.shop.model.bean.CustomProductDetail
import com.laka.ergou.mvp.shop.model.bean.HighVolumeInfoResponse
import com.laka.ergou.mvp.shop.model.bean.TPwdCreateResponse
import com.laka.ergou.mvp.shop.utils.BigDecimalUtils
import com.laka.ergou.mvp.shop.utils.ShopStringUtils
import com.laka.ergou.mvp.user.UserUtils.UserUtils
import com.laka.ergou.mvp.user.constant.UserConstant
import com.laka.ergou.mvp.user.model.bean.RelationResponse
import com.laka.ergou.mvp.user.model.bean.UrlResponse

/**
 * @Author:summer
 * @Date:2019/4/25
 * @Description:天貓详情页面，截取商品ID，查找优惠券
 */
class TmallProductPresenter : ITmallProductContract.ITmallProductPresenter {

    private lateinit var mView: ITmallProductContract.ITmallProductView
    private var mModel: ITmallProductContract.ITmallProductModel = TmallProductModel()

    override fun setView(view: ITmallProductContract.ITmallProductView) {
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

    /**
     * 产品详情
     * */
    override fun onLoadDetailData(id: String) {
        val params = HashMap<String, String>()
        params[HomeApiConstant.PRODUCT_ID] = id
        mModel.onLoadDetailData(params, object : ResponseCallBack<CustomProductDetail> {
            override fun onSuccess(t: CustomProductDetail) {
                if (!TextUtils.isEmpty(t.num_iid)) {
                    mView.onLoadDetailDataSuccess(t)
                } else {
                    LogUtils.info("查找失败：item_id 为空---$t")
                    mView.onLoadDetailDataFail("item_id 为空")
                }
            }

            override fun onFail(e: BaseException?) {
                LogUtils.info("查找失败：${e?.errorMsg}")
                mView.onLoadDetailDataFail("")
            }
        })
    }

    /**
     * 加载生成淘口令的数据
     * */
    override fun onLoadTPwdCreate(productId: String, title: String, url: String, logo: String) {
        val params = HashMap<String, String>()
        params[ShopDetailConstant.TEXT] = title
        params[ShopDetailConstant.URL] = url
        params[ShopDetailConstant.LOGO] = logo
        params[ShopDetailConstant.ITEM_ID] = productId
        mModel.onLoadTPwdCreate(params, object : ResponseCallBack<TPwdCreateResponse> {
            override fun onSuccess(t: TPwdCreateResponse) {
                mView.onLoadTPwdCreateSuccess(t)
            }

            override fun onFail(e: BaseException?) {
                ToastHelper.showCenterToast(e?.errorMsg)
            }
        })
    }

    override fun onCreateShareWorwds(pwd: String, product: CustomProductDetail): String {
        val couponValue = ShopStringUtils.onMatchStr(product?.coupon_info)
        return "${product?.title}\n----------\n【在售价】${product?.zk_final_price}元\n【劵后价】" +
                "${BigDecimalUtils.sub(product?.zk_final_price, "$couponValue")}元\n----------\n复制这条信息$pwd，\n打开【手掏】即可查看\n" +
                "===分享来自【二购App】==="
    }

    /**
     * 防止丢单流程：
     * 1、用户首先淘宝授权登录
     * 2、淘宝授权登录成功后，请求接口获取联盟授权url
     * 3、webview 打开获取到的授权url，操作并进行授权
     * 4、授权成功后，通过 webview 拦截到相应的url后，从url中获取渠道id，
     * 最后将渠道id 跟授权用户绑定即可
     *
     * 因为淘宝联盟的渠道 id 和用户是一对一的关系，有了渠道id 就知道后台的订单是哪个用户购买的，就可以防止app 丢单的问题
     * */
    /**普通淘宝授权*/
    override fun onTaoBaoAuthor(activity: Activity, type: Int) {
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
                    mView?.onTaoBaoAuthorSuccess(type)
                }

                override fun onFailure(p0: Int, p1: String?) {
                    mView.dismissLoading()
                    ToastHelper.showToast("授权失败 ")
                }
            })
        } else {
            mView?.onTaoBaoAuthorSuccess(type)
        }
    }

    /**
     * 获取绑定淘宝渠道ID的H5链接
     * */
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

    /**
     * 绑定淘宝渠道ID
     * */
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
                mView.handleUnionCodeFail()
            }
        })
    }

    /**
     * 获取高佣链接
     * */
    override fun onLoadHighVolumeCouponInfo(productId: String, type: Int) {
        val params = HashMap<String, String>()
        params[ShopDetailConstant.ITEM_ID] = productId
        mModel.onLoadHighVolumeCouponInfo(params, object : ResponseCallBack<HighVolumeInfoResponse> {
            override fun onSuccess(t: HighVolumeInfoResponse) {
                mView.onLoadHighVolumeCouponInfoSuccess(t, type)
            }

            override fun onFail(e: BaseException?) {
                mView.onLoadHightVolumeCouponInfoFail("${e?.errorMsg}", type)
            }
        })
    }
}