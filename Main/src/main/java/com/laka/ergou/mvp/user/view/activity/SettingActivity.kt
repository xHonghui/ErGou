package com.laka.ergou.mvp.user.view.activity

import android.Manifest
import android.content.pm.PackageManager
import android.view.View
import com.alibaba.baichuan.trade.biz.login.AlibcLogin
import com.laka.androidlib.base.activity.BaseMvpActivity
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.CacheUtil
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.PermissionUtils
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.androidlib.widget.dialog.CommonConfirmDialog
import com.laka.ergou.R
import com.laka.ergou.mvp.login.LoginModuleNavigator
import com.laka.ergou.mvp.login.model.event.UserEvent

import com.laka.ergou.mvp.user.UserModuleNavigator
import com.laka.ergou.mvp.user.UserUtils.UserUtils
import com.laka.ergou.mvp.user.constant.UserConstant
import com.laka.ergou.mvp.user.contract.SettingConstract
import com.laka.ergou.mvp.user.presenter.SettingPresenter
import kotlinx.android.synthetic.main.activity_setting.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @Author:Rayman
 * @Date:2019/1/8
 * @Description:设置页面
 */
class SettingActivity : BaseMvpActivity<String>(), SettingConstract.ISettingView, View.OnClickListener {

    private lateinit var mPresenter: SettingConstract.ISettingPresenter
    // private var mClearCacheConfirmDialog: CommonConfirmDialog? = null
    private var mLogoutConfirmDialog: CommonConfirmDialog? = null

    override fun setContentView(): Int {
        return R.layout.activity_setting
    }

    override fun initIntent() {
    }

    override fun initViews() {
        title_bar.setLeftIcon(R.drawable.ic_arrow_back_white)
                .setBackGroundColor(R.color.color_main)
                .setTitleTextColor(R.color.white)
        tv_logout.visibility = if (UserUtils.isLogin()) View.VISIBLE else View.GONE
    }

    override fun initData() {
        if (PermissionUtils.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            tv_clear_cache.text = CacheUtil.getTotalCacheSize(this)
        } else {
            tv_clear_cache.text = "未知"
        }
        initTaobaoAuthor()
    }

    private fun initTaobaoAuthor() {
        if (AlibcLogin.getInstance().isLogin) {
            tv_taobao_authori.text = "已授权"
        } else {
            tv_taobao_authori.text = "未授权"
        }
    }

    override fun initEvent() {
        cl_clear_cache.setOnClickListener(this)
        cl_taobao_authori.setOnClickListener(this)
        cl_about_us.setOnClickListener(this)
        tv_logout.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.cl_clear_cache -> {
                LogUtils.info("settingActivity","清理缓存点击")
                if (!PermissionUtils.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    PermissionUtils.requestPermission(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
                    return
                }
                if (isFinishing || isDestroyed) {
                    return
                }
                val commonConfirmDialog = CommonConfirmDialog(this)
                commonConfirmDialog?.setDefaultTitleTxt("您确定要清除缓存吗？")
                commonConfirmDialog?.setOnClickSureListener {
                    onClearCache()
                }
                commonConfirmDialog?.show()
            }
            R.id.cl_taobao_authori -> {
                if (!AlibcLogin.getInstance().isLogin) {
                    onTaoBaoAuthor() // 淘宝授权
                }
            }
            R.id.cl_about_us -> {
                UserModuleNavigator.startAboutUsActivity(this)
            }
            R.id.tv_logout -> {
                if (mLogoutConfirmDialog == null) {
                    mLogoutConfirmDialog = CommonConfirmDialog(this)
                    mLogoutConfirmDialog?.setDefaultTitleTxt("您确定要退出吗？")
                    mLogoutConfirmDialog?.setOnClickSureListener {
                        onLogout()
                    }
                }
                mLogoutConfirmDialog?.show()
            }
        }
    }

    /**
     * 淘宝授权
     * */
    private fun onTaoBaoAuthor() {
        LoginModuleNavigator.startTaoBaoAuthorActivity(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: UserEvent) {
        when (event.type) {
            UserConstant.TAOBAO_AUTHOR_SUCCESS_EVENT -> { // 淘宝授权
                initTaobaoAuthor()
            }
        }
    }

    override fun createPresenter(): IBasePresenter<*> {
        mPresenter = SettingPresenter()
        return mPresenter
    }

    override fun showData(data: String) {
        showLoading()
    }

    override fun showErrorMsg(msg: String?) {
        ToastHelper.showCenterToast(msg)
    }


    private fun onLogout() {
        mPresenter.onLogout(this)
    }

    override fun onLogoutSuccess() {
        finish()
    }

    private fun onClearCache() {
        CacheUtil.clearAllCache(this)
        ToastHelper.showCenterToast("缓存清除成功！")
        tv_clear_cache.text = CacheUtil.getTotalCacheSize(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val isGrant = grantResults.none { it != PackageManager.PERMISSION_GRANTED }
        if (!isGrant) {
            ToastHelper.showCenterToast("读取/清理缓存需要访问外部存储设备的权限，可前往设置->应用程序进行开启")
            LogUtils.info("settingActivity","读取/清理缓存需要访问外部存储设备的权限，可前往设置->应用程序进行开启")
        } else {
            // 用户何时同意权限，获取缓存
            tv_clear_cache.text = CacheUtil.getTotalCacheSize(this)
            LogUtils.info("settingActivity","用户何时同意权限，获取缓存")
        }
    }


}