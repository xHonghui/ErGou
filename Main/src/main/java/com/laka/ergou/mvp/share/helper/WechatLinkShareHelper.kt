package com.laka.ergou.mvp.share.helper

import android.content.Context
import android.text.method.ScrollingMovementMethod
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.laka.ergou.R
import com.laka.ergou.common.util.ClipBoardManagerHelper
import com.laka.ergou.mvp.share.model.bean.ShareResponse

/**
 * @Author:summer
 * @Date:2019/5/27
 * @Description:微信链接分享helper
 */
class WechatLinkShareHelper {

    private lateinit var mContext: Context
    private lateinit var mShareResponse: ShareResponse
    private lateinit var mShareCommentView: LinearLayout
    private lateinit var mTvShareComment1: TextView
    private lateinit var mTvShareComment2: TextView
    private lateinit var mTvShareComment3: TextView
    private lateinit var mTvShareText: TextView
    private var mContent: StringBuffer = StringBuffer()

    fun initViewAndData(context: Context, data: ShareResponse, etShareText: TextView, shareCommentView: LinearLayout) {
        this.mContext = context
        this.mShareResponse = data
        this.mTvShareText = etShareText
        this.mShareCommentView = shareCommentView
        mTvShareText.movementMethod = ScrollingMovementMethod() //设置 TextView 可滚动
        mTvShareComment1 = mShareCommentView.findViewById(R.id.tv_share_comment1)
        mTvShareComment2 = mShareCommentView.findViewById(R.id.tv_share_comment2)
        mTvShareComment3 = mShareCommentView.findViewById(R.id.tv_share_comment3)
        initContent()
        initEvent()
    }

    private fun initContent() {
        mTvShareComment1.isSelected = true
        mTvShareComment2.isSelected = true
        mTvShareComment3.isSelected = true
        mContent.append("\uD83D\uDC47\uD83D\uDC47\uD83D\uDC47\n")
                .append(mShareResponse.share.content).append("\n")
                .append(mShareResponse.share.contentDownloadLink).append("\n")
                .append(mShareResponse.share.contentInviteCode).append("\n")
                .append(mShareResponse.share.contentTkl)
        mTvShareText.text = mContent.toString()
    }

    private fun initEvent() {
        mTvShareComment1.setOnClickListener(mOnClickListener)
        mTvShareComment2.setOnClickListener(mOnClickListener)
        mTvShareComment3.setOnClickListener(mOnClickListener)
        mTvShareText.setOnTouchListener { v, event ->
            //防止ScrollView滚动冲突
            if (event?.action == MotionEvent.ACTION_DOWN) {
                //通知父控件不要干扰
                v.parent.requestDisallowInterceptTouchEvent(true)
            }
            if (event?.action == MotionEvent.ACTION_MOVE) {
                //通知父控件不要干扰
                v.parent.requestDisallowInterceptTouchEvent(true)
            }
            if (event?.action == MotionEvent.ACTION_UP) {
                v.parent.requestDisallowInterceptTouchEvent(false)
            }
            false
        }
    }

    private var mOnClickListener = View.OnClickListener {
        it.isSelected = !it.isSelected
        handleCommentCheck(it)
    }


    private fun handleCommentCheck(v: View) {
        mContent.delete(0, mContent.length)
        mContent.append("\uD83D\uDC47\uD83D\uDC47\uD83D\uDC47\n")
        mContent.append(mShareResponse.share.content).append("\n")
        if (mTvShareComment2.isSelected) { //下载链接
            mContent.append(mShareResponse.share.contentDownloadLink).append("\n")
        }
        if (mTvShareComment3.isSelected) { //邀请码
            mContent.append(mShareResponse.share.contentInviteCode).append("\n")
        }
        if (mTvShareComment1.isSelected) {  // 淘口令
            mContent.append(mShareResponse.share.contentTkl)
        }
        mTvShareText.text = mContent.toString()
    }

    private fun getCommentContent(): String {
        return mContent.toString()
    }

    /**
     * 复制评论内容到粘贴板
     * */
    fun copyCommentContent() {
        ClipBoardManagerHelper.getInstance.writeToClipBoardContent(getCommentContent())
    }

}