package com.laka.ergou.mvp.advert.utils

import android.os.Handler
import android.view.View
import android.widget.TextView
import com.laka.androidlib.net.thread.ThreadManager.runOnUiThread
import java.util.*

class CountDownUtils(val textview:TextView,val runnable:Runnable){
    var timer:Timer
    private var recLen = 3//跳过倒计时提示5秒r
    private var handler: Handler
    init {
        handler = Handler()
        timer =  Timer()
    }

    fun init(){
        textview.setText("${recLen} 跳过 ");
        handler.postDelayed(runnable,3000)
        timer.schedule(object :TimerTask(){
            override fun run() {
                runOnUiThread {
                    recLen--
                    textview.setText("${recLen} 跳过 ");
                    if (recLen <= 0) {
                        timer.cancel()
                        textview.setVisibility(View.GONE);//倒计时到0隐藏字体
                    }
                }

            }

        }, 1000, 1000)//等待时间一秒，停顿时间一秒
    }
    fun release(){
        runnable?.let {
            handler?.removeCallbacks(runnable)
        }
        timer?.cancel()
    }
}