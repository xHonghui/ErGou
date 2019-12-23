package com.laka.ergou.mvp.user.view.fragment

import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import cn.jpush.android.api.JPushInterface
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.laka.androidlib.base.fragment.BaseFragment
import com.laka.androidlib.ext.setNumTypeface
import com.laka.androidlib.util.GlideUtil
import com.laka.androidlib.util.ListUtils
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.ResourceUtils
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.androidlib.widget.SelectorButton
import com.laka.ergou.R
import com.laka.ergou.common.util.ClipBoardManagerHelper
import com.laka.ergou.mvp.advertbanner.adapter.AdvertBannerViewAdapter
import com.laka.ergou.mvp.advertbanner.constant.AdvertBannerConstant
import com.laka.ergou.mvp.advertbanner.model.bean.AdvertBannerBean
import com.laka.ergou.mvp.advertbanner.presenter.AdvertBannerPresenter
import com.laka.ergou.mvp.commission.model.bean.CommissionBean
import com.laka.ergou.mvp.login.LoginModuleNavigator
import com.laka.ergou.mvp.login.model.bean.UserBean
import com.laka.ergou.mvp.login.model.event.UserEvent
import com.laka.ergou.mvp.main.RouterNavigator
import com.laka.ergou.mvp.main.constant.HomeConstant
import com.laka.ergou.mvp.main.constant.HomeNavigatorConstant
import com.laka.ergou.mvp.main.contract.IAdvertBannerConstract
import com.laka.ergou.mvp.shop.utils.BigDecimalUtils
import com.laka.ergou.mvp.shopping.center.helper.HomePageStatusBarHelper
import com.laka.ergou.mvp.user.UserModuleNavigator
import com.laka.ergou.mvp.user.UserUtils.UserUtils
import com.laka.ergou.mvp.user.constant.UserConstant
import com.laka.ergou.mvp.user.contract.IUserHomeConstract
import com.laka.ergou.mvp.user.model.bean.BannerAdvBean
import com.laka.ergou.mvp.user.model.bean.PersonalBannerBean
import com.laka.ergou.mvp.user.model.bean.PersonalHintBean
import com.laka.ergou.mvp.user.model.bean.UserUtilsFactory
import com.laka.ergou.mvp.user.presenter.UserHomePresenter
import com.laka.ergou.mvp.user.view.adapter.PersonalHomeAdapter
import kotlinx.android.synthetic.main.fragment_my.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.HashMap
import kotlin.collections.ArrayList
import kotlin.collections.set

/**
 * @Author:Rayman
 * @Date:2018/12/21
 * @Description:我的页面Fragment
 */
class UserHomeFragment : BaseFragment(), IUserHomeConstract.IUserHomeView, View.OnClickListener {

    /**
     * description:UI控制
     **/
    private var mSpacingCount = 2
    private var mIvPersonalAvatar: ImageView? = null
    private var mTvPersonalName: TextView? = null
    private var mRvList: RecyclerView? = null

    /**
     * description:数据设置
     **/
    private var mUtils = ArrayList<MultiItemEntity>()
    private var mAdapter: PersonalHomeAdapter? = null
    private var mPresenter: IUserHomeConstract.IUserHomePresenter = UserHomePresenter()
    private lateinit var mBannerPresenter: IAdvertBannerConstract.IAdvertBannerPresenter
    private var commissionUrl = ""

    override fun setContentView(): Int {
        return R.layout.fragment_my
    }

    override fun initArgumentsData(arguments: Bundle?) {
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.onViewDestroy()
    }

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
        activity.lifecycle.addObserver(mPresenter)
        mBannerPresenter = AdvertBannerPresenter()
        mBannerPresenter.setView(advertBannerView)
        mPresenter.setView(this)
        mIvPersonalAvatar = findViewById(R.id.iv_personal_avatar)
        mTvPersonalName = findViewById(R.id.tv_personal_name)
        initRecycleView()
        if (!UserUtils.isLogin()) {
            mTvPersonalName?.text = ResourceUtils.getString(R.string.user_to_login)
        }
    }

    override fun initData() {
        tv_commission_valid.setNumTypeface()
        tv_commission_settlement.setNumTypeface()
        tv_commission_withdraw.setNumTypeface()
        tv_commission_frozen.setNumTypeface()
        // 刷新个人信息
        onRenderUserInfo()
        if (UserUtils.isLogin()) {
            //mPresenter.getBannerAdv()
            mBannerPresenter.onLoadAdvertBannerData(AdvertBannerConstant.TYPE_BANNER_CLASSID_USERCENTER)
        }
        val jpushId = JPushInterface.getRegistrationID(activity)
        LogUtils.info("userHomeFragment_jpushId:$jpushId")
    }

    override fun initEvent() {
        setClickView<ImageView>(R.id.iv_personal_letter)
        setClickView<ImageView>(R.id.iv_personal_setting)
        setClickView<TextView>(R.id.tv_personal_name)
        setClickView<ImageView>(R.id.iv_personal_avatar)
        setClickView<TextView>(R.id.tv_personal_copy)
        setClickView<TextView>(R.id.tv_commission_valid)
        setClickView<TextView>(R.id.tv_commission_settlement)
        setClickView<TextView>(R.id.tv_commission_settlement_hint)
        setClickView<TextView>(R.id.tv_commission_withdraw)
        setClickView<TextView>(R.id.tv_commission_withdraw_hint)
        setClickView<TextView>(R.id.tv_commission_hint)
        setClickView<TextView>(R.id.tv_commission_frozen)
        setClickView<TextView>(R.id.tv_commission_frozen_hint)
        setClickView<TextView>(R.id.tv_commission_valid_hint)
        setClickView<SelectorButton>(R.id.btn_commission_withdraw)

        //测试按钮
//        btn_test.setOnClickListener {
//           // FreeAdmissionModuleNavigator.startFreeAdmissionActivity(activity)
//        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_personal_setting,
            R.id.tv_personal_name,
            R.id.iv_personal_avatar -> {
                UserModuleNavigator.startUserInfoActivity(mActivity)
            }
            R.id.iv_personal_letter -> {
                UserModuleNavigator.startMessageCenterActivity(mActivity)
            }
            R.id.tv_commission_hint,
            R.id.tv_commission_valid,
            R.id.tv_commission_valid_hint,
            R.id.tv_commission_frozen_hint,
            R.id.tv_commission_settlement,
            R.id.tv_commission_settlement_hint,
            R.id.tv_commission_withdraw,
            R.id.tv_commission_frozen,
            R.id.btn_commission_withdraw -> {
                UserModuleNavigator.startMyCommissionActivity(mActivity)
            }
            R.id.tv_commission_withdraw_hint -> {
                val params = HashMap<String, String>()
                params[HomeConstant.TITLE] = ""
                params[HomeNavigatorConstant.ROUTER_VALUE] = "$commissionUrl"
                RouterNavigator.handleAppInternalNavigator(activity, RouterNavigator.bannerRouterReflectMap[5].toString(), params)
            }
            R.id.tv_personal_copy -> {
                ClipBoardManagerHelper.getInstance.writeToClipBoardContent(UserUtils.getUserAgentCode())
                ToastHelper.showToast("复制邀请码成功！")
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: UserEvent) {
        when (event.type) {
            UserConstant.LOGIN_EVENT -> {
                mUtils.clear()
                initData()
                initRecycleView()
            }

            UserConstant.LOGOUT_EVENT -> {
                //刷新UI
                mUtils.clear()
                initData()
                initRecycleView()
            }
            UserConstant.EDIT_USER_INFO -> {
                //onResume已经刷新，这里不需要重复
                //onRenderUserInfo()
                //mPresenter.onLoadUserInfo(activity)
                LogUtils.info("UserHomeFragment页面接收到更新用户信息的事件")
                val jpushId = JPushInterface.getRegistrationID(activity)
                LogUtils.info("userHomeFragment_jpushId2:$jpushId")
            }
            UserConstant.TAOBAO_UNAUTHOR_EVENT -> {//解绑(目前已经关闭解绑)
                if (TextUtils.isEmpty(UserUtils.getRelationId())) {
                    mUtils.add(0, UserUtilsFactory.createPreventOrderLostUtils())
                    mAdapter?.notifyDataSetChanged()
                }
            }
            UserConstant.TAOBAO_AUTHOR_SUCCESS_EVENT -> {
                //淘宝授权成功事件
                updateTaoBaoAuthor()
            }
            UserConstant.TAOBAO_AUTHOR_EVENT -> {
                //淘宝授权item点击
                mPresenter.onTaoBaoAuthor(activity)
            }
        }
    }

    /**淘宝授权成功*/
    private fun updateTaoBaoAuthor() {
        if (ListUtils.isNotEmpty(mUtils) && mUtils[0] is PersonalHintBean) {
            //删除动作执行前，锁定list的高度，待500ms后，恢复原来的高度设置
            //其实列表动画是补间动画，实际view的宽高在一执行notifyItemRemoved（）后，就变为删除后的高度了，
            //这样整个页面控件的高度就会变低（wrap_content），列表底部的一部分就会被遮挡住，为了显示完全，
            //所以在动画执行完前，锁定列表的高度，整个页面控件的高度就不会变化，就可以看到完成的item动画了
            //待动画执行完成再恢复列表应该有的高度
            mRvList?.layoutParams?.height = mRvList?.height
            mUtils.removeAt(0)
            mAdapter?.notifyItemRemoved(0)
            mRvList?.postDelayed({
                val params = mRvList?.layoutParams as? ConstraintLayout.LayoutParams
                params?.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
                mRvList?.layoutParams = params
            }, 500)
        }
    }

    private fun onRenderUserInfo() {
        if (!UserUtils.isLogin()) { // 未登录
            GlideUtil.loadCircleImage(mActivity, R.drawable.ic_user_default_avatar, mIvPersonalAvatar)
            mTvPersonalName?.text = ResourceUtils.getString(R.string.user_to_login)
            tv_personal_invitation_code.visibility = View.GONE
            tv_personal_copy.visibility = View.GONE
            iv_personal_agent.visibility = View.GONE

            // 重置补贴显示数据
            val commissionBean = CommissionBean(0,
                    "0.00", "0.00", "0.00", "0.00", 0
                    , "0.00", "0.00", "0.00", "")
            loadUserCommissionSuccess(commissionBean)
            return
        }
        //以下为已登录
        mTvPersonalName?.text = UserUtils.getUserName()
        if (TextUtils.isEmpty(UserUtils.getUserAvatar())) {
            GlideUtil.loadCircleImage(mActivity, R.drawable.ic_user_default_avatar, mIvPersonalAvatar)
        } else {
            GlideUtil.loadCircleImage(mActivity, UserUtils.getUserAvatar(), mIvPersonalAvatar)
        }

        if (TextUtils.isEmpty(UserUtils.getUserAgentCode())) {
            tv_personal_invitation_code.visibility = View.GONE
            tv_personal_copy.visibility = View.GONE
        } else {
            tv_personal_invitation_code.visibility = View.VISIBLE
            tv_personal_copy.visibility = View.VISIBLE
            tv_personal_invitation_code.text = ResourceUtils.getStringWithArgs(R.string.user_agent_code,
                    UserUtils.getUserAgentCode())
        }
        iv_personal_agent.visibility = View.VISIBLE
        when {
            UserUtils.getAgentLevel() == "10" -> iv_personal_agent.setImageResource(R.drawable.mine_img_super)
            UserUtils.getAgentLevel() == "20" -> iv_personal_agent.setImageResource(R.drawable.mine_img_partner)
            UserUtils.getAgentLevel() == "30" -> iv_personal_agent.setImageResource(R.drawable.mine_img_brand)
        }
        hasUnReadMessage()
        // 获取补贴数据
        //mPresenter.getUserCommissionData()
    }

    override fun onResume() {
        super.onResume()
        if (!TextUtils.isEmpty(UserUtils.getUserToken())) {
            mPresenter.onLoadUserInfo(mActivity)
        }
    }

    //    有未读消息时，显示消息小白点
    private fun hasUnReadMessage() {
//        if (UserUtils.hasUnReadMsgCount()) {
//            iv_spot.visibility = View.VISIBLE
//        } else {
//            iv_spot.visibility = View.GONE
//        }
    }

    private fun initRecycleView() {
        initDivderList()
        mRvList = findViewById(R.id.rv_setting_data)
        mRvList?.layoutManager = LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false)
//        mRvList?.addItemDecoration(DividerItemDecoration.Builder(mActivity, DividerItemDecoration.VERTICAL_LIST)
//                .setItemSpacing(ScreenUtils.dp2px(0.5f))
//                .setDividerList(mDividerList).build())

        if ((UserUtils.isLogin() && TextUtils.isEmpty(UserUtils.getRelationId()))) {
            LogUtils.info("用户防丢单入口item")
            mUtils.add(UserUtilsFactory.createPreventOrderLostUtils())//用户防丢单
        }
        mUtils.add(UserUtilsFactory.createMixtureUtils())//工具面板
        mUtils.addAll(UserUtilsFactory.createSingleUtils())//底部列表
        mAdapter = PersonalHomeAdapter(mUtils)
        rv_setting_data.adapter = mAdapter
        mRvList?.adapter = mAdapter
    }

    private fun initDivderList() {
        if (TextUtils.isEmpty(UserUtils.getRelationId())) {
            mSpacingCount++
        }
    }

    override fun showData(data: UserBean) {
    }

    //=================================== V 层实现 =========================================

    override fun onLoadUserInfoSuccess(userBean: UserBean) {
        onRenderUserInfo()
        //更新用户信息成功后，获取佣金信息
        mPresenter.getUserCommissionData()
    }

    override fun showErrorMsg(msg: String?) {
        ToastHelper.showCenterToast(msg)
    }

    override fun authorInvalid() {
        LoginModuleNavigator.startLoginActivity(mActivity)
    }

    override fun loadUserCommissionSuccess(commissionBean: CommissionBean) {
        // 更新补贴信息
        tv_commission_hint.text = if (TextUtils.isEmpty(commissionBean.subsidyWord)) {
            ResourceUtils.getString(R.string.util_commission_hint)
        } else {
            "* ${commissionBean.subsidyWord}"
        }
        //可用佣金
        tv_commission_valid.text = "¥${BigDecimalUtils.roundMode(commissionBean.usable)}"
        //本月结算佣金
        tv_commission_settlement.text = "¥${BigDecimalUtils.roundMode(commissionBean.currentMonthBalance)}"
        //已提现佣金
        tv_commission_withdraw.text = "¥${BigDecimalUtils.roundMode(commissionBean.withdrew)}"
        //上月结算佣金
        tv_commission_frozen.text = "¥${BigDecimalUtils.roundMode(commissionBean.upperMonthBalance)}"
        commissionUrl = commissionBean.help_url
    }

    override fun getBannerDataSuccess(bannerData: ArrayList<BannerAdvBean>) {

    }

    private val advertBannerView = object : AdvertBannerViewAdapter() {
        override fun onLoadAdvertBannerDataSuccess(bannerData: ArrayList<AdvertBannerBean>) {
            if (ListUtils.isNotEmpty(bannerData)) {
                val bannerBean = PersonalBannerBean(bannerData)
                var bannerIndex = if (mUtils[0] is PersonalHintBean) 1 else 0
                // 遍历出BannerBean类型，移除并添加
                if (ListUtils.isNotEmpty(mUtils)) {
                    mUtils.forEachIndexed { index, multiItemEntity ->
                        if (multiItemEntity is PersonalBannerBean) {
                            bannerIndex = index
                            mUtils.removeAt(index)
                        }
                    }
                }
                mUtils.add(bannerIndex, bannerBean)
                mAdapter?.notifyDataSetChanged()
            }
        }

        override fun onLoadAdvertBannerDataFail(msg: String) {

        }
    }


    /**淘宝授权*/
    override fun onTaoBaoAuthorSuccess() {
        if (TextUtils.isEmpty(UserUtils.getRelationId())) {
            mPresenter.getUnionCodeUrl()
        }
    }

    override fun getUnionCodeUrlSuccess(url: String) {
        UserModuleNavigator.startBindUnionCodeActivityForResultOnFragment(activity, this, url, UserConstant.BIND_UNION_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        LogUtils.info("onActivityResult:------------fragment")
        if (requestCode == UserConstant.BIND_UNION_REQUEST_CODE
                && resultCode == UserConstant.BIND_UNION_RESULT_CODE) {//绑定淘宝渠道
            LogUtils.info("onActivityResult:------------requestCode=$requestCode---resultCode=$resultCode")
            val code = data?.getStringExtra(UserConstant.UNION_CODE)
            val state = data?.getStringExtra(UserConstant.UNION_STATE)
            LogUtils.info("onActivityResult:------------code:$code-----state:$state")
            if (!TextUtils.isEmpty(code) && !TextUtils.isEmpty(state)) {
                mPresenter.handleUnionCode(code!!, state!!)
            }
        }
    }

    override fun handleUnionCodeSuccess() {
        ToastHelper.showCenterToast("淘宝授权成功")
        updateTaoBaoAuthor()
    }
}