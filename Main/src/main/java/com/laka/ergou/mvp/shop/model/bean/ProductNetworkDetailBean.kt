package com.laka.ergou.mvp.shop.model.bean

import com.laka.ergou.mvp.shopping.center.model.bean.ProductWithCoupon

/**
 * @Author:summer
 * @Date:2019/1/21
 * @Description:
 */
data class ProductNetworkDetailBean(val tbk_item_info_get_response: TbkItemInfoGetResponse
                                    , val error_response: ErrorResponse)

data class TbkItemInfoGetResponse(val results: ProductResults)

data class ProductResults(val n_tbk_item:ArrayList<ProductWithCoupon>)
