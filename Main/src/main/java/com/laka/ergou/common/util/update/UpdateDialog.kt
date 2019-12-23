package com.laka.ergou.common.util.update

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.*
import com.laka.androidlib.eventbus.Event
import com.laka.androidlib.eventbus.EventBusManager
import com.laka.androidlib.listener.OnDebounceClickListener
import com.laka.androidlib.util.ApplicationUtils
import com.laka.androidlib.util.FileUtils
import com.laka.androidlib.util.ResourceUtils
import com.laka.androidlib.util.retrofit.download.DownloadEvent
import com.laka.androidlib.util.system.SystemHelper
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.ergou.R
import com.laka.ergou.mvp.main.model.bean.AppUpdateInfo
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @ClassName: UpdateDialog
 * @Description: 更新弹框
 * @Author: chuan
 * @Date: 08/04/2018
 */
class UpdateDialog(context: Context, private val mUpdateBean: AppUpdateInfo?,
                   private val mListener: OnViewStateChangeListener?) : Dialog(context, R.style.commonDialog) {

    private var mTvVersion: TextView? = null
    private var mBtnUpdate: TextView? = null
    private var mTvDes: TextView? = null
    private var mIbClose: ImageButton? = null
    private var mProgressBar: ProgressBar? = null
    private var mTvProgress: TextView? = null

    // 进度条的原始高度，在动态计算后，用于恢复最原始的高度。
    // 靠拿dp的话，会有细微的高度差，体验不好。
    private var mTempHeight = -1

    /**
     * 进度条显示下载进度
     *
     * @param progress 下载进度
     */
    // 小于前15度，动态设置高度，实现放大效果
    // 设置过一次原始高度后，就不用再设置了
    var progress: Int
        get() = mProgressBar!!.progress
        set(progress) {

            if (progress < 0 || progress > 100) {
                throw RuntimeException("progress can't be less than 0 or higher than 100")
            }

            if (progress <= 15) {
                var scaleX = (progress / 15.0).toFloat()
                scaleX = if (scaleX > 1) 1f else scaleX
                if (mTempHeight < 0) {
                    mTempHeight = mProgressBar!!.layoutParams.height
                }
                setProgressHeight((ResourceUtils.getDimen(R.dimen.dp_40) * scaleX).toInt())
            } else if (mTempHeight != -1) {
                setProgressHeight(mTempHeight)
                mTempHeight = -1
            }

            mProgressBar!!.progress = progress
            mTvProgress!!.text = progress.toString() + "%"
        }

    init {
        setCancelable(false)
        EventBusManager.register(this)
        initView()
        initData()
    }

    private fun initView() {
        super.setContentView(R.layout.dialog_update)

        mTvVersion = findViewById(R.id.tv_version)
        mBtnUpdate = findViewById(R.id.btn_update)
        mTvDes = findViewById(R.id.tv_des)
        mIbClose = findViewById(R.id.ib_close)
        mProgressBar = findViewById(R.id.pb_update)
        mTvProgress = findViewById(R.id.tv_progress)
    }

    private fun initData() {
        if (mUpdateBean == null) {
            handleOnDismiss()
            return
        }

        //根据更新的状态显示不同的ui
        if (mUpdateBean?.isForceUpdate()) {
            mIbClose?.visibility = View.GONE
            mBtnUpdate!!.visibility = View.VISIBLE
            mBtnUpdate!!.setOnClickListener(object : OnDebounceClickListener() {
                override fun handleClickEvent(v: View) {
                    handleOnUpdateClick()
                }
            })
        } else {
            mIbClose?.visibility = View.VISIBLE
            mBtnUpdate!!.visibility = View.VISIBLE
            mBtnUpdate!!.setOnClickListener(object : OnDebounceClickListener() {
                override fun handleClickEvent(v: View) {
                    handleOnUpdateClick()
                }
            })
            mIbClose?.setOnClickListener(object : OnDebounceClickListener() {
                override fun handleClickEvent(v: View) {
                    handleOnDismiss()
                }
            })
        }

        mTvVersion?.text = "v${mUpdateBean?.appVersionName}"
        mTvDes?.text = mUpdateBean?.updateDescription + "\n" //多一行空行
        mTvProgress!!.visibility = View.GONE
        mProgressBar!!.visibility = View.GONE
    }

    /**
     * 处理更新按钮的点击事件
     */
    private fun handleOnUpdateClick() {
        if (mListener == null) {
            handleOnDismiss()
            return
        }

        mTvProgress!!.text = "0%"
        mProgressBar!!.progress = 0
        mTvProgress!!.visibility = View.VISIBLE
        mProgressBar!!.visibility = View.VISIBLE
        mBtnUpdate!!.visibility = View.GONE
        mIbClose?.visibility = View.GONE

        mListener.onUpdateClick()
    }

    /**
     * 处理弹窗主动消失时的事件
     */
    fun handleOnDismiss() {
        dismiss()
        mListener?.onDialogDismiss()
    }

    /**
     * 下载失败
     */
    fun showFailed() {

        mBtnUpdate!!.text = "点击重试"
        mBtnUpdate!!.visibility = View.VISIBLE
        mTvProgress!!.visibility = View.GONE
        mProgressBar!!.visibility = View.GONE

        // 不是强制更新的话，就允许退出
        if (!mUpdateBean?.isForceUpdate()!!) {
            mIbClose?.visibility = View.VISIBLE
        }

    }

    override fun dismiss() {
        super.dismiss()
        EventBusManager.unRegister(this)
    }

    /**
     * 按钮的状态改变回调
     */
    interface OnViewStateChangeListener {
        // Click to start updating your app
        fun onUpdateClick()

        // Called when the dialog is dismissing.
        fun onDialogDismiss()
    }

    /**
     * description:进度回调
     **/
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: Event) {
        when (event?.name) {
            DownloadEvent.DOWNLOAD_START -> {

            }
            DownloadEvent.DOWNLOAD_PROGRESS -> {
                val downloadEvent: DownloadEvent = event?.data as DownloadEvent
                progress = downloadEvent.downloadProgress
            }
            DownloadEvent.DOWNLOAD_FAILED -> {
                ToastHelper.showToast("下载更新包失败.")
                showFailed()
            }
            DownloadEvent.DOWNLOAD_FINISH -> {
                try {
                    // 下载成功后，如果不是强制更新，就自动关掉下载的界面。
                    if (!mUpdateBean?.isForceUpdate()!!) {
                        dismiss()
                    }
                    ToastHelper.showToast("下载完成")
                    SystemHelper.installApk(ApplicationUtils.getApplication(),
                            FileUtils.getAppFile("ergou.apk", null, true))

                } catch (e: Exception) {
                    ToastHelper.showToast("无法解析安装包")
                }
            }
        }
    }

    //动态设置进度条高度，实现放大效果
    private fun setProgressHeight(height: Int) {
        if (height < 0) {
            return
        }
        val layoutParams = mProgressBar!!.layoutParams as FrameLayout.LayoutParams
        layoutParams.height = height
        layoutParams.gravity = Gravity.START or Gravity.LEFT or Gravity.CENTER_VERTICAL
        mProgressBar!!.layoutParams = layoutParams
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

    }

    companion object {
        private val TAG = UpdateDialog::class.java.canonicalName
    }
}