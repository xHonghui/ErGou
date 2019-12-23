package com.laka.ergou.mvp.main.dialog

import android.content.Context
import android.view.View
import android.widget.TextView
import com.laka.androidlib.widget.dialog.BaseDialog
import com.laka.ergou.R

/**
 * @Author:summer
 * @Date:2019/1/22
 * @Description:
 */
class TbkSearchDialog : BaseDialog, View.OnClickListener {

    private var mOnCancelClickListener: (() -> Unit)? = null
    private var mOnSureClickListener: (() -> Unit)? = null
    private  var mTvCancel: TextView? = null
    private  var mTvSure: TextView?=null
    private  var mTvMsg: TextView?=null
    private var mMessage: String = ""

    fun setMessage(msg: String) {
        this.mMessage = msg
        if (mTvMsg != null) {
            mTvMsg?.text = mMessage
        }
    }

    fun getMessage(): String {
        return mMessage
    }

    constructor(context: Context?) : super(context)

    override fun getLayoutId(): Int {
        return R.layout.dialog_tbk_search
    }

    override fun initView() {
        mTvCancel = findViewById(R.id.tv_cancel)
        mTvSure = findViewById(R.id.tv_sure)
        mTvMsg = findViewById(R.id.tv_msg)
    }

    override fun initData() {
        mTvMsg?.text = "$mMessage"
    }

    override fun initEvent() {
        mTvCancel?.setOnClickListener(this)
        mTvSure?.setOnClickListener(this)
    }

    override fun initAnima() {
        //设置弹出收起动画
        window.setWindowAnimations(com.laka.androidlib.R.style.commonDialogAnim)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_cancel -> {
                mOnCancelClickListener?.invoke()
                dismiss()
            }
            R.id.tv_sure -> {
                mOnSureClickListener?.invoke()
                dismiss()
            }
        }
    }

    fun setOnBtnClickListener(onSureBtnClickListener: (() -> Unit), onCancelBtnClickListener: (() -> Unit)) {
        this.mOnSureClickListener = onSureBtnClickListener
        this.mOnCancelClickListener = onCancelBtnClickListener
    }

}