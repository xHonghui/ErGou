package com.laka.ergou.mvp.shop.view.activity


import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Build
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.StaggeredGridLayoutManager
import android.text.TextUtils
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import cn.jzvd.Jzvd
import com.alibaba.baichuan.trade.biz.core.taoke.AlibcTaokeParams
import com.alibaba.baichuan.trade.biz.login.AlibcLogin
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.ViewSkeletonScreen
import com.laka.androidlib.base.activity.BaseMvpActivity
import com.laka.androidlib.eventbus.EventBusManager
import com.laka.androidlib.features.login.OnRequestListener
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.*
import com.laka.androidlib.util.screen.ScreenUtils
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.androidlib.widget.dialog.TaoBaoAuthorDialog
import com.laka.androidlib.widget.refresh.OnResultListener
import com.laka.androidlib.widget.refresh.RefreshRecycleView
import com.laka.ergou.BuildConfig
import com.laka.ergou.R
import com.laka.ergou.common.util.ClipBoardManagerHelper
import com.laka.ergou.common.widget.SpacesStaggeredDecoration
import com.laka.ergou.common.widget.refresh.FrogRefreshRecyclerView
import com.laka.ergou.mvp.advertbanner.adapter.AdvertBannerViewAdapter
import com.laka.ergou.mvp.advertbanner.constant.AdvertBannerConstant
import com.laka.ergou.mvp.advertbanner.model.bean.AdvertBannerBean
import com.laka.ergou.mvp.advertbanner.presenter.AdvertBannerPresenter
import com.laka.ergou.mvp.login.LoginModuleNavigator
import com.laka.ergou.mvp.login.constant.LoginConstant
import com.laka.ergou.mvp.login.model.bean.UserInfoBean
import com.laka.ergou.mvp.login.model.event.UserEvent
import com.laka.ergou.mvp.share.ShopShareModuleNavigator
import com.laka.ergou.mvp.share.model.bean.ShareResponse
import com.laka.ergou.mvp.shop.ShopDetailModuleNavigator
import com.laka.ergou.mvp.shop.constant.ShopDetailConstant
import com.laka.ergou.mvp.shop.contract.IShopDetailContract
import com.laka.ergou.mvp.shop.model.bean.*
import com.laka.ergou.mvp.shop.presenter.ShopDetailPresenter
import com.laka.ergou.mvp.shop.utils.AliPageUtils
import com.laka.ergou.mvp.shop.utils.BigDecimalUtils
import com.laka.ergou.mvp.shop.view.adapter.ShopDetailListAdapter
import com.laka.ergou.mvp.shop.weight.ProductDetailSwitchTitleTabView
import com.laka.ergou.mvp.shopping.center.model.bean.ProductWithCoupon
import com.laka.ergou.mvp.user.UserModuleNavigator
import com.laka.ergou.mvp.user.UserUtils.UserUtils
import com.laka.ergou.mvp.user.constant.UserConstant


/**
 * @Author:summer
 * @Date:2018/12/20
 * @Description:商品详情页面
 */
class ShopDetailActivity : BaseMvpActivity<CustomProductDetail>(), OnRequestListener<OnResultListener>, IShopDetailContract.IShopDetailView, View.OnClickListener {

    companion object {
        //淘宝授权类型，1：普通授权，2：绑定渠道ID授权，授权成功后，继续进行渠道ID的绑定
        const val TYPE_TAOBAO_AUTHOR_COMMON = 1
        const val TYPE_TAOBAO_AUTHOR_COUPON = 2
    }

    private lateinit var mRvList: FrogRefreshRecyclerView
    private lateinit var mFlBack: FrameLayout
    private lateinit var mIvBack: ImageView
    private lateinit var mIvToTop: ImageView
    private lateinit var mLlBottom: LinearLayout
    private lateinit var mClAlert: ConstraintLayout
    private lateinit var mTvCommissionAlert: TextView
    private lateinit var mIvAlertDelete: ImageView
    private lateinit var mTvShare: TextView
    private lateinit var mTvReceiveCoupon: TextView
    private lateinit var mTvCouponTxt: TextView
    private lateinit var mClRootView: ConstraintLayout
    private lateinit var mLlShare: LinearLayout
    private lateinit var mLlCoupon: LinearLayout
    private lateinit var mTabTypeView: ProductDetailSwitchTitleTabView
    private lateinit var mAllProductDetail: CustomProductDetail
    private lateinit var mProductDetailVideo: ProductDetailVideos
    private lateinit var mBannerImageList: ProductBannerList
    private lateinit var mProductMore: TitleTypeBean
    private lateinit var mRecommendTitle: TitleTypeBean
    private lateinit var mShopDetailFirstListBean: ShopDetailListBean
    private lateinit var mShopAdapter: ShopDetailListAdapter
    private lateinit var mSellerDetail: SellerBean
    private var mAdvertBannerData: AdvertBannerListBean = AdvertBannerListBean()
    private var mRecommendDataList: ArrayList<ProductWithCoupon> = ArrayList()
    private var mResultListener: OnResultListener? = null
    private var mProductId: String = ""
    private var mEntrance: Int = -1
    private val mDataList: ArrayList<MultiItemEntity> = ArrayList()
    private var mImageDetailList: ArrayList<ImageDetail> = ArrayList()
    private lateinit var mCouponHighVolumeInfoBean: HighVolumeInfoResponse
    //“滑动到顶部”按钮控制参数
    private var mTotalDy = 0F
    private val mListScrollControlDis = ScreenUtils.dp2px(360F)
    private var mHostMap: HashMap<String, String> = HashMap()
    private lateinit var mShopPresenter: ShopDetailPresenter
    private lateinit var mBannerPresenter: AdvertBannerPresenter

    override fun attachBaseContext(newBase: Context?) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            super.attachBaseContext(newBase)
        } else {
            super.attachBaseContext(object : ContextWrapper(newBase) {
                override fun getSystemService(name: String?): Any {
                    if (Context.AUDIO_SERVICE == name) {
                        //创建AudioService的时候，使用全局的上下文
                        return ApplicationUtils.getApplication().getSystemService(name)
                    }
                    return super.getSystemService(name)
                }
            })
        }
    }

    private var isActive: Boolean = false

    /**activity任务栈中调用，用来清除前面打开的Fragment*/
    fun finish(active: Boolean) {
        isActive = active
        finish()
    }

    override fun finish() {
        if (!isActive) {
            //移除最后一个activity
            ShopDetailModuleNavigator.removeElementForActivityStack()
        } else {
            //移除第一个
            isActive = false
            ShopDetailModuleNavigator.removeElementForActivityStack(0)
        }
        super.finish()
    }

    override fun onStart() {
        super.onStart()
        mShopAdapter.onStart()
    }

    override fun onPause() {
        mShopAdapter.onPause()
        super.onPause()
        Jzvd.releaseAllVideos()
    }

    override fun onBackPressed() {
        Jzvd.backPress()
        super.onBackPressed()
    }

    override fun onDestroy() {
        mShopAdapter.release()
        super.onDestroy()
    }

    override fun showData(data: CustomProductDetail) {
        dismissLoading()
    }

    override fun showErrorMsg(msg: String?) {
        dismissLoading()
    }

    override fun setStatusBarColor(color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarUtil.setTranslucentForImageView(this)
        } else {
            super.setStatusBarColor(color)
        }
    }

    override fun createPresenter(): IBasePresenter<*> {
        mBannerPresenter = AdvertBannerPresenter()
        mBannerPresenter.setView(advertBannerView)
        mShopPresenter = ShopDetailPresenter()
        return mShopPresenter
    }

    override fun setContentView(): Int {
        return R.layout.activity_main_shop
    }

    override fun initIntent() {
        intent?.extras?.let {
            mEntrance = it.getInt(ShopDetailConstant.ENTRANCE, 1)
            mProductId = it.getString(ShopDetailConstant.PRODUCT_ID)
        }
    }

    override fun initViews() {
        //添加到任务管理栈中
        ShopDetailModuleNavigator.addElementForActivityStack(this)
        Jzvd.SAVE_PROGRESS = false//视频播放器设置不保存播放进度
        mRvList = findViewById(R.id.rv_list)
        mFlBack = findViewById(R.id.fl_back)
        mIvBack = findViewById(R.id.iv_back)
        mIvToTop = findViewById(R.id.iv_to_top)
        mLlBottom = findViewById(R.id.ll_bottom)
        mClAlert = findViewById(R.id.cl_top_alert)
        mTvCommissionAlert = findViewById(R.id.tv_commission_alert)
        mIvAlertDelete = findViewById(R.id.iv_commission_alert_delete)
        mLlShare = findViewById(R.id.ll_share)
        mLlCoupon = findViewById(R.id.ll_coupon)
        mTvShare = findViewById(R.id.tv_share_value)
        mTvReceiveCoupon = findViewById(R.id.tv_coupon_value)
        mTvCouponTxt = findViewById(R.id.tv_coupon)
        mClRootView = findViewById(R.id.cl_root_view)
        mTabTypeView = findViewById(R.id.tab_type_view)
        mShopAdapter = ShopDetailListAdapter(mDataList)
        mRvList.addItemDecoration(SpacesStaggeredDecoration(object : SpacesStaggeredDecoration.ItemDecorationCallBack {
            override fun getItemType(position: Int?): Int? {
                return mShopAdapter.getItemViewType(position!!)
            }
        }, true, ResourceUtils.getDimen(R.dimen.dp_10), ResourceUtils.getDimen(R.dimen.dp_6)))
        mRvList.setOnRequestListener(this)
        mRvList.onItemClickListener = mOnItemClickListener
        mRvList.enableRefresh(false)
        mRvList.enableLoadMore(false)
        mRvList.setLayoutManager(StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL))
        mRvList.adapter = mShopAdapter
        mRvList.refresh(false) //加载详情
        //设置statusBar高度
        mTabTypeView.setStatusBarHeight(StatusBarUtil.getStatusBarHeight(this))
    }

    private lateinit var mSkeleton: ViewSkeletonScreen

    override fun initData() {

    }

    override fun initEvent() {
        mFlBack.setOnClickListener(this)
        mIvToTop.setOnClickListener(this)
        mLlShare.setOnClickListener(this)
        mLlCoupon.setOnClickListener(this)
        mIvAlertDelete.setOnClickListener(this)
        //显示骨架框
        mSkeleton = Skeleton.bind(mClRootView)
                .load(R.layout.skeleton_activity_prodect_detail)
                .duration(1000)
                .color(R.color.shimmer_color)
                .angle(0)
                .show()
        mRvList.onScrollListener = RefreshRecycleView.OnScrollListener { recyclerView, dx, dy, _, lastVisibleItemViewType ->
            mTotalDy += dy
            // 纯粹累加 dy，最后得出的总和可能为负数，这样是不合理的，所以当 mTotalDy<0 时，将其置为 0
            mTotalDy = if (mTotalDy < 0) 0f else mTotalDy
            if (mTotalDy >= mListScrollControlDis) {
                mIvToTop.visibility = View.VISIBLE
            } else {
                mIvToTop.visibility = View.GONE
            }
            //使用当前可显示区域内最后一个显示 item 的 viewType
            mTabTypeView.onScroll(recyclerView, dx, dy, lastVisibleItemViewType) {
                if (it >= 0.5) {
                    StatusBarUtil.setLightMode(this)
                } else {
                    StatusBarUtil.setDarkMode(this)
                }
            }
        }
        mTabTypeView.bindRecyclerView(mRvList)
    }

    override fun onRequest(page: Int, resultListener: OnResultListener): String {
        mResultListener = resultListener
        if (UserUtils.isLogin()) {
            mShopPresenter.onLoadHighVolumeCouponInfo(mProductId, ShopDetailConstant.TYPE_LOAD_COUPONINFO_FOR_NORMAL)
        }
        mShopPresenter.onLoadProductDetail(mProductId)
        return ""
    }

    /**获取产品详情成功*/
    private fun updateProductDetail() {
        mProductDetailVideo = mAllProductDetail.productVideo
        //保存商店详情
        mSellerDetail = mAllProductDetail.seller
        mShopDetailFirstListBean = ShopDetailListBean()
        mShopDetailFirstListBean.setDataList(mDataList)
        if (mAllProductDetail.small_images != null && mAllProductDetail.small_images.imageList.size > 0) {
            val imageList = ArrayList<ProductBannerBean>()
            mAllProductDetail.small_images?.imageList?.forEach {
                imageList.add(ProductBannerBean(picture = it))
            }
            mBannerImageList = ProductBannerList(imageList, ShopDetailConstant.SHOP_DETAIL_BANNER)
            //加上第一张商品图片作为第一张banner图片
            mBannerImageList.imageList.add(0, ProductBannerBean(picture = mAllProductDetail?.pict_url))
        } else {
            mBannerImageList = ProductBannerList(arrayListOf(), ShopDetailConstant.SHOP_DETAIL_BANNER)
            mBannerImageList.imageList.add(0, ProductBannerBean(picture = mAllProductDetail?.pict_url))
        }
        if (!TextUtils.isEmpty(mAllProductDetail?.productVideo?.videoId)) {
            mBannerImageList.imageList.add(0,
                    ProductBannerBean(mAllProductDetail?.productVideo?.videoId,
                            mAllProductDetail?.productVideo?.videoUrl,
                            mAllProductDetail?.productVideo?.videoThumbnailURL))
        }
        mAllProductDetail.uiType = ShopDetailConstant.SHOP_DETAIL_BASIC
        mProductMore = TitleTypeBean(ShopDetailConstant.SHOP_DETAIL_MORE, getString(R.string.shop_detail_more_item_title))
        mImageDetailList = mAllProductDetail.image_detail
        mImageDetailList.forEach { it.uiType = ShopDetailConstant.SHOP_DETAIL_IMAGE_DETAIL }
        mRecommendTitle = TitleTypeBean(ShopDetailConstant.SHOP_DETAIL_RECOMMEND_TITLE, getString(R.string.shop_recommend_title))
        mTvReceiveCoupon.text = "预省¥${BigDecimalUtils.add(mAllProductDetail?.coupon_money, mAllProductDetail.fanli)}"
        mTvReceiveCoupon.visibility = View.VISIBLE
        mTvCouponTxt.text = "领券购买"
        mTvShare.text = "预赚¥${mAllProductDetail.fanli}"
        setListData()
    }

    private fun setListData() {
        mDataList.clear()
        //隐藏骨架框
        if (::mSkeleton.isInitialized) {
            mSkeleton.hide()
        }
        if (::mBannerImageList.isInitialized) {
            mDataList.add(mBannerImageList)
        }
        if (::mAllProductDetail.isInitialized) {
            mDataList.add(mAllProductDetail)
        }
        if (mAdvertBannerData?.data != null
                && mAdvertBannerData?.data?.size > 0) {
            mDataList.add(mAdvertBannerData)
        }
        if (::mSellerDetail.isInitialized
                && StringUtils.isNotEmpty(mSellerDetail.shopName)) {
            mDataList.add(mSellerDetail)
        }
        if (::mProductMore.isInitialized) {
            mDataList.add(mProductMore)
        }
        if (mImageDetailList?.size > 0) {
            mProductMore.open = 1
            mDataList.addAll(mImageDetailList)
        }
        if (mRecommendDataList?.size > 0) {
            mDataList.add(mRecommendTitle)
            mDataList.addAll(mRecommendDataList)
        }
        // 刷新数据后，隐藏加载控件和加载错误控件，
        // 防止遮挡到RecyclerView
        mRvList.hideLoadingView()
        mRvList.hideErrorView()
        mShopAdapter.replaceData(mDataList)
        mRvList.notifyDataSetChanged()
    }

    /**加载列表数据失败，显示相应加载错误或者网络链接失败的UI*/
    override fun onLoadListDataFail() {
        if (mShopAdapter.data == null || mShopAdapter.data.size == 0) {
            mResultListener?.onFailure(-1, "")
        }
    }

    override fun onInternetChange(isLostInternet: Boolean) {
        if (!isLostInternet) { //切换为有网络状态，刷新数据
            LogUtils.info("网络状态切换：$isLostInternet")
            mShopPresenter.onLoadProductDetail(mProductId)
            if (UserUtils.isLogin()) {
                mShopPresenter.onLoadHighVolumeCouponInfo(mProductId, ShopDetailConstant.TYPE_LOAD_COUPONINFO_FOR_NORMAL)
            }
        }
    }

    private val mOnItemClickListener = object : RefreshRecycleView.OnItemClickListener<MultiItemEntity> {
        override fun onItemClick(data: MultiItemEntity?, position: Int) {
            onItemClick(position)
        }

        override fun onChildClick(id: Int, data: MultiItemEntity?, position: Int) {

        }
    }

    private fun onItemClick(position: Int) {
        when (mShopAdapter.getItemViewType(position)) {
            ShopDetailConstant.SHOP_DETAIL_RECOMMEND_ITEM -> {
                val entity = mShopAdapter.getItem(position) as? ProductWithCoupon
                entity?.let {
                    ShopDetailModuleNavigator.startShopDetailActivity(this, "${entity.productId}")
                }
            }
            ShopDetailConstant.SHOP_DETAIL_MORE -> {
                //如果没有商品详情图片，则点击“查看商品详情”item，走领券的流程
                if (mImageDetailList.isEmpty()) {
                    onReceiveCoupons()
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fl_back -> {
                finish()//后退
            }
            R.id.ll_share -> {  // 分享
                onShopShare(true)
            }
            R.id.ll_coupon -> { // 领券
                onReceiveCoupons()
            }
            R.id.iv_to_top -> { // 滑动到顶部
                mRvList.scrollToTop()
            }
            R.id.iv_commission_alert_delete -> { // 补贴提示栏
                mClAlert.visibility = View.GONE
            }
        }
    }

    private fun onShopShare(isClick: Boolean = false) {
        if (!::mAllProductDetail.isInitialized) {
            mShopPresenter.onLoadProductDetail(mProductId)
            return
        }
        if (!isLoginOrTaoBaoAuthorForChannelId()) return
        if (!::mCouponHighVolumeInfoBean.isInitialized || mCouponHighVolumeInfoBean.share == null) {
            showLoading()
            mShopPresenter.onLoadHighVolumeCouponInfo(mProductId, ShopDetailConstant.TYPE_LOAD_COUPONINFO_FOR_SHARE)
            return
        }
        if (TextUtils.isEmpty(mCouponHighVolumeInfoBean.share?.tklShareUrl)) {
            if (isClick) {
                showLoading()
                mShopPresenter.onLoadHighVolumeCouponInfo(mProductId, ShopDetailConstant.TYPE_LOAD_COUPONINFO_FOR_SHARE)
            } else {
                ToastHelper.showToast("获取分享淘口令url为空，请稍后重试")
            }
            return
        }
        handleShopShareForData()
    }

    private fun handleShopShareForData() {
        if (MultiClickUtil.checkClickValid(R.id.ll_share)) {
            mCouponHighVolumeInfoBean.share.productId = mAllProductDetail.num_iid
            mCouponHighVolumeInfoBean.share.actualPrice = mAllProductDetail.actual_price
            mCouponHighVolumeInfoBean.share.volume = mAllProductDetail.volume.toInt()
            mCouponHighVolumeInfoBean.share.zkFinalPrice = mAllProductDetail.zk_final_price
            mCouponHighVolumeInfoBean.share.couponMoney = mAllProductDetail.coupon_money
            val smallImage = SmallImages()
            smallImage.imageList.addAll(mAllProductDetail.small_images.imageList)
            smallImage.imageList.add(0, mAllProductDetail?.pict_url)
            if (smallImage.imageList != null && smallImage.imageList.size > 0) {
                ShopShareModuleNavigator.startShopShareActivity(this, ShareResponse(mCouponHighVolumeInfoBean.share, smallImage))
            } else {
                ToastHelper.showToast("没有商品图片，无法进行商品分享，请选择其他商品")
            }
        } else {
            if (BuildConfig.DEBUG) {
                ToastHelper.showToast("您点太快了哟！")
            }
        }
    }

    private fun onReceiveCoupons() {
        if (!::mAllProductDetail.isInitialized) {
            mShopPresenter.onLoadProductDetail(mProductId)
            return
        }
        if (!isLoginOrTaoBaoAuthorForChannelId()) return
        if (!::mCouponHighVolumeInfoBean.isInitialized || TextUtils.isEmpty(mCouponHighVolumeInfoBean.coupon_click_url)) {
            showLoading()
            mShopPresenter.onLoadHighVolumeCouponInfo(mProductId, ShopDetailConstant.TYPE_LOAD_COUPONINFO_FOR_RECEIVE)
            return
        }
        if (MultiClickUtil.checkClickValid(R.id.ll_coupon)) {
            //进入淘宝领券页面前，清空本地粘贴板数据
            ClipBoardManagerHelper.getInstance.clearClipBoardContentForHasTkl()
            if (PackageUtils.isAppInstalled(this, PackageUtils.TAO_BAO)) {
                jumpToCoupons("${mCouponHighVolumeInfoBean.coupon_click_url}", false)
            } else {
                //jumpToCoupons("${mCouponHighVolumeInfoBean.coupon_click_url}", true)
                ToastHelper.showToast("未检测淘宝客户端，请安装后重试！")
            }
        } else {
            if (BuildConfig.DEBUG) {
                ToastHelper.showToast("您点太快了哟！")
            }
        }
    }

    //打开淘宝领券页面
    private fun jumpToCoupons(couponUrl: String, isH5: Boolean) {
        val userInfoBean = SPHelper.getObject(LoginConstant.USER_INFO_FILENAME, LoginConstant.USER_LOGIN_INFO, UserInfoBean::class.java)
        val tbkParams = AlibcTaokeParams("", "", "")
        val pid = "${userInfoBean?.userBean?.adzone?.adzonepid}"
        val adzoneId = "${userInfoBean?.userBean?.adzone?.adzone_id}"
        tbkParams.setPid(pid)
        tbkParams.setAdzoneid(adzoneId)
        if (isH5) {
            //AliPageUtils.openAliPageForAuto(this, couponUrl, tbkParams)
            ShopDetailModuleNavigator.startReceiveCouponActivity(this, couponUrl, pid, adzoneId)
        } else {
            AliPageUtils.openAliPage(this, couponUrl, tbkParams)
        }
    }

    //======================================= login ===============================================
    fun isLogin(): Boolean {
        if (!UserUtils.isLogin()) {
            LoginModuleNavigator.startLoginActivity(this)
            return false
        }
        return true
    }

    private fun isLoginOrTaoBaoAuthorForChannelId(): Boolean {
        if (!isLogin()) {
            return false
        } else if (!AlibcLogin.getInstance().isLogin) {
            val authorConfirm = TaoBaoAuthorDialog(this)
            authorConfirm.setOnSureClickListener {
                mShopPresenter.onTaoBaoAuthor(TYPE_TAOBAO_AUTHOR_COUPON)
            }
            authorConfirm.show()
            return false
        } else if (TextUtils.isEmpty(UserUtils.getRelationId())) {
            mShopPresenter.getUnionCodeUrl()
            return false
        }
        return true
    }

    //========================================= V层接口实现 =======================================

    /**获取产品基本详情、banner图等信息*/
    override fun onLoadProductDetailSuccess(result: CustomProductDetail) {
        if (!::mAllProductDetail.isInitialized) {
            mAllProductDetail = result
        }
        mShopPresenter.onGetProductDetailH5ServiceUrl(mProductId)
    }

    /**获取产品详情失败，并已显示失败页面*/
    override fun onLoadProductDetailFail() {

    }

    override fun onLoadRecommendDataSuccess(list: ArrayList<ProductWithCoupon>) {
        mRecommendDataList.clear()
        list.forEach {
            it.uiType = ShopDetailConstant.SHOP_DETAIL_RECOMMEND_ITEM
        }
        mRecommendDataList.addAll(list)
        setListData()
    }

    override fun onLoadHighVolumeCouponInfoSuccess(result: HighVolumeInfoResponse, type: Int) {
        mCouponHighVolumeInfoBean = result
        when (type) {
            ShopDetailConstant.TYPE_LOAD_COUPONINFO_FOR_RECEIVE -> //领券
                onReceiveCoupons()
            ShopDetailConstant.TYPE_LOAD_COUPONINFO_FOR_SHARE -> //分享
                onShopShare()
            ShopDetailConstant.TYPE_LOAD_COUPONINFO_FOR_NORMAL -> {
            }
        }
    }

    override fun onLoadHighVolumeCouponInfoFail(msg: String, type: Int) {
        when (type) {
            ShopDetailConstant.TYPE_LOAD_COUPONINFO_FOR_RECEIVE,
            ShopDetailConstant.TYPE_LOAD_COUPONINFO_FOR_SHARE -> //分享
                ToastHelper.showCenterToast(msg)
            ShopDetailConstant.TYPE_LOAD_COUPONINFO_FOR_NORMAL -> {
            }
        }
    }

    override fun onTaoBaoAuthorSuccess(type: Int) {
        when (type) {
            TYPE_TAOBAO_AUTHOR_COUPON -> {
                if (TextUtils.isEmpty(UserUtils.getRelationId())) {
                    mShopPresenter.getUnionCodeUrl()
                }
            }
        }
    }

    override fun getUnionCodeUrlSuccess(url: String) {
        UserModuleNavigator.startBindUnionCodeActivityForResult(this, url, UserConstant.BIND_UNION_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UserConstant.BIND_UNION_REQUEST_CODE
                && resultCode == UserConstant.BIND_UNION_RESULT_CODE) {
            val code = data?.getStringExtra(UserConstant.UNION_CODE)
            val state = data?.getStringExtra(UserConstant.UNION_STATE)
            if (!TextUtils.isEmpty(code) && !TextUtils.isEmpty(state)) {
                mShopPresenter.handleUnionCode(code!!, state!!)
            }
        }
    }

    /**绑定淘宝渠道ID成功*/
    override fun handleUnionCodeSuccess() {
        ToastHelper.showCenterToast("淘宝授权成功")
        EventBusManager.postEvent(UserEvent(UserConstant.TAOBAO_AUTHOR_SUCCESS_EVENT))
        ShopDetailModuleNavigator.startTaoBaoAuthorSuccessActivity(this)
        overridePendingTransition(R.anim.anim_slide_in, R.anim.anim_slide_out)
        //渠道ID绑定成功，则高佣链接会变化，需要刷新列表
        if (::mCouponHighVolumeInfoBean.isInitialized) {
            mCouponHighVolumeInfoBean.coupon_click_url = "" //清楚旧链接
        }
        mShopPresenter.onLoadHighVolumeCouponInfo(mProductId, ShopDetailConstant.TYPE_LOAD_COUPONINFO_FOR_NORMAL)
    }

    override fun handleUnionCodeFail() {
        ShopDetailModuleNavigator.startTaoBaoAuthorFailActivity(this)
        overridePendingTransition(R.anim.anim_slide_in, R.anim.anim_slide_out)
    }

    /**获取 h5 服务地址成功*/
    override fun onGetProductDetailH5ServiceUrlSuccess(resultMap: HashMap<String, String>) {
        mHostMap = resultMap
        val resultUrl = resultMap["url"]
        mShopPresenter.onGetProductDetailForH5Url("$resultUrl")
    }

    /**获取H5 url 失败，已进行失败UI处理*/
    override fun onGetProductDetailH5ServiceUrlFail(msg: String) {

    }

    /**
     * 请求H5 url 获取详情成功，则result 中含有商家、视频、banner图等数据
     * 先请求我们自己服务器的商品详情接口，然后再去请求h5 链接的商品详情
     * 统一保存在 mAllProductDetail 中
     * */
    override fun onGetProductDetailForH5UrlSuccess(result: CustomProductDetail) {
        LogUtils.info("detail-------${result.small_images}")
        if (::mAllProductDetail.isInitialized) {
            mAllProductDetail.seller = result.seller
            mAllProductDetail.productVideo = result.productVideo
            mAllProductDetail.freeShipping = result.freeShipping
            //mAllProductDetail.small_images = result.small_images
        } else {
            mAllProductDetail = result
        }
        updateProductDetail()
        if (!TextUtils.isEmpty(result.productImageDetailUrl)) {
            mShopPresenter.onGetProductDetailImageList(result.productImageDetailUrl)
        } else {
            mShopPresenter.onGetProductDetailImageList2("${mHostMap["get_desc"]}")
        }
        //获取广告banner
        mBannerPresenter.onLoadAdvertBannerData(AdvertBannerConstant.TYPE_BANNER_CLASSID_PRODUCT_DETAIL)
    }

    /**通过h5 url获取详情失败，已进行失败UI处理*/
    override fun onGetProductDetailForH5UrlFail(msg: String) {

    }

    /**获取产品详情图片成功*/
    override fun onGetProcutDetailImageListSuccess(imageDetailList: ArrayList<ImageDetail>) {
        mAllProductDetail.image_detail = imageDetailList
        mImageDetailList = mAllProductDetail.image_detail
        mImageDetailList.forEach { it.uiType = ShopDetailConstant.SHOP_DETAIL_IMAGE_DETAIL }
        setListData()
        onLoadRecommendData()
    }

    override fun onGetProcutDetailImageListFail(msg: String) {
        onLoadRecommendData()
    }

    /**相关推荐*/
    private fun onLoadRecommendData() {
        if (::mAllProductDetail.isInitialized) {
            mShopPresenter.onLoadRecommendData(this, mProductId)
        }
    }

    /**广告banner*/
    private val advertBannerView = object : AdvertBannerViewAdapter() {
        override fun onLoadAdvertBannerDataSuccess(bannerList: ArrayList<AdvertBannerBean>) {
            mAdvertBannerData.data = bannerList
            setListData()
        }

        override fun onLoadAdvertBannerDataFail(msg: String) {

        }
    }

}