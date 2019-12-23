package com.laka.ergou.mvp.order.model.bean

/**
 * @Author:summer
 * @Date:2019/1/21
 * @Description:
 */
data class OrderListBean(val current_page: Int,
                         val data: ArrayList<OrderDataBean>,
                         val first_page_url: String,
                         val from: Int,
                         val last_page: Int,
                         val last_page_url: String,
                         val next_page_url: String,
                         val path: String,
                         val per_page: Int,
                         val prev_page_url: String,
                         val to: Int,
                         val total: Int)

data class OrderDataBean(val type: Int,//订单类型
                         val order_id: String, //订单id
                         val item_id: String, //商品id
                         val title: String, //商品标题
                         val pay_time: String, //订单时间
                         val pic: String,  //图片
                         val comm_word: String, //佣金文本
                         val price: String,  //价格
                         val status_word: String, //状态文本，例如：已结算
                         val number: Int,  //商品数量
                         val commission: String,
                         val earning_time: String = ""  //结算时间
)