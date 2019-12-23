package com.laka.ergou.common.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.EditText
import com.laka.androidlib.util.LogUtils

/**
 * @Author:summer
 * @Date:2019/4/10
 * @Description:带监控的输入框，监听 复制、粘贴、全选、剪切、选择等状态
 */
class MonitorEditText : EditText {

    /*剪切或者复制监听器*/
    private var mOnInputCopyOrCutStateListener: (() -> Unit)? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    /**
     * static final int ID_SELECT_ALL = android.R.id.selectAll;
    static final int ID_UNDO = android.R.id.undo;
    static final int ID_REDO = android.R.id.redo;
    static final int ID_CUT = android.R.id.cut;
    static final int ID_COPY = android.R.id.copy;
    static final int ID_PASTE = android.R.id.paste;
    static final int ID_SHARE = android.R.id.shareText;
    static final int ID_PASTE_AS_PLAIN_TEXT = android.R.id.pasteAsPlainText;
    static final int ID_REPLACE = android.R.id.replaceText;
     * */
    override fun onTextContextMenuItem(id: Int): Boolean {
        when (id) {
            android.R.id.cut,
            android.R.id.copy -> {
                LogUtils.info("输入框复制，剪切等操作")
                if (mOnInputCopyOrCutStateListener != null) {
                    mOnInputCopyOrCutStateListener?.invoke()
                }
            }
            android.R.id.selectAll -> { //全选

            }
            android.R.id.paste -> { //粘贴

            }
        }
        return super.onTextContextMenuItem(id)
    }

    fun setOnInputStateListener(listener: (() -> Unit)) {
        this.mOnInputCopyOrCutStateListener = listener
    }

}