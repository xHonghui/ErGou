package com.laka.ergou.mvp.receiver

/**
 * @Author:Rayman
 * @Date:2019/3/14
 * @Description:推送数据Bean
 */
data class PushReceiverBean(
        var scene_id: String = "",
        var scene_value: String = "",
        var scene_extra: HashMap<String,String> = HashMap()
)
