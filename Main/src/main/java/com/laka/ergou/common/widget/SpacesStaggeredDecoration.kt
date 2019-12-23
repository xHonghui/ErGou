package com.laka.ergou.common.widget

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import com.laka.ergou.mvp.shop.constant.ShopDetailConstant
import com.laka.ergou.mvp.shopping.center.constant.ShoppingCenterConstant

/**
 * @Author:summer
 * @Date:2018/12/26
 * @Description:表格列表边距
 */
class SpacesStaggeredDecoration : RecyclerView.ItemDecoration {

    private var mCallBack: ItemDecorationCallBack
    private var mSpace: Int = 0
    private var mArrayMap: HashMap<Int, Int>
    private var isAddHeader = false //专题商品 headView 边距适配
    private var isAssign = false //是否指定间距
    private var mCenterSpace = 0//中间间距
    fun isAddHeader(addHeader: Boolean) {
        this.isAddHeader = addHeader
    }

    constructor(callBack: ItemDecorationCallBack, space: Int) : super() {
        this.mCallBack = callBack
        this.mSpace = space
        mArrayMap = HashMap()
    }

    constructor(callBack: ItemDecorationCallBack, isAssign: Boolean, leftAndRight: Int, center: Int) : super() {
        this.mCallBack = callBack
        this.isAssign = isAssign
        this.mSpace = leftAndRight
        this.mCenterSpace = center
        mArrayMap = HashMap()
    }

    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        val position = parent?.getChildAdapterPosition(view)
        mCallBack?.let {

            val type = mCallBack.getItemType(position)
            val layoutParams = view?.layoutParams as? StaggeredGridLayoutManager.LayoutParams
            outRect?.left = 0
            outRect?.top = 0
            outRect?.right = 0
            outRect?.bottom = 0

            /*
            * 处理以下指定type的item，适配多样式列表
            * ShopDetailConstant.SHOP_DETAIL_RECOMMEND_ITEM
            * ShoppingCenterConstant.LIST_UI_TYPE_GRID
            * */
            if (type == ShopDetailConstant.SHOP_DETAIL_RECOMMEND_ITEM
                    || type == ShoppingCenterConstant.LIST_UI_TYPE_GRID) {

                //如果含有头布局，则头布局不参与边距计算，直接return
                if (isAddHeader && position == 0) {
                    return
                }

                outRect?.top = mSpace
                layoutParams?.let {
                    if (it.spanIndex != StaggeredGridLayoutManager.LayoutParams.INVALID_SPAN_ID) {
                        if (layoutParams.spanIndex % 2 == 0) {
                            //左列
                            outRect?.right = if (isAssign) {
                                mCenterSpace / 2
                            } else {
                                mSpace / 2
                            }
                            outRect?.left = mSpace
                        } else {
                            //右列
                            outRect?.left = if (isAssign) {
                                mCenterSpace / 2
                            } else {
                                mSpace / 2
                            }
                            outRect?.right = mSpace
                        }
                        if (isAddHeader) {
                            // 首行不设置 outRect?.top = 0，这里的1和2可以通过头布局和spanCount来进行计算
                            // 目前为了方便就直接写死了
                            if (position == 1 || position == 2) {
                                outRect?.top = 0
                            }
                        }
                    }
                }
            }
        }
    }

    interface ItemDecorationCallBack {
        // 获取item类型
        fun getItemType(position: Int?): Int?
    }

}