package com.laka.ergou.mvp.shopping.center.model.bean

import android.text.TextUtils
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.google.gson.annotations.SerializedName
import com.laka.androidlib.net.utils.parse.anno.Exclude
import com.laka.ergou.mvp.shopping.center.constant.ShoppingCenterConstant
import java.io.Serializable

/**
 * @Author:Rayman
 * @Date:2018/12/15
 * @Description:淘宝客基本商品信息Bean（大部分商品都存在当前的字段数据）
 */

open class BaseProduct : MultiItemEntity, Serializable {

    @SerializedName("num_iid")
    var productId: Long = 0
    @SerializedName("title")
    var productName: String = ""
    @SerializedName("item_url")
    var productUrl: String = ""
    @SerializedName("pict_url")
    var productPic: String = ""
    @SerializedName("small_images")
    var productPics: ProductImageList = ProductImageList()
    @SerializedName("zk_final_price")
    var productPrice: String = ""
    @SerializedName("reserve_price")
    var originPrice: String = ""
    @SerializedName("provcity")
    var place: String = ""
    @SerializedName("volume")
    var sellCount: String = ""
    @SerializedName("user_type")
    var sellerType: String = ""
    @SerializedName("nick")
    var sellerName: String = ""
    @SerializedName("seller_id")
    var sellerId: Long = 0

    @Exclude
    @ShoppingCenterConstant.ProductListUiType
    var uiType: Int = 0
        set(value) {
            field = if (value == 0) ShoppingCenterConstant.LIST_UI_TYPE_COMMON else value
        }
        get() = if (field == 0) ShoppingCenterConstant.LIST_UI_TYPE_COMMON else field

    override fun getItemType(): Int {
        return uiType
    }

    fun getSmallPic(): String {
        return if (TextUtils.isEmpty(productPic)) {
            ""
        } else {
            if (productPic.startsWith("http") || productPic.startsWith("https")) {
                "$productPic" + "_300x300"
            } else {
                "https:$productPic" + "_300x300"
            }
        }
    }

    fun getBigPic(): String {
        return if (TextUtils.isEmpty(productPic)) {
            ""
        } else {
            "$productPic" + "_600x600"
        }
    }

    override fun toString(): String {
        return "BaseProduct(favoritesId=$productId, productName='$productName', productUrl='$productUrl', productPic='$productPic', productPics=$productPics, productPrice='$productPrice', originPrice='$originPrice', place='$place', sellCount='$sellCount', sellerType='$sellerType', sellerName='$sellerName', sellerId=$sellerId, listMode=$uiType')"
    }
}