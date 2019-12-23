package com.laka.ergou.mvp.chat.model.bean

/**
 * @Author:summer
 * @Date:2019/6/6
 * @Description:
 */
data class ImageMessage(
        var thumbUrl: String = "",  //缩略图
        var thumbLength: Long = 0,  //缩略图大小
        var thumbHeight: Int = 0,  //缩略图高度
        var thumbWidth: Int = 0,  //缩略图宽度
        var bigUrl: String = "",   //大图
        var length: Long = 0,  //大图大小
        var md5: String = ""  // MD5
)