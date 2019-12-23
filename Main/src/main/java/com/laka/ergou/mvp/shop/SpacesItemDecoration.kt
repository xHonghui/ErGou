package com.laka.ergou.mvp.shop

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import com.laka.ergou.mvp.shop.constant.ShopDetailConstant
import com.laka.ergou.mvp.shopping.center.constant.ShoppingCenterConstant

/**
 * @Author:summer
 * @Date:2018/12/26
 * @Description:表格列表边距
 */
class SpacesItemDecoration : RecyclerView.ItemDecoration {

    private var mCallBack: ItemDecorationCallBack
    private var mSpace: Int = 0
    private var mArrayMap: HashMap<Int, Int>

    constructor(callBack: ItemDecorationCallBack, space: Int) : super() {
        this.mCallBack = callBack
        this.mSpace = space
        mArrayMap = HashMap()
    }

    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        val position = parent?.getChildAdapterPosition(view)

        if (mCallBack != null) {

            val type = mCallBack?.getItemType(position)
            outRect?.left = 0
            outRect?.top = 0
            outRect?.bottom = 0
            outRect?.right = 0

            if (type == ShopDetailConstant.SHOP_DETAIL_RECOMMEND_ITEM
                    || type == ShoppingCenterConstant.LIST_UI_TYPE_GRID) {

                outRect?.top = mSpace
                if (!mArrayMap.containsKey(position)) {
                    //适配多样式布局列表，将需要使用这种分隔线样式的item的position记录下来
                    mArrayMap[position!!] = mArrayMap.size
                }
                if (mArrayMap[position]!! % 2 == 0) {
                    outRect?.right = mSpace / 2
                    outRect?.left = mSpace
                } else {
                    outRect?.left = mSpace / 2
                    outRect?.right = mSpace
                }
            }
        }
    }

    interface ItemDecorationCallBack {
        // 获取item类型
        fun getItemType(position: Int?): Int?
    }

}