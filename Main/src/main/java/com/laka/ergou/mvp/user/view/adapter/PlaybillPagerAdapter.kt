package com.laka.ergou.mvp.user.view.adapter

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.support.v4.view.PagerAdapter
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.laka.androidlib.util.GlideUtil
import com.laka.androidlib.util.ListUtils
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.QRCodeUtil
import com.laka.androidlib.util.screen.ScreenUtils
import com.laka.ergou.R
import com.laka.ergou.mvp.main.constant.HomeApiConstant
import com.laka.ergou.mvp.shop.utils.BigDecimalUtils
import com.laka.ergou.mvp.user.UserUtils.UserUtils
import com.laka.ergou.mvp.user.model.bean.InvitationPagerBean

/**
 * @Author:summer
 * @Date:2019/4/23
 * @Description:邀请海报页面pager adapter
 */
class PlaybillPagerAdapter : PagerAdapter {

    private var mDataList: ArrayList<String>
    private var mContext: Context

    constructor(context: Context, data: ArrayList<String>) : super() {
        this.mDataList = data
        this.mContext = context
    }

    override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
        return view == `object`
    }

    /**
     * 生成二维码图片
     * */
    override fun instantiateItem(container: ViewGroup?, position: Int): Any {
        val rootView = LayoutInflater.from(mContext).inflate(R.layout.item_invitation_playbill_pager, container, false) as? ConstraintLayout
        rootView?.let {
            val imageView = rootView?.findViewById<ImageView>(R.id.image_view)
            GlideUtil.loadImage(mContext,mDataList[position],imageView)

//            //计算二维码的位置
//            val bean = mDataList[position]
//            val height = container?.measuredHeight
//            val width = container?.measuredWidth
//            val left = BigDecimalUtils.multi(width.toString(), bean.leftRadio.toString())
//            val top = BigDecimalUtils.multi(height.toString(), bean.topRadio.toString())
//            val qrcodeWidth = BigDecimalUtils.multi(width.toString(), bean.widthRadio.toString())
//            val qrcodeHeight = BigDecimalUtils.multi(height.toString(), bean.heightRadio.toString())
//            LogUtils.info("pager---------left=$left,top=$top,qrcodeWidth=$qrcodeWidth,qrHeight=$qrcodeHeight")
//            LogUtils.info("pager---------height=$height,width=$width")
//
//            //生成二维码并创建相应的ImageView
//            val qrCodeImageView = ImageView(mContext)
//            qrCodeImageView.setImageResource(R.drawable.default_img)
//            qrCodeImageView.scaleType = ImageView.ScaleType.CENTER_CROP
//            val qrCodeLayoutParames = ConstraintLayout.LayoutParams(qrcodeWidth.toInt(), qrcodeWidth.toInt())
//            qrCodeLayoutParames.topMargin = top.toInt()
//            qrCodeLayoutParames.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
//            qrCodeLayoutParames.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID
//            qrCodeLayoutParames.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
//            val content = "${HomeApiConstant.URL_SCAN_DOWNLOAD}?code=${UserUtils.getAgentCode()}"
//            val bitmap = QRCodeUtil.createQRCodeBitmap(content, qrcodeWidth.toInt(), 0)
//            bitmap?.let {
//                qrCodeImageView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_main))
//                qrCodeImageView.setImageBitmap(bitmap)
//            }
//
//            val txtLeft = BigDecimalUtils.multi(bean.txtLeftRadio.toString(), width.toString())
//            val txtTop = BigDecimalUtils.multi(bean.txtTopRadio.toString(), height.toString())
//            val tvInvitationCode = TextView(mContext)
//            tvInvitationCode.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14F)
//            val tvInvitationCodeLayoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
//            tvInvitationCodeLayoutParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
//            tvInvitationCodeLayoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
//            tvInvitationCodeLayoutParams.leftMargin = txtLeft.toInt()
//            tvInvitationCodeLayoutParams.topMargin = txtTop.toInt()
//
//            tvInvitationCode.setTextColor(ContextCompat.getColor(mContext, bean.txtColor))
//            tvInvitationCode.text = UserUtils.getAgentCode()
//
//            //添加 qrCodeImageView
//            if (bean.txtLeftRadio > 0 && bean.txtTopRadio > 0) {
//                rootView.addView(tvInvitationCode, tvInvitationCodeLayoutParams)
//            }
//            rootView.addView(qrCodeImageView, qrCodeLayoutParames)

            container?.addView(rootView)
            return rootView
        }

        val view = View(mContext)
        container?.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
        container?.removeView(`object` as View?)
    }

    override fun getCount(): Int {
        return if (ListUtils.isNotEmpty(mDataList)) mDataList.size else 0
    }
}