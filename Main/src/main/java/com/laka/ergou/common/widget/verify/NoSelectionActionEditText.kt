package com.laka.ergou.common.widget.verify

import android.content.Context
import android.util.AttributeSet

/**
 * @Author:summer
 * @Date:2019/6/5
 * @Description:禁止复制粘贴剪切等操作
 */
class NoSelectionActionEditText : HelperEditText {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onTextContextMenuItem(id: Int): Boolean {
        when (id) {
            android.R.id.cut -> {  //剪切

            }
            android.R.id.copy -> {  //复制

            }
            android.R.id.selectAll -> { //全选

            }
            android.R.id.paste -> { //粘贴

            }
        }
        // return super.onTextContextMenuItem(id)
        return false
    }

}