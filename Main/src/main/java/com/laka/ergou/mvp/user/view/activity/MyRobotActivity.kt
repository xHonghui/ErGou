package com.laka.ergou.mvp.user.view.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.constraint.ConstraintLayout
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.laka.androidlib.base.activity.BaseMvpActivity
import com.laka.androidlib.features.login.OnRequestListener
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.Base64Utils
import com.laka.androidlib.util.ResourceUtils
import com.laka.androidlib.util.screen.ScreenUtils
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.androidlib.widget.dialog.CommonConfirmDialog
import com.laka.androidlib.widget.refresh.BaseListBean
import com.laka.androidlib.widget.refresh.OnResultListener
import com.laka.androidlib.widget.refresh.RefreshRecycleView
import com.laka.androidlib.widget.refresh.decoration.DividerItemDecoration
import com.laka.ergou.R
import com.laka.ergou.mvp.shop.utils.ShareUtils
import com.laka.ergou.mvp.shop.view.dialog.ShareDialog
import com.laka.ergou.mvp.user.UserModuleNavigator
import com.laka.ergou.mvp.user.contract.IUserRobotContract
import com.laka.ergou.mvp.user.model.bean.RobotInfo
import com.laka.ergou.mvp.user.presenter.UserRobotPresenter
import com.laka.ergou.mvp.user.view.adapter.RobotListAdapter
import kotlinx.android.synthetic.main.activity_my_robot.*
import kotlinx.android.synthetic.main.layout_my_robot_list.*
import java.lang.Exception

/**
 * @Author:Rayman
 * @Date:2019/1/17
 * @Description:我的机器人页面（已弃用）
 */
class MyRobotActivity : BaseMvpActivity<RobotInfo>(), IUserRobotContract.IUserRobotView,
        OnRequestListener<OnResultListener>, View.OnClickListener {

    /**
     * description:UI配置
     **/
    private lateinit var mErrorView: ConstraintLayout
    private lateinit var mIvError: ImageView
    private lateinit var mTvError: TextView

    /**
     * description:剪切板
     **/
    private lateinit var clipboardManager: ClipboardManager
    private lateinit var confirmDialog: CommonConfirmDialog

    /**
     * description:数据设置
     **/
    private var mRobotPresenter: IUserRobotContract.IUserRobotPresenter? = null
    private var isViewInit = false
    private var mAdapter: RobotListAdapter? = null
    private lateinit var mResultListener: OnResultListener


    override fun setContentView(): Int {
        return R.layout.activity_my_robot
    }

    override fun initIntent() {
    }

    override fun initViews() {
        title_bar.setTitle(ResourceUtils.getString(R.string.util_my_robot))
                .setLeftIcon(R.drawable.seletor_nav_btn_back)
                .setTitleTextColor(R.color.black)
                .setTitleTextSize(16)

        mErrorView = findViewById(R.id.cl_robot_error)
        mIvError = findViewById(R.id.iv_robot_error)
        mTvError = findViewById(R.id.tv_robot_error)
        confirmDialog = CommonConfirmDialog(this)
        confirmDialog.setOnClickSureListener {
            ShareUtils.goToThirdParty(this@MyRobotActivity, ShareDialog.WEIXIN_PACKAGE_NAME)
        }
    }

    override fun initData() {
        clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        mRobotPresenter?.getRobotList()
    }

    override fun initEvent() {
        setClickView<TextView>(R.id.tv_robot_add)
        setClickView<TextView>(R.id.tv_robot_we_chat_copy)
        setClickView<ConstraintLayout>(R.id.cl_robot_error)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_robot_add -> UserModuleNavigator.startAddRobotActivity(this)
            R.id.tv_robot_we_chat_copy -> {
                // copy到剪切板，跳转到微信
                clipboardManager.primaryClip = ClipData.newPlainText("label", tv_robot_we_chat_id?.text)
                confirmDialog.show()
                confirmDialog?.setDefaultTitleTxt("复制成功，是否跳转微信？")
            }
            R.id.cl_robot_error -> {
                // 点击错误信息，重新加载
                mRobotPresenter?.getRobotList()
            }
        }
    }

    override fun onInternetChange(isLostInternet: Boolean) {
        super.onInternetChange(isLostInternet)
        if (isLostInternet) {
            // 无网络状态
            showErrorView(true)
        } else {
            // 重新加载数据
            hideErrorView()
            mRobotPresenter?.getRobotList()
        }
    }

    override fun createPresenter(): IBasePresenter<*> {
        mRobotPresenter = UserRobotPresenter()
        return mRobotPresenter!!
    }

    override fun onRequest(page: Int, resultListener: OnResultListener?): String {
        mResultListener = resultListener!!
        mRobotPresenter?.getRobotList(page)
        return ""
    }

    override fun showData(data: RobotInfo) {
        hideErrorView()
        tv_robot_we_chat_id.text = data?.robotWxId
        val loadBase64Data = Base64Utils.stringToByteArray(data?.robotAvatar)
        if (loadBase64Data != null && loadBase64Data.isNotEmpty()) {
            Glide.with(this)
                    .load(loadBase64Data)
                    .asBitmap()
                    .into(object : SimpleTarget<Bitmap>() {
                        override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                            // 当资源准备就绪的时候，显示View
                            iv_robot_qr_code.visibility = View.VISIBLE
                            iv_robot_qr_code.setImageBitmap(resource)
                        }

                        override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
                            super.onLoadFailed(e, errorDrawable)
                            // 显示ImageView
                            iv_robot_qr_code.visibility = View.VISIBLE
                            ToastHelper.showToast("二维码加载错误啦")
                        }
                    })
        } else {
            iv_robot_qr_code.visibility = View.VISIBLE
            ToastHelper.showToast("二维码加载错误啦")
        }
    }

    override fun showRobotList(robotList: BaseListBean<RobotInfo>) {
        if (!isViewInit) {
            initViewStub()
        } else {
            mResultListener?.onResponse(robotList)
        }
    }

    override fun showErrorView() {
        showErrorView(false)
    }

    override fun showErrorMsg(msg: String?) {
        ToastHelper.showToast(msg)
    }

    /**
     * description:初始化ViewStub，因为ViewStub的特性。所以只能被初始化一次
     **/
    private fun initViewStub() {
        stub_robot_list.inflate()
        rv_robot_list.addItemDecoration(DividerItemDecoration.Builder(this, DividerItemDecoration.VERTICAL_LIST)
                .setItemSpacing(ScreenUtils.dp2px(0.5f)).build())
                .setEnableMultiClick(false)
                .setOnRequestListener(this)
        mAdapter = RobotListAdapter()
        rv_robot_list.adapter = mAdapter
        rv_robot_list.setOnRequestListener(this)
        // 暂时是个缺陷
        rv_robot_list.refresh(false)
        rv_robot_list.onItemClickListener = object : RefreshRecycleView.OnItemClickListener<RobotInfo> {
            override fun onChildClick(id: Int, data: RobotInfo?, position: Int) {
                when (id) {
                    R.id.tv_robot_id_copy -> {
                        // 复制到剪切板
                        clipboardManager.primaryClip = ClipData.newPlainText("label", data?.robotWxId)
                        ToastHelper.showToast(ResourceUtils.getString(R.string.robot_copy_toast))
                    }
                    R.id.tv_robot_operation -> {
                        if (data?.robotStatus == 1) {
                            // 在线状态，跳转到赚取补贴页面
                            UserModuleNavigator.startEarnCommissionActivity(this@MyRobotActivity, data?.robotWxId,
                                    data?.robotName)
                        } else {
                            // 不在线，弹出toast提示
                            ToastHelper.showToast(ResourceUtils.getString(R.string.robot_call_server_toast))
                        }
                    }
                }
            }

            override fun onItemClick(data: RobotInfo?, position: Int) {

            }
        }
        isViewInit = true
    }

    private fun showErrorView(isNetWorkError: Boolean) {
        mErrorView.visibility = View.VISIBLE
        if (isNetWorkError) {
            mIvError.setImageResource(R.drawable.ic_no_network)
            mTvError.text = ResourceUtils.getString(R.string.no_network_hint)
        } else {
            mIvError.setImageResource(R.drawable.ic_no_data)
            mTvError.text = ResourceUtils.getString(R.string.no_data_hint)
        }
        cl_robot_info.visibility = View.GONE
    }

    private fun hideErrorView() {
        mErrorView.visibility = View.GONE
        cl_robot_info.visibility = View.VISIBLE
        if (rv_robot_list != null) {
            cl_robot_info.visibility = View.VISIBLE
        }
    }
}
