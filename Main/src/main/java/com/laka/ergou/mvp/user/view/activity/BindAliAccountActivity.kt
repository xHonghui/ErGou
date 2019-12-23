package com.laka.ergou.mvp.user.view.activity

import android.support.constraint.ConstraintLayout
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import com.laka.androidlib.base.activity.BaseMvpActivity
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.ResourceUtils
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.ergou.R
import com.laka.ergou.mvp.user.UserUtils.UserUtils
import com.laka.ergou.mvp.user.contract.IUserBindAliAccountContract
import com.laka.ergou.mvp.user.model.bean.CommonData
import com.laka.ergou.mvp.user.presenter.UserBindAliAccountPresenter
import kotlinx.android.synthetic.main.activity_bind_ali_account.*

/**
 * @Author:Rayman
 * @Date:2019/1/18
 * @Description:绑定支付宝账号页面
 */
class BindAliAccountActivity : BaseMvpActivity<CommonData>(), IUserBindAliAccountContract.IUserBindAliAccountView
        , View.OnClickListener {

    /**
     * description:UI控制
     **/
    private lateinit var inputTextWatcher: InputTextWatcher

    /**
     * description:数据设置
     **/
    private lateinit var mPresenter: IUserBindAliAccountContract.IUserBindAliAccountPresenter

    override fun setContentView(): Int {
        return R.layout.activity_bind_ali_account
    }

    override fun initIntent() {
    }

    override fun initViews() {
        title_bar.setTitle(ResourceUtils.getString(R.string.util_bind_ali_account))
                .setLeftIcon(R.drawable.seletor_nav_btn_back)
                .setTitleTextColor(R.color.black)
                .setTitleTextSize(16)

        inputTextWatcher = InputTextWatcher()
        et_bind_account_name.addTextChangedListener(inputTextWatcher)
        et_bind_account.addTextChangedListener(inputTextWatcher)
        // 默认添加显示
        et_bind_account_name.setText(UserUtils.getUserAliRealName())
        et_bind_account.setText(UserUtils.getUserAliAccount())
    }

    override fun initData() {

    }

    override fun initEvent() {
        setClickView<ConstraintLayout>(R.id.btn_bind_account_commit)
        setClickView<ImageView>(R.id.iv_bind_account_name_delete)
        setClickView<ImageView>(R.id.iv_bind_account_delete)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_bind_account_name_delete -> {
                et_bind_account_name.setText("")
                iv_bind_account_name_delete.visibility = View.GONE
            }
            R.id.iv_bind_account_delete -> {
                et_bind_account.setText("")
                iv_bind_account_delete.visibility = View.GONE
            }
            R.id.btn_bind_account_commit -> {
                if (TextUtils.isEmpty(et_bind_account_name.text)) {
                    ToastHelper.showToast("支付宝用户名不能为空")
                    return
                }
                if (TextUtils.isEmpty(et_bind_account.text)) {
                    ToastHelper.showToast("支付宝账号不能为空")
                    return
                }

                // 绑定账号
                mPresenter.bindAliAccount("${et_bind_account_name.text}",
                        "${et_bind_account.text}")
            }
        }
    }

    override fun createPresenter(): IBasePresenter<*> {
        mPresenter = UserBindAliAccountPresenter()
        return mPresenter
    }

    override fun showData(data: CommonData) {

    }

    override fun showErrorMsg(msg: String?) {
        ToastHelper.showToast(msg)
    }

    override fun bindAliAccountSuccess(msg: String) {
        ToastHelper.showToast(msg)
        this.finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        et_bind_account_name.removeTextChangedListener(inputTextWatcher)
        et_bind_account.removeTextChangedListener(inputTextWatcher)
    }

    inner class InputTextWatcher : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (!TextUtils.isEmpty(et_bind_account_name?.text)) {
                if (iv_bind_account_name_delete.visibility == View.GONE) {
                    iv_bind_account_name_delete.visibility = View.VISIBLE
                }
            } else {
                iv_bind_account_name_delete.visibility = View.GONE
            }

            if (!TextUtils.isEmpty(et_bind_account?.text)) {
                if (iv_bind_account_delete.visibility == View.GONE) {
                    iv_bind_account_delete.visibility = View.VISIBLE
                }
            } else {
                iv_bind_account_delete.visibility = View.GONE
            }

        }
    }
}