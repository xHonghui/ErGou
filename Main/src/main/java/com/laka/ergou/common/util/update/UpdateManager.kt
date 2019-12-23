package com.laka.ergou.common.util.update

import android.content.Context
import com.laka.androidlib.eventbus.Event
import com.laka.androidlib.eventbus.EventBusManager
import com.laka.androidlib.features.update.util.DefaultDownloadTaskImpl
import com.laka.androidlib.features.update.util.DownloadApkUtil
import com.laka.androidlib.features.update.util.OnDownloadListener
import com.laka.androidlib.util.*
import com.laka.androidlib.util.rx.RxSchedulerComposer
import com.laka.androidlib.util.system.SystemHelper
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.ergou.R
import com.laka.ergou.mvp.main.constant.HomeEventConstant
import com.laka.ergou.mvp.main.model.bean.AppUpdateInfo
import com.laka.ergou.mvp.main.model.repository.HomeApiRepository
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import okhttp3.ResponseBody

/**
 * @Author UnKnown
 * @Changer: lyf
 * @ChangeTime: 2018/5/17
 * @Description UpdateManager作用：
 * 1、检测更新。
 * 2、下载最新版本的应用。
 */
class UpdateManager(private val mContext: Context) {

    private var mUpdateDialog: UpdateDialog? = null
    private var mDownloadApkUtil: DownloadApkUtil? = null
    private var mAppUpdateInfo: AppUpdateInfo? = null
    private var isUpdateFailed = true


    /**
     * @param isShowToast 如果为最新版的话：true, 则提示“当前为最新版”。false，则不提示。
     */
    fun checkUpdate(isShowToast: Boolean) {
        if (isUpdateFailed) {
            HomeApiRepository.getAppUpdateInfo()
                    .subscribe(object : Observer<AppUpdateInfo> {
                        override fun onComplete() {

                        }

                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onNext(t: AppUpdateInfo) {
                            isUpdateFailed = false
                            mAppUpdateInfo = t

                            // 保存最新版本号
                            //MainApplication.getInstance().setServerVersion(mAppUpdateInfo?.getVersionName())

                            if (mAppUpdateInfo?.isNeedUpdate()!!) {
                                ApplicationUtils.setIsShowUpdateDialog(showUpdateDialog())
                                EventBusManager.postEvent(Event(HomeEventConstant.EVENT_CHECK_UPDATE_FINISH, 0))
                            } else {
                                if (isShowToast) {
                                    ToastHelper.showCenterToast(ResourceUtils.getString(R.string.new_version))
                                }
                                ApplicationUtils.setIsShowUpdateDialog(false)
                                EventBusManager.postEvent(Event(HomeEventConstant.EVENT_CHECK_UPDATE_FINISH, 0))
                            }
                        }

                        override fun onError(e: Throwable) {
                            isUpdateFailed = true
                            if (isShowToast) {
                                ToastHelper.showToast("检查更新失败")
                            }
                            ApplicationUtils.setIsShowUpdateDialog(false)
                            EventBusManager.postEvent(Event(HomeEventConstant.EVENT_CHECK_UPDATE_FINISH, 0))
                        }
                    })
        }
    }

    // 显示更新Dialog
    private fun showUpdateDialog(): Boolean {
        if (!ContextUtil.isValidContext(mContext)) {
            return false
        }
        mUpdateDialog = UpdateDialog(mContext, mAppUpdateInfo,
                object : UpdateDialog.OnViewStateChangeListener {
                    override fun onUpdateClick() {
                        startDownload()
                    }

                    override fun onDialogDismiss() {
                        // 退出了，就不要再下载了。
                        if (mDownloadApkUtil != null) {
                            mDownloadApkUtil!!.cancelDownloadTask()
                        }
                    }
                })
        mUpdateDialog?.setOnDismissListener {
            ApplicationUtils.setIsShowUpdateDialog(false)
            EventBusManager.postEvent(Event(HomeEventConstant.EVENT_CHECK_UPDATE_FINISH, 0))
        }
        mUpdateDialog?.show()
        return true
    }

    // 开始下载
    private fun startDownload() {

        ToastHelper.showToast("正在下载更新包")

        // 获取本地的文件,用于下载apk
        val mApkFile = FileUtils.getAppFile("ergou.apk", null, true)

        if (mApkFile == null) {

            ToastHelper.showToast("更新失败，创建文件失败")
            if (mAppUpdateInfo?.isForceUpdate()!!) {
                mUpdateDialog?.showFailed()
            } else {
                if (!ContextUtil.isValidContext(mContext)) {
                    return
                }
                mUpdateDialog?.dismiss()
            }
            return
        }

        // 开始下载Apk
        LogUtils.info("输出下载地址:${mAppUpdateInfo?.downLoadUrl!!}")
        HomeApiRepository.downloadAPK(mAppUpdateInfo?.downLoadUrl!!)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : Observer<ResponseBody> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                        LogUtils.info("开始下载")
                    }

                    override fun onNext(response: ResponseBody) {
                        mDownloadApkUtil = DownloadApkUtil.Builder()
                                .setFilePath(mApkFile.absolutePath) // Apk要存放的位置
                                .setInputStream(response?.byteStream()) // 下载流
                                .setContentLength(response?.contentLength()) // Apk总大小
                                .setDownLoadTaskImpl(DefaultDownloadTaskImpl())// 实际下载的代码在这个类里面
                                .setCallback(object : OnDownloadListener {
                                    override fun onError(e: Exception) {
                                        ToastHelper.showToast("下载更新包失败.")
                                        mUpdateDialog?.showFailed()
                                    }

                                    override fun onProgress(progress: Int) {
                                        // 更新下载进度
                                        mUpdateDialog?.progress = progress
                                    }

                                    override fun onCancel() {
                                        if (mUpdateDialog?.progress!! < 100) {
                                            ToastHelper.showToast("已取消下载")
                                        }
                                    }

                                    override fun onFinish(filePath: String) {
                                        try {
                                            // 下载成功后，如果不是强制更新，就自动关掉下载的界面。
                                            if (!mAppUpdateInfo?.isForceUpdate()!!) {
                                                mUpdateDialog?.dismiss()
                                            }
                                            ToastHelper.showToast("下载完成")
                                            SystemHelper.installApk(mContext, FileUtils.getAppFile("ergou.apk", null, true))
                                            //清除首页/活动/广告的缓存
                                            SPHelper.saveObject("SP_KEY_SHOPPING_HOME", null)
                                            SPHelper.saveObject("home_popup_bean", null)
                                            SPHelper.saveObject("SP_KEY_ADVERT", null)
                                            SPHelper.putString("SP_KEY_ADVERT_PATH", "")

                                        } catch (e: Exception) {
                                            ToastHelper.showToast("无法解析安装包")
                                        }
                                    }
                                }).build()
                                .startDownloadApk()
                    }

                    override fun onError(e: Throwable) {
                        ToastHelper.showToast("下载更新包失败.")
                        mUpdateDialog?.showFailed()
                    }
                })
    }
}