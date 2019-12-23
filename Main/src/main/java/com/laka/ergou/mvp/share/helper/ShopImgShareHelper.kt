package com.laka.ergou.mvp.share.helper

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Environment
import android.provider.MediaStore
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.laka.androidlib.util.ApplicationUtils
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.PackageUtils
import com.laka.androidlib.util.PermissionUtils
import com.laka.androidlib.util.image.BitmapUtils
import com.laka.androidlib.util.network.NetworkUtils
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.androidlib.widget.dialog.CommonConfirmDialog
import com.laka.androidlib.widget.dialog.LoadingDialog
import com.laka.ergou.R
import com.laka.ergou.common.threadpool.ThreadPoolHelp
import com.laka.ergou.common.threadpool.ThreadPoolType
import com.laka.ergou.common.util.share.WxShareUtils
import com.laka.ergou.common.widget.SharePostDialog
import com.laka.ergou.mvp.share.utils.SingleMediaScanner
import com.laka.ergou.mvp.shop.utils.ShareUtils
import com.laka.ergou.mvp.shop.view.dialog.ShareDialog
import com.laka.ergou.mvp.shop.weight.ShareProductDialog
import java.io.File
import java.io.IOException
import java.io.OutputStream
import java.util.concurrent.ExecutorService


/**
 * @Author:summer
 * @Date:2019/5/27
 * @Description:商品详情图片分享工具类
 */
class ShopImgShareHelper<T> {

    private lateinit var mContext: Activity
    //线程池
    private lateinit var mThreadPool: ExecutorService
    //线程池名称
    private var mThreadPoolName: String = "thread_save_share_image"
    //核心线程数
    private var mCorePoolSize: Int = 5
    private var mBitmapList = ArrayList<Bitmap>()
    private var mShareContent: String = ""
    private lateinit var mScanner: SingleMediaScanner
    private lateinit var mImageList: ArrayList<T>
    private lateinit var mLoadDialog: LoadingDialog
    private lateinit var mShareFriendCircleDialog: ShareProductDialog
    private lateinit var mShareTypeSelectDialog: SharePostDialog

    constructor(context: Activity) {
        this.mContext = context
        initShareFriendCircleDialog(mContext)
        initShareTypeSelectDialog(mContext)
    }

    private fun initShareFriendCircleDialog(context: Activity) {
        mShareFriendCircleDialog = ShareProductDialog(context)
        mShareFriendCircleDialog.setOnOpenListener {
            //进入微信
            ShareUtils.goToThirdParty(context, ShareDialog.WEIXIN_PACKAGE_NAME)
        }
    }

    private fun initShareTypeSelectDialog(context: Activity) {
        mShareTypeSelectDialog = SharePostDialog(context)
        mShareTypeSelectDialog.setOnItemClickListener {
            when (it) {
                ShareDialog.WEIXIN_CLICK -> { //微信
                    if (mImageList?.isNotEmpty()) {
                        onShareWechat()
                    } else {
                        WxShareUtils.getInstance(context).shareTextAction(mShareContent, true)
                    }
                }
                ShareDialog.FRIEND_CIRCLE_CLICK -> { //朋友圈
                    if (mImageList?.isNotEmpty()) {
                        onShareFriendsCircle()
                    } else {
                        WxShareUtils.getInstance(context).shareTextAction(mShareContent, false)
                    }
                }
                ShareDialog.CREATE_POST -> {

                }
            }
        }
    }

    /**
     * 分享
     * */
    fun postProductShare() {
        if (::mShareTypeSelectDialog.isInitialized) {
            mShareTypeSelectDialog.show()
            mShareTypeSelectDialog.showShareItem(false)
        }
    }

    /**
     * 绑定数据
     * */
    fun bindData(data: ArrayList<T>) {
        this.mImageList = data
        releaseBitmap()
    }

    /**
     * 设置标题，当没有商品图片时，分享的是存文本，也就是标题
     * */
    fun setShareContent(content: String) {
        mShareContent = content
    }

    /**
     * 刷新数据
     * */
    fun notifyDataChanged(data: ArrayList<T>) {
        bindData(data)
    }

    private var mTaskCount: Int = 0

    /**
     * 保存相册到本地
     * */
    private fun saveAllShareImage(saveSuccessListener: (rootFile: File, fileList: ArrayList<File>) -> Unit, saveFailListener: () -> Unit) {
        if (!::mThreadPool.isInitialized || mThreadPool.isTerminated) {
            mThreadPool = ThreadPoolHelp.Builder(ThreadPoolType.CUSTOM, mCorePoolSize).name(mThreadPoolName).builder()
        }
        mTaskCount = mImageList.size
        var folder = File("${Environment.getExternalStorageDirectory().absolutePath}/ergou")
        val fileList = ArrayList<File>()
        if (!folder.exists()) folder.mkdirs()
        for (i in 0 until mImageList.size) {
            val file = File(folder, "ergou_${i}_${System.currentTimeMillis()}.jpg")
            fileList.add(file)
            Glide.with(mContext).load(mImageList[i]).asBitmap().into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                    saveNormalImageTask(resource, file, saveSuccessListener, folder, fileList, saveFailListener)
                }
            })
        }
    }

    private fun saveNormalImageTask(resource: Bitmap?, file: File, saveSuccessListener: (rootFile: File, fileList: ArrayList<File>) -> Unit,
                                    folder: File, fileList: ArrayList<File>, saveFailListener: () -> Unit) {
        if (mThreadPool.isTerminated) {
            return
        }
        try {
            mThreadPool.execute {
                if (resource != null) {
                    val currentTime = System.currentTimeMillis()
                    val values = ContentValues()
                    val resolver = mContext.contentResolver
                    values.put(MediaStore.Images.ImageColumns.DATA, file.absolutePath)
                    values.put(MediaStore.Images.ImageColumns.TITLE, file.name)
                    values.put(MediaStore.Images.ImageColumns.DISPLAY_NAME, file.name)
                    values.put(MediaStore.Images.ImageColumns.DATE_TAKEN, currentTime)
                    values.put(MediaStore.Images.ImageColumns.DATE_ADDED, currentTime / 1000)
                    values.put(MediaStore.Images.ImageColumns.DATE_MODIFIED, currentTime / 1000)
                    values.put(MediaStore.Images.ImageColumns.MIME_TYPE, "image/jpeg")
                    values.put(MediaStore.Images.ImageColumns.WIDTH, resource.width)
                    values.put(MediaStore.Images.ImageColumns.HEIGHT, resource.height)
                    val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                    var outputStream: OutputStream? = null
                    try {
                        outputStream = resolver.openOutputStream(uri)
                        resource.compress(Bitmap.CompressFormat.JPEG, 40, outputStream)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } finally {
                        outputStream?.flush()
                        outputStream?.close()
                    }
                    values.clear()
                    values.put(MediaStore.Images.ImageColumns.SIZE, file.length())
                    resolver.update(uri, values, null, null)
                    //保存成功
                    if (--mTaskCount <= 0) {
                        saveAllImageSuccess(saveSuccessListener, folder, fileList)
                    }
                    //MediaStore.Images.Media.insertImage(mContext.contentResolver, resource, file.name, null)
                } else {
                    handleSaveImageFail(saveFailListener)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            handleSaveImageFail(saveFailListener)
        }
    }

    /**
     * 保存推广图失败处理（终止线程任务）
     * */
    private fun handleSaveImageFail(saveFailListener: () -> Unit) {
        if (::mThreadPool.isInitialized && !mThreadPool.isTerminated) {
            try {
                ApplicationUtils.getMainHandler().post { saveFailListener.invoke() }
                mThreadPool.shutdownNow()
            } catch (e: Exception) {
                LogUtils.error("保存推广图片失败${e.message}")
            }
        }
    }

    //保存相册成功
    private fun saveAllImageSuccess(finishListener: (rootFile: File, fileList: ArrayList<File>) -> Unit, rootFile: File, fileList: ArrayList<File>) {
        val iterator = fileList.iterator()
        while (iterator.hasNext()) {//删除保存失败的file
            val item = iterator.next()
            if (!item.exists()) {
                iterator.remove()
            }
        }
        ApplicationUtils.getMainHandler().post {
            finishListener.invoke(rootFile, fileList)
        }
    }

    /**
     * 微信朋友圈分享
     * */
    private fun onShareFriendsCircle() {
        if (!NetworkUtils.isNetworkAvailable()) {
            ToastHelper.showToast(mContext.getString(R.string.no_network_alert))
            return
        }
        if (mImageList.isEmpty()) return
        //ClipBoardManagerHelper.getInstance.writeToClipBoardContent(mShareData.share.title)
        ToastHelper.showToast(mContext.getString(R.string.share_title_copy_alert))
        if (mImageList.size == 1) {
            saveSingleImage({
                WxShareUtils.getInstance(mContext).shareImageAction(false, "分享朋友圈",
                        "分享朋友圈", BitmapUtils.compressBitmapToByte(mBitmapList[0], 40, false))
                releaseBitmap()
            }, { msg ->
                ToastHelper.showToast(msg)
            })
        } else {
            saveMultiplyImageAndShareFriendCircle()
        }
    }

    /**
     * 保存分享图片并分享朋友圈
     * */
    private fun saveMultiplyImageAndShareFriendCircle() {
        if (PermissionUtils.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            showLoadingDialog()
            saveAllShareImage({ rootFile, listFile ->
                dismissLoadingDialog()
                sendAlbumBroadcast(rootFile, listFile)
                if (::mShareFriendCircleDialog.isInitialized) {
                    mShareFriendCircleDialog.show()
                }
            }, {
                dismissLoadingDialog()
                ToastHelper.showCenterToastLong("保存图片失败，请稍后重试")
            })
        } else {
            PermissionUtils.requestPermission(mContext, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE))
        }
    }

    /**
     * 微信好友分享
     * */
    private fun onShareWechat() {
        if (!NetworkUtils.isNetworkAvailable()) {
            ToastHelper.showToast(mContext.getString(R.string.no_network_alert))
            return
        }
        if (mImageList.isEmpty()) return
        //复制文本
        //ClipBoardManagerHelper.getInstance.writeToClipBoardContent(mShareData.share.title)
        ToastHelper.showToast(mContext.getString(R.string.share_title_copy_alert))
        if (mImageList.size == 1) {
            saveSingleImage({
                WxShareUtils.getInstance(mContext).shareImageAction(true, "微信好友分享",
                        "微信好友分享", BitmapUtils.compressBitmapToByte(mBitmapList[0], 40, false))
                releaseBitmap()
            }, { msg ->
                ToastHelper.showToast(msg)
            })
        } else {
            saveMultiplyImageAndShareWechat()
        }
    }

    /**
     * 保存分享图片并分享微信好友
     * */
    private fun saveMultiplyImageAndShareWechat() {
        if (PermissionUtils.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            showLoadingDialog()
            saveAllShareImage({ rootFile, listFile ->
                dismissLoadingDialog()
                sendAlbumBroadcast(rootFile, listFile)
                if (!PackageUtils.isAppInstalled(mContext, PackageUtils.WEI_XIN)) {
                    val confirmDialog = CommonConfirmDialog(mContext)
                    confirmDialog.setDefaultTitleTxt("未检测到微信客户端！")
                    confirmDialog.setOnClickSureListener { confirmDialog.dismiss() }
                    confirmDialog.show()
                } else {
                    WxShareUtils.getInstance(mContext).shareMultiImageAction(mContext, listFile)
                }
            }, {
                dismissLoadingDialog()
                ToastHelper.showCenterToastLong("保存图片失败，请稍后重试")
            })
        } else {
            PermissionUtils.requestPermission(mContext, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE))
        }
    }

    private fun saveSingleImage(successListener: (() -> Unit), failListener: ((String) -> Unit)) {
        showLoadingDialog()
        releaseBitmap()
        Glide.with(mContext).load(mImageList[0]).asBitmap().into(object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                resource?.let {
                    mBitmapList.add(resource)
                    successListener.invoke()
                }
                dismissLoadingDialog()
                if (resource == null) {
                    ToastHelper.showToast("保存图片失败，请稍后重试！")
                }
            }

            override fun onLoadFailed(e: java.lang.Exception?, errorDrawable: Drawable?) {
                super.onLoadFailed(e, errorDrawable)
                dismissLoadingDialog()
                failListener.invoke("${e?.message}")
            }
        })
    }

    /**
     * 发送系统广播，刷新照片
     * */
    private fun sendAlbumBroadcast(folder: File, listFile: ArrayList<File>) {
        val arrayFile = arrayOfNulls<String>(listFile.size)
        val arrayType = arrayOfNulls<String>(listFile.size)
        for (i in 0 until listFile.size) {
            arrayFile[i] = listFile[i].absolutePath
            arrayType[i] = "image/JPEG"
        }
        if (!::mScanner.isInitialized) {
            mScanner = SingleMediaScanner(ApplicationUtils.getApplication())
        }
        mScanner.setScannerData(arrayFile, arrayType)
    }

    /**
     * 终止所有任务
     * */
    private fun shutdownAllTask() {
        if (::mThreadPool.isInitialized && !mThreadPool.isTerminated) {
            try {
                LogUtils.info("shutdown------开始终止所有任务")
                mThreadPool.shutdownNow()
                LogUtils.info("shutdown------终止所有任务成功")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun showLoadingDialog() {
        if (!::mLoadDialog.isInitialized) {
            mLoadDialog = LoadingDialog(mContext)
        }
        mLoadDialog.show()
    }

    private fun dismissLoadingDialog() {
        if (::mLoadDialog.isInitialized) {
            mLoadDialog.dismiss()
        }
        //如果用户主动关闭弹窗，则停止所有任务
        shutdownAllTask()
    }

    /**
     * activity中finish调用
     * */
    fun release() {
        releaseBitmap()
        if (::mThreadPool.isInitialized && !mThreadPool.isShutdown) {
            mThreadPool.shutdownNow()
        }
        if (::mLoadDialog.isInitialized && mLoadDialog.isShowing) {
            mLoadDialog.dismiss()
        }
        if (::mShareFriendCircleDialog.isInitialized && mShareFriendCircleDialog.isShowing) {
            mShareFriendCircleDialog.dismiss()
        }
        if (::mShareTypeSelectDialog.isInitialized && mShareTypeSelectDialog.isShowing) {
            mShareTypeSelectDialog.dismiss()
        }
    }

    private fun releaseBitmap() {
        mBitmapList.forEach {
            if (!it?.isRecycled) {
                it?.recycle()
            }
        }
        mBitmapList.clear()
    }

}