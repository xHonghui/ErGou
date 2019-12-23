package com.laka.ergou.mvp.shopping.center.view.adapter

import android.content.Context
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseViewHolder
import com.laka.androidlib.base.adapter.MultipleAdapterItem
import com.laka.androidlib.util.GlideUtil
import com.laka.androidlib.util.ListUtils
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.widget.page.MZBannerView
import com.laka.androidlib.widget.page.holder.MZViewHolder
import com.laka.ergou.R
import com.laka.ergou.mvp.advertbanner.model.bean.AdvertBannerBean
import com.laka.ergou.mvp.main.RouterNavigator
import com.laka.ergou.mvp.main.constant.HomeConstant
import com.laka.ergou.mvp.main.constant.HomeNavigatorConstant
import com.laka.ergou.mvp.shop.model.bean.AdvertBannerListBean

/**
 * @Author:summer
 * @Date:2019/7/23
 * @Description:商品详情页面 广告banner
 */
class ProductAdvertBannerItem : ViewPager.OnPageChangeListener, MultipleAdapterItem<AdvertBannerListBean> {

    private var mBannerView: MZBannerView? = null
    private var mDataList: ArrayList<AdvertBannerBean> = ArrayList()

    override fun convert(helper: BaseViewHolder?, item: AdvertBannerListBean?) {
        mBannerView = helper?.itemView?.findViewById(R.id.banner_view_advert) as? MZBannerView
        mDataList.clear()
        item?.data?.let {
            mDataList.addAll(it)
        }
        initEvent()
    }

    private fun initEvent() {
        mBannerView?.setIndicatorVisible(true)
        mBannerView?.setBannerPageClickListener { view, position ->
            LogUtils.info("banner_item-----------")
            handleBannerRouter(view.context, position)
        }
        mBannerView?.addPageChangeListener(this)
        mBannerView?.setPages(mDataList) {
            BannerViewHolder()
        }
    }

    /**
     * 更新数据源
     * */
    fun notifiDataSet(dataList: ArrayList<AdvertBannerBean>) {
        this.mDataList = dataList
        mBannerView?.setPages(mDataList) {
            BannerViewHolder()
        }
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
            val view = LayoutInflater.from(context).inflate(R.layout.item_banner_advert, null)
            mImageView = view.findViewById(R.id.banner_image)
            return view
        }

        override fun onBind(context: Context?, position: Int, data: AdvertBannerBean?) {
            // 数据绑定
            data?.let {
                GlideUtil.loadImage(context, data?.imgPath, R.drawable.default_img, mImageView)
            }
        }
    }

    fun onStart() {
        mBannerView?.start()
    }

    fun onPause() {
        mBannerView?.pause()
    }

    fun release() {
        //mBannerView?.removePageChangeListener()
    }

    /**
     * 处理页面跳转
     * */
    private fun handleBannerRouter(context: Context, position: Int) {
        val banner = ListUtils.get(mDataList, position)
        banner?.let {
            val params = banner.sceneExtra ?: HashMap()
            params[HomeConstant.TITLE] = banner.title
            params[HomeNavigatorConstant.ROUTER_VALUE] = banner.sceneValue
            if (!params.containsKey(HomeConstant.TOPIC_BIG_IMAGE_URL)) {
                params[HomeConstant.TOPIC_BIG_IMAGE_URL] = banner.imgPath
            }
            val target = "${RouterNavigator.bannerRouterReflectMap[banner.sceneId.toInt()]}"
            RouterNavigator.handleAppInternalNavigator(context, target, params)
        }
    }

}