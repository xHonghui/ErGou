package com.laka.ergou.mvp.share.helper

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.laka.androidlib.util.ApplicationUtils
import com.laka.androidlib.util.GlideUtil
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.QRCodeUtil
import com.laka.androidlib.util.image.BitmapUtils
import com.laka.androidlib.util.screen.ScreenUtils
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.androidlib.util.transform.GlideRoundTransform
import com.laka.ergou.R
import com.laka.ergou.common.threadpool.ThreadPoolHelp
import com.laka.ergou.common.threadpool.ThreadPoolType
import com.laka.ergou.mvp.circle.weight.imagewatcher.ImageWatcherHelper
import com.laka.ergou.mvp.share.ShopShareModuleNavigator
import com.laka.ergou.mvp.share.model.bean.ShareResponse
import com.laka.ergou.mvp.shop.model.bean.WechatShareBean
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.concurrent.ExecutorService


/**
 * @Author:summer
 * @Date:2019/5/27
 * @Description:商品详情图片分享工具类
 */
class HorizonalShopImgShareHelper {

    private lateinit var mContext: Context
    private lateinit var mLlRootView: LinearLayout
    private lateinit var mCheckArray: Array<Int>
    private lateinit var mWechatShareBean: WechatShareBean
    private var mScreenshotData = HashMap<Int, ByteArray>()
    private var mShareImageData = HashMap<Int, ByteArray>()
    private var mImageList = ArrayList<String>()
    private var mExtensionPosition = 0
    private lateinit var mScreenShotView: View
    private lateinit var mAllRootView: ViewGroup
    //本地推广图
    private lateinit var mSaveFile: File
    //首次截图延时
    private var mFirstTime: Long = 200
    //非首次截图延时
    private var mDelayedTime: Long = 100
    //线程池
    private lateinit var mThreadPool: ExecutorService
    //线程池名称
    private var mThreadPoolName: String = "thread_save_extension_image"
    //核心线程数
    private var mCorePoolSize: Int = 5
    //首次开始生成推广图监听
    private lateinit var mFirstStartCreateScreenShotListener: ((pos: Int) -> Unit)
    //首次生成推广图流程结束监听（可能是生成失败，也有可能是生成成功）
    private lateinit var mFirstFinishCreateScreenShotListener: ((pos: Int) -> Unit)
    //    private lateinit var imageList:MutableList<ImageView>
    var imageList = SparseArray<ImageView>()
    lateinit var iwHelper: ImageWatcherHelper
    fun initViewAndData(context: Context, data: ShareResponse, contentView: LinearLayout, screenShopView: View, rootView: ViewGroup, iwHelper: ImageWatcherHelper) {
        this.mContext = context
        this.mLlRootView = contentView
        this.mScreenShotView = screenShopView
        this.mAllRootView = rootView
        this.iwHelper = iwHelper
        this.mImageList = data.smallImages.imageList
        this.mSaveFile = File(context.cacheDir, "ergou_extension_img.jpg")
        mWechatShareBean = data.share
        mCheckArray = Array(mImageList.size, { 0 })
        for (i in 0 until mImageList.size) {
            val itemView = LayoutInflater.from(context).inflate(R.layout.item_wechat_share_shop_img, mLlRootView, false)
            val ivImg = itemView.findViewById<ImageView>(R.id.iv_img)
            val ivCheck = itemView.findViewById<ImageView>(R.id.iv_check)
            val tvTxt = itemView?.findViewById<TextView>(R.id.tv_txt)
            val layoutParams = LinearLayout.LayoutParams(ScreenUtils.dp2px(85f), ScreenUtils.dp2px(85f))
            layoutParams.rightMargin = ScreenUtils.dp2px(3f)
            layoutParams.leftMargin = ScreenUtils.dp2px(3f)
            ivCheck.isSelected = false
            tvTxt?.visibility = View.GONE
            GlideUtil.loadImage(mContext, mImageList[i], R.drawable.default_img, ivImg)
            if (i == 0) {
                mCheckArray[0] = 1
                mExtensionPosition = i
                ivCheck.isSelected = true
                tvTxt?.visibility = View.VISIBLE
                layoutParams.leftMargin = ScreenUtils.dp2px(0f)
            }
            imageList.put(i, ivImg)
            mLlRootView.addView(itemView, layoutParams)
            ivCheck.tag = i
            ivCheck.setOnClickListener(mOnClickListener)
            itemView.tag = i
            itemView.setOnClickListener(mItemViewOnClickListener)
        }
        if (::mFirstStartCreateScreenShotListener.isInitialized) {
            mFirstStartCreateScreenShotListener.invoke(mExtensionPosition)
        }
        //生成第一张推广图
        createExtensionImage(true)
    }

    // itemView 点击事件
    private var mItemViewOnClickListener = View.OnClickListener {
        val tag = it.tag as? Int
        var list = mutableListOf<Uri>()
        for (i in 0 until mImageList.size) {
            list.add(Uri.parse(mImageList[i]))
        }

        tag?.let {
            iwHelper.show(imageList[tag], imageList, list, mWechatShareBean,mExtensionPosition)
//            ShopShareModuleNavigator.startSeeBigImageActivity(mContext, list, tag)
        }
    }
    //勾选按钮点击事件
    private var mOnClickListener = View.OnClickListener {
        val tag = it.tag as Int
        if (tag >= 0 && tag <= mCheckArray.size - 1) {
            if (!isCancelLastItem(it)) {
                it.isSelected = !it.isSelected
                mCheckArray[tag] = if (it.isSelected) 1 else 0
            } else {
                ToastHelper.showCenterToast("至少需要选择一张图片哦！")
                return@OnClickListener
            }
        }
        handleExtensionPosition()
    }

    /**
     * 判断是否是取消最后一个
     * */
    private fun isCancelLastItem(view: View): Boolean {
        var checkCount = 0
        mCheckArray.forEach { if (it == 1) checkCount++ }
        return checkCount == 1 && view.isSelected
    }

    /**
     * 处理推广图位置相关（切换推广图）
     * */
    private fun handleExtensionPosition() {
        for (i in 0 until mCheckArray.size) {
            if (mCheckArray[i] == 1) {
                if (i != mExtensionPosition) { //推广图更换了
                    val preTxtView = mLlRootView.getChildAt(mExtensionPosition).findViewById<TextView>(R.id.tv_txt)
                    val nowTxtView = mLlRootView.getChildAt(i).findViewById<TextView>(R.id.tv_txt)
                    preTxtView.visibility = View.GONE
                    nowTxtView.visibility = View.VISIBLE
                    mExtensionPosition = i
                    createExtensionImage(false)
                }
                break
            }
        }
    }

    /**
     * 生成推广图
     * */
    private fun createExtensionImage(isFirst: Boolean) {
        val byteArray = mScreenshotData[mExtensionPosition]
        if (byteArray != null && byteArray.isNotEmpty()) {
            saveBitmap()
        } else {
            //初始化UI
            initScreenShotView(mScreenShotView, mWechatShareBean, isFirst)
        }
    }

    private fun initScreenShotView(view: View, data: WechatShareBean, isFirst: Boolean) {
        if (::mFirstStartCreateScreenShotListener.isInitialized) {
            mFirstStartCreateScreenShotListener.invoke(mExtensionPosition)
        }
        val tvTitle = view.findViewById<TextView>(R.id.tv_shop_title)
        val tvPrice = view.findViewById<TextView>(R.id.tv_shop_price)
        val tvPrice2 = view.findViewById<TextView>(R.id.tv_shop_price2)
        val tvOriginPrice = view.findViewById<TextView>(R.id.tv_origin_price)
        val tvVolume = view.findViewById<TextView>(R.id.tv_volume)
        val tvCouponValue = view.findViewById<TextView>(R.id.tv_coupon_value)
        val ivQrCode = view.findViewById<ImageView>(R.id.iv_qr_code)
        val ivShopDetail = view.findViewById<ImageView>(R.id.iv_shop_detail)
        tvTitle.text = data.title
        tvPrice.text = data.actualPrice
        tvPrice2.text = data.actualPrice
        tvOriginPrice.text = "￥${data.zkFinalPrice}"
        tvVolume.text = "销量${data.volume}"
        tvCouponValue.text = "￥${data.couponMoney}"
        val content = "${mWechatShareBean.tklShareUrl}"
        val bitmap = QRCodeUtil.createQRCodeBitmap(content, ScreenUtils.dp2px(83f), 0)
        if (bitmap != null) {
            ivQrCode.setImageBitmap(bitmap)
            downloadShopDetailImage(isFirst, ivShopDetail)
        } else {
            if (::mFirstFinishCreateScreenShotListener.isInitialized) {
                mFirstFinishCreateScreenShotListener.invoke(mExtensionPosition)
            }
            ToastHelper.showCenterToast("生成二维码失败")
        }
    }

    private fun downloadShopDetailImage(isFirst: Boolean, ivShopDetail: ImageView) {
        Glide.with(mContext)
                .load(mImageList[mExtensionPosition])
                .asBitmap()
                .transform(CenterCrop(mContext), GlideRoundTransform(mContext, 6))
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                        val delayedTime = if (isFirst) mFirstTime else mDelayedTime
                        ivShopDetail.setImageBitmap(resource)
                        ivShopDetail.postDelayed({
                            screenShotView()
                        }, delayedTime)
                    }
                })
    }

    /**
     * 截图并保存操作
     * */
    private fun screenShotView() {
        mScreenShotView?.let {
            mScreenShotView.buildDrawingCache()
            var bitmap = mScreenShotView.drawingCache
            val array = BitmapUtils.compressBitmapToByte(bitmap, 50, false)
            if (array != null && array.isNotEmpty()) {
                mScreenshotData[mExtensionPosition] = array
                saveBitmap()
            } else {
                ToastHelper.showCenterToast("生成推广图失败，请稍后重试")
            }
        }
        if (::mFirstFinishCreateScreenShotListener.isInitialized) { //截图压缩成功
            mFirstFinishCreateScreenShotListener.invoke(mExtensionPosition)
        }
    }

    private fun saveBitmap(): Boolean {
        mSaveFile.delete()
        mSaveFile = File(mContext.cacheDir, "${System.currentTimeMillis()}_ergou_extension_img.jpg")
        return BitmapUtils.gainBitmapFromByte(mScreenshotData[mExtensionPosition]).compress(Bitmap.CompressFormat.JPEG, 60, FileOutputStream(mSaveFile))
    }

    fun getExtensionImageFile(): File {
        if (::mSaveFile.isInitialized) {
            return mSaveFile
        }
        return File("")
    }

    fun getSelectedImg(): ArrayList<String> {
        val list = ArrayList<String>()
        for (i in 0 until mCheckArray.size) {
            if (mCheckArray[i] == 1) {
                list.add(mImageList[i])
                LogUtils.info("HorizonalShopImgShareHelper-----选中的位置=${i + 1}")
            }
        }
        return list
    }

    /**
     * 获取推广图byte数据
     * */
    fun getExtensionImage(): ByteArray {
        return if (mScreenshotData[mExtensionPosition] != null) {
            mScreenshotData[mExtensionPosition]!!
        } else {
            ByteArray(1)
        }
    }

    private var mTaskCount: Int = 0

    /**
     * 保存相册到本地
     * */
    fun saveAllShareImage(saveSuccessListener: (pos: Int, rootFile: File, fileList: ArrayList<File>) -> Unit, saveFailListener: () -> Unit) {
        if (!::mThreadPool.isInitialized || mThreadPool.isTerminated) {
            mThreadPool = ThreadPoolHelp.Builder(ThreadPoolType.CUSTOM, mCorePoolSize).name(mThreadPoolName).builder()
            LogUtils.info("threadPool------创建新线程池----mThreadPool=$mThreadPool")
        }
        val selectImageList = getSelectedImg()
        mTaskCount = selectImageList.size
        var folder = File("${Environment.getExternalStorageDirectory().absolutePath}/ergou")
        val fileList = ArrayList<File>()
        if (!folder.exists()) folder.mkdirs()
        for (i in 0 until mImageList.size) {
            if (mCheckArray[i] != 1) continue
            val file = File(folder, "ergou_${i}_${System.currentTimeMillis()}.jpg")
            fileList.add(file)
            if (i == mExtensionPosition) { //保存推广图
                if (mThreadPool.isTerminated) {
                    return
                }
                try {
                    mThreadPool.execute {
                        saveExtensionImageTask(file, saveFailListener, saveSuccessListener, folder, fileList)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    handleSaveImageFail(saveFailListener)
                }
            } else {  //商品详情图
                Glide.with(mContext).load(mImageList[i]).asBitmap().into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                        saveNormalImageTask(resource, file, saveSuccessListener, folder, fileList, saveFailListener)
                    }
                })
            }
        }
    }

    private fun saveNormalImageTask(resource: Bitmap?, file: File, saveSuccessListener: (pos: Int, rootFile: File, fileList: ArrayList<File>) -> Unit,
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
                } else {
                    handleSaveImageFail(saveFailListener)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            handleSaveImageFail(saveFailListener)
        }
    }

    private fun saveExtensionImageTask(file: File, saveFailListener: () -> Unit,
                                       saveSuccessListener: (pos: Int, rootFile: File, fileList: ArrayList<File>) -> Unit,
                                       folder: File, fileList: ArrayList<File>) {
        val bitmap = BitmapUtils.gainBitmapFromByte(mScreenshotData[mExtensionPosition])
        if (bitmap != null) {
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
            values.put(MediaStore.Images.ImageColumns.WIDTH, bitmap.width)
            values.put(MediaStore.Images.ImageColumns.HEIGHT, bitmap.height)
            val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            var outputStream: OutputStream? = null
            try {
                outputStream = resolver.openOutputStream(uri)
                val isSuccess = bitmap.compress(Bitmap.CompressFormat.JPEG, 40, outputStream)
                if (!isSuccess) {
                    handleSaveImageFail(saveFailListener) //推广图保存本地失败，终止所有任务
                    return
                }
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
        } else {
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
    fun saveAllImageSuccess(finishListener: (pos: Int, rootFile: File, fileList: ArrayList<File>) -> Unit, rootFile: File, fileList: ArrayList<File>) {
        val iterator = fileList.iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()
            if (!item.exists()) {
                iterator.remove()
            }
        }
        ApplicationUtils.getMainHandler().post {
            finishListener.invoke(mExtensionPosition, rootFile, fileList)
        }
    }

    fun setOnCreateScreenShotStatusListener(startListener: ((pos: Int) -> Unit), finishListener: ((pos: Int) -> Unit)) {
        this.mFirstStartCreateScreenShotListener = startListener
        this.mFirstFinishCreateScreenShotListener = finishListener
    }

    fun release() {
        mScreenshotData.clear()
        if (::mThreadPool.isInitialized && !mThreadPool.isShutdown) {
            mThreadPool.shutdownNow()
        }
    }

}