package com.laka.ergou.common.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.text.TextUtils
import com.laka.androidlib.util.ApplicationUtils
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.SPHelper
import com.laka.androidlib.util.StringUtils
import com.laka.ergou.mvp.main.constant.HomeConstant
import com.laka.ergou.mvp.shop.constant.ShopDetailConstant
import java.util.regex.Pattern

/**
 * @Author:Rayman
 * @Date:2019/2/22
 * @Description:粘贴板帮助类
 */
class ClipBoardManagerHelper : ClipboardManager.OnPrimaryClipChangedListener {

    private val LOCAL_CLIP = "LOCAL_CLIP"
    private var clipBoardManager: ClipboardManager? = null
    private var callBacks = ArrayList<ClipBoardContentChangeListener>()
    private var tempContent = ""
    var isTaoCommand = false

    companion object {
        const val CLIP_BOARD_LABEL = "label"
        val getInstance: ClipBoardManagerHelper by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            ClipBoardManagerHelper()
        }
    }

    init {
        clipBoardManager = ApplicationUtils.getApplication().getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        clipBoardManager?.addPrimaryClipChangedListener(this)
    }

    /*剪贴板内容变化监听*/
    override fun onPrimaryClipChanged() {
        val pwd = SPHelper.getString(ShopDetailConstant.TPWD_CREATE, "")
        val clipData = clipBoardManager?.primaryClip
        val item = clipData?.getItemAt(0)
        val content = "${item?.text}"
        val matches = Pattern.compile("${StringUtils.URL_PATTERN}|${HomeConstant.TAO_COMMAND_PATTERN_ALL}").matcher(content)
                .find()

        // 复制内容为空不做处理
        if (TextUtils.isEmpty(content)) {
            return
        }

        //内容长度小于登录 8 ，不处理
        if (content.length <= 8) {
            return
        }

        // 对App内文字复制不做任何处理
        tempContent = SPHelper.getString(LOCAL_CLIP, tempContent)
        LogUtils.error("输出获取到的content：$tempContent")
        if (!TextUtils.isEmpty(tempContent) && tempContent == content) {
            LogUtils.info("一样的啊！！！")
            return
        }

        //和上一次搜索的关键词一样，不处理
        val preSearchKey = SPHelper.getString(HomeConstant.PRE_SEARCH_KEY, "")
        LogUtils.info("上一次的搜索关键词：$preSearchKey")
        if (!TextUtils.isEmpty(preSearchKey) && preSearchKey == content) {
            LogUtils.info("一样的啊！！！")
            return
        }

        if (matches) {
            //匹配应用内生成的淘口令，匹配成功则不处理
            isTaoCommand = true
            if (TextUtils.isEmpty(pwd)) {
                callBacks.forEach {
                    it.contentChange(content, true)
                }
            } else {
                if (!content.contains(pwd)) {
                    callBacks.forEach {
                        it.contentChange(content, true)
                    }
                }
            }
        } else {
            isTaoCommand = false
            callBacks.forEach {
                it.contentChange(content, false)
            }
        }
    }

    /*获取剪贴板内容*/
    fun getClipboardContent(): String {
        val clipData = clipBoardManager?.primaryClip
        val item = clipData?.getItemAt(0)
        return "${item?.text}"
    }

    /**
     * description:涉及到H5页面 copy淘口令到剪切板。
     * 但是这个工具类是不能主动设置本地的剪切板数据，所以暴露一个入口设置本地Copy数据。
     **/
    fun setLocalCopyContent(copyContent: String) {
        tempContent = copyContent
        SPHelper.putString(LOCAL_CLIP, tempContent)
        LogUtils.error("设置本地CopyContent：$tempContent")
    }

    fun getLocalCopyContent(): String {
        if (TextUtils.isEmpty(tempContent)) {
            tempContent = SPHelper.getString(LOCAL_CLIP, "")
        }
        return tempContent
    }

    fun setPreSearchKey() {
        if (!TextUtils.isEmpty(ClipBoardManagerHelper.getInstance.getClipboardContent())) {
            SPHelper.putString(HomeConstant.PRE_SEARCH_KEY, ClipBoardManagerHelper.getInstance.getClipboardContent())
        }
    }

    fun getPreSearchKey(): String {
        return SPHelper.getString(HomeConstant.PRE_SEARCH_KEY, "")
    }

    fun addListener(listener: ClipBoardContentChangeListener) {
        // 去重添加
        if (!callBacks.contains(listener)) {
            callBacks.add(listener)
        }
    }

    fun removeListener(listener: ClipBoardContentChangeListener) {
        // 判断移除有效性
        if (callBacks.contains(listener)) {
            callBacks.remove(listener)
        }
    }

    /*是否含有淘口令*/
    fun isTaoCommand(command: String): Boolean {
        val matches = Pattern.compile("${StringUtils.URL_PATTERN}|${HomeConstant.TAO_COMMAND_PATTERN_ALL}").matcher(command)
                .find()
        if (TextUtils.isEmpty(command)) {
            return false
        }
        return matches
    }

    fun count(): Int {
        return callBacks.size
    }

    fun clearClipBoardContent() {
        clipBoardManager?.primaryClip = ClipData.newPlainText(CLIP_BOARD_LABEL, "")
    }

    /**
     * 如果粘贴板的内容含有淘口令，则清除
     * */
    fun clearClipBoardContentForHasTkl() {
        val content = getClipboardContent()
        val matches = Pattern.compile("${HomeConstant.TAO_COMMAND_PATTERN_ALL}").matcher(content).find()
        if (matches) {
            clearClipBoardContent()
        }
    }

    /**
     * 写入剪贴板
     * */
    fun writeToClipBoardContent(content: String) {
        clipBoardManager?.primaryClip = ClipData.newPlainText(CLIP_BOARD_LABEL, content)
        setLocalCopyContent(content)
        LogUtils.error("输出Copy到的数据：$tempContent")
    }

    interface ClipBoardContentChangeListener {

        /**
         * description:
         * @param content   内容回调
         * @param isCommandValid 是否符合购小二数据内容（URL和淘口令）
         **/
        fun contentChange(content: String, isCommandValid: Boolean)

    }
}