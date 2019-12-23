package com.laka.ergou.mvp.share.utils

import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri

/**
 * @Author:summer
 * @Date:2019/6/3
 * @Description:
 */
class SingleMediaScanner : MediaScannerConnection.MediaScannerConnectionClient {

    private var mMediaScannerConnection: MediaScannerConnection
    private var mContext: Context
    private lateinit var mListFile: Array<String?>
    private lateinit var mListType: Array<String?>
    private lateinit var mFinishListener: (() -> Unit)

    constructor(context: Context) {
        this.mContext = context
        this.mMediaScannerConnection = MediaScannerConnection(context, this)
        this.mMediaScannerConnection.connect()
    }

    fun setScannerData(mListFile: Array<String?>, mListType: Array<String?>) {
        this.mListFile = mListFile
        this.mListType = mListType
        //扫描文件
        if (mMediaScannerConnection.isConnected) {
            for (i in 0 until mListFile.size) {
                mMediaScannerConnection.scanFile(mListFile[i], mListType[i])
            }
        } else {
            mMediaScannerConnection.connect()
        }
    }

    override fun onMediaScannerConnected() {

    }

    override fun onScanCompleted(p0: String?, p1: Uri?) {

    }

    fun setOnFinishListener(listener:()->Unit){
        this.mFinishListener = listener
    }

    fun release() {
        mMediaScannerConnection.disconnect()
    }
}