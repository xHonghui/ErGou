package com.laka.ergou.common.widget

import android.content.Context
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.laka.ergou.R
import com.laka.ergou.mvp.shop.view.dialog.ShareDialog

/**
 * @Author:Rayman
 * @Date:2019/1/21
 * @Description:分享海报Dialog
 */
class SharePostDialog(context: Context) : ShareDialog(context) {

    private lateinit var mIvSharePost: ImageView
    private lateinit var mLlPost: LinearLayout

    private var sharePostByteArray: ByteArray? = null

    override fun getLayoutId(): Int {
        return R.layout.dialog_share_post
    }

    override fun initView() {
        super.initView()
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        mIvSharePost = findViewById(R.id.iv_share_post)
        mLlPost = findViewById(R.id.ll_post)
    }

    override fun initEvent() {
        super.initEvent()
        mLlPost.setOnClickListener {
            mItemClickListener?.invoke(CREATE_POST)
        }
    }

    /**
     * description:显示和隐藏海报分享
     **/
    fun showShareItem(isShow: Boolean) {
        if (isShow) {
            mLlPost.visibility = View.VISIBLE
            mIvSharePost.visibility = View.INVISIBLE
        } else {
            mLlPost.visibility = View.GONE
            mIvSharePost.visibility = View.VISIBLE
        }
    }


    /**
     * description:设置海报数据
     **/
    fun showSharePostImage(byteArray: ByteArray) {
        this.sharePostByteArray = byteArray
        Glide.with(context)
                .load(sharePostByteArray)
                .asBitmap()
                .into(mIvSharePost)
    }
}