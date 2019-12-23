package com.laka.ergou.common.widget.helper

import android.content.Context
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.laka.androidlib.util.GlideUtil
import com.laka.androidlib.util.ListUtils
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.widget.page.MZBannerView
import com.laka.androidlib.widget.page.holder.MZViewHolder
import com.laka.ergou.R
import com.laka.ergou.common.widget.refresh.FrogRefreshRecyclerView
import com.laka.ergou.mvp.advertbanner.model.bean.AdvertBannerBean
import com.laka.ergou.mvp.main.RouterNavigator
import com.laka.ergou.mvp.main.constant.HomeConstant
import com.laka.ergou.mvp.main.constant.HomeNavigatorConstant

/**
 * @Author:summer
 * @Date:2019/7/23
 * @Description:
 */
class MZBannerHelper : ViewPager.OnPageChangeListener {

    private var mContext: Context
    private var mRootView: ViewGroup
    private var mBannerView: MZBannerView
    private var mBannerRootView: View
    private var mDataList: ArrayList<AdvertBannerBean>
    //true:则mRootView为RecyclerView
    //false:viewGroup
    private var mIsList: Boolean

    private constructor(builder: Builder) {
        if (builder.rootView == null) {
            throw RuntimeException("rootView 必须不为空")
        }
        if (builder.bannerView == null) {
            throw RuntimeException("bannerView 必须不为空")
        }
        if (builder.dataList == null) {
            throw RuntimeException("dataList 必须不为空")
        }
        this.mRootView = builder.rootView!!
        this.mBannerRootView = builder.bannerView!!
        this.mBannerView = mBannerRootView?.findViewById(R.id.banner_view)
        this.mDataList = builder.dataList!!
        this.mIsList = builder.isList
        this.mContext = builder.context
        init()
    }

    private fun init() {
        mBannerView.setCanLoop(false)
        mBannerView.setIndicatorVisible(true)
        mBannerView.setBannerPageClickListener { _, position ->
            val item = ListUtils.get(mDataList, position)
            item?.let {
                LogUtils.info("banner_item-----$item")
            }
        }
        mBannerView.addPageChangeListener(this)
        mBannerView.setPages(mDataList) {
            BannerViewHolder()
        }
        mBannerView.setBannerPageClickListener { _, position ->
            handleBannerRouter(position)
        }
        if (mIsList) {
            val recyclerView = mRootView as? FrogRefreshRecyclerView
            recyclerView?.let {
                recyclerView.addAdapterHeader(mBannerRootView)
            }
        } else {
            mRootView.addView(mBannerRootView, 0)
        }
    }

    /**
     * 处理页面跳转
     * */
    private fun handleBannerRouter(position: Int) {
        val banner = ListUtils.get(mDataList, position)
        banner?.let {
            val params = banner.sceneExtra ?: HashMap()
            params[HomeConstant.TITLE] = banner.title
            params[HomeNavigatorConstant.ROUTER_VALUE] = banner.sceneValue
            if (!params.containsKey(HomeConstant.TOPIC_BIG_IMAGE_URL)) {
                params[HomeConstant.TOPIC_BIG_IMAGE_URL] = banner.imgPath
            }
            val target = "${RouterNavigator.bannerRouterReflectMap[banner.sceneId.toInt()]}"
            RouterNavigator.handleAppInternalNavigator(mContext, target, params)
        }
    }

    /**
     * 更新数据源
     * */
    fun notifiDataSet(dataList: ArrayList<AdvertBannerBean>) {
        this.mDataList = dataList
        mBannerView.setPages(mDataList) {
            BannerViewHolder()
        }
    }

    fun setVisiable(visible: Int) {
        mBannerView.visibility = visible
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {

    }

    class BannerViewHolder : MZViewHolder<AdvertBannerBean> {
        private var mImageView: ImageView? = null

        override fun createView(context: Context?): View {
            // 返回页面布局文件
            val view = LayoutInflater.from(context).inflate(R.layout.banner_item_custom, null)
            mImageView = view?.findViewById(R.id.banner_image)
            return view
        }

        override fun onBind(context: Context?, position: Int, data: AdvertBannerBean?) {
            // 数据绑定
            data?.let {
                GlideUtil.loadImage(context, data.imgPath, R.drawable.default_img, mImageView)
            }
        }
    }

    fun onStart() {
        mBannerView.start()
    }

    fun onPause() {
        mBannerView.pause()
    }

    fun release() {
        //mBannerView.removePageChangeListener()
    }

    class Builder {
        var bannerView: View? = null
        var rootView: ViewGroup? = null
        var dataList: ArrayList<AdvertBannerBean>? = null
        //是否是添加到列表中
        var isList: Boolean = false
        var context: Context

        constructor(context: Context) {
            this.context = context
        }

        fun setIsList(isList: Boolean): Builder {
            this.isList = isList
            return this
        }

        fun setRootView(rootView: ViewGroup): Builder {
            this.rootView = rootView
            return this
        }

        fun setBannerView(bannerView: View): Builder {
            this.bannerView = bannerView
            return this
        }

        fun setData(list: ArrayList<AdvertBannerBean>): Builder {
            this.dataList = list
            return this
        }

        fun build(): MZBannerHelper {
            return MZBannerHelper(this)
        }
    }

}