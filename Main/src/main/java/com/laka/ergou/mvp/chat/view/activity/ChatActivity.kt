package com.laka.ergou.mvp.chat.view.activity

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import com.laka.androidlib.base.activity.BaseMvpActivity
import com.laka.androidlib.eventbus.Event
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.*
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.androidlib.widget.dialog.CommonConfirmDialog
import com.laka.ergou.R
import com.laka.ergou.common.util.ClipBoardManagerHelper
import com.laka.ergou.common.util.SettingUtils
import com.laka.ergou.common.util.ui.UIUtils
import com.laka.ergou.mvp.chat.ChatModuleNavigator
import com.laka.ergou.mvp.chat.constant.ChatConstant
import com.laka.ergou.mvp.chat.constant.ChatEventConstant
import com.laka.ergou.mvp.chat.contract.IChatContract
import com.laka.ergou.mvp.chat.model.bean.Message
import com.laka.ergou.mvp.chat.model.bean.event.ChatEvent
import com.laka.ergou.mvp.chat.presenter.ChatPresenter
import com.laka.ergou.mvp.chat.view.adapter.ChatAdapter
import com.laka.ergou.mvp.chat.view.anim.ChatItemAnimator
import com.laka.ergou.mvp.chat.view.widget.ChatNoticeDialog
import com.laka.ergou.mvp.chat.view.widget.NumberOperationDialog
import com.laka.ergou.mvp.main.HomeModuleNavigator
import com.laka.ergou.mvp.main.RouterNavigator
import com.laka.ergou.mvp.main.constant.HomeConstant
import com.laka.ergou.mvp.main.constant.HomeNavigatorConstant
import com.laka.ergou.mvp.user.UserModuleNavigator
import com.laka.ergou.mvp.user.UserUtils.UserUtils
import com.lqr.audio.AudioRecordManager
import com.lqr.emoji.EmotionKeyboard
import com.lqr.emoji.IEmotionExtClickListener
import com.lqr.emoji.IEmotionSelectedListener
import com.lqr.recyclerview.LQRRecyclerView
import com.lqr.refresh.ChatRefreshLayout
import kotlinx.android.synthetic.main.activity_chat.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.HashMap

/**
 * @Author:summer
 * @Date:2019/2/14
 * @Description:聊天界面
 */
class ChatActivity : BaseMvpActivity<Message>(), IChatContract.IChatView, ChatRefreshLayout.BGARefreshLayoutDelegate, IEmotionSelectedListener {

    private var mConfirmDialog: CommonConfirmDialog? = null
    private lateinit var mPresenter: ChatPresenter
    private lateinit var mEmotionKeyboard: EmotionKeyboard
    private var mAdapter: ChatAdapter? = null
    private var mData: ArrayList<Message> = ArrayList()
    private var noticeDialog: ChatNoticeDialog? = null
    private lateinit var clipBoardListener: ClipBoardManagerHelper.ClipBoardContentChangeListener
    private var contactId = ""
    private var contentFromHome = "" //后期的一个需求，假若Home页面Copy了内容合法，就直接传递发送
    private var shakeUtils: ShakeUtils? = null
    private var isOpenGouXiaoEr = false
    private var isSendUserTipsMessage: Boolean = false //是否发送了 userTipsMessage 事件
    private var tempMessage = ""

    // 判断是否从WebView复制
    private var isCopyFromWebActivity = false

    override fun setContentView(): Int {
        return R.layout.activity_chat
    }

    override fun onStop() {
        super.onStop()
        LogUtils.info("onStop：")
        etContent?.clearFocus()
    }

    override fun createPresenter(): IBasePresenter<*> {
        mPresenter = ChatPresenter()
        mPresenter.setContext(this)
        return mPresenter
    }

    override fun onInternetChange(isLostInternet: Boolean) {
        if (::mPresenter.isInitialized) {
            mPresenter.onInternetChange(isLostInternet)
        }
    }

    override fun initIntent() {
        intent?.extras?.let {
            contactId = it.getString(ChatConstant.CHAT_CONTACT_ID, "")
            contentFromHome = it.getString(ChatConstant.CHAT_CONTENT, "")
        }
    }

    override fun initViews() {

        isOpenGouXiaoEr = SettingUtils.isOpenGouXiaoEr()
        //未解锁购小二（首次进入购小二）
        if (!isOpenGouXiaoEr) {
            //未解锁状态，直接获取图片
            isSendUserTipsMessage = true
            mPresenter.getUserTipsMessage()
            isOpenGouXiaoEr = true
            SettingUtils.updateOpenGouXiaoEr(isOpenGouXiaoEr)
        }
        // 假若从Home传递数据进来，默认是解锁状态。
        if (!TextUtils.isEmpty(contentFromHome)) {
            isOpenGouXiaoEr = true
            SettingUtils.updateOpenGouXiaoEr(isOpenGouXiaoEr)
        }


        elEmotion.attachEditText(etContent)
        initEmotionKeyboard()
        initRefreshLayout()

        //处理解锁弹窗逻辑
        //handleLockLogic()

        initShakeUtil()
        clipBoardListener = object : ClipBoardManagerHelper.ClipBoardContentChangeListener {
            override fun contentChange(content: String, isCommandValid: Boolean) {
//                todo v2.2.0 修改
                // 假若未开通购小二的情况下，默认弹窗显示。
//                if (!isOpenGouXiaoEr) {
////                    v2.2.0不弹框
////                    if (!TextUtils.isEmpty(content)) {
////                        noticeDialog?.show(content)
////                    }
//                } else {
//                    //无论粘贴板是什么内容，都发送，因为判断留在外面做
//                    tempMessage = content
//                }

                //这里的content是经过判断才回调出来的
                tempMessage = content
            }
        }
        ClipBoardManagerHelper.getInstance.addListener(clipBoardListener)
        initRecyclerView()

        //TODO v2.2.0修改
        // 获取系统消息
        mPresenter.getSystemNotification()
    }

    private fun initRecyclerView() {
        val animator = ChatItemAnimator()
        animator.addDuration = ChatConstant.CHAT_LIST_ITEM_ANIM_DURATION
        animator.removeDuration = ChatConstant.CHAT_LIST_ITEM_ANIM_DURATION
        rvMsg.itemAnimator = animator
    }

    // 初始化表情面板
    private fun initEmotionKeyboard() {
        mEmotionKeyboard = EmotionKeyboard.with(this)
        mEmotionKeyboard.bindToEditText(etContent)
        mEmotionKeyboard.bindToContent(llContent)
        mEmotionKeyboard.bindToRecyclerView(rvMsg)
        mEmotionKeyboard.setEmotionLayout(flEmotionView)
        mEmotionKeyboard.bindToEmotionButton(ivEmo, ivMore)
        mEmotionKeyboard.setOnEmotionButtonOnClickListener(object : EmotionKeyboard.OnEmotionButtonOnClickListener {
            override fun onEmotionButtonOnClickListener(view: View?): Boolean {
                when (view?.id) {
                    R.id.ivEmo -> {  // 表情
                        etContent.clearFocus()
                        if (!elEmotion.isShown) {
                            if (llMore.isShown) {
                                showEmotionLayout()
                                hideMoreLayout()
                                hideAudioButton()
                                return true
                            }
                        } else if (elEmotion.isShown && !llMore.isShown) {
                            ivEmo.setImageResource(R.drawable.selector_wechat_btn_face)
                            return false
                        }
                        showEmotionLayout()
                        hideMoreLayout()
                        hideAudioButton()
                    }
                    R.id.ivMore -> {  // 更多功能
                        etContent.clearFocus()
                        if (!llMore.isShown) {
                            if (elEmotion.isShown) {
                                showMoreLayout()
                                hideEmotionLayout()
                                hideAudioButton()
                                return true
                            }
                        }
                        showMoreLayout()
                        hideEmotionLayout()
                        hideAudioButton()
                    }
                }
                return false
            }

            override fun onEmotionUIRenderingFinish() {
                UIUtils.postTaskDelay({ rvMsg.moveToPosition(0) }, 50)
            }
        })
    }

    private fun initRefreshLayout() {
        refreshLayout.setDelegate(this)
    }

    /**
     * 是否有更多数据
     * */
    override fun hasMoreData(refreshLayout: ChatRefreshLayout?): Boolean {
        return mPresenter.isHasMoreData(0, 0, mData.size)
    }

    /**
     * 刷新数据
     * */
    override fun onBGARefreshLayoutBeginLoadingMore(refreshLayout: ChatRefreshLayout?) {
        mPresenter.onGetHistoryMessages(0, UserUtils.getUserId(), mData.size)
    }

    override fun initData() {
        mPresenter.onGetHistoryMessages(0, UserUtils.getUserId(), mData.size)
    }

    override fun initEvent() {
        elEmotion.setEmotionSelectedListener(this)
        elEmotion.setEmotionAddVisiable(false)
        elEmotion.setEmotionSettingVisiable(false)
        elEmotion.setEmotionExtClickListener(object : IEmotionExtClickListener {
            override fun onEmotionAddClick(view: View) {
                ToastHelper.showCenterToast("add")
            }

            override fun onEmotionSettingClick(view: View) {
                ToastHelper.showCenterToast("setting")
            }
        })
        llContent.setOnTouchListener({ _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> closeBottomAndKeyboard()
            }
            false
        })
        // 消息列表滑动，则键盘或者底部 UI 收起
        rvMsg.setOnTouchListener({ _, _ ->
            if (elEmotion.isShown || llMore.isShown || mEmotionKeyboard.isSoftInputShown) {
                closeBottomAndKeyboard()
            }
            false
        })
        // 语音按钮
        ivAudio.setOnClickListener({ _ ->
            ToastHelper.showCenterToast(getString(R.string.no_online_alert))
        })
        // 文本输入框监听
        etContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (etContent.text.toString().trim({ it <= ' ' }).isNotEmpty()) {
                    btnSend.visibility = View.VISIBLE
                    ivMore.visibility = View.GONE
                } else {
                    btnSend.visibility = View.GONE
                    ivMore.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })
        // 文本输入框焦点更改监听
        etContent.setOnFocusChangeListener({ _, hasFocus ->
            if (hasFocus) {
                ivEmo.setImageResource(R.drawable.selector_wechat_btn_face)
                UIUtils.postTaskDelay({ rvMsg.moveToPosition(0) }, 100)
            }
        })
        // 监听键盘弹起
        llRoot.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            if (oldBottom != -1 && oldBottom > bottom) {
                mEmotionKeyboard.supportSoftInputHeight //键盘弹起，获取高度
                UIUtils.postTaskDelay({ rvMsg.moveToPosition(0) }, 100)
            }
        }
        // 发送消息
        btnSend.setOnClickListener({ _ ->
            val contentMsg = etContent.text.toString().trim()
            if (TextUtils.isEmpty(contentMsg)) {
                return@setOnClickListener
            }
            mPresenter.sendMessage(contentMsg, contactId)
            etContent.setText("")
        })
        // 按住说话（按钮）
        btnAudio.setOnTouchListener({ v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> AudioRecordManager.getInstance(this@ChatActivity).startRecord()
                MotionEvent.ACTION_MOVE -> if (isCancelled(v, event)) {
                    AudioRecordManager.getInstance(this@ChatActivity).willCancelRecord()
                } else {
                    AudioRecordManager.getInstance(this@ChatActivity).continueRecord()
                }
                MotionEvent.ACTION_UP -> {
                    AudioRecordManager.getInstance(this@ChatActivity).stopRecord()
                    AudioRecordManager.getInstance(this@ChatActivity).destroyRecord()
                }
            }
            false
        })
        //删除所有聊天记录
        tv_right.setOnClickListener {
            if (mConfirmDialog == null) {
                mConfirmDialog = CommonConfirmDialog(this)
                mConfirmDialog?.setDefaultTitleTxt("您确定要清除聊天记录吗？")
                mConfirmDialog?.setOnClickSureListener {
                    mPresenter.deleteAllMessageRecordForUser(0, UserUtils.getUserId())
                    //TODO 更新数据
                    mData.clear()
                    initData()
                }
            }
            mConfirmDialog?.show()
        }
        iv_back.setOnClickListener { finish() }
    }

    override fun onEvent(event: Event?) {
        super.onEvent(event)
        when (event?.name) {
            ChatEventConstant.EVENT_BLOCK_COPY_FROM_WEB -> {
                isCopyFromWebActivity = event.data as Boolean
            }
        }
    }

    /**
     * description:处理弹窗逻辑
     **/
    private fun handleLockLogic() {
        if (!isOpenGouXiaoEr) {
            noticeDialog = ChatNoticeDialog(this)
            noticeDialog?.callback = object : ChatNoticeDialog.ChatLockCallback {
                override fun closeDialog() {
                    // 退出当前页面
                    if (noticeDialog?.isLock!!) {
                        this@ChatActivity.finish()
                    }
                    noticeDialog?.dismiss()
                }

                override fun clickEventCallback(isLock: Boolean) {
//                    //正常流程解锁购小二
//                    if (isLock) {
//                        // 跳转到淘宝
//                        ThirdAppNavigator.startTaobaoApp()
//                    } else {
//                        // 解锁隐藏，并发送Content
//                        noticeDialog?.let {
//
//                        }
//                        noticeDialog?.dismiss()
//                    }

                    //todo v2.2.0 活动版本，暂时不用去淘宝复制标题，可直接解锁购小二
                    // 解锁隐藏，并发送Content
                    noticeDialog?.isLock = false
                    noticeDialog?.dismiss()
                    //发送信息
                    mPresenter.getUserTipsMessage()
                }

                override fun skip() {
                    noticeDialog?.isLock = false
                    noticeDialog?.dismiss()
                }
            }

            noticeDialog?.setOnDismissListener {
                isOpenGouXiaoEr = !(noticeDialog?.isLock!!)
                SettingUtils.updateOpenGouXiaoEr(isOpenGouXiaoEr)

                //清空剪切板数据
                //ClipBoardManagerHelper.getInstance.clearClipBoardContent()
                //todo 将这一次搜索的关键词存放到磁盘中，不清空粘贴板
                //todo 下一次匹配粘贴板数据时，如果和上一次搜索的一样，则不处理
                ClipBoardManagerHelper.getInstance.setPreSearchKey()

                if (noticeDialog?.isLock!!) {
                    // 加锁状态，返回即退出整个Activity
                    this@ChatActivity.finish()
                } else {
                    // 解锁隐藏，并发送Content
                    noticeDialog?.let {
                        if (!TextUtils.isEmpty(noticeDialog?.msgContent)) {
                            mPresenter.sendMessage(noticeDialog?.msgContent!!, contactId)
                        }
                        // 获取系统消息
                        mPresenter.getSystemNotification()
                    }
                }
            }
            //显示弹窗
            noticeDialog?.let {
                if (it.isShowing) {
                    it.dismiss()
                } else {
                    it.show()
                }
            }
        } else {
            // v2.2.0更改位置
            // 获取系统消息
            mPresenter.getSystemNotification()
        }
    }

    /**
     * description:初始化摇一摇功能
     **/
    private fun initShakeUtil() {
//        不用弹窗，暂时不初始化这个
//        shakeUtils = ShakeUtils(ApplicationUtils.getApplication())
//        shakeUtils?.setOnShakeListener {
//            noticeDialog?.dismiss()
//            // 发送一次之后，取消摇一摇功能
//            shakeUtils?.release()
//        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this@ChatActivity.finish()
    }

    override fun onResume() {
        super.onResume()
        // 配合剪贴板使用，当copy到消息的时候。自动发送
        if (isOpenGouXiaoEr && !TextUtils.isEmpty(tempMessage) && !TextUtils.isEmpty(contactId)) {
            // 从当前页面进入到WebActivity的时候，假若是淘口令就不重复发送
            if (ClipBoardManagerHelper.getInstance.isTaoCommand && isCopyFromWebActivity) {
                // 假若是H5 复制的淘口令。通过工具类重新设置本地CopyContent成员变量
                ClipBoardManagerHelper.getInstance.setLocalCopyContent(ClipBoardManagerHelper.getInstance.getClipboardContent())
            } else {
                LogUtils.info("只发送一次啊？？？")
                mPresenter.sendMessage(tempMessage, contactId)
                //todo 存储上一次搜索的关键词到本地
                //ClipBoardManagerHelper.getInstance.clearClipBoardContent()
                ClipBoardManagerHelper.getInstance.setPreSearchKey()
                tempMessage = ""
            }
        }
    }

    override fun onDestroy() {
        if (ContextUtil.isValidContext(this)) {
            LogUtils.error("生命周期执行完了：onDestroy")
            noticeDialog?.release()
            noticeDialog?.dismiss()
            noticeDialog = null
            shakeUtils?.release()
            mConfirmDialog?.dismiss()
            mConfirmDialog = null
        }
        if (::clipBoardListener.isInitialized) {
            ClipBoardManagerHelper.getInstance.removeListener(clipBoardListener)
        }
        tv_chat_notification?.reset() //停止跑马灯滚动
        KeyboardHelper.hideKeyBoard(this, etContent)
        super.onDestroy()
    }

    private fun isCancelled(view: View, event: MotionEvent): Boolean {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        return (event.rawX < location[0] || event.rawX > location[0] + view.width
                || event.rawY < location[1] - 40)
    }

    //============================== 列表更新 start =====================================
    /**
     * 首次加载聊天记录
     * */
    override fun onMessageFirstLoadFinish(data: ArrayList<Message>) {
        runOnUiThread {
            mData.addAll(data)
            setOrNotifyAdapter()

            // 历史消息加载完毕的时候，发送HomeCopy到的信息。
            // 带有数据进入购小二，并且未发送有 userTipsMessage 消息，则获取聊天记录成功就发送 contentFromHome
            if (!TextUtils.isEmpty(contentFromHome) /*&& !isSendUserTipsMessage*/) {
                mPresenter.sendMessage(contentFromHome, contactId)
            }
        }
        //LogUtils.info("首次加载完毕")
    }

    /**
     * 加载更多聊天记录
     * */
    override fun onMessageLoadMoreFinish(data: ArrayList<Message>) {
        runOnUiThread {
            if (data.size <= 0) {
                LogUtils.info("--------没有更多数据-----------")
                refreshLayout.setHasMoreData(false)
                refreshLayout?.endLoadingMore()
                return@runOnUiThread
            }
            //避免加载本地数据太快
            UIUtils.postTaskDelay({
                LogUtils.info("-----------------更新数据--------------")
                mData.addAll(data)
                setOrNotifyAdapter()
                refreshLayout?.endLoadingMore()
            }, 200)
        }
    }

    /**
     * 发送或者接收消息
     * */
    override fun onMessageReceiverOrSend(message: Message) {
        runOnUiThread {
            mData.add(0, message)
            mAdapter?.notifyItemInserted(0)
            //开启了 item 动画，则需要调用 notifyItemRangeChanged() 方法排列列表的位置，
            //否则会出现 position 不准确的问题
            mAdapter?.notifyItemRangeChanged(0, mData.size)
            rvMsg.moveToPosition(0)
        }
    }

    override fun setOrNotifyAdapter() {
        if (mAdapter == null) {
            mAdapter = ChatAdapter(mContext, mData, mPresenter, rvMsg)
            mAdapter?.setOnItemClickListener({ helper, parent, itemView, position ->
                onMediaItemClick(mData, position)
            })
            mAdapter?.setOnItemLongClickListener({ helper, viewGroup, view, position ->
                false
            })
            mAdapter?.setItemClickListener(object : ChatAdapter.ItemClickListener {
                override fun sendErrorClick(view: View?, item: Message?, position: Int) {
                    item?.let {
                        mPresenter.resendMessage(it.requestId)
                    }
                }

                override fun avatarItemClick(view: View?, item: Message?, position: Int) {

                }

            })
            rvMsg.adapter = mAdapter
        } else {
            //LogUtils.info("---------------notifyAdapterData----------------")
            mAdapter?.notifyDataSetChangedWrapper()
        }
    }

    /**
     * 视频、图片等多媒体消息类型条目点击
     * */
    private fun onMediaItemClick(data: ArrayList<Message>, position: Int) {
        closeBottomAndKeyboard()
        val message = data[position]
        val content = message.content
        val msgType = message.msgType
        when (msgType) {
            ChatConstant.CHAT_MSG_TYPE_IMAGE -> { // 图片消息类型

            }
            ChatConstant.CHAT_MSG_TYPE_VIDEO -> { // 视频消息类型
            }
        }
    }

    //===================================== eventBus ============================================
    /**
     * 更新用户数据
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onChatEvent(event: ChatEvent) {
        when (event.type) {
            ChatConstant.CHAT_LIST_UPDATE_EVENT -> {
                LogUtils.info("列表未填满屏幕，发送了event事件")
                setOrNotifyAdapter()
            }
            ChatConstant.DELETE_MESSAGE_FOR_POSITION_EVENT -> {
                onDeleteItemForAnim(event)       //删除
            }
            ChatConstant.LINK_TEXT_CLICK_EVENT -> { // 链接
                if (event.data is String) {
                    val content = event.data as String
                    if (!TextUtils.isEmpty(content)) {
                        val params = HashMap<String, String>()
                        params[HomeConstant.TITLE] = ""
                        params[HomeNavigatorConstant.ROUTER_VALUE] = "$content"
                        RouterNavigator.handleAppInternalNavigator(this, RouterNavigator.bannerRouterReflectMap[5].toString(), params)
                    }
                }
            }
            ChatConstant.NUMBER_TEXT_CLICK_EVENT -> { // number
                if (event.data is String) {
                    val content = event.data as String
                    if (!TextUtils.isEmpty(content)) {
                        onNumberClickOperation(content)
                    }
                }
            }
            ChatConstant.CREATE_TABLE_ERROR -> {
                ToastHelper.showCenterToast("创建数据表失败，请稍后重试！")
                finish()
            }
            ChatConstant.UPDATE_TABLE_ERROR -> {
                ToastHelper.showCenterToast("更新数据表结构失败，请稍后重试！")
                finish()
            }
        }
    }

    private var mNumberOperationDialog: NumberOperationDialog? = null

    /**
     * number 点击操作
     * */
    private fun onNumberClickOperation(content: String) {
        if (mNumberOperationDialog == null) {
            mNumberOperationDialog = NumberOperationDialog(this)
        }
        mNumberOperationDialog?.setMsg("$content 可能是一个订单编号，你可以")
        mNumberOperationDialog?.setItemClickListener { type, _ ->
            when (type) {
                NumberOperationDialog.CLICK_COPY_NUMBER -> { //复制号码
                    ClipBoardManagerHelper.getInstance.writeToClipBoardContent(content)
                    ToastHelper.showCenterToast("复制成功")
                }
                NumberOperationDialog.CLICK_SEE_ORDER -> {//查看订单
                    ChatModuleNavigator.startMyOrderActivity(this)
                }
                NumberOperationDialog.CLICK_CANCEL -> {

                }
            }
        }
        mNumberOperationDialog?.show()
    }

    /**
     * 列表删除的item动画，通过 notifyItemRemoved(position) 删除item，并不会重新触发 adapter 的 onBindViewholder 方法，也就是说，不会重新排列 position
     * 从而导致当前删除的item往后的并且显示在屏幕中的 item 的 position 错乱(未显示的item因为拉出来会走onBindViewHolder,所以位置是正确的)
     * */
    private fun onDeleteItemForAnim(event: ChatEvent) {
        if (event.data is Int) {
            val position = event.data as Int
            if (position >= 0 && position < mData.size) {
                mData.removeAt(position)
                mAdapter?.notifyItemRemoved(position)
                when (position) {
                    0 -> mAdapter?.notifyItemRangeChanged(0, mData.size)
                    mData.size - 1 -> {

                    }
                    else -> mAdapter?.notifyItemRangeChanged(position, mData.size - position)
                }
                LogUtils.info("chat------dataSize=${mData.size}------position=$position")
            }
        }
    }

    //============================ 表情选择监听 start ===================================
    override fun onEmojiSelected(key: String?) {

    }

    override fun onStickerSelected(categoryName: String?, stickerName: String?, stickerBitmapPath: String?) {

    }

    //============================== UI 操作 start =============================================
    private fun showEmotionLayout() {
        elEmotion.visibility = View.VISIBLE
        ivEmo.setImageResource(R.drawable.seletor_icon_keyboard)
    }

    private fun hideEmotionLayout() {
        elEmotion.visibility = View.GONE
        ivEmo.setImageResource(R.drawable.selector_wechat_btn_face)
    }

    private fun showMoreLayout() {
        llMore.visibility = View.VISIBLE
    }

    private fun hideMoreLayout() {
        llMore.visibility = View.GONE
    }

    private fun showAudioButton() {
        btnAudio.visibility = View.VISIBLE
        etContent.visibility = View.GONE
        ivAudio.setImageResource(R.drawable.seletor_icon_keyboard)
    }

    private fun hideAudioButton() {
        btnAudio.visibility = View.GONE
        etContent.visibility = View.VISIBLE
        ivAudio.setImageResource(R.drawable.seletor_wechat_btn_voice)
    }

    private fun closeBottomAndKeyboard() {
        elEmotion.visibility = View.GONE
        llMore.visibility = View.GONE
        if (mEmotionKeyboard != null) {
            mEmotionKeyboard.interceptBackPress()
            ivEmo.setImageResource(R.drawable.selector_wechat_btn_face)
            if (mEmotionKeyboard.isSoftInputShown) {
                mEmotionKeyboard.hideSoftInput() // 隐藏软键盘
            }
        }
    }

    //=============================== ChatView 接口实现 start ====================================
    override fun getRvMsg(): LQRRecyclerView {
        return rvMsg
    }

    override fun getDataList(): ArrayList<Message> {
        return mData
    }

    override fun showData(data: Message) {

    }

    override fun showErrorMsg(msg: String?) {
        ToastHelper.showToast(msg)
    }

    override fun getSystemMessageSuccess(msgList: ArrayList<String>) {
        // 系统消息回调
        if (ListUtils.isNotEmpty(msgList)) {
            ll_chat_notification.visibility = View.VISIBLE
            LogUtils.info("issue--------：系统消息数组大小：${msgList.size}")
            tv_chat_notification.reset()
            tv_chat_notification.addAnnounces(msgList, true)
        } else {
            LogUtils.info("issue--------：系统消息数组为空")
            ll_chat_notification.visibility = View.GONE
        }
    }

    override fun updateSystemMessage(msgList: ArrayList<String>) {
        // 更新列表数据
        tv_chat_notification.updatemAnnounce(msgList)
    }

    override fun getNotificationCurPosition(): Int {
        return tv_chat_notification.currentPosition
    }

    /**用户非法或者token失效*/
    override fun onUserIllegalOrTokenInvalid() {
        //出现任何关于用户token 失效、用户不存在或者用户不合法等情况，都需要先请求本地用户信息再进行后续操作
        //不然本地还存在用户信息的缓存，后续判断不准确
        UserUtils.clearUserInfo()
        UserModuleNavigator.startLoginActivity(this)
        finish()
    }

    /**
     * 获取
     * */
    override fun onGetUserTipsMessageSuccess() {
        //当带有数据进入 ，并且有发送 userTipsMessage 事件，则获取 UserTipsMessage 成功后才发送发送 contentFromHome
//        if (!TextUtils.isEmpty(contentFromHome) && isSendUserTipsMessage) {
//            mPresenter.sendMessage(contentFromHome, contactId)
//        }
    }
}