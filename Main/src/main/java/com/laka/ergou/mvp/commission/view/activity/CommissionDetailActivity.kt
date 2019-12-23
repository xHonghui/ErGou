package com.laka.ergou.mvp.commission.view.activity

import android.widget.TextView
import com.laka.androidlib.base.activity.BaseMvpActivity
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.ResourceUtils
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.androidlib.widget.refresh.BaseListBean
import com.laka.androidlib.widget.refresh.OnResultListener
import com.laka.ergou.R
import com.laka.ergou.mvp.commission.constract.ICommissionDetailConstract
import com.laka.ergou.mvp.commission.model.bean.CommissionDetailData
import com.laka.ergou.mvp.commission.presenter.CommissionDetailPresenter
import com.laka.ergou.mvp.commission.view.adapter.CommissionDetailListAdapter
import kotlinx.android.synthetic.main.activity_main_shop.*
import kotlinx.android.synthetic.main.activity_setting.*

/**
 * @Author:summer
 * @Date:2019/1/18
 * @Description:
 */
class CommissionDetailActivity : BaseMvpActivity<BaseListBean<CommissionDetailData>>(), ICommissionDetailConstract.ICommissionDetailView {

    private var mDataList: ArrayList<CommissionDetailData> = ArrayList()
    private var mAdapter: CommissionDetailListAdapter? = null
    private lateinit var mResultListener: OnResultListener
    private lateinit var mPresenter: CommissionDetailPresenter

    override fun showData(data: BaseListBean<CommissionDetailData>) {
    }

    override fun createPresenter(): IBasePresenter<*> {
        mPresenter = CommissionDetailPresenter()
        return mPresenter
    }

    override fun setContentView(): Int {
        return R.layout.activity_commission_detail
    }

    override fun initIntent() {
    }

    override fun initViews() {
        title_bar.setLeftIcon(R.drawable.nav_btn_back_n)
                .setBackGroundColor(R.color.white)
                .setTitleTextColor(R.color.black)
        rv_list?.noDataView?.findViewById<TextView>(R.id.tv_no_data)?.text = ResourceUtils.getString(R.string.no_commission_data_hint)
    }

    override fun initEvent() {
        rv_list.setOnRequestListener { page, resultListener ->
            mResultListener = resultListener
            mPresenter.onLoadCommissionDetailData(page)
            ""
        }
        rv_list.refresh()
    }

    override fun initData() {
        mAdapter = CommissionDetailListAdapter(R.layout.item_commission_detail_list, mDataList)
        rv_list.adapter = mAdapter
    }

    override fun onLoadCommissionDetailDataSuccess(result: BaseListBean<CommissionDetailData>) {
        mResultListener?.onResponse(result)
    }

    override fun onLoadError() {
        mResultListener?.onFailure(-1,"")
    }

    override fun showErrorMsg(msg: String?) {
        ToastHelper.showToast("$msg")
    }

}