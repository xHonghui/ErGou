package com.laka.ergou.mvp.shopping.center.helper

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.laka.androidlib.util.LogUtils
import com.laka.ergou.R
import com.laka.ergou.mvp.main.RouterNavigator
import com.laka.ergou.mvp.main.constant.HomeConstant
import com.laka.ergou.mvp.main.constant.HomeNavigatorConstant
import com.laka.ergou.mvp.shopping.center.model.bean.TopicBean

/**
 * @Author:summer
 * @Date:2019/5/13
 * @Description:兼容topic item数量在0-5之间变动的情况
 */
class TopicItemHelper : View.OnClickListener {

    private var mContext: Context
    private lateinit var mRootView: View
    private lateinit var mLlRootView: View
    private lateinit var mIvImage1: ImageView
    private lateinit var mTvTxt1: TextView
    private lateinit var mIvImage2: ImageView
    private lateinit var mTvTxt2: TextView
    private lateinit var mIvImage3: ImageView
    private lateinit var mTvTxt3: TextView
    private lateinit var mIvImage4: ImageView
    private lateinit var mTvTxt4: TextView
    private lateinit var mIvImage5: ImageView
    private lateinit var mTvTxt5: TextView
    private lateinit var mClNode1: ConstraintLayout
    private lateinit var mClNode2: ConstraintLayout
    private lateinit var mClNode3: ConstraintLayout
    private lateinit var mClNode4: ConstraintLayout
    private lateinit var mClNode5: ConstraintLayout
    private var mDataList: ArrayList<TopicBean> = ArrayList()

    constructor(context: Context) {
        this.mContext = context
    }

    fun bindTopicView(rootView: View) {
        mRootView = rootView
        mLlRootView = mRootView.findViewById(R.id.ll_root_view)
        mIvImage1 = mRootView.findViewById(R.id.iv_img1)
        mIvImage2 = mRootView.findViewById(R.id.iv_img2)
        mIvImage3 = mRootView.findViewById(R.id.iv_img3)
        mIvImage4 = mRootView.findViewById(R.id.iv_img4)
        mIvImage5 = mRootView.findViewById(R.id.iv_img5)
        mTvTxt1 = mRootView.findViewById(R.id.tv_txt1)
        mTvTxt2 = mRootView.findViewById(R.id.tv_txt2)
        mTvTxt3 = mRootView.findViewById(R.id.tv_txt3)
        mTvTxt4 = mRootView.findViewById(R.id.tv_txt4)
        mTvTxt5 = mRootView.findViewById(R.id.tv_txt5)
        mClNode1 = mRootView.findViewById(R.id.cl_node1)
        mClNode2 = mRootView.findViewById(R.id.cl_node2)
        mClNode3 = mRootView.findViewById(R.id.cl_node3)
        mClNode4 = mRootView.findViewById(R.id.cl_node4)
        mClNode5 = mRootView.findViewById(R.id.cl_node5)
        initEvent()
    }

    private fun initEvent() {
        mClNode1.setOnClickListener(this)
        mClNode2.setOnClickListener(this)
        mClNode3.setOnClickListener(this)
        mClNode4.setOnClickListener(this)
        mClNode5.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        var params: HashMap<String, String>
        var topicBean: TopicBean? = null
        when (view?.id) {
            R.id.cl_node1 -> {
                if (mDataList.size >= 1) {
                    topicBean = mDataList[0]
                }
            }
            R.id.cl_node2 -> {
                if (mDataList.size >= 2) {
                    topicBean = mDataList[1]
                }
            }
            R.id.cl_node3 -> {
                if (mDataList.size >= 3) {
                    topicBean = mDataList[2]
                }
            }
            R.id.cl_node4 -> {
                if (mDataList.size >= 4) {
                    topicBean = mDataList[3]
                }
            }
            R.id.cl_node5 -> {
                if (mDataList.size >= 5) {
                    topicBean = mDataList[4]
                }
            }
        }
        LogUtils.info("topicBean:$topicBean")
        topicBean?.let {
            var target = "${RouterNavigator.bannerRouterReflectMap[topicBean.sceneId.toInt()]}"
            params = topicBean.sceneExtra ?: HashMap()
            params[HomeConstant.TITLE] = topicBean.title
            params[HomeNavigatorConstant.ROUTER_VALUE] = topicBean.sceneValue
            if (!params.containsKey(HomeConstant.TOPIC_BIG_IMAGE_URL)) {
                params[HomeConstant.TOPIC_BIG_IMAGE_URL] = it.imageUrl
            }
            RouterNavigator.handleAppInternalNavigator(mContext, target, params)
        }
    }

    fun setData(dataList: ArrayList<TopicBean>) {
        if (dataList.isEmpty()) {
            mLlRootView.visibility = View.GONE
            return
        } else {
            mDataList = dataList
            mLlRootView.visibility = View.VISIBLE
        }

        mClNode1.visibility = View.GONE
        mClNode2.visibility = View.GONE
        mClNode3.visibility = View.GONE
        mClNode4.visibility = View.GONE
        mClNode5.visibility = View.GONE

        if (dataList.size >= 1) {
            mClNode1.visibility = View.VISIBLE
            loadImage(mContext, dataList[0].imageUrl, mIvImage1)
            mTvTxt1.text = dataList[0].title
        }
        if (dataList.size >= 2) {
            mClNode2.visibility = View.VISIBLE
            loadImage(mContext, dataList[1].imageUrl, mIvImage2)
            mTvTxt2.text = dataList[1].title
        }
        if (dataList.size >= 3) {
            mClNode3.visibility = View.VISIBLE
            loadImage(mContext, dataList[2].imageUrl, mIvImage3)
            mTvTxt3.text = dataList[2].title
        }
        if (dataList.size >= 4) {
            mClNode4.visibility = View.VISIBLE
            loadImage(mContext, dataList[3].imageUrl, mIvImage4)
            mTvTxt4.text = dataList[3].title
        }
        if (dataList.size >= 5) {
            mClNode5.visibility = View.VISIBLE
            loadImage(mContext, dataList[4].imageUrl, mIvImage5)
            mTvTxt5.text = dataList[4].title
        }
    }

    private fun loadImage(context: Context, imgUrl: String, iv: ImageView) {
        Glide.with(context)
                .load(imgUrl)
                .placeholder(R.drawable.default_img)
                .error(R.drawable.default_img)
                .into(iv)
    }

}