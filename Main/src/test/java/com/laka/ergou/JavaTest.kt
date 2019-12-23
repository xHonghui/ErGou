package com.laka.ergou

import android.content.Context
import com.laka.androidlib.net.utils.parse.ParseUtil

import com.laka.androidlib.util.LogUtils
import com.laka.ergou.common.util.regex.RegexUtils
import com.laka.ergou.mvp.receiver.PushReceiverBean

import org.junit.Test

import java.util.HashMap

/**
 * @Author:Rayman
 * @Date:2019/3/13
 * @Description:
 */

class JavaTest {

    @Test
    fun testOr() {
        val testValue = 306
        val flag = -0x80000000
        val orValueInt = testValue or flag
        val orValueLong = flag or testValue
        println("输出Int位运算：$orValueInt,Long:$orValueLong")
    }


    @Test
    fun testRegex() {
        val url = "https://www.laka-inc.com/?code=omkcyCW3ZhtL154zyz6yv8dB1102237&state=getRelationIdByCode"
        val params = RegexUtils.findParamsForUrl(url)
        val keySet = params.keys
        val iterator = keySet.iterator()
        while (iterator.hasNext()) {
            val key = iterator.next()
            println("key=" + key + ",,,,value=" + params[key])
        }
    }

    fun changeColor(context: Context, id: Int): String {
        val stringBuffer = StringBuffer()
        val color = context.resources.getColor(id)
        val red = color and 0xff0000 shr 16
        val green = color and 0x00ff00 shr 8
        val blue = color and 0x0000ff
        stringBuffer.append(Integer.toHexString(red))
        stringBuffer.append(Integer.toHexString(green))
        stringBuffer.append(Integer.toHexString(blue))
        return stringBuffer.toString()
    }



}
