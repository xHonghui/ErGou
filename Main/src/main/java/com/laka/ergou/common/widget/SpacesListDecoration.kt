package com.laka.ergou.common.widget

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import com.laka.ergou.mvp.shop.constant.ShopDetailConstant
import com.laka.ergou.mvp.shopping.center.constant.ShoppingCenterConstant

/**
 * @Author:summer
 * @Date:2018/12/26
 * @Description:普通通用List边框
 */
class SpacesListDecoration : RecyclerView.ItemDecoration {

    private var mTopSpace: Int = 0
    private var mBottomSpace: Int = 0
    private var mLeftSpace: Int = 0
    private var mRightSpace: Int = 0
    var isAddHeader = false

    constructor(space: Int) : super() {
        this.mTopSpace = space
        this.mBottomSpace = space
        this.mLeftSpace = space
        this.mRightSpace = space
    }

    constructor(leftSpace: Int, topSpace: Int, rightSpace: Int, bottomSpace: Int) : super() {
        this.mTopSpace = topSpace
        this.mBottomSpace = bottomSpace
        this.mLeftSpace = leftSpace
        this.mRightSpace = rightSpace
    }


    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        val position = parent?.getChildAdapterPosition(view)
        if (isAddHeader&&position==0) {
            return
        }
        outRect?.top = mTopSpace
        outRect?.bottom = mBottomSpace
        outRect?.left = mLeftSpace
        outRect?.right = mRightSpace
    }

}