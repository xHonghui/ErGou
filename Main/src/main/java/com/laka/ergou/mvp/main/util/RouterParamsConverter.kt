package com.laka.ergou.mvp.main.util

import android.text.TextUtils
import com.laka.ergou.mvp.main.constant.HomeConstant
import com.laka.ergou.mvp.shop.constant.ShopDetailConstant
import com.laka.ergou.mvp.shopping.center.constant.ShoppingCenterConstant
import org.json.JSONObject

/**
 * @Author:Rayman
 * @Date:2019/3/7
 * @Description: 项目路由参数转换类
 */
object RouterParamsConverter {

    /**
     * description:统一转换后端的路由参数，转换成Map。供RouterNavigator跳转
     **/
    fun convertParamsToMap(paramJson: JSONObject): HashMap<String, String> {
        val paramsMap = HashMap<String, String>()

        // 解析JSON
        // 对应H5页面跳转
        if (paramJson.has("url")) {
            val url = paramJson.getString("url")
            if (!TextUtils.isEmpty(url)) {
                paramsMap[HomeConstant.WEB_URL] = url
            }
        }

        if (paramJson.has("title")) {
            val title = paramJson.getString("title")
            if (!TextUtils.isEmpty(title)) {
                paramsMap[HomeConstant.WEB_TITLE] = title
            }
        }

        // 对应商品ID
        if (paramJson.has("item_id")) {
            val itemId = paramJson.getString("item_id")
            if (!TextUtils.isEmpty(itemId)) {
                paramsMap[ShopDetailConstant.ITEM_ID] = itemId
            }
        }

        //TODO 后续再 scene_extra 中添加新的参数，需要在这里添加解析模型

        return paramsMap
    }



}