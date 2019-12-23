package com.laka.ergou.mvp.user.view.activity

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import com.laka.androidlib.base.activity.BaseMvpActivity
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.ResourceUtils
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.androidlib.widget.SelectorButton
import com.laka.ergou.R
import com.laka.ergou.R.id.tv_bind_order_status
import com.laka.ergou.mvp.user.UserModuleNavigator
import com.laka.ergou.mvp.user.contract.IUserBindOrderContract
import com.laka.ergou.mvp.user.presenter.UserBindOrderPresenter
import kotlinx.android.synthetic.main.activity_bind_order.*
import java.util.*

/**
 * @Author:Rayman
 * @Date:2019/1/17
 * @Description:绑定订单页面
 */
class BindOrderActivity : BaseMvpActivity<Objects>(), IUserBindOrderContract.IUserBindOrderView,
        View.OnClickListener {

    /**
     * description:数据设置
     **/
    private lateinit var mBindOrderPresenter: IUserBindOrderContract.IUserBindOrderPresenter

    private var textWatcher = InputTextWatcher()

    override fun setContentView(): Int {
        return R.layout.activity_bind_order
    }

    override fun initIntent() {
    }

    override fun initViews() {
        title_bar.setTitle(ResourceUtils.getString(R.string.util_bind_order))
                .setLeftIcon(R.drawable.seletor_nav_btn_back)
                .setTitleTextColor(R.color.black)
                .setTitleTextSize(16)
    }

    override fun initData() {
    }

    override fun initEvent() {
        setClickView<ImageView>(R.id.iv_bind_order_delete)
        setClickView<SelectorButton>(R.id.btn_bind_order)
        et_bind_order_input.addTextChangedListener(textWatcher)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_bind_order_delete -> {
                et_bind_order_input.setText("")
                iv_bind_order_delete.visibility = View.GONE
            }
            R.id.btn_bind_order -> {
                if (TextUtils.isEmpty(et_bind_order_input.text)) {
                    ToastHelper.showToast("输入的订单号不能为空")
                } else {
                    // 请求数据
                    mBindOrderPresenter.bindOrder(et_bind_order_input.text.toString())
                }
            }
        }
    }

    override fun createPresenter(): IBasePresenter<*> {
        mBindOrderPresenter = UserBindOrderPresenter()
        return mBindOrderPresenter
    }

    override fun showData(data: Objects) {
    }

    override fun showErrorMsg(msg: String?) {
        tv_bind_order_status.text = msg
        cl_bind_order_error_hint.visibility = View.VISIBLE
    }

    override fun bindOrderSuccess(msg: String) {
        // 跳转到我的订单页面
        ToastHelper.showToast(msg)
        UserModuleNavigator.startMyOrderActivity(this)
    }

    inner class InputTextWatcher : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (!TextUtils.isEmpty(et_bind_order_input?.text)) {
                if (iv_bind_order_delete.visibility == View.GONE) {
                    iv_bind_order_delete.visibility = View.VISIBLE
                }
            } else {
                iv_bind_order_delete.visibility = View.GONE
            }
        }
    }
}