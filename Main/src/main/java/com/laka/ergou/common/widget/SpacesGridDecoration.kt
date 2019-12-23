package com.laka.ergou.common.widget

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import com.laka.androidlib.util.LogUtils
import com.laka.ergou.mvp.shop.constant.ShopDetailConstant
import com.laka.ergou.mvp.shopping.center.constant.ShoppingCenterConstant

/**
 * @Author:summer
 * @Date:2018/12/26
 * @Description:普通通用grid边框
 */
class SpacesGridDecoration : RecyclerView.ItemDecoration {

    private var mTopSpace: Int = -1
    private var mBottomSpace: Int = -1
    private var mSpace: Int = -1
    private var mArrayMap: HashMap<Int, Int>
    var isAddHeader = false

    constructor(space: Int) : super() {
        mSpace = space
        mArrayMap = HashMap()
    }

    constructor(topSpace: Int, bottomSpace: Int, space: Int) : super() {
        this.mSpace = space
        this.mTopSpace = topSpace
        this.mBottomSpace = bottomSpace
        mArrayMap = HashMap()
    }


    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        val position = parent?.getChildAdapterPosition(view)
        if (position == 0) return
        if (mSpace < 0) {
            outRect?.top = 0
            outRect?.bottom = 0
            outRect?.left = 0
            outRect?.right = 0
            return
        }
        //默认间距添加到item的头部，如果想添加到底部，可以使用第二个构造方法，传入具体的space值
        if (position != null && position % 2 == 0) { //left
            outRect?.top = mSpace
            outRect?.bottom = 0
            if (isAddHeader) {
                outRect?.left = mSpace / 2
                outRect?.right = mSpace
            } else {
                outRect?.left = mSpace
                outRect?.right = mSpace / 2
            }
        } else if (position != null && position % 2 != 0) {
            outRect?.top = mSpace
            outRect?.bottom = 0
            if (isAddHeader) {
                outRect?.left = mSpace
                outRect?.right = mSpace / 2
            } else {
                outRect?.left = mSpace / 2
                outRect?.right = mSpace
            }
        }

        if (mTopSpace > -1) {
            outRect?.top = mTopSpace
        }
        if (mBottomSpace > -1) {
            outRect?.bottom = mBottomSpace
        }

    }

}