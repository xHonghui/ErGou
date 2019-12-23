package com.laka.ergou.mvp.shopping.search.model.bean

import com.google.gson.annotations.SerializedName

/**
 * @Author:Rayman
 * @Date:2019/1/30
 * @Description:搜索页面热词Response
 */
class SearchHotResponse(@SerializedName("search_word") var keyWords: List<String>)