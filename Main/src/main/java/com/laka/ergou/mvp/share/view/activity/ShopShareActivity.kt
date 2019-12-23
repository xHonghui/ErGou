package com.laka.ergou.mvp.share.view.activity

import android.Manifest
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.view.View
import com.laka.androidlib.base.activity.BaseMvpActivity
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.ApplicationUtils
import com.laka.androidlib.util.PermissionUtils
import com.laka.androidlib.util.StatusBarUtil
import com.laka.androidlib.util.network.NetworkUtils
import com.laka.androidlib.util.screen.ScreenUtils
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.ergou.R
import com.laka.ergou.common.util.ClipBoardManagerHelper
import com.laka.ergou.common.util.share.WxShareUtils
import com.laka.ergou.mvp.circle.weight.GlideSimpleLoader
import com.laka.ergou.mvp.circle.weight.imagewatcher.ImageWatcherHelper
import com.laka.ergou.mvp.main.constant.HomeApiConstant
import com.laka.ergou.mvp.share.ShopShareModuleNavigator
import com.laka.ergou.mvp.share.constant.ShareConstant
import com.laka.ergou.mvp.share.contract.ShopShareConstract
import com.laka.ergou.mvp.share.helper.HorizonalShopImgShareHelper
import com.laka.ergou.mvp.share.helper.WechatLinkShareHelper
import com.laka.ergou.mvp.share.model.bean.ShareResponse
import com.laka.ergou.mvp.share.presenter.ShopSharePresenter
import com.laka.ergou.mvp.share.utils.SingleMediaScanner
import com.laka.ergou.mvp.shop.utils.ShareUtils
import com.laka.ergou.mvp.shop.view.dialog.ShareDialog
import com.laka.ergou.mvp.shop.weight.ShareProductDialog
import kotlinx.android.synthetic.main.activity_see_share_image.*
import kotlinx.android.synthetic.main.activity_shop_share.*
import java.io.File
import java.util.*

/**
 * @Author:summer
 * @Date:2019/5/24
 * @Description:商品分享
 */
class ShopShareActivity : BaseMvpActivity<String>(), View.OnClickListener, ShopShareConstract.IShopShareView {

    private lateinit var mShareData: ShareResponse
    private lateinit var mHorizonalShopImgHelper: HorizonalShopImgShareHelper
    private lateinit var mShareWechatCommentHelper: WechatLinkShareHelper
    private var mHandler = Handler()
    private var mIsRuning: Boolean = false
    private lateinit var mPresenter: ShopSharePresenter
    private lateinit var mShareProductDialog: ShareProductDialog
    private lateinit var iwHelper: ImageWatcherHelper

    override fun showData(data: String) {

    }

    override fun showErrorMsg(msg: String?) {

    }

    override fun createPresenter(): IBasePresenter<*>? {
        mPresenter = ShopSharePresenter()
        return mPresenter
    }

    override fun setContentView(): Int {
        return R.layout.activity_shop_share
    }

    override fun setStatusBarColor(color: Int) {
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.color_ffd11c), 0)
    }

    override fun initIntent() {
        mShareData = intent.getSerializableExtra(ShareConstant.SHARE_DATA_FOR_WECHAT) as ShareResponse
    }

    override fun initViews() {
        initWatcher()
        initShopDetailImg()
        initFriendCircleComment()
        initSahreDialog()
    }

    private fun initWatcher() {
        iwHelper = ImageWatcherHelper.with(this, GlideSimpleLoader()) // 一般来讲， ImageWatcher 需要占据全屏的位置
                .setTranslucentStatus(ScreenUtils.getStatusBarHeight()) // 如果不是透明状态栏，你需要给ImageWatcher标记 一个偏移值，以修正点击ImageView查看的启动动画的Y轴起点的不正确
                .setErrorImageRes(R.drawable.default_img) // 配置error图标 如果不介意使用lib自带的图标，并不一定要调用这个API
    }

    private fun initSahreDialog() {
        mShareProductDialog = ShareProductDialog(this)
        mShareProductDialog.setOnOpenListener {
            //进入微信
            ShareUtils.goToThirdParty(this, ShareDialog.WEIXIN_PACKAGE_NAME)
        }
    }

    private fun initFriendCircleComment() {

    }

    private fun initShopDetailImg() {
        mHorizonalShopImgHelper = HorizonalShopImgShareHelper()
        mHorizonalShopImgHelper.setOnCreateScreenShotStatusListener({
            showLoading()
        }, {
            dismissLoading()
        })
        mHorizonalShopImgHelper.initViewAndData(this, mShareData, ll_shop_img, cl_extension, cl_root,iwHelper)
        mShareWechatCommentHelper = WechatLinkShareHelper()
        mShareWechatCommentHelper.initViewAndData(this, mShareData, tv_share_text, ll_share_comment)
    }

    override fun initData() {
        //下载二维码图片
        //mPresenter.onLoadQrCode(mShareData.share.productId)
    }

    override fun initEvent() {
        tv_see_video_course.setOnClickListener(this)
        sb_friend_wechat.setOnClickListener(this)
        sb_friend_circle.setOnClickListener(this)
        sb_copy_comment.setOnClickListener(this)
        iv_wiki.setOnClickListener(this)
        iv_more.setOnClickListener(this)
        iv_back.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        if (mIsRuning) return
        when (view?.id) {
            R.id.iv_wiki,
            R.id.iv_more,
            R.id.tv_see_video_course -> {
                ShopShareModuleNavigator.startWechatMomentCourseWebActivity(this, HomeApiConstant.URL_WECHAT_MOMENT, "朋友圈分享介绍")
            }
            R.id.sb_friend_wechat -> { //微信好友分享
                onWechatShare()
            }
            R.id.sb_friend_circle -> { //微信朋友圈分享
                onFriendCircleShare()
            }
            R.id.sb_copy_comment -> {  //复制朋友圈评论
                if (::mShareWechatCommentHelper.isInitialized) {
                    mShareWechatCommentHelper.copyCommentContent()
                    ToastHelper.showToast("复制成功，快去朋友圈论吧")
                    mHandler.postDelayed({
                        ShareUtils.goToThirdParty(this, ShareDialog.WEIXIN_PACKAGE_NAME)
                        mIsRuning = false
                    }, 1000)
                    mIsRuning = true
                }
            }
            R.id.iv_back -> {
                finish()
            }
            else -> {

            }
        }
    }

    /**
     * 微信分享
     * */
    private fun onWechatShare() {
        if (!NetworkUtils.isNetworkAvailable()) {
            ToastHelper.showToast(getString(R.string.no_network_alert))
            return
        }
        val imageList = mHorizonalShopImgHelper.getSelectedImg()
        ClipBoardManagerHelper.getInstance.writeToClipBoardContent(mShareData.share.title)
        ToastHelper.showToast(getString(R.string.share_title_copy_alert))
        if (imageList.size == 1) {
            mIsRuning = true
            mHandler.postDelayed({
                mIsRuning = false
                WxShareUtils.getInstance(this)
                        .shareImageAction(true, mShareData.share.title, mShareData.share.content, mHorizonalShopImgHelper.getExtensionImage())
            }, 500)
        } else {
            saveMultiplyImageAndShareWechat()
        }
    }

    /**
     * 保存分享图片并分享微信好友
     * */
    private fun saveMultiplyImageAndShareWechat() {
        if (PermissionUtils.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            showLoading()
            mHorizonalShopImgHelper.saveAllShareImage({ _, rootFile, listFile ->
                dismissLoading()
                sendAlbumBroadcast(rootFile, listFile)
                WxShareUtils.getInstance(this).shareMultiImageAction(this, listFile)
            }, {
                dismissLoading()
                ToastHelper.showCenterToastLong("保存图片失败，请稍后重试")
            })
        } else {
            PermissionUtils.requestPermission(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE))
        }
    }

    /**
     * 朋友圈分享
     * */
    private fun onFriendCircleShare() {
        if (!NetworkUtils.isNetworkAvailable()) {
            ToastHelper.showToast(getString(R.string.no_network_alert))
            return
        }
        val imageList = mHorizonalShopImgHelper.getSelectedImg()
        ClipBoardManagerHelper.getInstance.writeToClipBoardContent(mShareData.share.title)
        if (imageList.size == 1) {
            ToastHelper.showToast(getString(R.string.share_title_copy_alert))
            mIsRuning = true
            mHandler.postDelayed({
                mIsRuning = false
                //复制商品标题，一秒后分享微信好友
                WxShareUtils.getInstance(this)
                        .shareImageAction(false, mShareData.share.title, mShareData.share.content, mHorizonalShopImgHelper.getExtensionImage())
            }, 500)
        } else {
            saveMultiplyImageAndShareFriendCircle()
        }
    }

    /**
     * 保存分享图片并分享朋友圈
     * */
    private fun saveMultiplyImageAndShareFriendCircle() {
        if (PermissionUtils.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            showLoading()
            mHorizonalShopImgHelper.saveAllShareImage({ _, rootFile, listFile ->
                dismissLoading()
                sendAlbumBroadcast(rootFile, listFile)
                if (::mShareProductDialog.isInitialized) {
                    mShareProductDialog.show()
                }
            }, {
                dismissLoading()
                ToastHelper.showCenterToastLong("保存图片失败，请稍后重试")
            })
        } else {
            PermissionUtils.requestPermission(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE))
        }
    }

    private lateinit var mScanner: SingleMediaScanner

    private fun sendAlbumBroadcast(file: File, listFile: ArrayList<File>) {
        // 发送广播通知图库更新相册，一些低版本的机型不能及时更新相册
//        val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
//        val uri = Uri.fromFile(file)
//        intent.data = uri
//        mContext.sendBroadcast(intent)

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

    override fun onLoadQrCodeSuccess(path: String) {

    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
        if (::mHorizonalShopImgHelper.isInitialized) {
            mHorizonalShopImgHelper.release()
        }
        if (::mScanner.isInitialized) {
            mScanner.release()
        }
    }

}