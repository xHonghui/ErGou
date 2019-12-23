package com.laka.ergou.mvp.user.view.widget

import android.content.Context
import android.os.CountDownTimer
import android.support.v4.content.ContextCompat
import android.view.View
import com.laka.androidlib.util.ResourceUtils
import com.laka.androidlib.widget.dialog.BaseDialog
import com.laka.ergou.R
import kotlinx.android.synthetic.main.dialog_unbind_wechat_account.*

/**
 * @Author:Rayman
 * @Date:2019/3/8
 * @Description:解绑微信弹窗
 */
class UnbindAccountDialog(context: Context?) : BaseDialog(context), View.OnClickListener {

    /**
     * description:计时器
     **/
    private var countDownTimer = object : CountDownTimer(3 * 1000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            tv_unbind_wx_confirm.isEnabled = false
            tv_unbind_wx_confirm.setTextColor(ResourceUtils.getColor(R.color.color_aaaaaa))
            tv_unbind_wx_confirm.setBgaColor("#dbd7f1")
            var time = millisUntilFinished / 1000
            time += 1
            tv_unbind_wx_confirm.text = ResourceUtils.getStringWithArgs(R.string.user_info_unbind_wx_count, time)
        }

        override fun onFinish() {
            tv_unbind_wx_confirm.isEnabled = true
            tv_unbind_wx_confirm.setTextColor(ResourceUtils.getColor(R.color.white))
            tv_unbind_wx_confirm.setBgaColor("#7966e3")
            tv_unbind_wx_confirm.text = ResourceUtils.getString(R.string.unbind)
        }
    }

    /**
     * description:回调函数
     **/
    lateinit var callback: (isWx: Boolean) -> Unit?
    private var isUnBindWeChat = false

    override fun getLayoutId(): Int {
        return R.layout.dialog_unbind_wechat_account
    }

    override fun initData() {

    }

    override fun initEvent() {
        tv_unbind_wx_cancel.setOnClickListener(this)
        tv_unbind_wx_confirm.setOnClickListener(this)
        iv_delete.setOnClickListener(this)
    }

    override fun initView() {
        countDownTimer.start()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_unbind_wx_cancel -> {
                dismiss()
            }
            R.id.tv_unbind_wx_confirm -> {
                // 传递解绑
                if (::callback.isInitialized) {
                    callback.invoke(isUnBindWeChat)
                }
            }
            R.id.iv_delete -> {
                dismiss()
            }
        }
    }

    fun showUnBindWxAccount() {
        super.show()
        isUnBindWeChat = true
        tv_unbind_wx_title.text = ResourceUtils.getString(R.string.user_info_unbind_wx)
        tv_unbind_wx_content.text = ResourceUtils.getString(R.string.user_info_unbind_wx_hint)
        tv_unbind_wx_content_detail_first.text = ResourceUtils.getString(R.string.user_info_unbind_wx_hint_detail1)
        tv_unbind_wx_content_detail_second.text = ResourceUtils.getString(R.string.user_info_unbind_wx_hint_detail2)
        tv_unbind_wx_content_detail_third.text = ResourceUtils.getString(R.string.user_info_unbind_wx_hint_detail3)
        countDownTimer.start()
    }

    fun showUnBindTaoBaoAccount() {
        // 设置文案
        super.show()
        isUnBindWeChat = false
        tv_unbind_wx_title.text = ResourceUtils.getString(R.string.user_info_unbind_taobao)
        tv_unbind_wx_content.text = ResourceUtils.getString(R.string.user_info_unbind_taobao_hint)
        tv_unbind_wx_content_detail_first.text = ResourceUtils.getString(R.string.user_info_unbind_taobao_detail)
        countDownTimer.start()
    }

    fun release() {
        countDownTimer.let {
            it.cancel()
        }
    }


}