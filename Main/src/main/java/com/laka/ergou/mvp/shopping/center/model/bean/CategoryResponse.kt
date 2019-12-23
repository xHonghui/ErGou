package com.laka.ergou.mvp.shopping.center.model.bean

/**
 * @Author:summer
 * @Date:2019/4/3
 * @Description:banner、category、精选等数据响应体
 */
data class CategoryResponse(
        val banners: ArrayList<Banner> = ArrayList(),
        val cate: ArrayList<String> = ArrayList(),
        val home_page: HomePage = HomePage()
)

data class HomePage(
        val favorites_id: Int = -1,
        val favorites_title: String = "",
        val type: Int = 0
)

data class Banner(
        val banner: String = "",
        val favorites_id: Int = -1,
        val favorites_title: String = "",
        val type: Int = 0
)