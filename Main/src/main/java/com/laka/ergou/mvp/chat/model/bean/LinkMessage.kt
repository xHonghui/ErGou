package com.laka.ergou.mvp.chat.model.bean

/**
 * @Author:Rayman
 * @Date:2019/2/20
 * @Description:链接类型Message数据类
 */
class LinkMessage {

    var thumbUrl: String = ""
    var title: String = ""
    var description: String = ""
    var linkUrl: String = ""
    var id: String = "" //当type=2 时，点击跳转商品详情是使用 ； 当 type=3 时，这里表示的内容为 title
    var type: Int = 0  //链接类型  1：普通H5链接  2：产品详情页链接  3：商品搜索页面
}