package com.laka.ergou.mvp.login.view.activity

import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Vibrator
import android.text.TextUtils
import cn.bingoogolapple.qrcode.core.QRCodeView
import com.laka.androidlib.base.activity.BaseMvpActivity
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.net.utils.Util
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.PhotoHelper
import com.laka.androidlib.util.StatusBarUtil
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.ergou.R
import com.laka.ergou.common.util.regex.RegexUtils
import com.laka.ergou.mvp.login.constant.LoginConstant
import kotlinx.android.synthetic.main.activity_scan_qrcode.*
import java.io.File
import java.io.IOException


/**
 * @Author:summer
 * @Date:2019/3/7
 * @Description:扫描二维码
 */
class ScanQRCodeActivity : BaseMvpActivity<String>() {

    private var isLight: Boolean = false
    /**
     * description:声音震动控制
     */
    private val playBeep = true
    private val BEEP_VOLUME = 0.10f
    private val VIBRATE_DURATION = 200L
    private var mediaPlayer: MediaPlayer? = null

    override fun showData(data: String) {
    }

    override fun showErrorMsg(msg: String?) {
    }

    override fun createPresenter(): IBasePresenter<*>? {
        return null
    }

    override fun setContentView(): Int {
        return R.layout.activity_scan_qrcode
    }

    override fun setStatusBarColor(color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarUtil.setTranslucentForImageView(this,0, fl_back)
            StatusBarUtil.setLightMode(this)
        }
    }

    override fun initIntent() {
    }

    override fun onResume() {
        super.onResume()
        initQrView()
    }

    override fun onPause() {
        super.onPause()
        qr_view.stopSpot()
    }

    override fun onDestroy() {
        super.onDestroy()
        qr_view.onDestroy()
    }

    override fun initViews() {
        initBeepSound()
    }

    private fun initQrView() {
        qr_view.setDelegate(object : QRCodeView.Delegate {
            override fun onScanQRCodeSuccess(result: String?) {
                LogUtils.info("result:$result")
                if (!TextUtils.isEmpty(result)) {
                    val code = RegexUtils.findTergetStrForRegex(result!!, LoginConstant.MATCHER_INVITATION_CODE_REGEX, 1)
                    if (!TextUtils.isEmpty(code) && code.length == 6) {
                        playBeepSoundAndVibrate()
                        onScanQrResult(result)
                    } else {//非正确链接
                        ToastHelper.showCenterToast(getString(R.string.scan_current_invitation_code))
                        qr_view.startSpot()
                    }
                } else {
                    ToastHelper.showCenterToast(getString(R.string.image_enable_scan))
                    qr_view.startSpot()
                }
            }

            /**
             * 摄像头环境亮度发生变化
             * @isDark:是否变暗
             * */
            override fun onCameraAmbientBrightnessChanged(isDark: Boolean) {
                if (isDark) {
                    ToastHelper.showCenterToast("环境变暗，可开启手电筒进行扫描")
                }
            }

            override fun onScanQRCodeOpenCameraError() {
                ToastHelper.showToast("打开相机错误")
                finish()
            }
        })
        qr_view.showScanRect()
        qr_view.startSpot()
    }

    /**
     * 扫描二维码结果
     * */
    private fun onScanQrResult(result: String) {
        val intent = Intent()
        LogUtils.info("result:$result")
        intent.putExtra(LoginConstant.RESULT_SCAN_QR, result)
        setResult(LoginConstant.RESULT_SCAN_QR_CODE, intent)
        finish()
    }

    private fun initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            volumeControlStream = AudioManager.STREAM_MUSIC
            mediaPlayer = MediaPlayer()
            mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
            mediaPlayer?.setOnCompletionListener(beepListener)
            val file = resources.openRawResourceFd(R.raw.bee)
            try {
                mediaPlayer?.setDataSource(file.fileDescriptor, file.startOffset, file.length)
                file.close()
                mediaPlayer?.setVolume(BEEP_VOLUME, BEEP_VOLUME)
                mediaPlayer?.prepare()
            } catch (e: IOException) {
                mediaPlayer = null
            }
        }
    }

    override fun initData() {
    }

    override fun initEvent() {
        fl_back.setOnClickListener { finish() }
        iv_torch.setOnClickListener { toogleOpenTorch() }
        btn_check_img.setOnClickListener { checkLocalImage() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LoginConstant.REQUEST_CHOICE_PHOTO_CODE) {
            data?.let {
                val imgPath = Util.getRealPathFromURI(this, data.data)
                LogUtils.info("imgPath:$imgPath")
                if (File(imgPath).exists()) {
                    qr_view.decodeQRCode(imgPath)
                } else {
                    ToastHelper.showCenterToast("获取图片失败")
                }
            }
        }
    }

    /**选择图片*/
    private fun checkLocalImage() {
        PhotoHelper(this).choicePhoto(this, LoginConstant.REQUEST_CHOICE_PHOTO_CODE)
    }

    /**打开手电筒*/
    private fun toogleOpenTorch() {
        if (!isLight) {
            qr_view.openFlashlight()
        } else {
            qr_view.closeFlashlight()
        }
        isLight = !isLight
    }

    private fun playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer?.start()
        }
        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(VIBRATE_DURATION)
    }

    private val beepListener = MediaPlayer.OnCompletionListener { mediaPlayer -> mediaPlayer.seekTo(0) }
}