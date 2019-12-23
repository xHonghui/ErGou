package com.laka.ergou.mvp.user.view.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.View
import com.alibaba.baichuan.trade.biz.login.AlibcLogin
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback
import com.alibaba.sdk.android.oss.model.PutObjectRequest
import com.alibaba.sdk.android.oss.model.PutObjectResult
import com.laka.androidlib.base.activity.BaseMvpActivity
import com.laka.androidlib.eventbus.Event
import com.laka.androidlib.eventbus.EventBusManager
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.*
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.androidlib.widget.dialog.CommonConfirmDialog
import com.laka.androidlib.widget.dialog.PhotoDialog
import com.laka.ergou.R
import com.laka.ergou.common.upload.UploadCallBack
import com.laka.ergou.common.upload.UploadManager
import com.laka.ergou.common.util.share.WxLoginUtils
import com.laka.ergou.mvp.login.LoginModuleNavigator
import com.laka.ergou.mvp.login.constant.LoginConstant
import com.laka.ergou.mvp.login.model.event.UserEvent
import com.laka.ergou.mvp.shopping.center.constant.ShoppingApiConstant
import com.laka.ergou.mvp.shopping.center.constant.ShoppingCenterConstant
import com.laka.ergou.mvp.user.UserModuleNavigator
import com.laka.ergou.mvp.user.UserUtils.UserUtils
import com.laka.ergou.mvp.user.constant.UserConstant
import com.laka.ergou.mvp.user.contract.IUserInfoContract
import com.laka.ergou.mvp.user.model.bean.CommonData
import com.laka.ergou.mvp.user.model.bean.StsTokenBean
import com.laka.ergou.mvp.user.presenter.UserInfoPresenter
import com.laka.ergou.mvp.user.view.widget.UnbindAccountDialog
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendAuth
import kotlinx.android.synthetic.main.activity_user_info.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File

/**
 * @Author:summer
 * @Date:2019/1/10
 * @Description: 用户基本信息页面
 */
class UserInfoActivity : BaseMvpActivity<CommonData>(), IUserInfoContract.IUserInfoView, View.OnClickListener {

    /**
     * description:UI配置
     **/
    private var mConfirmDialog: CommonConfirmDialog? = null
    private var mUnBindDialog: UnbindAccountDialog? = null

    /**
     * description:数据设置
     **/
    private var userName: String? = ""
    private var userAvatar: String? = ""
    private var userGender: String? = ""
    private var weChatAccount: String? = ""
    private var isWeChatBind = false
    private var userPhone: String? = ""
    private var aliUserName: String? = ""
    private var taobaoUserName = ""
    private lateinit var mPresenter: UserInfoPresenter
    private lateinit var mPhotoDialog: PhotoDialog
    private lateinit var mImageFile: File
    private lateinit var mPhotoHelper: PhotoHelper
    private lateinit var mUploadManager: UploadManager

    override fun showErrorMsg(msg: String?) {
        ToastHelper.showCenterToast(msg)
    }

    override fun createPresenter(): IBasePresenter<*> {
        mPresenter = UserInfoPresenter()
        return mPresenter
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::mUploadManager.isInitialized) {
            mUploadManager.cancelUpload()
        }
    }

    override fun setContentView(): Int {
        return R.layout.activity_user_info
    }

    override fun initIntent() {

    }

    override fun initViews() {
        title_bar.setTitle(ResourceUtils.getString(R.string.util_my_setting))
                .setLeftIcon(R.drawable.seletor_nav_btn_back)
                .setTitleTextColor(R.color.black)
                .setTitleTextSize(16)
        mPhotoDialog = PhotoDialog(this, R.style.commonDialog)
        tv_logout.visibility = if (UserUtils.isLogin()) View.VISIBLE else View.GONE
        mConfirmDialog = CommonConfirmDialog(this)
        mPhotoHelper = PhotoHelper(this)
        mUnBindDialog = UnbindAccountDialog(this)

        mUnBindDialog?.callback = { isUnbindWx ->
            // 回调
            if (isUnbindWx) {
                mPresenter.unbindWxAccount()
            } else {
                mPresenter.unbindTaoBaoAccount()
            }
            mUnBindDialog?.dismiss()
        }
    }

    override fun initData() {
        if (PermissionUtils.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            tv_clear_cache.text = CacheUtil.getTotalCacheSize(this)
        } else {
            tv_clear_cache.text = "未知"
        }

        if (UserUtils.isLogin()) {
            updateUserInfo()
        }
        mPresenter.onLoadUserInfo("$UserUtils.getUserId()")
    }

    override fun initEvent() {
        setClickView<ConstraintLayout>(R.id.cl_user_avatar)
        setClickView<ConstraintLayout>(R.id.cl_user_name)
        setClickView<ConstraintLayout>(R.id.cl_user_gender)

        setClickView<ConstraintLayout>(R.id.cl_bind_we_chat)
        setClickView<ConstraintLayout>(R.id.cl_user_phone)
        setClickView<ConstraintLayout>(R.id.cl_bind_ali_pay)
        setClickView<ConstraintLayout>(R.id.cl_bind_taobao_account)

        setClickView<ConstraintLayout>(R.id.cl_clear_cache)
        setClickView<ConstraintLayout>(R.id.cl_about_us)
        setClickView<ConstraintLayout>(R.id.tv_logout)
        mPhotoDialog.setOnClickListener(this)
    }

    private fun checkPermission(): Boolean {
        val check = PermissionUtils.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
        if (!check) {
            PermissionUtils.requestPermission(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA))
        }
        return check
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.cl_user_avatar -> {
                if (UserUtils.isLogin()) {
                    if (checkPermission()) {
                        mPhotoDialog.show()
                    }
                } else {
                    UserModuleNavigator.startLoginActivity(this)
                }
            }
            R.id.cl_user_name -> {
                UserModuleNavigator.startUserNickEditActivity(this)
            }
            R.id.cl_user_gender -> {
                UserModuleNavigator.startUserGenderEditActivity(this)
            }
            R.id.cl_bind_we_chat -> {
                if (!TextUtils.isEmpty(weChatAccount)) {
                    // 解绑微信，弹窗提示
                    mUnBindDialog?.showUnBindWxAccount()
                } else {
                    // 跳转绑定
                    WxLoginUtils.getInstance(this@UserInfoActivity).sendAuthorizationReq()
                }
            }
            R.id.cl_user_phone -> {
                // 弹窗判断，跳转到更换手机号页面
                if (!TextUtils.isEmpty(userPhone)) {
                    mConfirmDialog?.show()
                    mConfirmDialog?.setDefaultTitleTxt("账号存在已绑定手机号，确认更改手机号码？")
                    mConfirmDialog?.setOnClickSureListener {
                        UserModuleNavigator.startChangeUserPhoneActivity(this)
                    }
                } else {
                    UserModuleNavigator.startChangeUserPhoneActivity(this)
                }
            }
            R.id.cl_bind_ali_pay -> {
                // 弹窗判断，跳转到绑定支付宝账号页面
                if (!TextUtils.isEmpty(aliUserName)) {
                    mConfirmDialog?.show()
                    mConfirmDialog?.setDefaultTitleTxt("账号存在已绑定支付宝，确认更改支付宝账号？")
                    mConfirmDialog?.setOnClickSureListener {
                        UserModuleNavigator.startBindAliAccountActivity(this)
                    }
                } else {
                    UserModuleNavigator.startBindAliAccountActivity(this)
                }
            }
            R.id.cl_bind_taobao_account -> {
                if (!AlibcLogin.getInstance().isLogin || TextUtils.isEmpty(UserUtils.getRelationId())) {
                    mPresenter.onTaoBaoAuthor(this)
                }
            }
            R.id.tv_take_photo -> { // 拍照
                if (PermissionUtils.checkPermission(Manifest.permission.CAMERA)) {
                    onTakePhoto()
                } else {
                    PermissionUtils.requestPermission(this, arrayOf(Manifest.permission.CAMERA))
                }
            }
            R.id.tv_album -> { // 照片选择
                if (PermissionUtils.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    onPhotoSelect()
                } else {
                    PermissionUtils.requestPermission(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
                }
            }
            R.id.tv_cancel -> { // 取消
                mPhotoDialog.dismiss()
            }
            R.id.cl_clear_cache -> {
                if (!PermissionUtils.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    PermissionUtils.requestPermission(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
                    return
                }
                if (isFinishing || isDestroyed) {
                    return
                }
                val commonConfirmDialog = CommonConfirmDialog(this)
                commonConfirmDialog?.show()
                commonConfirmDialog?.setDefaultTitleTxt("您确定要清除缓存吗？")
                commonConfirmDialog?.setOnClickSureListener {
                    onClearCache()
                }
            }
            R.id.cl_about_us -> {
                UserModuleNavigator.startAboutUsActivity(this)
            }
            R.id.tv_logout -> {
                mConfirmDialog?.show()
                mConfirmDialog?.setDefaultTitleTxt("您确定要退出吗？")
                mConfirmDialog?.setOnClickSureListener {
                    onLogout()
                }
            }
        }
    }

    /**
     * 更新用户数据
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onUserEvent(event: UserEvent) {
        when (event.type) {
            UserConstant.EDIT_USER_INFO -> {
                if (PermissionUtils.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    tv_clear_cache.text = CacheUtil.getTotalCacheSize(this)
                } else {
                    tv_clear_cache.text = "未知"
                }
                if (UserUtils.isLogin()) {
                    updateUserInfo()
                }
                LogUtils.info("UserInfoActivity页面接收到更新用户信息的事件")
            }
        }
    }

    override fun onEvent(event: Event?) {
        super.onEvent(event)
        when (event?.name) {
            LoginConstant.EVENT_LOGIN_WX -> {
                // 微信授权后回调
                val resp = event.data as SendAuth.Resp
                LogUtils.info("收到了授权回调：${resp.errCode}")
                if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
                    val code = resp.code
                    LogUtils.info("输出code：${resp.code}")
                    mPresenter.bindWxAccount(code)
                } else {
                    ToastHelper.showToast("微信授权失败")
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (UserUtils.isLogin()) {
            updateUserInfo()
        }
    }

    private fun onTakePhoto() {
        mPhotoHelper.takePhoto(this, PhotoHelper.TAKE_PHOTO)
        mPhotoDialog.dismiss()
    }

    private fun onPhotoSelect() {
        mPhotoHelper.choicePhoto(this, PhotoHelper.ALBUM_PHOTO)
        mPhotoDialog.dismiss()
    }

    private fun onClearCache() {
        CacheUtil.clearAllCache(this)
        ToastHelper.showCenterToast("缓存清除成功！")
        tv_clear_cache.text = CacheUtil.getTotalCacheSize(this)
        //清除新手教程显示记录
        SPHelper.putBoolean(ShoppingApiConstant.IS_SHOW_VIDEO_COURSE, true)
        SPHelper.putString(ShoppingCenterConstant.SP_KEY_ADVERT_PATH, "")
        val isShow = SPHelper.getBoolean(ShoppingApiConstant.IS_SHOW_VIDEO_COURSE, true)
        LogUtils.info("是否显示video教程$isShow")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == UserConstant.BIND_UNION_REQUEST_CODE
                && resultCode == UserConstant.BIND_UNION_RESULT_CODE) {//绑定淘宝渠道
            val code = data?.getStringExtra(UserConstant.UNION_CODE)
            val state = data?.getStringExtra(UserConstant.UNION_STATE)
            if (!TextUtils.isEmpty(code) && !TextUtils.isEmpty(state)) {
                mPresenter.handleUnionCode(code!!, state!!)
            }
        }
        mPhotoHelper.onResult(requestCode, resultCode, data, object : PhotoHelper.DecodeResultCallback {
            override fun onDecodeResultSuccess(bitmap: Bitmap?, path: String?, size: Long, duration: Long) {
                mImageFile = File(path)
                onGetStsToken()
            }

            override fun onDecodeResultFail(message: String?, requestCode: Int) {
                ToastHelper.showCenterToast(message)
            }
        })
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (i in 0 until permissions.size) {
            if (permissions[i] == Manifest.permission.READ_EXTERNAL_STORAGE
                    && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                tv_clear_cache.text = CacheUtil.getTotalCacheSize(this)
            }
        }
    }

    private fun onLogout() {
        mPresenter.onLogout(this)
    }

    private fun updateUserInfo() {
        userAvatar = UserUtils.getUserAvatar()
        if (TextUtils.isEmpty(userAvatar)) {
            GlideUtil.loadCircleImage(this, R.drawable.ic_user_default_avatar, iv_user_avatar)
        } else {
            GlideUtil.loadCircleImage(this, userAvatar, iv_user_avatar)
        }

        userName = UserUtils.getUserName()
        if (TextUtils.isEmpty(userName)) {
            tv_user_name.text = ResourceUtils.getString(R.string.user_default_name)
        } else {
            tv_user_name.text = UserUtils.getUserName()
        }

        userGender = UserUtils.getUserGender()
        if (TextUtils.isEmpty(userGender)) {
            tv_user_gender.text = "未知"
        } else {
            if ("1" == userGender) {
                tv_user_gender.text = "男"
            } else if ("2" == userGender) {
                tv_user_gender.text = "女"
            }
        }

        isWeChatBind = UserUtils.isWeChatBind()
        if (isWeChatBind) {
            // 假若已绑定用户名，同时用户名不为空。显示用户名
            weChatAccount = UserUtils.getWeChatAccount()
            tv_bind_we_chat.text = if (TextUtils.isEmpty(weChatAccount)) {
                ResourceUtils.getString(R.string.bind_status)
            } else {
                weChatAccount
            }
        } else {
            tv_bind_we_chat.text = ResourceUtils.getString(R.string.unbind_status)
        }

        userPhone = UserUtils.getUserPhone()
        if (TextUtils.isEmpty(userPhone)) {
            tv_user_phone.text = ResourceUtils.getString(R.string.unbind_status)
        } else {
            tv_user_phone.text = StringConvertUtils.convertEncryptionPhone(userPhone)
        }

        aliUserName = UserUtils.getUserAliRealName()
        if (TextUtils.isEmpty(aliUserName)) {
            tv_bind_ali_pay.text = ResourceUtils.getString(R.string.unbind_status)
        } else {
            tv_bind_ali_pay.text = aliUserName
        }
        updateTaoBaoAuthorInfo()
    }

    private fun updateTaoBaoAuthorInfo() {
        if (TextUtils.isEmpty(UserUtils.getRelationId())) {
            taobaoUserName = ResourceUtils.getString(R.string.unbind_status)
            tv_bind_taobao_account.setTextColor(ContextCompat.getColor(this, R.color.gray_88))
        } else {

            taobaoUserName = ResourceUtils.getString(R.string.bind_status)
            tv_bind_taobao_account.setTextColor(ContextCompat.getColor(this, R.color.color_main))
        }
        tv_bind_taobao_account.text = taobaoUserName
    }

    override fun showData(data: CommonData) {

    }

    private fun onGetStsToken() {
        mPresenter.onGetStsToken()
    }

    //===================================== V 层实现 =============================================

    override fun onGetStsTokenSuccess(resultBean: StsTokenBean) {
        UploadManager.accessKeyId = resultBean.id
        UploadManager.accessKeySecret = resultBean.secret
        UploadManager.accessKeyToken = resultBean.token
        showLoading()
        mUploadManager = UploadManager()
        mUploadManager.uploadFile(resultBean.bucket, UserUtils.getUploadObjectKey(), mImageFile.toString(), object : UploadCallBack {
            override fun onSuccess(request: PutObjectRequest, result: PutObjectResult) {
                LogUtils.info("request.toString:" + request.toString())
                LogUtils.info("result.toString:" + result.toString())
                runOnUiThread {
                    mPresenter.onUploadUserAvatar(request.objectKey)
                }
                dismissLoading()
            }

            override fun onFail(message: String) {
                dismissLoading()
            }
        })
    }

    override fun onUploadUserAvatarSuccess(resultBean: CommonData) {
        UserUtils.updateUserAvatar(resultBean.avatar)
        ToastHelper.showCenterToast("修改头像成功")
        GlideUtil.loadCircleImage(this, resultBean.avatar, R.drawable.default_img, iv_user_avatar)
        EventBusManager.postEvent(UserEvent(UserConstant.EDIT_USER_INFO))
    }

    override fun unbindTaoBaoAccountCallBack(isSuccess: Boolean) {
        // 更新绑定状态
        if (isSuccess) {
            AlibcLogin.getInstance().logout(object : AlibcLoginCallback {
                override fun onSuccess(p0: Int, p1: String?, p2: String?) {
                    taobaoUserName = ResourceUtils.getString(R.string.unbind_status)
                    tv_bind_taobao_account.text = taobaoUserName
                    tv_bind_taobao_account.setTextColor(ContextCompat.getColor(this@UserInfoActivity, R.color.gray_88))
                    ToastHelper.showToast("解绑成功")
                    // 发送事件更新用户资料列表
                    EventBusManager.postEvent(UserEvent(UserConstant.TAOBAO_UNAUTHOR_EVENT))
                }

                override fun onFailure(p0: Int, p1: String?) {
                    ToastHelper.showToast("解除手掏账号绑定错误")
                }
            })
        } else {
            ToastHelper.showToast("解除手掏账号绑定错误")
        }
    }

    override fun onLogoutSuccess() {
        finish()
    }

    override fun authorInvalid() {
        finish()
        ToastHelper.showCenterToast("用户未登录！")
        LoginModuleNavigator.startLoginActivity(this)
    }

    override fun operateWxAccount(isBind: Boolean, isSuccess: Boolean) {
        // 绑定微信数据回调
        if (isSuccess) {
            weChatAccount = UserUtils.getWeChatAccount()
            tv_bind_we_chat.text = if (isBind) {
                ToastHelper.showCenterToast("绑定成功")
                if (TextUtils.isEmpty(weChatAccount)) {
                    ResourceUtils.getString(R.string.bind_status)
                } else {
                    weChatAccount
                }
            } else {
                ToastHelper.showCenterToast("解绑成功")
                ResourceUtils.getString(R.string.unbind_status)
            }
        }
    }

    /**淘宝授权成功*/
    override fun onTaoBaoAuthorSuccess() {
        // 更新淘宝绑定状态
        //绑定淘宝渠道ID成功后再发送该事件
        mPresenter.getUnionCodeUrl()
    }

    /**获取联盟授权 codeurl */
    override fun getUnionCodeUrlSuccess(url: String) {
        UserModuleNavigator.startBindUnionCodeActivityForResult(this, url, UserConstant.BIND_UNION_REQUEST_CODE)
    }

    /**淘宝联盟授权成功*/
    override fun handleUnionCodeSuccess() {
        ToastHelper.showCenterToast("淘宝授权成功")
        EventBusManager.postEvent(UserEvent(UserConstant.TAOBAO_AUTHOR_SUCCESS_EVENT))
        updateTaoBaoAuthorInfo()
    }
}