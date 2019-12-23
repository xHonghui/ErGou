package com.laka.ergou.mvp.circle.model.bean

data class CircleCategoryResponse(
    val list: List<CircleCategory>
)

data class CircleCategory(
    val category_id: Int,
    val name: String
)