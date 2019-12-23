package com.laka.ergou.mvp.chat.view.widget

import android.content.Context
import android.view.View
import android.view.WindowManager
import com.laka.androidlib.util.ResourceUtils
import com.laka.androidlib.widget.dialog.BaseDialog
import com.laka.ergou.R
import kotlinx.android.synthetic.main.dialog_chat_lock2.*


/**
 * @Author:Rayman
 * @Date:2019/2/13
 * @Description:购小二模块弹窗
 */
class ChatNoticeDialog(context: Context?) : BaseDialog(context), View.OnClickListener {

    var isLock = true
    var callback: ChatLockCallback? = null
    var msgContent: String = ""

    override fun getLayoutId(): Int {
        return R.layout.dialog_chat_lock2
    }


    override fun initView() {
        mLayoutParamsWidth = WindowManager.LayoutParams.MATCH_PARENT
        mLayoutParamsHeight = WindowManager.LayoutParams.MATCH_PARENT
    }

    override fun initData() {

    }

    override fun initEvent() {
        iv_chat_pop_close.setOnClickListener(this)
        tv_chat_pop_skip.setOnClickListener(this)
        sb_back.setOnClickListener(this)
        sb_go.setOnClickListener(this)
    }

    override fun initAnima() {
        //设置弹出收起动画
        window.setWindowAnimations(com.laka.androidlib.R.style.commonDialogAnim)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.sb_go -> { //摇一摇或者去淘宝
                callback?.clickEventCallback(isLock)
            }
            R.id.sb_back,
            R.id.iv_chat_pop_close -> { //返回
                callback?.closeDialog()
            }
            R.id.tv_chat_pop_skip -> { //跳过
                callback?.skip()
            }
        }
    }

    fun show(message: String) {
        isLock = false
        tv_chat_pop_skip.visibility = View.GONE
        msgContent = message
        tv_operation_title.text = ResourceUtils.getString(R.string.chat_unlock_title_hint)
        tv_tkl_search_key.text = message
        tv_tkl_search_key.visibility = View.VISIBLE
        ll_chat_pop_tutorial.visibility = View.GONE
        sb_go.text = ResourceUtils.getString(R.string.shake)
    }

    fun release() {
        callback = null
    }

    interface ChatLockCallback {

        fun closeDialog()

        fun clickEventCallback(isLock: Boolean)

        fun skip()
    }
}