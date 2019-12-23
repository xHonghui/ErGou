package com.laka.ergou.mvp.chat.view.widget

import android.content.Context
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.laka.androidlib.widget.dialog.BaseDialog
import com.laka.ergou.R

/**
 * @Author:summer
 * @Date:2019/2/18
 * @Description:聊天记录订单编号识别弹窗
 */
class NumberOperationDialog : BaseDialog, View.OnClickListener {

    companion object {
        const val CLICK_SEE_ORDER: Int = 0x1001
        const val CLICK_COPY_NUMBER: Int = 0x1002
        const val CLICK_CANCEL: Int = 0x1003
    }

    private var mTvMsg: TextView? = null
    private var mTvCopy: TextView? = null
    private var mTvSeeOrder: TextView? = null
    private var mTvCancel: TextView? = null
    private var mMessage: String = ""
    private var mItemClickListener: ((Int, String) -> Unit)? = null

    constructor(context: Context?) : super(context)

    override fun getLayoutId(): Int {
        return R.layout.dialog_number_operation
    }

    override fun initView() {
        mTvMsg = findViewById(R.id.tv_msg)
        mTvCopy = findViewById(R.id.tv_copy)
        mTvSeeOrder = findViewById(R.id.tv_see_order)
        mTvCancel = findViewById(R.id.tv_cancel)
        mTvMsg?.text = mMessage
        gravityType = Gravity.BOTTOM
    }

    override fun initData() {

    }

    override fun initEvent() {
        mTvSeeOrder?.setOnClickListener(this)
        mTvCopy?.setOnClickListener(this)
        mTvCancel?.setOnClickListener(this)
    }

    override fun initAnima() {
        window.setWindowAnimations(R.style.NumberOperationDialogAnim)
    }

    override fun onClick(v: View?) {
        var type = when (v?.id) {
            R.id.tv_see_order -> CLICK_SEE_ORDER
            R.id.tv_copy -> CLICK_COPY_NUMBER
            R.id.tv_cancel -> CLICK_CANCEL
            else -> -1
        }
        val number = mTvMsg?.text.toString()
        mItemClickListener?.invoke(type, number)
        dismiss()
    }

    fun setItemClickListener(listener: ((Int, String) -> Unit)?) {
        this.mItemClickListener = listener
    }

    fun setMsg(msg: String) {
        if (TextUtils.isEmpty(msg)) return
        mTvMsg?.text = msg
        mMessage = msg
    }


}