package com.laka.ergou.mvp.chat.view.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import com.laka.androidlib.eventbus.EventBusManager
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.ResourceUtils
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.ergou.R
import com.laka.ergou.mvp.chat.constant.ChatEventConstant
import com.laka.ergou.mvp.chat.view.widget.ChatNoticeDialog
import com.laka.ergou.mvp.main.view.fragment.HomeFragment
import kotlinx.android.synthetic.main.fragment_chat_home.*

/**
 * @Author:Rayman
 * @Date:2019/2/12
 * @Description:聊天页面Fragment
 */
class ChatHomeFragment : HomeFragment(), View.OnClickListener {

    private var noticeDialog: ChatNoticeDialog? = null

    override fun setContentView(): Int {
        return R.layout.fragment_chat_home
    }

    override fun initArgumentsData(arguments: Bundle?) {
    }

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
        title_bar.setLeftIcon(R.drawable.seletor_nav_btn_back)
                .setBackGroundColor(R.color.white)
                .setTitleTextColor(R.color.black)
                .setTitle(ResourceUtils.getString(R.string.module_chat))

        noticeDialog = ChatNoticeDialog(mActivity)
    }

    override fun initDataLazy() {

    }

    override fun createPresenter(): IBasePresenter<*>? {
        return null
    }

    override fun initEvent() {
        setClickView<ImageView>(R.id.iv_chat_audio)
        setClickView<ImageView>(R.id.iv_chat_emoji)
        setClickView<ImageView>(R.id.iv_chat_function)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_chat_audio -> {
                noticeDialog?.show()
            }
            R.id.iv_chat_emoji -> {
                noticeDialog?.show("测试")
            }
            R.id.iv_chat_function -> {
                var message = et_chat_message_input.text.toString()
                if (TextUtils.isEmpty(message)) {
                    ToastHelper.showToast("发送消息不能为空哦")
                } else {
                    et_chat_message_input.setText("")
                    EventBusManager.postEvent(ChatEventConstant.EVENT_SEND_MESSAGE, message)
                }
            }
        }
    }
}