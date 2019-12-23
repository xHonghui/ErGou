package com.laka.ergou.mvp.main.constant

import android.support.annotation.StringDef

/**
 * @Author:Rayman
 * @Date:2019/1/7
 * @Description:主页常量类
 */
object HomeConstant {

    const val BIND_UNION_REQUEST_CODE:Int = 0x2011
    const val KEY_REQUEST_CODE:String = "requestCode"
    const val KEY_RESULT_CODE:String = "resultCode"

    //二购自定义图片缓存目录
    const val ERGOU_IMAGE_CACHE_PATH = "ergou_image"
    //首页活动弹窗图片缓存
    const val HOME_ACTIVITS_POPUP_IMAGE_NAME = "activits_dialog_img.png"
    //首页活动弹窗SP存储key
    const val KEY_HOME_POPUP_BEAN = "home_popup_bean"

    /**
     * requestCode
     * */
    const val REQUEST_CODE_INTO_ACTIVITS_POPUP_DETAIL = 0x001 //点击活动弹窗

    /**
     * description:部分页面常量限定
     **/
    const val WEB_KERNEL = "WEB_KERNEL"
    const val WEB_URL = "WEB_URL"
    const val URL = "url"
    const val TITLE = "title"
    const val WEB_TITLE = "WEB_TITLE"

    //
    const val USER_PID = "USER_PID"
    const val USER_ADZONE_ID = "USER_ADZONE_ID"

    /**
     * scene_extra：扩展字段的一些key
     * */
    const val TOPIC_BIG_IMAGE_URL = "big_img_url"


    /**
     * description:搜索页面常量
     **/
    const val SEARCH_KEY_WORD = "SEARCH_KEY_WORD"
    const val SEARCH_HISTORY = "SEARCH_HISTORY"

    /**
     * description:主页搜索页面筛选类型常量
     **/
    const val SEARCH_SORT_COMPLEX = "SEARCH_SORT_COMPLEX"
    const val SEARCH_SORT_COUNT = "SEARCH_SORT_COUNT"
    const val SEARCH_SORT_PRICE = "SEARCH_SORT_PRICE"

    @StringDef(SEARCH_SORT_COMPLEX, SEARCH_SORT_COUNT, SEARCH_SORT_PRICE)
    annotation class SEARCH_SORT_TYPE

    //上一次搜索弹窗或者购小二搜索的关键词
    const val PRE_SEARCH_KEY: String = "PRE_SEARCH_KEY"
    /**
     * description:首页tab的位置
     * */
    const val TAB_ERGOU_HOME: Int = 0
    const val TAB_ERGOU_CHAT: Int = 1
    const val TAB_ERGOU_MINE: Int = 2

    const val TODAY_IS_SHOW: String = ""

    //是否是淘口令的匹配规则，新版的淘口令不仅仅是使用人民币符号 ￥ ,还会使用 $ 或者其他货币符号，例如
    // € £ Ұ ₴ $ ₰ ¢ ₤ ¥ ₳ ₲ ₪ ₵ ₣ ₱ ฿ ¤ ₡ ₮ ₭ ₩ރ 円 ₢ ₥ ₫ ₦ ł ﷼ ₠ ₧ ₯ ₨ č र ₹ ƒ ₸ ￠
    private const val TAO_COMMAND_PATTERN1: String = "￥([a-zA-Z0-9]{11})￥"
    private const val TAO_COMMAND_PATTERN2: String = "₴([a-zA-Z0-9]{11})₴"
    private const val TAO_COMMAND_PATTERN3: String = "$([a-zA-Z0-9]{11})$"
    private const val TAO_COMMAND_PATTERN4: String = "₤([a-zA-Z0-9]{11})₤"
    private const val TAO_COMMAND_PATTERN5: String = "€([a-zA-Z0-9]{11})€"
    private const val TAO_COMMAND_PATTERN6: String = "¥([a-zA-Z0-9]{11})¥"
    private const val TAO_COMMAND_PATTERN7: String = "£([a-zA-Z0-9]{11})£"
    private const val TAO_COMMAND_PATTERN8: String = "₰([a-zA-Z0-9]{11})₰"
    private const val TAO_COMMAND_PATTERN9: String = "¢([a-zA-Z0-9]{11})¢"
    private const val TAO_COMMAND_PATTERN10: String = "₳([a-zA-Z0-9]{11})₳"
    private const val TAO_COMMAND_PATTERN11: String = "₲([a-zA-Z0-9]{11})₲"
    private const val TAO_COMMAND_PATTERN12: String = "₪([a-zA-Z0-9]{11})₪"
    private const val TAO_COMMAND_PATTERN13: String = "₵([a-zA-Z0-9]{11})₵"
    private const val TAO_COMMAND_PATTERN14: String = "₣([a-zA-Z0-9]{11})₣"
    private const val TAO_COMMAND_PATTERN15: String = "₱([a-zA-Z0-9]{11})₱"
    private const val TAO_COMMAND_PATTERN16: String = "฿([a-zA-Z0-9]{11})฿"
    private const val TAO_COMMAND_PATTERN17: String = "¤([a-zA-Z0-9]{11})¤"
    private const val TAO_COMMAND_PATTERN18: String = "₡([a-zA-Z0-9]{11})₡"
    private const val TAO_COMMAND_PATTERN19: String = "₮([a-zA-Z0-9]{11})₮"
    private const val TAO_COMMAND_PATTERN20: String = "₭([a-zA-Z0-9]{11})₭"
    private const val TAO_COMMAND_PATTERN21: String = "円([a-zA-Z0-9]{11})円"
    private const val TAO_COMMAND_PATTERN22: String = "₢([a-zA-Z0-9]{11})₢"
    private const val TAO_COMMAND_PATTERN23: String = "₥([a-zA-Z0-9]{11})₥"
    private const val TAO_COMMAND_PATTERN24: String = "₫([a-zA-Z0-9]{11})₫"
    private const val TAO_COMMAND_PATTERN25: String = "₦([a-zA-Z0-9]{11})₦"
    private const val TAO_COMMAND_PATTERN26: String = "ł([a-zA-Z0-9]{11})ł"
    private const val TAO_COMMAND_PATTERN27: String = "₠([a-zA-Z0-9]{11})₠"
    private const val TAO_COMMAND_PATTERN28: String = "₧([a-zA-Z0-9]{11})₧"
    private const val TAO_COMMAND_PATTERN29: String = "₯([a-zA-Z0-9]{11})₯"
    private const val TAO_COMMAND_PATTERN30: String = "₨([a-zA-Z0-9]{11})₨"
    private const val TAO_COMMAND_PATTERN31: String = "č([a-zA-Z0-9]{11})č"
    private const val TAO_COMMAND_PATTERN32: String = "र([a-zA-Z0-9]{11})र"
    private const val TAO_COMMAND_PATTERN33: String = "₹([a-zA-Z0-9]{11})₹"
    private const val TAO_COMMAND_PATTERN34: String = "ƒ([a-zA-Z0-9]{11})ƒ"
    private const val TAO_COMMAND_PATTERN35: String = "₸([a-zA-Z0-9]{11})₸"
    private const val TAO_COMMAND_PATTERN36: String = "￠([a-zA-Z0-9]{11})￠"
    private const val TAO_COMMAND_PATTERN37: String = "Ұ([a-zA-Z0-9]{11})Ұ"
    private const val TAO_COMMAND_PATTERN38: String = "₩([a-zA-Z0-9]{11})₩"
    private const val TAO_COMMAND_PATTERN39: String = "﷼([a-zA-Z0-9]{11})﷼"
    //匹配所有淘口令的正则表达式
    const val TAO_COMMAND_PATTERN_ALL: String = "${HomeConstant.TAO_COMMAND_PATTERN1}" +
            "|${HomeConstant.TAO_COMMAND_PATTERN2}" +
            "|${HomeConstant.TAO_COMMAND_PATTERN3}" +
            "|${HomeConstant.TAO_COMMAND_PATTERN4}" +
            "|${HomeConstant.TAO_COMMAND_PATTERN5}" +
            "|${HomeConstant.TAO_COMMAND_PATTERN6}" +
            "|${HomeConstant.TAO_COMMAND_PATTERN7}" +
            "|${HomeConstant.TAO_COMMAND_PATTERN8}" +
            "|${HomeConstant.TAO_COMMAND_PATTERN9}" +
            "|${HomeConstant.TAO_COMMAND_PATTERN10}" +
            "|${HomeConstant.TAO_COMMAND_PATTERN11}" +
            "|${HomeConstant.TAO_COMMAND_PATTERN12}" +
            "|${HomeConstant.TAO_COMMAND_PATTERN13}" +
            "|${HomeConstant.TAO_COMMAND_PATTERN14}" +
            "|${HomeConstant.TAO_COMMAND_PATTERN15}" +
            "|${HomeConstant.TAO_COMMAND_PATTERN16}" +
            "|${HomeConstant.TAO_COMMAND_PATTERN17}" +
            "|${HomeConstant.TAO_COMMAND_PATTERN18}" +
            "|${HomeConstant.TAO_COMMAND_PATTERN19}" +
            "|${HomeConstant.TAO_COMMAND_PATTERN20}" +
            "|${HomeConstant.TAO_COMMAND_PATTERN21}" +
            "|${HomeConstant.TAO_COMMAND_PATTERN22}" +
            "|${HomeConstant.TAO_COMMAND_PATTERN23}" +
            "|${HomeConstant.TAO_COMMAND_PATTERN24}" +
            "|${HomeConstant.TAO_COMMAND_PATTERN25}" +
            "|${HomeConstant.TAO_COMMAND_PATTERN26}" +
            "|${HomeConstant.TAO_COMMAND_PATTERN27}" +
            "|${HomeConstant.TAO_COMMAND_PATTERN28}" +
            "|${HomeConstant.TAO_COMMAND_PATTERN29}" +
            "|${HomeConstant.TAO_COMMAND_PATTERN30}" +
            "|${HomeConstant.TAO_COMMAND_PATTERN31}" +
            "|${HomeConstant.TAO_COMMAND_PATTERN32}" +
            "|${HomeConstant.TAO_COMMAND_PATTERN33}" +
            "|${HomeConstant.TAO_COMMAND_PATTERN34}" +
            "|${HomeConstant.TAO_COMMAND_PATTERN35}" +
            "|${HomeConstant.TAO_COMMAND_PATTERN36}" +
            "|${HomeConstant.TAO_COMMAND_PATTERN37}" +
            "|${HomeConstant.TAO_COMMAND_PATTERN38}" +
            "|${HomeConstant.TAO_COMMAND_PATTERN39}"

}