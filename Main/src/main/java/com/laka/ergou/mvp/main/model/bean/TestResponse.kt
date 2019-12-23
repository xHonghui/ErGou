package com.laka.ergou.mvp.main.model.bean

/**
 * @Author:Rayman
 * @Date:2018/12/25
 * @Description:
 */
data class TestResponse(
        val tbk_item_get_response: TbkItemGetResponse
)

data class TbkItemGetResponse(
        val request_id: String,
        val results: Results,
        val total_results: Int
)

data class Results(
        val n_tbk_item: List<NTbkItem>
)

data class NTbkItem(
        val item_url: String,
        val nick: String,
        val num_iid: Long,
        val pict_url: String,
        val provcity: String,
        val reserve_price: String,
        val seller_id: Int,
        val small_images: SmallImages,
        val title: String,
        val user_type: Int,
        val volume: Int,
        val zk_final_price: String
)

data class SmallImages(
        val string: List<String>
)