package com.laka.ergou

import com.laka.androidlib.net.utils.parse.ParseUtil
import com.laka.androidlib.util.LogUtils
import com.laka.ergou.common.util.regex.RegexUtils
import com.laka.ergou.mvp.receiver.PushReceiverBean
import org.junit.Test

import org.junit.Assert.*
import java.net.URLDecoder

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }


    @Test
    fun test() {
        val sourceStr = "hhttps://a.m.taobao.com/i587971675049.htm?spm=608.brandpage.pmod-zebra-minisite-feature-5731_1560338800787_21.1&tg_key=jhs&v=0&umpChannel=juhuasuan&u_channel=juhuasuan\n" +
                "https://item.taobao.com/item.htm?id=586461612626&spm=dccjy.campaign-10588.private_domain_goods_list_1600456017.0"
        val invitationCode = RegexUtils.findTergetStrForRegex(sourceStr, "/i([0-9]+)\\.htm", 1)
        println("id----:$invitationCode")
    }

    @Test
    fun testOr() {
        val orValue = 306
        val flag: Long = 0x80000000
        println(orValue.or(flag.toInt()))
        println(flag.or(orValue.toLong()))
    }

    @Test
    fun testDecode() {
        val value = "你是我的眼"
        println("value:" + URLDecoder.decode(value, "utf-8"))
    }

    @Test
    fun testRegex() {
        val url = "https://www.laka-inc.com/?code=omkcyCW3ZhtL154zyz6yv8dB1102237&state=getRelationIdByCode"
        val params = RegexUtils.findParamsForUrl(url)
        val keySet = params.keys
        keySet.forEach {
            LogUtils.info("key=$it,,,value=${params[it]}")
        }
    }

    @Test
    fun jPushTest() {
        val json = "{\"scene_id\":\"11\",\"scene_value\":\"http:\\/\\/ergou-app.test.lm1314.xyz\\/lottery\",\"scene_extra\":{\"test\":\"android\"}}"
        val pushData = ParseUtil.parseJson(json, PushReceiverBean::class.java)
        print(pushData)
    }

}
