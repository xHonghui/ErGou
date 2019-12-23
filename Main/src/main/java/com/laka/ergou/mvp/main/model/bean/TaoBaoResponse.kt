package com.laka.ergou.mvp.main.model.bean

import com.google.gson.annotations.SerializedName

/**
 * @Author:Rayman
 * @Date:2018/12/25
 * @Description:淘宝客类型数据返回
 */
data class TaoBaoResponse<T>(
        @SerializedName("tbk_item_get_response",
                alternate = ["tbk_dg_material_optional_response", "error_response",
                    "tbk_uatm_favorites_get_response", "tbk_uatm_favorites_item_get_response"])
        val response: TBKResponse<T>
)