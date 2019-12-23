package com.laka.ergou.mvp.circle.model.bean

data class CircleArticleResponse(
        val list: MutableList<CircleArticle>
)

data class CircleArticle(
        val admin: Admin,
        val admin_id: Int,
        val article_id: Int,
        var comment: String,
        var finalComment: String,
        val content: String,
        val id: Int,
        val images: ArrayList<String>,
        val product: Product,
        val product_id: Long,
        val send_time: Long,
        var send_status: String,
        val share_count: Int
)

data class Product(
        val actual_price: String,
        val bonus_amount: String,
        val coupon_amount: Int,
        val has_coupon: Int,
        val volume: Int,
        val id: Long,
        val coupon_id: String = "",
        val large_coupon: Int,
        val pict_url: String,
        val product_id: Long,
        val num_id: String,
        val title: String,
        val coupon_share_url: String,
        val zk_final_price: String
)

data class Admin(
        val admin_id: Int,
        val avatar: String,
        val id: Int,
        val nickname: String
)