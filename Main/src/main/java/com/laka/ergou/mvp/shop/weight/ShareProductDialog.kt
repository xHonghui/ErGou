package com.laka.ergou.mvp.shop.weight

import android.content.Context
import android.content.DialogInterface
import android.view.View
import com.laka.androidlib.widget.SelectorButton
import com.laka.androidlib.widget.dialog.BaseDialog
import com.laka.ergou.R

/**
 * @Author:summer
 * @Date:2019/5/22
 * @Description:产品详情页面 --> 分享商品dialog
 */
class ShareProductDialog : BaseDialog {

    private lateinit var mCancelSbutton: SelectorButton
    private lateinit var mOpenSbutton: SelectorButton
    private lateinit var mOnCancelListener: ((view: View) -> Unit)
    private lateinit var mOnOpenListener: ((view: View) -> Unit)

    constructor(context: Context?) : super(context)
    constructor(context: Context?, themeResId: Int) : super(context, themeResId)
    constructor(context: Context?, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?) : super(context, cancelable, cancelListener)

    override fun getLayoutId(): Int {
        return R.layout.dialog_product_share
    }

    override fun initView() {
        setCanceledOnTouchOutside(false)
        mCancelSbutton = findViewById(R.id.sb_cancel)
        mOpenSbutton = findViewById(R.id.sb_open)
    }

    override fun initData() {

    }

    override fun initEvent() {
        mCancelSbutton.setOnClickListener {
            dismiss()
            if (::mOnCancelListener.isInitialized) {
                mOnCancelListener.invoke(it)
            }
        }
        mOpenSbutton.setOnClickListener {
            dismiss()
            if (::mOnOpenListener.isInitialized) {
                mOnOpenListener.invoke(it)
            }
        }
    }

    override fun initAnima() {
        //设置弹出收起动画
        window!!.setWindowAnimations(com.laka.androidlib.R.style.commonDialogAnim)
    }


    fun setOnCancelListener(listener: ((view: View) -> Unit)) {
        this.mOnCancelListener = listener
    }

    fun setOnOpenListener(listener: ((view: View) -> Unit)) {
        this.mOnOpenListener = listener
    }

}