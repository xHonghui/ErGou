package com.laka.ergou.mvp.shop.view.dialog

import android.content.Context
import android.content.DialogInterface
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.laka.androidlib.widget.dialog.BaseDialog
import com.laka.ergou.R

/**
 * @Author:summer
 * @Date:2019/1/15
 * @Description:
 */
open class ShareDialog : BaseDialog {

    companion object {
        const val WEIXIN_CLICK = 1001
        const val QQ_CLICK = 1002
        const val FRIEND_CIRCLE_CLICK = 1003
        const val QQ_KJ_CLICK = 1004
        const val CREATE_POST = 1005
        const val WEIXIN_PACKAGE_NAME = "com.tencent.mm"
        const val QQ_PACKAGE_NAME = "com.tencent.mobileqq"
        const val QQKJ_PACKAGE_NAME = "com.qzone"
        const val FRIEND_CIRCLE_PACKAGE_NAME = "com.tencent.mm"
    }

    private var mTvShare: TextView? = null
    private var mTvCancel: TextView? = null
    private var mLlWeiXin: LinearLayout? = null
    private var mLlPyq: LinearLayout? = null
    private var mLlQQ: LinearLayout? = null
    private var mLlKj: LinearLayout? = null

    private var isEnableWx = true
    private var isEnableWxCommunity = true
    private var isEnableQQ = true
    private var isEnableQQZone = true
    private var isEnablePoster = true

    protected var mItemClickListener: ((type: Int) -> Unit)? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, themeResId: Int) : super(context, themeResId)
    constructor(context: Context?, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?) : super(context, cancelable, cancelListener)


    override fun getLayoutId(): Int {
        return R.layout.dialog_share
    }

    override fun initView() {
        mTvShare = findViewById(R.id.tv_share)
        mTvCancel = findViewById(R.id.tv_cancel)
        mLlWeiXin = findViewById(R.id.ll_weixin)
        mLlPyq = findViewById(R.id.ll_pyq)
        mLlQQ = findViewById(R.id.ll_qq)
        mLlKj = findViewById(R.id.ll_kj)

        mLlWeiXin?.visibility = if (isEnableWx) View.VISIBLE else View.GONE
        mLlWeiXin?.isEnabled = isEnableWx
        mLlPyq?.visibility = if (isEnableWxCommunity) View.VISIBLE else View.GONE
        mLlPyq?.isEnabled = isEnableWxCommunity

        mLlQQ?.visibility = if (isEnableQQ) View.VISIBLE else View.GONE
        mLlQQ?.isEnabled = isEnableQQ
        mLlKj?.visibility = if (isEnableQQZone) View.VISIBLE else View.GONE
        mLlKj?.isEnabled = isEnableQQZone
    }

    override fun initData() {

    }

    override fun initEvent() {
        mLlKj?.setOnClickListener {
            mItemClickListener?.invoke(QQ_KJ_CLICK)
            dismiss()
        }
        mLlPyq?.setOnClickListener {
            mItemClickListener?.invoke(FRIEND_CIRCLE_CLICK)
            dismiss()
        }
        mLlQQ?.setOnClickListener {
            mItemClickListener?.invoke(QQ_CLICK)
            dismiss()
        }
        mLlWeiXin?.setOnClickListener {
            mItemClickListener?.invoke(WEIXIN_CLICK)
            dismiss()
        }
        mTvCancel?.setOnClickListener {
            dismiss()
        }
    }


    override fun getGravityType(): Int {
        return Gravity.BOTTOM
    }

    fun setOnItemClickListener(itemClickListener: ((type: Int) -> Unit)?) {
        this.mItemClickListener = itemClickListener
    }

    /**
     * description:使用微信分享
     **/
    fun enableWx(isEnable: Boolean = true): ShareDialog {
        this.isEnableWx = isEnable
        return this
    }

    /**
     * description:使用微信朋友圈分享
     **/
    fun enableWxCommunity(isEnable: Boolean = true): ShareDialog {
        this.isEnableWxCommunity = isEnable
        return this
    }

    /**
     * description:使用QQ分享
     **/
    fun enableQQShare(isEnable: Boolean = true): ShareDialog {
        this.isEnableQQ = isEnable
        return this
    }

    /**
     * description:使用QQ空间分享
     **/
    fun enableQQZoneShare(isEnable: Boolean = true): ShareDialog {
        this.isEnableQQZone = isEnable
        return this
    }

    /**
     * description:是否使用生产海报
     **/
    fun enablePoster(isEnable: Boolean = false): ShareDialog {
        this.isEnablePoster = isEnable
        return this
    }
}