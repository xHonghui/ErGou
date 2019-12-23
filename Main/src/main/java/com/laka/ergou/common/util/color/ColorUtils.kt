package com.laka.ergou.common.util.color

import android.content.Context
import com.laka.androidlib.util.LogUtils

/**
 * @Author:summer
 * @Date:2019/5/7
 * @Description:
 */
object ColorUtils {

    @JvmStatic
    fun changeColor2String(context: Context, id: Int): String {
        val stringBuffer = StringBuffer()
        val color = context.resources.getColor(id)
        var red = Integer.toHexString(color and 0xff0000 shr 16)
        var green = Integer.toHexString(color and 0x00ff00 shr 8)
        var blue = Integer.toHexString(color and 0x0000ff)
        red = if (red.length == 1) "0$red" else red
        green = if (green.length == 1) "0$green" else green
        blue = if (blue.length == 1) "0$blue" else blue
        LogUtils.info("red=$red,,,green=$green,,,blue=$blue")
        stringBuffer.append(red)
        stringBuffer.append(green)
        stringBuffer.append(blue)
        return "#$stringBuffer"
    }

}