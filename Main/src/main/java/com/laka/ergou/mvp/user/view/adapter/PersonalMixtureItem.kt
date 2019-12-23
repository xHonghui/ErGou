package com.laka.ergou.mvp.user.view.adapter

import android.graphics.Rect
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.chad.library.adapter.base.BaseViewHolder
import com.laka.androidlib.base.adapter.MultipleAdapterItem
import com.laka.androidlib.util.screen.ScreenUtils
import com.laka.ergou.R
import com.laka.ergou.common.widget.SpacesListDecoration
import com.laka.ergou.mvp.user.model.bean.PersonalMixtureBean

/**
 * @Author:Rayman
 * @Date:2019/3/7
 * @Description:用户主页---水平工具表
 */
class PersonalMixtureItem : MultipleAdapterItem<PersonalMixtureBean> {

    override fun convert(helper: BaseViewHolder?, item: PersonalMixtureBean?) {
        val utilList = helper?.getView<RecyclerView>(R.id.rv_personal_mixture)
        utilList?.let {
            it.addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                    val position = parent?.getChildAdapterPosition(view)
                    val gridLayoutManager = parent?.layoutManager as? GridLayoutManager
                    gridLayoutManager?.let {
                        position?.let {
                            if (position < gridLayoutManager.spanCount) { //首行
                                outRect?.left = 0
                                outRect?.top = 0
                                outRect?.right = 0
                                outRect?.bottom = ScreenUtils.dp2px(8f)
                            }
                        }
                    }
                }
            })
            it.layoutManager = GridLayoutManager(helper.convertView.context, 4)
            it.adapter = PersonalMixtureAdapter(item?.utilsList!!)
        }
    }
}