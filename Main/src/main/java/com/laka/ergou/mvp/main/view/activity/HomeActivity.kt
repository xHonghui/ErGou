package com.laka.ergou.mvp.main.view.activity

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import com.laka.androidlib.base.activity.BaseActivity
import com.laka.androidlib.eventbus.Event
import com.laka.androidlib.eventbus.EventBusManager
import com.laka.androidlib.util.*
import com.laka.androidlib.util.screen.ScreenUtils
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.ergou.R
import com.laka.ergou.common.util.ClipBoardManagerHelper
import com.laka.ergou.common.util.device.DeviceInfoUtils
import com.laka.ergou.common.util.manager.ActivityManagerUtils
import com.laka.ergou.mvp.chat.ChatModuleNavigator
import com.laka.ergou.mvp.chat.constant.ChatEventConstant
import com.laka.ergou.mvp.chat.utils.socket.SocketManagerHelper
import com.laka.ergou.mvp.circle.constant.CircleConstant
import com.laka.ergou.mvp.circle.weight.GlideSimpleLoader
import com.laka.ergou.mvp.circle.weight.imagewatcher.ImageWatcher
import com.laka.ergou.mvp.circle.weight.imagewatcher.ImageWatcherHelper
import com.laka.ergou.mvp.constant.MainConstant
import com.laka.ergou.mvp.launch.constant.LaunchConstant
import com.laka.ergou.mvp.login.LoginModuleNavigator
import com.laka.ergou.mvp.login.model.bean.UserBean
import com.laka.ergou.mvp.login.model.event.UserEvent
import com.laka.ergou.mvp.main.HomeModuleNavigator
import com.laka.ergou.mvp.main.RouterNavigator
import com.laka.ergou.mvp.main.constant.HomeConstant
import com.laka.ergou.mvp.main.constant.HomeEventConstant
import com.laka.ergou.mvp.main.constant.HomeNavigatorConstant
import com.laka.ergou.mvp.main.dialog.TbkSearchDialog
import com.laka.ergou.mvp.main.helper.MagicTabHelper
import com.laka.ergou.mvp.main.model.event.ImageDataEvent
import com.laka.ergou.mvp.main.model.repository.HomeApiRepository
import com.laka.ergou.mvp.main.view.adapter.HomePagerAdapter
import com.laka.ergou.mvp.main.view.adapter.UserHomeViewAdapter
import com.laka.ergou.mvp.main.view.fragment.HomeFragment
import com.laka.ergou.mvp.shopping.center.model.bean.HomePopupBean
import com.laka.ergou.mvp.shopping.center.view.fragment.ShoppingHomeFragment
import com.laka.ergou.mvp.shopping.dialog.CommonHomeDialog
import com.laka.ergou.mvp.user.UserUtils.UserUtils
import com.laka.ergou.mvp.user.constant.UserConstant
import com.laka.ergou.mvp.user.presenter.UserHomePresenter
import com.lqr.utils.OsUtils
import kotlinx.android.synthetic.main.activity_home.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

/**
 * @Author:Rayman
 * @Date:2018/12/24
 * @Description:项目主页Activity
 */
class HomeActivity : BaseActivity(), ImageWatcher.OnPictureLongPressListener {
    override fun onPictureLongPress(p0: ImageView?, p1: Uri?, p2: Int) {

    }

    private var mOnBackTime = 0L
    private val onClickTimeInterval = 1000
    private var isFirstLoadUserInfo = true

    /**
     * description:界面UI设置
     * ResourceUtils.getString(R.string.module_chat)
     **/
    private var currentIndex = 0
    private lateinit var clipBoardListener: ClipBoardManagerHelper.ClipBoardContentChangeListener

    private lateinit var magicTabHelper: MagicTabHelper;

    private var mLotteryDialog: CommonHomeDialog? = null
    /**
     * description:页面数据设置
     **/
    private var fragmentList = ArrayList<Fragment>()

    /**
     * description:Token设置
     **/
    private var isTokenValid = false
    private var isBlockLoginCopy = false
    //是否展示广告页
    private var isShow = false
    //广告页是否在展示中
    private var advertIsShowing = false
    //是否点击活动弹窗进入了活动详情
    private var clickPopup: Boolean = false
    private var iwHelper: ImageWatcherHelper? = null
    override fun setContentView(): Int {
        return R.layout.activity_home
    }

    override fun initIntent() {
    }

    override fun initViews() {
        intent?.extras?.let {
            isShow = it.getBoolean(LaunchConstant.ADVERT_IS_SHOW, false)
        }
        magicTabHelper = MagicTabHelper()
        magicTabHelper.initMagicIndicator(this, magicIndicator, vp_home_container)
        magicTabHelper.setOnSelectIndexListener(object : MagicTabHelper.onSelectIndexListener {
            override fun onSelectIndex(index: Int) {
                // 假若是购小二页面，跳转
                if (index == HomeConstant.TAB_ERGOU_CHAT) {
                    startChatActivity()
                } else {
                    if (!advertIsShowing) {
                        currentIndex = index
                        vp_home_container.setCurrentItem(index)
                    }
                }
            }
        })
        if (isShow) {
            advertSwitch(false)
        }
        setSwipeBackEnable(false)
        initHomeFragment()
        initClipboardHelper()
        iwHelper = ImageWatcherHelper.with(this, GlideSimpleLoader()) // 一般来讲， ImageWatcher 需要占据全屏的位置
                .setTranslucentStatus(ScreenUtils.getStatusBarHeight()) // 如果不是透明状态栏，你需要给ImageWatcher标记 一个偏移值，以修正点击ImageView查看的启动动画的Y轴起点的不正确
                .setErrorImageRes(R.drawable.default_img) // 配置error图标 如果不介意使用lib自带的图标，并不一定要调用这个API

    }

    override fun setStatusBarColor(color: Int) {
        StatusBarUtil.setTranslucentForImageView(this)
    }

    override fun initData() {
        //checkUpdate()
        DeviceInfoUtils.init(this)
        checkPermissions()
        getAppUrls()
        //刷新用户信息
        updateUserToken(1)
        //首次进入，检查更新
        ApplicationUtils.setIsShowUpdateDialog(true)
    }

    private fun checkPermissions() {
        val permissionList = arrayOf(Manifest.permission.CAMERA
                , Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS
                , Manifest.permission.READ_PHONE_STATE
                , Manifest.permission.REQUEST_INSTALL_PACKAGES)
        PermissionUtils.requestPermission(this, permissionList)
    }

    override fun initEvent() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: ImageDataEvent) {
        iwHelper?.show(event.imageView, event.imageGroupList, event.urlList)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: UserEvent?) {
        when (event?.type) {
            UserConstant.SOCKET_CONNECT_EVENT -> {
                updateUserToken(1)
            }
            UserConstant.LOGIN_EVENT -> {
                // 更新Token回调
                LogUtils.error("登陆成功")
                isTokenValid = true
                isBlockLoginCopy = true
                LogUtils.info("刚刚登录，将block标志为true，不可跳转页面")
                LogUtils.info("issue-------------：socket发起重连")
                SocketManagerHelper.instance.initSocket()
                hasUnReadMessage()
            }
            UserConstant.LOGOUT_EVENT -> {
                // 退出登陆，Socket断开连接
                LogUtils.error("退出登陆")
                // 切换到购物Fragment
                if (currentIndex == 3) {
                    currentIndex = 0
                    vp_home_container.currentItem = currentIndex
                }
                isBlockLoginCopy = false
                SocketManagerHelper.instance.stopSocketConnect()
                hasUnReadMessage()
            }
            UserConstant.READ_COMMISSION_MSG_EVENT, //补贴消息
            UserConstant.READ_OTHER_MSG_EVENT -> {
                hasUnReadMessage()
            }
        }
    }

    /**
     * 活动弹窗处理
     * */
    private fun handleActivitsPopup() {
        val popupBean = SPHelper.getObject(HomeConstant.KEY_HOME_POPUP_BEAN, HomePopupBean::class.java)
        LogUtils.info("popup-----$popupBean")
        popupBean?.let {
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val preDayStr = sdf.format(it.preShopTimestamp)
            val curDayStr = sdf.format(System.currentTimeMillis())

            //测试阶段
            /*if (BuildConfig.DEBUG) {
                initPoup(it, preDayStr, curDayStr)
                return
            }*/
            if (File(popupBean.localImgPath).exists() && preDayStr != curDayStr) {
                initPoup(it, preDayStr, curDayStr)
            } else {
                handleClipBoardNavigator()
            }
        } ?: handleClipBoardNavigator()
    }

    private fun initPoup(it: HomePopupBean, preDayStr: String?, curDayStr: String?) {
        if (mLotteryDialog == null) {
            mLotteryDialog = CommonHomeDialog(this, it) {
                var target = RouterNavigator.bannerRouterReflectMap[it.sceneId.toInt()].toString()
                var paramsMap = it.sceneExtraParams ?: HashMap()
                paramsMap[HomeConstant.TITLE] = it.title
                paramsMap[HomeNavigatorConstant.ROUTER_VALUE] = it.sceneValue
                RouterNavigator.handleAppInternalNavigator(this, target, paramsMap, HomeConstant.REQUEST_CODE_INTO_ACTIVITS_POPUP_DETAIL)
                if (UserUtils.isLogin()) {
                    mLotteryDialog?.dismiss()
                }
                clickPopup = true //点击了活动弹框
            }
            mLotteryDialog?.setOnDismissListener {
                if (!clickPopup) {
                    handleClipBoardNavigator()
                }
            }
        }
        LogUtils.info("timestamp----preDayStr=$preDayStr-----curDayStr=$curDayStr")
        mLotteryDialog?.show()
        it.preShopTimestamp = System.currentTimeMillis()
        SPHelper.saveObject(HomeConstant.KEY_HOME_POPUP_BEAN, it)
    }

    /**
     * 接口请求的未读消息
     * */
    private fun hasUnReadMessage() {
        //ShoppingHomeFragment 页面消息显示
        if (fragmentList.size >= 1) {
            val homeFragment = fragmentList[0] as? ShoppingHomeFragment
            if (UserUtils.hasUnReadMsgCount()) {
                homeFragment?.let {
                    homeFragment.hasUnReadMsgCount(View.VISIBLE)
                }
            } else {
                homeFragment?.let {
                    homeFragment.hasUnReadMsgCount(View.GONE)
                }
            }
        }
    }

    override fun onEvent(event: Event?) {
        super.onEvent(event)
        when (event?.name) {
            HomeEventConstant.EVENT_UPDATE_UNREAD_MESSAGE -> {
                val unReadCount = event.data as Int

                magicTabHelper.setMsgNums(unReadCount)

            }
            HomeEventConstant.EVENT_IMAGE_DOWNLOAD_FINISH, //活动图片缓存完成
            HomeEventConstant.EVENT_CHECK_UPDATE_FINISH -> {
                if (!ApplicationUtils.isIsShowUpdateDialog()) {
                    handleActivitsPopup()
                }
            }
            ChatEventConstant.EVENT_GET_ROBOT_ID -> {
                //判断当前activity是否是获取焦点
                if (ActivityManagerUtils.isActivityTop(this.javaClass, this)) {
                    // todo 假若重启app的情况下，需要获取到机器人ID再跳转
                    handleClipBoardNavigator()
                }
            }
        }
    }

    private fun initClipboardHelper() {
        if (!::clipBoardListener.isInitialized) {
            clipBoardListener = object : ClipBoardManagerHelper.ClipBoardContentChangeListener {
                override fun contentChange(content: String, isCommandValid: Boolean) {
                    isBlockLoginCopy = false
                    LogUtils.info("剪贴板---重置block标志位为false")
                }
            }
        }
        ClipBoardManagerHelper.getInstance.addListener(clipBoardListener)
    }

    private var mTbkDialog: TbkSearchDialog? = null

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun showTbkDialog(content: String) {
        if (isFinishing || isDestroyed) {
            return
        }
        if (mTbkDialog == null) {
            mTbkDialog = TbkSearchDialog(this)
        }
        mTbkDialog?.setOnBtnClickListener({
            HomeModuleNavigator.startSearchActivity(this, content)
            //todo 记录本地
            ClipBoardManagerHelper.getInstance.setPreSearchKey()
        }, {})
        mTbkDialog?.setOnDismissListener {
            //todo 记录本地
            //ClipBoardManagerHelper.getInstance.clearClipBoardContent()
            ClipBoardManagerHelper.getInstance.setPreSearchKey()
        }
        mTbkDialog?.setMessage(content)
        if (mTbkDialog != null && !mTbkDialog?.isShowing!!) {
            mTbkDialog?.show()
        }
    }

    /**
     * description:网络状态回调。原本想在每个Fragment里面设置，但是发现重复太多
     * 直接在Activity判断，然后EventBus传递事件给各个listFragment
     * ----更新，之后还是在BaseFragment等中集成了网络处理类。之后废弃EventBus发送网络切换处理。
     **/
    override fun onInternetChange(isLostInternet: Boolean) {
        super.onInternetChange(isLostInternet)
        if (isLostInternet) {
            EventBusManager.postStickyEvent(HomeEventConstant.EVENT_ON_NETWORK_ERROR)
        } else {
            EventBusManager.postStickyEvent(HomeEventConstant.EVENT_ON_NETWORK_RESUME)
            //checkUpdate()
        }
    }

    override fun onResume() {
        super.onResume()
        // 在键盘未弹起的时候，获取虚拟按键高度并保存本地
        OsUtils.getAndSaveVirtualKeyHeight(this)
        //不是从活动详情回来触发的
        if (!clickPopup) {
            handleClipBoardNavigator()
        }
        LogUtils.info("homeActivity---------$clickPopup")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            HomeConstant.REQUEST_CODE_INTO_ACTIVITS_POPUP_DETAIL -> {
                handleClipBoardNavigator()
                clickPopup = false
            }
            CircleConstant.BUTLER_REQUEST_CODE_FOR_CIRCLE -> {

            }
            CircleConstant.BUTLER_REQUEST_CODE_FOR_USERCENTER -> {
                if (resultCode == CircleConstant.BUTLER_RESULT_CODE_FOR_USERCENTER) {
                    vp_home_container?.currentItem = 2
                }
            }
        }
        //将data传给CircleFragmnet处理
        data?.putExtra(HomeConstant.KEY_REQUEST_CODE, requestCode)
        data?.putExtra(HomeConstant.KEY_RESULT_CODE, resultCode)
        EventBusManager.postEvent(HomeEventConstant.EVENT_BIND_RELEATION_ID_SUCCESS, data)
    }

    /**
     * description:初始化主页Fragment
     **/
    private fun initHomeFragment() {
        fragmentList.add(HomeFragment.newInstance(MainConstant.HOMEPAGE_SHOPPING))
        fragmentList.add(HomeFragment.newInstance(MainConstant.HOMEPAGE_ADVERT))
        fragmentList.add(HomeFragment.newInstance(MainConstant.HOMEPAGE_CIRCLE))
        fragmentList.add(HomeFragment.newInstance(MainConstant.HOMEPAGE_MINE))
        vp_home_container.adapter = HomePagerAdapter(supportFragmentManager, fragmentList)
        if (isShow) {
            vp_home_container.currentItem = 1
        } else {
            vp_home_container.currentItem = 0
        }
    }

    /**
     * description:更新用户信息
     **/
    private fun updateUserToken(type: Int = 0) {
        UserUtils.updateLocalUserInfo()
        var userPresenter = UserHomePresenter()
        userPresenter.setView(object : UserHomeViewAdapter() {
            override fun showErrorMsg(msg: String?) {
                isTokenValid = false
            }

            override fun onLoadUserInfoSuccess(userBean: UserBean) {
                isTokenValid = true
                if (type == 1) {
                    // 更新信息成功的时候，开始Socket连接
                    LogUtils.info("issue------------:HomeActivity 发起 Socket 重连")
                    SocketManagerHelper.instance.initSocket()
                } else {
                    //判断未读消息
                    hasUnReadMessage()
                }
            }

            override fun authorInvalid() {
                isTokenValid = false
            }
        })
        userPresenter.onLoadUserInfo(this)
    }

    private fun getAppUrls() {
        HomeApiRepository.getAppUrls()
    }

    override fun onBackPressed() {
        iwHelper?.let {
            if (it.imageWatcher?.visibility == View.VISIBLE) {
                it.imageWatcher?.handleBackPressed()
                return
            }
        }
        if (System.currentTimeMillis() - mOnBackTime < onClickTimeInterval) {
            super.onBackPressed()
            //System.exit(0) 部分手机有问题
        } else {
            mOnBackTime = System.currentTimeMillis()
            ToastHelper.showCenterToast("再次点击退出程序")
        }
    }

    private fun handleClipBoardNavigator() {
        //更新弹窗正在显示中
        if (ApplicationUtils.isIsShowUpdateDialog()) {
            return
        }

        //广告页正在显示中
        if (advertIsShowing) {
            return
        }

        // 主动获取剪切板数据
        var copyCommand = ""
        val isCommandValid: Boolean
        val tempData = ClipBoardManagerHelper.getInstance.getClipboardContent()
        val localCopyData = ClipBoardManagerHelper.getInstance.getLocalCopyContent()
        // 去除当前App复制出去的内容
        if (tempData != localCopyData) {
            copyCommand = ClipBoardManagerHelper.getInstance.getClipboardContent()
        } else {
            return
        }
        if (copyCommand.length < 10 || TextUtils.isDigitsOnly(copyCommand)) {
            return
        }

        //和上一次搜索的关键词一样，不处理
        val preSearchKey = ClipBoardManagerHelper.getInstance.getPreSearchKey()
        if (copyCommand == preSearchKey) {
            return
        }

        // 用户未登录，则弹起搜索弹窗；用户已登录，则进入购小二页面，并自动发送粘贴板上面的数据
//        if (!UserUtils.isLogin()&&) {
//            LogUtils.info("未登录，弹起搜索弹窗")
//            showTbkDialog(copyCommand)
//        } else {
//            // 假若是符合App规则（URL或者淘口令，直接跳入ChatActivity发送）
//            startChatActivity(copyCommand)
//            //todo 存储搜索关键词
//            //ClipBoardManagerHelper.getInstance.clearClipBoardContent()
//            ClipBoardManagerHelper.getInstance.setPreSearchKey()
//        }

        val matches = Pattern.compile("${StringUtils.URL_PATTERN}|${HomeConstant.TAO_COMMAND_PATTERN_ALL}").matcher(copyCommand).find()
        if (UserUtils.isLogin() && matches) {
            // 假若是符合App规则（URL或者淘口令，直接跳入ChatActivity发送）
            startChatActivity(copyCommand)
            ClipBoardManagerHelper.getInstance.setPreSearchKey()
        } else {
            LogUtils.info("未登录，弹起搜索弹窗")
            showTbkDialog(copyCommand)
        }
    }

    private fun startChatActivity(content: String = "") {
        if (UserUtils.isLogin()) {
            val robotId = SocketManagerHelper.instance.getContactRobotId()
            LogUtils.info("输出robotId:$robotId,\n输出content：$content")
            ChatModuleNavigator.startChatActivity(this@HomeActivity, robotId, content = content)
        } else {
            // 假若Token失效，跳转到登陆页面
            LoginModuleNavigator.startLoginActivity(this@HomeActivity)
        }
    }

    override fun finish() {
        if (::clipBoardListener.isInitialized) {
            ClipBoardManagerHelper.getInstance.removeListener(clipBoardListener)
        }
        if (mTbkDialog != null) {
            mTbkDialog?.dismiss()
            mTbkDialog = null
        }
        super.finish()
    }

    fun advertSwitch(switch: Boolean) {
        if (switch) {
            advertIsShowing = false
            magicIndicator.visibility = View.VISIBLE
            if (vp_home_container.currentItem == 1) {
                vp_home_container.currentItem = 0
            }
        } else {
            advertIsShowing = true
            magicIndicator.visibility = View.GONE
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        //释放持有的activity
        DeviceInfoUtils.release()
    }
}