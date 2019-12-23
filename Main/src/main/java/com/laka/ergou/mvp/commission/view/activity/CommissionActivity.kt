package com.laka.ergou.mvp.commission.view.activity

import android.content.Intent
import android.graphics.Paint
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import com.laka.androidlib.base.activity.BaseMvpActivity
import com.laka.androidlib.base.adapter.SimpleFragmentPagerAdapter
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.ergou.R
import com.laka.ergou.common.util.ui.TabUtils
import com.laka.ergou.mvp.commission.CommissionModelNavigator
import com.laka.ergou.mvp.commission.constant.CommissionConstant
import com.laka.ergou.mvp.commission.constract.ICommissionConstract
import com.laka.ergou.mvp.commission.model.bean.CommissionNewBean
import com.laka.ergou.mvp.commission.presenter.CommissionPresenter
import com.laka.ergou.mvp.commission.view.fragment.CommissionFragment
import com.laka.ergou.mvp.user.UserModuleNavigator
import com.laka.ergou.mvp.user.UserUtils.UserUtils
import kotlinx.android.synthetic.main.activity_commission.*
import android.graphics.Typeface.createFromAsset
import android.widget.TextView
import com.laka.androidlib.ext.setNumTypeface
import com.laka.ergou.mvp.main.RouterNavigator
import com.laka.ergou.mvp.main.constant.HomeConstant
import com.laka.ergou.mvp.main.constant.HomeNavigatorConstant
import java.util.HashMap


/**
 * @Author:summer
 * @Date:2019/1/18
 * @Description:
 */
class CommissionActivity : BaseMvpActivity<CommissionNewBean>(), ICommissionConstract.IBaseCommissonView, View.OnClickListener {


    private lateinit var mPresenter: CommissionPresenter
    private var resultData: CommissionNewBean? = null
    private var mCommissionExplainPreClickTime = 0L
    private var mClickTimeInterval = 1000
    private var fragmentList = ArrayList<Fragment>()
    private lateinit var titleList: Array<String>
    private lateinit var mAdapter: SimpleFragmentPagerAdapter
    override fun showErrorMsg(msg: String?) {
        dismissLoading()
    }

    override fun createPresenter(): IBasePresenter<*> {
        mPresenter = CommissionPresenter()
        return mPresenter
    }

    override fun setContentView(): Int {
        return R.layout.activity_commission
    }

    override fun initIntent() {
    }

    override fun initViews() {
        title_bar
                .setLeftIcon(R.drawable.seletor_nav_btn_back)
                .setTitleTextColor(R.color.black)
                .setBackGroundColor(R.color.white)
        tv_commisson_detail_txt.paint.flags = Paint.UNDERLINE_TEXT_FLAG //下划线
        tv_commisson_detail_txt.paint.isAntiAlias = true//抗锯齿
        tv_rmb_icon.setNumTypeface()
        tv_commisson_value.setNumTypeface()

    }

    override fun initData() {
        fragmentList?.clear()
        fragmentList.add(CommissionFragment.newInstance(CommissionConstant.MONTH))
        fragmentList.add(CommissionFragment.newInstance(CommissionConstant.LAST_MONTH))
        fragmentList.add(CommissionFragment.newInstance(CommissionConstant.TODAY))
        fragmentList.add(CommissionFragment.newInstance(CommissionConstant.YESTERDAY))
        titleList = Array(fragmentList.size, { String() })
        val array = resources.getStringArray(R.array.commmission_type)
        System.arraycopy(array, 0, titleList, 0, array.size)
        mAdapter = SimpleFragmentPagerAdapter(supportFragmentManager, fragmentList, titleList.asList())
        tabCommissionType?.setupWithViewPager(mViewPager)
        mViewPager?.adapter = mAdapter
        TabUtils.setTabLayoutIndicator(tabCommissionType)
    }

    override fun initEvent() {
        tv_commisson_detail_txt.setOnClickListener(this)
        tv_btn_txt.setOnClickListener(this)
        iv_settlement_detail.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_commisson_detail_txt -> {
                CommissionModelNavigator.startCommissionDetailActivity(this)
            }
            R.id.tv_btn_txt -> { // 提现
                onApplyWithdrawal()
            }
            R.id.iv_settlement_detail -> { //结算说明H5
                if (System.currentTimeMillis() - mCommissionExplainPreClickTime > mClickTimeInterval) {
                    val params = HashMap<String, String>()
                    params[HomeNavigatorConstant.ROUTER_VALUE] = "${resultData?.help_url}"
                    if ("${resultData?.settlement_order_type}" == CommissionConstant.COMMISSION_SETTLEMENTWAY_ORDER) {//订单结算时结算
                        params[HomeConstant.TITLE] = getString(R.string.commission_introd_order)
                    } else {//每月固定时间结算
                        params[HomeConstant.TITLE] = getString(R.string.commission_introd_month)
                    }
                    RouterNavigator.handleAppInternalNavigator(this, RouterNavigator.bannerRouterReflectMap[5].toString(), params)
                    mCommissionExplainPreClickTime = System.currentTimeMillis()
                }
            }
        }
    }

    /**
     * 申请提现
     * */
    private fun onApplyWithdrawal() {
        val aliUserName = UserUtils.getUserAliAccount()
        val tag = tv_btn_txt.tag as? Int
        if (tag != null && tag == 1) { // 审核中
            return
        }
        if (TextUtils.isEmpty(aliUserName)) {
            UserModuleNavigator.startBindAliAccountActivityForResult(this, CommissionConstant.REQUEST_CODE_BIND_ALI_ACCOUNT)
        } else {
            if (resultData != null) {
                if (resultData?.usable?.toDouble()!! >= resultData?.reach?.toDouble()!!) {
                    onWithdrawal()
                } else {
                    ToastHelper.showCenterToast("至少${resultData?.reach}元可用补贴才可以提现！")
                }
            }
        }
    }

    // 提现
    private fun onWithdrawal() {
        showLoading()
        mPresenter.onLoadWithdrawal()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CommissionConstant.REQUEST_CODE_BIND_ALI_ACCOUNT) {
            if (!TextUtils.isEmpty(UserUtils.getUserAliRealName())
                    && !TextUtils.isEmpty(UserUtils.getUserAliAccount())) {
                onApplyWithdrawal()
            }
        }
    }


    override fun showData(data: CommissionNewBean) {
        when (data.withdrawing) {
            CommissionConstant.WITHDRAWING -> {
                tv_btn_txt.text = "审核中"
                tv_btn_txt.tag = 1
//                tv_btn_txt.isEnabled = false
                tv_btn_txt.setShowShadow(true)
                tv_btn_txt.setOnTouchListener { v, event -> true }

            }
            CommissionConstant.AUDIT_PASS,
            CommissionConstant.REFUSE,
            CommissionConstant.NOTING -> {
                withdrawEnableStatus(data)
            }
            else -> {
                withdrawEnableStatus(data)
            }
        }
        if (data.withdrawing == CommissionConstant.REFUSE) {//拒绝
            linear_cash_withdrawal.visibility = View.VISIBLE
            linear_cash_withdrawal.setBackgroundColor(resources.getColor(R.color.bg_common_text_hint))
            iv_cash_withdrawal_icon.setImageResource(R.drawable.default_icon_notic)
            tv_cash_withdrawal_value.text = "${data.tip_word}"
            tv_cash_withdrawal_value.setTextColor(resources.getColor(R.color.common_text_hint))
        } else if (data.withdrawing == CommissionConstant.WITHDRAWING) {//审核中
            linear_cash_withdrawal.visibility = View.VISIBLE
            iv_cash_withdrawal_icon.setImageResource(R.drawable.default_icon_tips_g)
            linear_cash_withdrawal.setBackgroundColor(resources.getColor(R.color.color_eafefa))
            tv_cash_withdrawal_value.text = "提现中补贴：¥${data.withdrawing_money}"
            tv_cash_withdrawal_value.setTextColor(resources.getColor(R.color.color_05a585))
        }

        data.balance_word?.let {
            tv_commission_hint.text = "* $it"

        } ?: setDefaultBalanceWord()
        iv_settlement_detail.visibility = View.VISIBLE
        //tv_cash_withdrawal_tips.text = "至少${resultData?.reach}元可用补贴才可以提现。"
//        tv_rule1.text = "1、补贴满${resultData?.reach}元即可提现，每次提现为全额提现"
        tv_commisson_value.text = data.usable
    }

    private fun setDefaultBalanceWord() {
        tv_commission_hint.text = "* 每月25日后可提现上月确认收货（结算）的可用补贴"
    }

    private fun withdrawEnableStatus(data: CommissionNewBean) {
        tv_btn_txt.text = "申请提现"
        tv_btn_txt.tag = 2
        tv_btn_txt.isEnabled = true
    }

    override fun onWithdrawalSuccess() {
        fragmentList.forEach {
            if (it is CommissionFragment) {
                it.reloadData()
            }
        }
    }


    fun setData(result: CommissionNewBean) {
        resultData = result
        showData(result)
    }


}