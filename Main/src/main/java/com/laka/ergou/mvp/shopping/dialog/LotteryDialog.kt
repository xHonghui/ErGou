package com.laka.ergou.mvp.main.dialog

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.laka.androidlib.util.DateUtils
import com.laka.androidlib.widget.SelectorButton
import com.laka.androidlib.widget.dialog.BaseDialog
import com.laka.ergou.R
import com.laka.ergou.mvp.shopping.center.model.bean.Promotion
import java.text.SimpleDateFormat


/**
 * @Author:summer
 * @Date:2019/1/22
 * @Description:618活动弹窗
 */
class LotteryDialog(context: Context, val response: Promotion, val click: (() -> Unit)) : BaseDialog(context), View.OnClickListener {
    var ivLottery: ImageView? = null
    var ivClose: ImageView? = null
    var tvDate: TextView? = null
    var tvTry: SelectorButton? = null
    lateinit var format: SimpleDateFormat
    override fun onClick(p0: View) {

    }

    override fun getLayoutId(): Int {
        return R.layout.dialog_lottery
    }

    override fun initData() {
        format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        var data = DateUtils.timeStamp2DateTime(response.expire.toLong() * 1000, "yyyy-MM-dd HH:mm:ss")
//        var dataNow = "2019-06-14 17:14:34"
        var dataNow = DateUtils.timeStamp2DateTime(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss")
        val d1 = format.parse(data)//后的时间
        val d2 = format.parse(dataNow) //前的时间
        val diff = d1.getTime() - d2.getTime()   //两时间差，精确到毫秒
        val day = diff / (1000 * 60 * 60 * 24)          //以天数为单位取整
        val hour = diff / (60 * 60 * 1000) - day * 24


        tvDate?.text = when (day.toInt()) {
            0 -> "最后1天"
            1 -> "剩  1  天"
            2 -> "剩  2  天"
            else -> "仅  7  天"
        }
    }

    override fun initEvent() {
        ivLottery?.setOnClickListener {
            click()
        }
        tvTry?.setOnClickListener {
            click()
        }
        ivClose?.setOnClickListener {
            dismiss()
        }
    }

    override fun initView() {
        ivLottery = findViewById(R.id.ivLottery)
        ivClose = findViewById(R.id.ivClose)
        tvDate = findViewById(R.id.tv_date)
        tvTry = findViewById(R.id.tv_try)
    }


}