package com.laka.ergou.mvp.chat.view.widget

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.ergou.R

@SuppressLint("StaticFieldLeak")
/**
 * @Author:summer
 * @Date:2019/3/1
 * @Description:聊天列表操作弹窗（复制，全选，删除）
 */
class PopChatOperation : View.OnClickListener {

    companion object {
        const val TYPE_CLICK_COPY: Int = 0x1001//复制
        const val TYPE_CLICK_ALLSELECTION: Int = 0x1002//全选
        const val TYPE_CLICK_DELETE: Int = 0x1002//删除
    }

    private lateinit var mPopWindow: PopupWindow
    private lateinit var mView: View
    private lateinit var mTvLeftOperation: TextView
    private lateinit var mTvRightOperation: TextView
    private lateinit var mLeftClickListener: ((type: Int) -> Unit)
    private lateinit var mRightClickListener: ((type: Int) -> Unit)
    private var mRightType = TYPE_CLICK_DELETE

    /**
     * 初始化
     * */
    fun init(context: Context): PopChatOperation {
        if (!::mPopWindow.isInitialized) {
            mView = LayoutInflater.from(context).inflate(R.layout.pop_chat_operation, null)
            mPopWindow = PopupWindow(mView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            mPopWindow.isOutsideTouchable = true
            mPopWindow.isFocusable = true
            initView()
            initEvent()
        }
        return this
    }

    private fun initView() {
        mTvLeftOperation = mView.findViewById(R.id.tv_left)
        mTvRightOperation = mView.findViewById(R.id.tv_right)
        mTvLeftOperation.text = "复制"
        mTvRightOperation.text = "删除"
    }

    private fun initEvent() {
        mTvLeftOperation.setOnClickListener(this)
        mTvRightOperation.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_left -> {
                if (::mLeftClickListener.isInitialized) {
                    mLeftClickListener.invoke(TYPE_CLICK_COPY)
                }
                mPopWindow.dismiss()
            }
            R.id.tv_right -> {
                if (::mRightClickListener.isInitialized) {
                    mRightClickListener.invoke(mRightType)
                }
                mPopWindow.dismiss()
            }
        }
    }

    /**
     * 显示在顶部
     * */
    fun showAtDropTop(view: View) {
        if (::mPopWindow.isInitialized) {
            mView.measure(0, 0)
            val height = view.measuredHeight + mView.measuredHeight
            mPopWindow.showAsDropDown(view, 0, -height, Gravity.CENTER)
        }
    }

    fun setLeftText(leftStr: String): PopChatOperation {
        if (TextUtils.isEmpty(leftStr)) {
            return this
        }
        if (::mTvLeftOperation.isInitialized) {
            mTvLeftOperation.text = leftStr
        }
        return this
    }

    private fun setRightText(rightStr: String) {
        if (TextUtils.isEmpty(rightStr)) {
            return
        }
        if (::mTvRightOperation.isInitialized) {
            mTvRightOperation.text = rightStr
        }
    }

    fun setRightOperationType(type: Int): PopChatOperation {
        if (type == TYPE_CLICK_ALLSELECTION) {
            setRightText("全选")
        } else if (type == TYPE_CLICK_DELETE) {
            setRightText("删除")
        }
        mRightType = type
        return this
    }

    fun setLeftClickListener(listener: ((type: Int) -> Unit)): PopChatOperation {
        this.mLeftClickListener = listener
        return this
    }

    fun setRightClickListener(listener: (type: Int) -> Unit): PopChatOperation {
        this.mRightClickListener = listener
        return this
    }


}