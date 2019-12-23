package com.laka.ergou.mvp.shopping.dialog

import android.content.Context
import android.graphics.BitmapFactory
import android.support.constraint.ConstraintLayout
import android.view.View
import android.widget.ImageView
import com.laka.androidlib.util.GlideUtil
import com.laka.androidlib.util.screen.ScreenUtils
import com.laka.androidlib.widget.dialog.BaseDialog
import com.laka.ergou.R
import com.laka.ergou.mvp.shopping.center.model.bean.HomePopupBean
import com.laka.ergou.mvp.shopping.center.model.bean.Promotion

/**
 * @Author:summer
 * @Date:2019/7/31
 * @Description:首页活动/广告弹窗
 */
class CommonHomeDialog(context: Context, val response: HomePopupBean, private val clickListener: (() -> Unit)) : BaseDialog(context), View.OnClickListener {

    private lateinit var mIvImg: ImageView
    private lateinit var mIvClose: ImageView
    private var mLeftPadding = ScreenUtils.dp2px(10f)
    private var mRightPadding = ScreenUtils.dp2px(10f)

    override fun getLayoutId(): Int {
        return R.layout.dialog_common_home
    }

    override fun initView() {
        setCanceledOnTouchOutside(false)
        mIvImg = findViewById(R.id.iv_img)
        mIvClose = findViewById(R.id.iv_close)
    }

    override fun initData() {
        val option = BitmapFactory.Options()
        option.inJustDecodeBounds = true
        BitmapFactory.decodeFile(response.localImgPath, option)
        val layoutParams = mIvImg.layoutParams as? ConstraintLayout.LayoutParams
        layoutParams?.let {
            val scale = option.outWidth.toDouble() / (ScreenUtils.getScreenWidth() - mLeftPadding - mRightPadding)
            layoutParams.width = ScreenUtils.getScreenWidth() - mLeftPadding - mRightPadding
            layoutParams.height = (option.outHeight / scale).toInt()
            GlideUtil.loadImage(context, response.localImgPath, R.drawable.default_img, mIvImg)
        }
    }

    override fun initEvent() {
        mIvClose.setOnClickListener(this)
        mIvImg.setOnClickListener {
            clickListener.invoke()
        }
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_close -> {
                dismiss()
            }
            else -> {

            }
        }
    }
}