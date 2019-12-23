package com.laka.ergou.mvp.shopping.center.view.activity

import android.view.View
import com.laka.androidlib.base.activity.BaseMvpActivity
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.EncryptUtils.test
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.ergou.R
import com.laka.ergou.mvp.shopping.center.contract.IProductDetailContract
import com.laka.ergou.mvp.shopping.center.model.bean.ProductDetail
import com.laka.ergou.mvp.shopping.center.presenter.ProductDetailPresenter
import kotlinx.android.synthetic.main.activity_product_detail.*

/**
 * @Author:Rayman
 * @Date:2018/12/20
 * @Description:商品详情类
 */
class ProductDetailActivity : BaseMvpActivity<ProductDetail>(), IProductDetailContract.IProductDetailView,
        View.OnClickListener {

    lateinit var mDetailPresenter: IProductDetailContract.IProductDetailPresenter

    override fun setContentView(): Int {
        return R.layout.activity_product_detail
    }

    override fun initIntent() {
    }

    override fun initViews() {
    }

    override fun initData() {
        mDetailPresenter.getProductDetail("")
    }

    override fun initEvent() {
        btn_product_detail.setOnClickListener {
            test()
        }

//        btn_product_detail.setOnClickListener(imageClick)
    }

    override fun createPresenter(): IBasePresenter<IProductDetailContract.IProductDetailView> {
        mDetailPresenter = ProductDetailPresenter()
        return mDetailPresenter
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_product_detail -> showErrorMsg(btn_product_detail?.text as String)
        }
    }

    override fun showLoading() {
    }


    override fun dismissLoading() {
    }

    override fun showData(data: ProductDetail) {
        tv_product_detail?.text = data.toString()
    }

    override fun showErrorMsg(msg: String?) {
        ToastHelper.showToast(msg)
    }

    // 定义两个lambda函数  ->左侧为参数，右侧为返回内容（也可以是表达式）
    val sum1 = { x: Int, y: Int -> x + y }

    // 定义了一个lambda函数，格式为两个Int参数，并返回Int类型。 =  号右边为具体的函数体
    // 可以看到具体函数实现里面还有两个变量ab，是从x,y传递过来的
    val sum2: (x: Int, y: Int) -> Int = { a, b -> a + b }

    fun highOrderFunc(a: Int, b: Int, paramsFunc: (c: Int, d: Int) -> Boolean): Int {
        return if (paramsFunc(a, b)) {
            a
        } else {
            b
        }
    }

    fun test() {

        val max = { x: Int, y: Int -> x > y }

        val undefine = { x: Int, y: Int -> String }

        println(sum1)
        println(sum2)
        println(max)
        println(undefine)
        println(sum1(10, 20))
        println(sum2(10, 20))
        println(max(10, 20))
        println(undefine(10, 20))
        println(highOrderFunc(20, 1, max))
    }

    // 定义一个lambda函数，参数为View，返回值为void。函数体为view调用当前的View.onClick函数
    val imageClick: (v: View) -> Unit = { view -> viewClicked(view) }
    val viewClicked: (v: View) -> Unit = {
        println(it.id)
    }


    // 定义了一个列表变化的传递。
    val onPageChangeListener: (position: Int) -> Unit = { position -> pageChange(position) }
    val onItemClickListener: (v: View, position: Int) -> Unit = { view, position -> onItemClick(view, position) }

    fun pageChange(curPage: Int) {
        println("当前页数：${curPage}")
    }

    fun onItemClick(view: View, position: Int) {
        println("点击当前Item：${position}")
    }
}