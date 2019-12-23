package com.laka.ergou.common.widget.titlesort

import com.laka.androidlib.interfaces.ICustomViewInit

/**
 * @Author:Rayman
 * @Date:2019/1/11
 * @Description:标题筛选（倒序-正序）控件
 */
interface ITitleSortView : ICustomViewInit<String> {

    fun setTitle(title: String): ITitleSortView

    fun setTitleColor(redId: Int): ITitleSortView

    fun setSortColor(resId: Int): ITitleSortView


}