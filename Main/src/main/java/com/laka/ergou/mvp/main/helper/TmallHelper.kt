package com.laka.ergou.mvp.main.helper

import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.alibaba.baichuan.trade.biz.core.taoke.AlibcTaokeParams
import com.alibaba.baichuan.trade.biz.login.AlibcLogin
import com.laka.androidlib.eventbus.EventBusManager
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.PackageUtils
import com.laka.androidlib.util.SPHelper
import com.laka.androidlib.util.screen.ScreenUtils
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.androidlib.widget.dialog.TaoBaoAuthorDialog
import com.laka.ergou.R
import com.laka.ergou.common.util.ClipBoardManagerHelper
import com.laka.ergou.common.util.regex.RegexUtils
import com.laka.ergou.mvp.login.LoginModuleNavigator
import com.laka.ergou.mvp.login.model.event.UserEvent
import com.laka.ergou.mvp.main.constant.HomeApiConstant
import com.laka.ergou.mvp.main.contract.ITmallProductContract
import com.laka.ergou.mvp.main.model.bean.InnerPageBean
import com.laka.ergou.mvp.main.presenter.TmallProductPresenter
import com.laka.ergou.mvp.share.ShopShareModuleNavigator
import com.laka.ergou.mvp.share.model.bean.ShareResponse
import com.laka.ergou.mvp.shop.ShopDetailModuleNavigator
import com.laka.ergou.mvp.shop.constant.ShopDetailConstant
import com.laka.ergou.mvp.shop.model.bean.CustomProductDetail
import com.laka.ergou.mvp.shop.model.bean.HighVolumeInfoResponse
import com.laka.ergou.mvp.shop.model.bean.TPwdCreateResponse
import com.laka.ergou.mvp.shop.utils.AliPageUtils
import com.laka.ergou.mvp.shop.utils.BigDecimalUtils
import com.laka.ergou.mvp.shop.utils.ShareUtils
import com.laka.ergou.mvp.shop.utils.ShopStringUtils
import com.laka.ergou.mvp.shop.view.activity.ShopDetailActivity
import com.laka.ergou.mvp.user.UserModuleNavigator
import com.laka.ergou.mvp.user.UserUtils.UserUtils
import com.laka.ergou.mvp.user.constant.UserConstant

/**
 * @Author:summer
 * @Date:2019/4/26
 * @Description: H5 页面拦截天猫超市-- 产品详情链接helper
 * 目前生成淘口令没有请求高佣链接，如果需要，后期补上
 */
class TmallHelper : View.OnClickListener {

    companion object {
        const val SEARCH_COUPON_FAIL = 0
        const val SEARCH_COUPON_LOADING = 0
        const val SEARCH_COUPON_SUCCESS = 0
    }

    private var mClickInterval: Long = 1000L
    private var mCreateTklClickTime: Long = 0L
    private var mReceiveCouponClickTime: Long = 0L
    private var mProductId: String = ""
    private var mRootView: ConstraintLayout
    private lateinit var mBottomSearchView: View
    private lateinit var mBottomCouponView: View
    private lateinit var mBgView: View
    private lateinit var mLlSearchAlert: ConstraintLayout
    private lateinit var mLlShare: View
    private lateinit var mLlCoupon: View
    private lateinit var mTopView: View
    private lateinit var mTvSearch: TextView
    private lateinit var mTvShareValue: TextView
    private lateinit var mTvCouponValue: TextView
    //商品详情
    private lateinit var mProductDetail: CustomProductDetail
    private lateinit var mHighVolumeInfoResponse: HighVolumeInfoResponse
    private var mPresenter: ITmallProductContract.ITmallProductPresenter = TmallProductPresenter()
    private var mContext: Activity

    constructor(mRootView: ConstraintLayout, mContext: Activity) {
        this.mRootView = mRootView
        this.mContext = mContext
        //presenter
        mPresenter.setView(TmallProductView())
    }

    private fun isProductDetailPage(url: String): InnerPageBean {
        var innerPager = InnerPageBean()
        if (!TextUtils.isEmpty(url)) {
            for (i in 0 until HomeApiConstant.URL_TMALL_PREFIX_LIST.size) {
                val bean = HomeApiConstant.URL_TMALL_PREFIX_LIST[i]
                if (url.contains(bean.host)) {
                    innerPager.isPager = true
                    innerPager.urlData = bean
                    break
                }
            }
        }
        return innerPager
    }

    private fun initSearchProductDetailUI() {
        if (!::mTopView.isInitialized || !::mBottomCouponView.isInitialized || !::mBottomSearchView.isInitialized) {
            mTopView = LayoutInflater.from(mContext).inflate(R.layout.tmall_h5detail_commission_alert, mRootView, false)
            val ivDelete = mTopView.findViewById<ImageView>(R.id.iv_commission_alert_delete)
            ivDelete.visibility = View.GONE //隐藏删除按钮
            val topLayoutP = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
            topLayoutP.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
            topLayoutP.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID
            topLayoutP.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
            topLayoutP.topMargin = ScreenUtils.dp2px(48f)
            //添加到倒数第二的位置，是为了能让 titleBar 覆盖住 mTopView
            mTopView.visibility = View.GONE
            mRootView.addView(mTopView, mRootView.childCount - 1, topLayoutP)

            mBottomCouponView = LayoutInflater.from(mContext).inflate(R.layout.tmall_h5detail_bottom_coupon, mRootView, false)
            mBottomSearchView = LayoutInflater.from(mContext).inflate(R.layout.tmall_h5detail_bottom_seek, mRootView, false)
            mTvShareValue = mBottomCouponView.findViewById(R.id.tv_share_value)
            mTvCouponValue = mBottomCouponView.findViewById(R.id.tv_coupon_value)
            mLlShare = mBottomCouponView.findViewById(R.id.ll_share)
            mLlCoupon = mBottomCouponView.findViewById(R.id.ll_coupon)
            mTvSearch = mBottomSearchView.findViewById(R.id.tv_search_txt)
            val bottomLayoutP = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ScreenUtils.dp2px(64f))
            bottomLayoutP.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
            bottomLayoutP.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
            bottomLayoutP.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID
            mBgView = View(mContext)
            mBgView.setOnClickListener { }
            mBgView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white))
            mRootView.addView(mBgView, bottomLayoutP)
            mRootView.addView(mBottomCouponView, bottomLayoutP)
            mRootView.addView(mBottomSearchView, bottomLayoutP)
            mBgView.visibility = View.GONE
            mBottomCouponView.visibility = View.GONE
            mBottomSearchView.visibility = View.GONE
            initBottomViewEvent()
        }
        if (::mBgView.isInitialized && mBgView.visibility == View.GONE) {
            Handler().postDelayed({
                if (!TextUtils.isEmpty(mProductId)) {
                    mBgView.visibility = View.VISIBLE
                }
            }, 200)
        }
        if (mTopView.visibility == View.GONE) {
            Handler().postDelayed({
                showTopViewForAnim()
            }, 200)
        }
        if (mBottomSearchView.visibility == View.GONE && mBottomCouponView.visibility == View.GONE) {
            Handler().postDelayed({
                showBottomSearchViewForAnim()
            }, 200)
        }
    }

    //======================================== UI动画操作 start ====================================

    private fun showTopViewForAnim() {
        if (TextUtils.isEmpty(mProductId)) return
        if (mTopView.visibility == View.VISIBLE) return
        val slideIn = AnimationUtils.loadAnimation(mContext, R.anim.anim_top_slide_in)
        mTopView.visibility = View.VISIBLE
        mTopView.startAnimation(slideIn)
    }

    private fun hideTopViewForAnim() {
        if (TextUtils.isEmpty(mProductId)) return
        if (mTopView.visibility == View.GONE) return
        val slideOut = AnimationUtils.loadAnimation(mContext, R.anim.anim_top_slide_out)
        mTopView.visibility = View.VISIBLE
        mTopView.startAnimation(slideOut)
        slideOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                mTopView.visibility = View.GONE
            }

            override fun onAnimationStart(animation: Animation?) {

            }
        })
    }

    private fun showBottomSearchViewForAnim() {
        if (TextUtils.isEmpty(mProductId)) return
        if (mBottomSearchView.visibility == View.VISIBLE) return
        val slideIn = AnimationUtils.loadAnimation(mContext, R.anim.anim_bottom_slide_in)
        mTvSearch.text = mContext.resources.getString(R.string.search_shop_coupon)
        mBottomSearchView.visibility = View.VISIBLE
        mBottomSearchView.startAnimation(slideIn)
        slideIn.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                if (!TextUtils.isEmpty(mProductId)) {
                    mPresenter.onLoadDetailData(mProductId)
                    mPresenter.onLoadHighVolumeCouponInfo(mProductId, ShopDetailConstant.TYPE_LOAD_COUPONINFO_FOR_NORMAL)
                }
            }

            override fun onAnimationStart(animation: Animation?) {

            }
        })
    }

    private fun hideBottomSearchViewForAnim() {
        if (TextUtils.isEmpty(mProductId)) return
        if (mBottomSearchView.visibility == View.GONE) return
        val slideOut = AnimationUtils.loadAnimation(mContext, R.anim.anim_bottom_slide_out)
        mBottomSearchView.visibility = View.VISIBLE
        mBottomSearchView.startAnimation(slideOut)
        slideOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                mBottomSearchView.visibility = View.GONE
            }

            override fun onAnimationStart(animation: Animation?) {

            }
        })
    }

    private fun showBottomCouponViewForAnim() {
        if (mBottomCouponView.visibility == View.VISIBLE) return
        if (!TextUtils.isEmpty(mProductId)) {
            val slideIn = AnimationUtils.loadAnimation(mContext, R.anim.anim_bottom_slide_in)
            mBottomCouponView.visibility = View.VISIBLE
            mBottomCouponView.startAnimation(slideIn)
        }
    }

    private fun hideBottomCouponViewForAnim() {
        if (mBottomCouponView.visibility == View.GONE) return
        if (TextUtils.isEmpty(mProductId)) return
        val slideOut = AnimationUtils.loadAnimation(mContext, R.anim.anim_bottom_slide_out)
        mBottomCouponView.visibility = View.VISIBLE
        mBottomCouponView.startAnimation(slideOut)
        slideOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                mBottomCouponView.visibility = View.GONE
            }

            override fun onAnimationStart(animation: Animation?) {

            }
        })
    }

    private fun hideBottomCouponViewAndShowSearchBottomView() {
        if (!TextUtils.isEmpty(mProductId)) {
            showBottomSearchViewForAnim()
            hideBottomCouponViewForAnim()
        }
    }

    private fun hideBottomSearchViewAndShowCouponBottomView() {
        if (!TextUtils.isEmpty(mProductId)) { // mProductId 为空则页面不是在详情页面
            showBottomCouponViewForAnim()
            hideBottomSearchViewForAnim()
        }
    }

    //======================================== UI动画操作 end ======================================

    /**
     * webview 页面加载完成
     * */
    fun onPageFinished(pageUrl: String) {
        var innerPager = isProductDetailPage(pageUrl)
        if (innerPager.isPager) { //详情页
            val params = RegexUtils.findParamsForUrl(pageUrl)
            val id = "${params[innerPager.urlData.key]}"
            //加判断是为了避免一个页面走多次 onPageFinished 方法导致接口请求多次的问题
            if ((TextUtils.isEmpty(mProductId) && !TextUtils.isEmpty(id)) || mProductId != id) {
                mProductId = id
                handleTmallProductDetail(mProductId)
            }
        } else {
            if (::mBottomCouponView.isInitialized && mBottomCouponView.visibility == View.VISIBLE) {
                hideBottomCouponViewForAnim()
            }
            if (::mBottomSearchView.isInitialized && mBottomSearchView.visibility == View.VISIBLE) {
                hideBottomSearchViewForAnim()
            }
            if (::mTopView.isInitialized && mTopView.visibility == View.VISIBLE) {
                hideTopViewForAnim()
            }
            if (::mBgView.isInitialized && mBgView.visibility == View.VISIBLE) {
                mBgView.visibility = View.GONE
            }
            mProductId = ""
        }
    }

    /**
     * 底部按钮点击事件
     * */
    private fun initBottomViewEvent() {
        if (::mLlShare.isInitialized) {
            mLlShare.setOnClickListener(this)
        }
        if (::mLlCoupon.isInitialized) {
            mLlCoupon.setOnClickListener(this)
        }
        if (::mBottomSearchView.isInitialized) {
            mBottomSearchView.setOnClickListener(this)
        }
    }

    private fun handleTmallProductDetail(productId: String) {
        if (!TextUtils.isEmpty(productId)) {
            initSearchProductDetailUI()
        } else {
            mTvSearch.text = mContext?.getString(R.string.tmall_search_coupon_fail)
        }
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ll_share -> { //复制淘口令
                onCreateTkl()
            }
            R.id.ll_coupon -> { // 返利购买
                onReceiveCoupons()
            }
            R.id.ll_search_alert -> {
                //如果查找失败，点击继续查找
                val tag = mTvSearch.tag as? Int
                tag?.let {
                    if (tag == SEARCH_COUPON_FAIL && !TextUtils.isEmpty(mProductId)) {
                        mTvSearch.tag = SEARCH_COUPON_LOADING
                        mPresenter.onLoadDetailData(mProductId)
                        mPresenter.onLoadHighVolumeCouponInfo(mProductId, ShopDetailConstant.TYPE_LOAD_COUPONINFO_FOR_NORMAL)
                        mTvSearch.text = mContext.resources.getString(R.string.search_shop_coupon)
                    }
                }
            }
        }
    }

    /**
     * 领券
     * */
    private fun onReceiveCoupons() {
        if (::mProductDetail.isInitialized) {
            if (isLoginOrTaoBaoAuthorForChannelId()) {
                if (::mHighVolumeInfoResponse.isInitialized && !TextUtils.isEmpty(mHighVolumeInfoResponse.coupon_click_url)) {
                    var couponUrl = "${mHighVolumeInfoResponse.coupon_click_url}"
                    if (System.currentTimeMillis() - mReceiveCouponClickTime > mClickInterval) {
                        mReceiveCouponClickTime = System.currentTimeMillis()
                        if (PackageUtils.isAppInstalled(mContext, PackageUtils.TAO_BAO)) {
                            jumpToCoupons(couponUrl)
                        } else {
                            jumpToCoupons(couponUrl)
                        }
                    }
                } else {
                    mPresenter.onLoadHighVolumeCouponInfo(mProductId, ShopDetailConstant.TYPE_LOAD_COUPONINFO_FOR_RECEIVE)
                }
            }
        } else {
            mPresenter.onLoadDetailData(mProductId)
        }
    }

    /**
     * 领券
     * */
    private fun jumpToCoupons(couponUrl: String) {
        val tbkParams = AlibcTaokeParams("", "", "")
        tbkParams.setPid("${UserUtils.getUserInfoBean()?.userBean?.adzone?.adzonepid}")
        tbkParams.setAdzoneid("${UserUtils.getUserInfoBean()?.userBean?.adzone?.adzone_id}")
        AliPageUtils.openAliPage(mContext, couponUrl, tbkParams)
    }

    /**
     * 复制淘口令
     * */
    private fun onCreateTkl() {
        if (::mProductDetail.isInitialized) {
            if (System.currentTimeMillis() - mCreateTklClickTime > mClickInterval) {
                if (isLoginOrTaoBaoAuthorForChannelId()) {
                    if (::mHighVolumeInfoResponse.isInitialized && mHighVolumeInfoResponse.share != null) {
                        mHighVolumeInfoResponse.share.productId = mProductDetail.num_iid
                        mHighVolumeInfoResponse.share.actualPrice = mProductDetail.actual_price
                        mHighVolumeInfoResponse.share.volume = mProductDetail.volume.toInt()
                        mHighVolumeInfoResponse.share.zkFinalPrice = mProductDetail.zk_final_price
                        mHighVolumeInfoResponse.share.couponMoney = mProductDetail.coupon_money
                        ShopShareModuleNavigator.startShopShareActivity(mContext, ShareResponse(mHighVolumeInfoResponse.share, mProductDetail.small_images))
                    } else {
                        mPresenter.onLoadHighVolumeCouponInfo(mProductId, ShopDetailConstant.TYPE_LOAD_COUPONINFO_FOR_SHARE)
                    }
                }
                mCreateTklClickTime = System.currentTimeMillis()
            }
        } else {
            mPresenter.onLoadDetailData(mProductId)
        }
    }

    /**
     * 登录判断逻辑
     * */
    private fun isLoginOrTaoBaoAuthorForChannelId(): Boolean {
        if (!isLogin()) {
            LogUtils.info("webview--------用户未登录")
            return false
        } else if (!AlibcLogin.getInstance().isLogin || TextUtils.isEmpty(UserUtils.getRelationId())) {
            LogUtils.info("webview--------淘宝为登录或者未绑定渠道ID")
            val authorConfim = TaoBaoAuthorDialog(mContext)
            authorConfim.setOnSureClickListener {
                mPresenter.onTaoBaoAuthor(mContext, ShopDetailActivity.TYPE_TAOBAO_AUTHOR_COUPON)
            }
            authorConfim.show()
            return false
        }
        return true
    }

    fun isLogin(): Boolean {
        if (!UserUtils.isLogin()) {
            LoginModuleNavigator.startLoginActivity(mContext)
            return false
        }
        return true
    }

    fun onViewDestroy() {
        mProductId = ""
        mPresenter.onViewDestroy()
    }

    inner class TmallProductView : ITmallProductContract.ITmallProductView {

        override fun showLoading() {

        }

        override fun dismissLoading() {

        }

        override fun showData(data: CustomProductDetail) {

        }

        override fun showErrorMsg(msg: String?) {

        }

        override fun onLoadDetailDataSuccess(result: CustomProductDetail) {
            LogUtils.info("webview------加载商品详情：$result")
            mTvSearch.tag = SEARCH_COUPON_FAIL
            mProductDetail = result
            mTvCouponValue.text = "返利购买（预省￥${BigDecimalUtils.add(ShopStringUtils.onMatchStr(result?.coupon_info), result.fanli)}）"
            mTvShareValue.text = "分享预赚￥${result.fanli}"
            hideBottomSearchViewAndShowCouponBottomView()
        }

        override fun onLoadDetailDataFail(errorMsg: String) {
            LogUtils.info("load---error----$errorMsg")
            mBottomCouponView.visibility = View.GONE
            mBottomSearchView.visibility = View.VISIBLE
            mTvSearch.text = mContext?.getString(R.string.tmall_search_coupon_fail)
            mTvSearch.tag = SEARCH_COUPON_FAIL
        }

        /**
         * 获取复制头口令信息成功
         * */
        override fun onLoadTPwdCreateSuccess(result: TPwdCreateResponse) {
            val pwd = result.model
            val shareWords = createShareWords(pwd)
            ClipBoardManagerHelper.getInstance.writeToClipBoardContent(shareWords)
            ToastHelper.showCenterToast("复制成功")
            ShareUtils.showShareDialog(mContext!!)
            //存储复制的淘口令
            ClipBoardManagerHelper.getInstance.setLocalCopyContent(shareWords)
        }

        /**
         * 淘宝授权成功
         * */
        override fun onTaoBaoAuthorSuccess(type: Int) {
            when (type) {
                ShopDetailActivity.TYPE_TAOBAO_AUTHOR_COMMON -> {

                }
                ShopDetailActivity.TYPE_TAOBAO_AUTHOR_COUPON -> {
                    if (TextUtils.isEmpty(UserUtils.getRelationId())) {
                        mPresenter.getUnionCodeUrl()
                    }
                }
            }
        }

        /**
         * 获取绑定淘宝渠道ID的H5链接成功
         * */
        override fun getUnionCodeUrlSuccess(url: String) {
            UserModuleNavigator.startBindUnionCodeActivityForResult(mContext, url, UserConstant.BIND_UNION_REQUEST_CODE)
        }

        /**
         * 绑定渠道ID 成功
         * */
        override fun handleUnionCodeSuccess() {
            ToastHelper.showCenterToast("淘宝授权成功")
            EventBusManager.postEvent(UserEvent(UserConstant.TAOBAO_AUTHOR_SUCCESS_EVENT))
            ShopDetailModuleNavigator.startTaoBaoAuthorSuccessActivity(mContext)
            mContext.overridePendingTransition(R.anim.anim_slide_in, R.anim.anim_slide_out)
            hideBottomCouponViewAndShowSearchBottomView()
            if (::mHighVolumeInfoResponse.isInitialized) {
                mHighVolumeInfoResponse.coupon_click_url = "" //清楚旧链接
            }
            mPresenter.onLoadHighVolumeCouponInfo(mProductId, ShopDetailConstant.TYPE_LOAD_COUPONINFO_FOR_NORMAL)
        }

        /**
         * 绑定渠道ID失败
         * */
        override fun handleUnionCodeFail() {
            ShopDetailModuleNavigator.startTaoBaoAuthorFailActivity(mContext)
            mContext.overridePendingTransition(R.anim.anim_slide_in, R.anim.anim_slide_out)
        }

        /**
         * 获取高佣链接成功
         * */
        override fun onLoadHighVolumeCouponInfoSuccess(result: HighVolumeInfoResponse, type: Int) {
            mHighVolumeInfoResponse = result
            when (type) {
                ShopDetailConstant.TYPE_LOAD_COUPONINFO_FOR_RECEIVE -> //领券
                    onReceiveCoupons()
                ShopDetailConstant.TYPE_LOAD_COUPONINFO_FOR_SHARE -> //分享
                    onCreateTkl()
                ShopDetailConstant.TYPE_LOAD_COUPONINFO_FOR_NORMAL -> {
                }
            }
        }

        /**
         * 获取高佣链接失败
         * */
        override fun onLoadHightVolumeCouponInfoFail(msg: String, type: Int) {
            when (type) {
                ShopDetailConstant.TYPE_LOAD_COUPONINFO_FOR_RECEIVE,
                ShopDetailConstant.TYPE_LOAD_COUPONINFO_FOR_SHARE -> //分享
                    ToastHelper.showCenterToast(msg)
                ShopDetailConstant.TYPE_LOAD_COUPONINFO_FOR_NORMAL -> {
                }
            }
        }
    }

    /**
     * activity 返回时调用
     * */
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == UserConstant.BIND_UNION_REQUEST_CODE
                && resultCode == UserConstant.BIND_UNION_RESULT_CODE) {
            val code = data?.getStringExtra(UserConstant.UNION_CODE)
            val state = data?.getStringExtra(UserConstant.UNION_STATE)
            if (!TextUtils.isEmpty(code) && !TextUtils.isEmpty(state)) {
                mPresenter.handleUnionCode(code!!, state!!)
            }
        }
    }

    private fun createShareWords(pwd: String): String {
        // 保存淘口令，用于过滤
        SPHelper.putString(ShopDetailConstant.TPWD_CREATE, "$pwd")
        return mPresenter.onCreateShareWorwds(pwd, mProductDetail)
    }

}