package com.laka.ergou.common.dialog

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
 * @Description:视频操作弹窗
 */
class VideoOperationDialog : BaseDialog, View.OnClickListener {

    companion object {
        const val CLICK_SAVE_VIDEO_OR_IMAGE: Int = 0x1001
        const val CLICK_COPY_NUMBER: Int = 0x1002
        const val CLICK_CANCEL: Int = 0x1003
        // 视频或者图片
        const val TYPE_OPERATION_VIDEO = 0X1101
        const val TYPE_OPERATION_IMAGE = 0X1102
    }

    private var mTvCopy: TextView? = null
    private var mTvSaveVideoOrImage: TextView? = null
    private var mTvCancel: TextView? = null
    private var mItemClickListener: ((Int, Int) -> Unit)? = null
    private var mOperationType: Int = TYPE_OPERATION_IMAGE
    private var mOpertaionMsg: String = ""

    constructor(context: Context?) : super(context)

    override fun getLayoutId(): Int {
        return R.layout.dialog_operation_video
    }

    override fun initView() {
        mTvCopy = findViewById(R.id.tv_copy)
        mTvSaveVideoOrImage = findViewById(R.id.tv_save_video_or_image)
        mTvCancel = findViewById(R.id.tv_cancel)
        gravityType = Gravity.BOTTOM
        mTvSaveVideoOrImage?.text = mOpertaionMsg
    }

    override fun initData() {

    }

    override fun initEvent() {
        mTvSaveVideoOrImage?.setOnClickListener(this)
        mTvCopy?.setOnClickListener(this)
        mTvCancel?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        var type = when (v?.id) {
            R.id.tv_see_order -> CLICK_SAVE_VIDEO_OR_IMAGE
            R.id.tv_copy -> CLICK_COPY_NUMBER
            R.id.tv_cancel -> CLICK_CANCEL
            else -> -1
        }
        // 默认 mOperationType = TYPE_OPERATION_IMAGE，只有当 type = CLICK_SAVE_VIDEO_OR_IMAGE 时，才会处理 mOperationType
        mItemClickListener?.invoke(type, mOperationType)
        dismiss()
    }

    fun setItemClickListener(listener: ((Int, Int) -> Unit)?) {
        this.mItemClickListener = listener
    }

    fun setOperationMsg(msg: String) {
        if (TextUtils.isEmpty(msg)) return
        mTvSaveVideoOrImage?.text = msg
        mOpertaionMsg = msg
    }

    fun setOperationType(type: Int) {
        this.mOperationType = type
    }

}