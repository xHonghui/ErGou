package com.laka.ergou.mvp.shopping.center.helper

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.laka.androidlib.util.GlideUtil
import com.laka.ergou.R
import com.laka.ergou.mvp.main.RouterNavigator
import com.laka.ergou.mvp.main.constant.HomeConstant
import com.laka.ergou.mvp.main.constant.HomeNavigatorConstant
import com.laka.ergou.mvp.shopping.center.model.bean.PromotionBean

/**
 * @Author:summer
 * @Date:2019/5/14
 * @Description:活动专区
 */
class PromotionHelper : View.OnClickListener {

    private var mContext: Context
    private lateinit var mRootView: View
    private lateinit var mIvLeft: ImageView
    private lateinit var mIvTop: ImageView
    private lateinit var mIvCenter: ImageView
    private lateinit var mIvRightBottom: ImageView
    private var mDataList: ArrayList<PromotionBean> = ArrayList()

    constructor(context: Context) {
        this.mContext = context
    }

    fun bindView(view: View): PromotionHelper {
        R.layout.layout_home_activity_region
        mRootView = view
        mIvLeft = mRootView.findViewById(R.id.iv_left)
        mIvTop = mRootView.findViewById(R.id.iv_top)
        mIvCenter = mRootView.findViewById(R.id.iv_center)
        mIvRightBottom = mRootView.findViewById(R.id.iv_right_bottom)
        initEvent()
        return this
    }

    private fun initEvent() {
        mIvLeft.setOnClickListener(this)
        mIvTop.setOnClickListener(this)
        mIvCenter.setOnClickListener(this)
        mIvRightBottom.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        //测试
//        val promotion = PromotionBean()
//        promotion.title = "天猫超市"
////        promotion.sceneValue = "http://chaoshi.tmall.com/?targetPage=index"  // 天猫超市
////        promotion.sceneValue = "http://ju.taobao.com/"   //聚划算
//        promotion.sceneValue = "https://pages.tmall.com/wow/import/17151/tmallglobal"  //天猫国际商城
//        promotion.sceneId = "9"
//        handleItemClick(promotion)

        if (mDataList.isNotEmpty()) {
            when (v?.id) {
                R.id.iv_left -> {
                    if (mDataList.isNotEmpty() && mDataList.size >= 1) {
                        handleItemClick(mDataList[0])
                    }
                }
                R.id.iv_top -> {
                    if (mDataList.isNotEmpty() && mDataList.size >= 2) {
                        handleItemClick(mDataList[1])
                    }
                }
                R.id.iv_center -> {
                    if (mDataList.isNotEmpty() && mDataList.size >= 3) {
                        handleItemClick(mDataList[2])
                    }
                }
                R.id.iv_right_bottom -> {
                    if (mDataList.isNotEmpty() && mDataList.size >= 4) {
                        handleItemClick(mDataList[3])
                    }
                }
                else -> {

                }
            }
        }
    }

    private fun handleItemClick(promotion: PromotionBean) {
        val target = RouterNavigator.bannerRouterReflectMap[promotion.sceneId.toInt()].toString()
        val params = promotion.sceneExtra ?: HashMap()
        params[HomeConstant.TITLE] = promotion.title
        params[HomeNavigatorConstant.ROUTER_VALUE] = promotion.sceneValue
        if (!params.containsKey(HomeConstant.TOPIC_BIG_IMAGE_URL)) {
            params[HomeConstant.TOPIC_BIG_IMAGE_URL] = promotion.imgUrl
        }
        RouterNavigator.handleAppInternalNavigator(mContext, target, params)
    }

    fun setData(dataList: ArrayList<PromotionBean>) {
        this.mDataList = dataList
        mIvLeft.setImageResource(R.drawable.default_img)
        mIvTop.setImageResource(R.drawable.default_img)
        mIvCenter.setImageResource(R.drawable.default_img)
        mIvRightBottom.setImageResource(R.drawable.default_img)
        if (mDataList.isNotEmpty() && mDataList.size >= 1) {
            GlideUtil.loadImage(mContext, mDataList[0].imgUrl, R.drawable.default_img, mIvLeft)
        }
        if (mDataList.isNotEmpty() && mDataList.size >= 2) {
            GlideUtil.loadImage(mContext, mDataList[1].imgUrl, R.drawable.default_img, mIvTop)
        }
        if (mDataList.isNotEmpty() && mDataList.size >= 3) {
            GlideUtil.loadImage(mContext, mDataList[2].imgUrl, R.drawable.default_img, mIvCenter)
        }
        if (mDataList.isNotEmpty() && mDataList.size >= 4) {
            GlideUtil.loadImage(mContext, mDataList[3].imgUrl, R.drawable.default_img, mIvRightBottom)
        }
    }

}