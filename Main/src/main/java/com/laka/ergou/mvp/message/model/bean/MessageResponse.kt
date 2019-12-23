package com.laka.ergou.mvp.message.model.bean

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.laka.ergou.mvp.message.constant.MessageConstant

/**
 * @Author:summer
 * @Date:2019/3/8
 * @Description:补贴消息
 */
data class MessageResponse(
        val msgs: ArrayList<Msg>,
        val total: Int = 0,
        val type: Int = -1
)

data class Msg(
        val id: String = "",
        val msg_type: Int = -1,// 消息的类型，1：订单付款返利补贴，2：订单结算返利补贴，3：订单失效和扣除补贴，4：提现补贴失败，5：提现补贴成功，6：邀请战友奖励补贴，7：战友购物提成补贴
        val title: String = "",
        val money: String = "",
        val money_type: Int = -1,// 0：无意义，1：待结金额，2：可用金额, 3: 失效金额
        val context: String = "", //消息的描述，后台拼装好返回
        val create_time: Long = 0,
        val img_url: String = "",
        val scene_id: Int = -1,   // 场景：1：邀请好友，2：其他消息，3：我的订单，4：商品详情，5：H5链接
        val scene_value: String = "", // 场景值：场景为商品详情是为商品id,为链接是为链接URL
        val scene_extra: String = "", //扩展字段，运用于多个参数的场景，json格式的字符串
        val push_to: Int = -1,// 推送范围, 1:全部, 2: 部分用户
        var viewType: Int = MessageConstant.ITEM_OTHER_MESSAGE_TYPE_NORMAL
) : MultiItemEntity {
    override fun getItemType(): Int {
        return viewType
    }
}