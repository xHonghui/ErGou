package com.laka.ergou.mvp.shopping.center.helper

import android.content.Context
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.laka.androidlib.util.GlideUtil
import com.laka.androidlib.util.ListUtils
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.widget.page.MZBannerView
import com.laka.androidlib.widget.page.holder.MZViewHolder
import com.laka.ergou.R
import com.laka.ergou.mvp.main.RouterNavigator
import com.laka.ergou.mvp.main.constant.HomeConstant
import com.laka.ergou.mvp.main.constant.HomeNavigatorConstant
import com.laka.ergou.mvp.shopping.center.model.bean.BannerBean
import com.laka.ergou.mvp.shopping.center.weight.GradualChangeBgView

/**
 * @Author:summer
 * @Date:2019/5/8
 * @Description:首页banner工具类
 */
class HomeBannerHelper : ViewPager.OnPageChangeListener {

    private var mContext: Context
    private var mBannerView: MZBannerView
    private var mDataList: ArrayList<BannerBean> = ArrayList()
    private lateinit var mBgView: GradualChangeBgView
    private lateinit var mPageSelectedListener: ((position: Int, view: View, dataList: ArrayList<BannerBean>) -> Unit)
    private lateinit var mPageClickListener: ((position: Int, view: View, dataList: ArrayList<BannerBean>) -> Unit)

    constructor(context: Context, bannerView: MZBannerView, dataList: ArrayList<BannerBean>) {
        this.mContext = context
        this.mBannerView = bannerView
        this.mDataList = dataList
        mBannerView.setIndicatorVisible(false)
        initEvent()
    }

    private fun initEvent() {
        //这个监听器需要设置在 setPages 方法前，否则无效，bannerView 的bug吧，有空在改
        mBannerView.setBannerPageClickListener { _, position ->
            val banner = ListUtils.get(mDataList, position)
            banner?.let {
                handleRouter(banner.sceneId, banner.sceneValue, banner)
            }
        }
        mBannerView.addPageChangeListener(this)
        mBannerView.setPages(mDataList) {
            BannerViewHolder()
        }
        updateBgViewColor()
    }

    fun updateBannerData(dataList: ArrayList<BannerBean>): HomeBannerHelper {
        if (dataList.isEmpty()) return this
        this.mDataList = dataList
        mBannerView.pause()
        if (mDataList.size == 1) {
            mBannerView.setCanLoop(false)
        }else{
            mBannerView.setCanLoop(true)
        }
        mBannerView.setIndicatorVisible(false)
        mBannerView.setPages(mDataList) {
            BannerViewHolder()
        }
        updateBgViewColor()
        return this
    }

    /**
     * 页面切换监听
     * */
    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        if (position >= 0 && position < mDataList.size) {
            val bean = mDataList[position] as? BannerBean
            bean?.let {
                if (::mBgView.isInitialized) {
                    mBgView.setEndColor(it.upColor)
                    mBgView.setStartColor(it.downColor)
                }
            }
            if (::mPageSelectedListener.isInitialized) {
                mPageSelectedListener.invoke(position, mBannerView, mDataList)
            }
        }
    }

    private fun handleRouter(sceneId: String, sceneValue: String, banner: BannerBean) {
        val target = RouterNavigator.bannerRouterReflectMap[sceneId.toInt()].toString()
        var params = banner.sceneExtraParams ?: HashMap()
        params[HomeConstant.TITLE] = banner.title
        params[HomeNavigatorConstant.ROUTER_VALUE] = sceneValue
        if (!params.containsKey(HomeConstant.TOPIC_BIG_IMAGE_URL)) {
            params[HomeConstant.TOPIC_BIG_IMAGE_URL] = banner.bigImageUrl
        }
        RouterNavigator.handleAppInternalNavigator(mContext, target, params)
    }

    /**
     * 这里涉及到业务需求，因为首页背景墙图片需要跟随banner图片进行颜色切换
     * */
    fun bindBgView(bgView: GradualChangeBgView) {
        this.mBgView = bgView
        //第一个页面不触发 onPageSelected() 方法，所以需要手动设置
        updateBgViewColor()
    }

    /**
     * 设置背景颜色为第一个item的值
     * */
    private fun updateBgViewColor() {
        if (mDataList.isNotEmpty() && ::mBgView.isInitialized) {
            this.mBgView.setEndColor(mDataList[0].upColor)
            this.mBgView.setStartColor(mDataList[0].downColor)
        }
    }

    class BannerViewHolder : MZViewHolder<BannerBean> {
        private var mImageView: ImageView? = null

        override fun createView(context: Context?): View {
            // 返回页面布局文件
            val view = LayoutInflater.from(context).inflate(R.layout.banner_item, null)
            mImageView = view.findViewById(R.id.banner_image)
            return view
        }

        override fun onBind(context: Context?, position: Int, data: BannerBean?) {
            // 数据绑定
            data?.let {
                GlideUtil.loadImage(context, it.imgUrl, R.drawable.default_img, mImageView)
            }
        }
    }

    fun setOnPageClickListener(pageClickListener: ((position: Int, view: View, dataList: ArrayList<BannerBean>) -> Unit)) {
        this.mPageClickListener = pageClickListener
    }

    fun setOnPageSelectedListener(pageSelectedListener: ((position: Int, view: View, dataList: ArrayList<BannerBean>) -> Unit)) {
        this.mPageSelectedListener = pageSelectedListener
    }

    fun onPause() {
        mBannerView.pause()
    }

    fun onStart() {
        mBannerView.start()
    }

    fun release() {
        //mBannerView.removePageChangeListener()
    }

}