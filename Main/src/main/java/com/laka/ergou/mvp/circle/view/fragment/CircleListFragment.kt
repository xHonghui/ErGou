package com.laka.ergou.mvp.circle.view.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.SparseArray
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.laka.androidlib.eventbus.Event
import com.laka.androidlib.eventbus.EventBusManager
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.StringUtils
import com.laka.androidlib.util.screen.ScreenUtils
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.androidlib.widget.dialog.CommonConfirmDialog
import com.laka.androidlib.widget.refresh.BaseListBean
import com.laka.androidlib.widget.refresh.RefreshRecycleView
import com.laka.ergou.R
import com.laka.ergou.common.dsl.RVWrapper
import com.laka.ergou.common.dsl.refreshInit
import com.laka.ergou.common.ext.onClick
import com.laka.ergou.common.util.ClipBoardManagerHelper
import com.laka.ergou.common.util.share.WxShareUtils
import com.laka.ergou.common.widget.refresh.FrogRefreshRecyclerView
import com.laka.ergou.mvp.circle.CircleNavigator
import com.laka.ergou.mvp.circle.constant.CircleConstant
import com.laka.ergou.mvp.circle.constract.ICircleConstract
import com.laka.ergou.mvp.circle.model.bean.CircleArticle
import com.laka.ergou.mvp.circle.model.bean.CircleCommentResponse
import com.laka.ergou.mvp.circle.model.bean.ParentRefreshEvent
import com.laka.ergou.mvp.circle.presenter.CirclePresenter
import com.laka.ergou.mvp.circle.view.adapter.CircleListAdapter
import com.laka.ergou.mvp.commission.model.bean.CommissionNewBean
import com.laka.ergou.mvp.login.model.event.UserEvent
import com.laka.ergou.mvp.main.constant.HomeApiConstant
import com.laka.ergou.mvp.main.constant.HomeEventConstant
import com.laka.ergou.mvp.main.helper.BindReltaionIdHelper
import com.laka.ergou.mvp.main.model.event.ImageDataEvent
import com.laka.ergou.mvp.main.view.fragment.HomeFragment
import com.laka.ergou.mvp.share.helper.ShopImgShareHelper
import com.laka.ergou.mvp.shop.ShopDetailModuleNavigator
import com.laka.ergou.mvp.user.UserUtils.UserUtils
import com.laka.ergou.mvp.user.constant.UserConstant
import kotlinx.android.synthetic.main.fragment_circle_list.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class CircleListFragment : HomeFragment(), ICircleConstract.IBaseCircleView, CircleListAdapter.Callback {

    private lateinit var mBindRelationIdHelper: BindReltaionIdHelper
    private lateinit var mRvList: FrogRefreshRecyclerView
    private lateinit var mIvToTop: ImageView
    private lateinit var mCirclePresenter: CirclePresenter
    private lateinit var mRVWrapper: RVWrapper
    var mCategoryId = ""
    private var mConfirmDialog: CommonConfirmDialog? = null
    //“滑动到顶部”按钮控制参数
    private var mTotalDy = 0F
    private val mListScrollControlDis = ScreenUtils.dp2px(360F)
    private var mCircleArticle: CircleArticle? = null
    private var mSendCirclePos: Int = -1
    private var mCopyCommentPos: Int = -1
    private var mPage = 0
    private var oneParent = 0
    private var twoParent = 0

    companion object {
        fun newInstance(oneParent: Int, twoParent: Int, categoryId: Int): CircleListFragment {
            val bundle = Bundle()
            bundle.putString(CircleConstant.CATEGORY_ID, categoryId.toString())
            bundle.putInt(CircleConstant.CATEGORY_POSITION2, twoParent)
            bundle.putInt(CircleConstant.CATEGORY_POSITION, oneParent)
            var fragment = CircleListFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun createPresenter(): IBasePresenter<*> {
        mCirclePresenter = CirclePresenter()
        return mCirclePresenter
    }

    override fun setContentView(): Int {
        return R.layout.fragment_circle_list
    }

    override fun initDataLazy() {
        mRvList?.refresh()
    }

    override fun showData(data: CommissionNewBean) {

    }

    override fun showErrorMsg(msg: String?) {

    }


    override fun initArgumentsData(arguments: Bundle?) {
        arguments?.let {
            mCategoryId = it.getString(CircleConstant.CATEGORY_ID)
            oneParent = it.getInt(CircleConstant.CATEGORY_POSITION)
            twoParent = it.getInt(CircleConstant.CATEGORY_POSITION2)
        }
    }

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
        mRvList = rv_circle_list
        mIvToTop = iv_to_top
        mConfirmDialog = CommonConfirmDialog(context)
        var mAdapter = CircleListAdapter(context).setPictureClickCallback(this)
        mRVWrapper = refreshInit {
            view = rv_circle_list
            adapter = mAdapter
            onRequest = { page, resultListener ->
                //第一次，请求数据
                if (mPage == 0) {
                    mPage = page
                    mCirclePresenter.getArticleList(mCategoryId, mPage)
                } else {
                    //下拉刷新，发送EventBus到CircleFrament 检查是否有新的分类
                    mPage = page
                    if (page == 1) {
                        EventBusManager.postEvent(ParentRefreshEvent(oneParent, twoParent))
                    } else {
                        //加载更多
                        mCirclePresenter.getArticleList(mCategoryId, mPage)
                    }
                }
            }
            adapterItemChildClick = { adapter, view, position ->
                var item = adapter.getItem(position) as CircleArticle
                mCircleArticle = item
                when (view.id) {
                    R.id.tv_share -> shareGoods(item)
                    R.id.tv_one_send -> {
                        mSendCirclePos = position
                        sendCircle(item, position)
                    }
                    R.id.tv_copy -> {
                        mCopyCommentPos = position
                        copyComment(item, position)
                    }
                    R.id.cl_goods_detail -> {
                        ShopDetailModuleNavigator.startShopDetailActivity(activity, "${item.product.num_id}")
                    }
                }
            }
        }
    }

    //查看大图
    override fun onThumbPictureClick(circleArticle: CircleArticle, i: ImageView, imageGroupList: SparseArray<ImageView>, urlList: List<Uri>) {
        EventBusManager.postEvent(ImageDataEvent(i, imageGroupList, urlList, circleArticle))
    }

    //父类数据无变化，刷新列表
    fun refreshData() {

        if (::mCirclePresenter.isInitialized) {
            mCirclePresenter.getArticleList(mCategoryId, mPage)
        }

    }

    private fun sendCircle(item: CircleArticle, position: Int) {
        if (UserUtils.isLogin() && !TextUtils.isEmpty(UserUtils.getRelationId())) {
            if (CircleNavigator.loginHandle(context)) {
                if (item.send_status != "0") return
                showLoading()
                mCirclePresenter.sendCircle(item.article_id.toString(), position)
            }
        } else {
            //绑定渠道ID
            if (!::mBindRelationIdHelper.isInitialized) {
                mBindRelationIdHelper = BindReltaionIdHelper(activity)
            }
            mBindRelationIdHelper.bindRelationId()
        }
    }

    override fun onSendCircle(erroeCode: Int, error: String, position: Int) {
        dismissLoading()
        if (position != mSendCirclePos) return
        when (erroeCode) {
            0 -> {
                ToastHelper.showToast("已发送")
                mCircleArticle?.send_status = "2"
                mRVWrapper.adapter?.notifyDataSetChanged()
            }
            904 -> showCircleHint(1, error)
            905 -> showCircleHint(0, error)
            906 -> showCircleHint(2, error)
            907 -> {
                ToastHelper.showToast("$error")
                mCircleArticle?.send_status = "2"
                mRVWrapper.adapter?.notifyDataSetChanged()
            }
            else -> ToastHelper.showToast("$error")
        }
    }

    private fun showCircleHint(type: Int, error: String) {
        mConfirmDialog?.show()
        mConfirmDialog?.setLayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        mConfirmDialog?.let {
            it.setDefaultTitleTxt("$error")
                    .setCancelTxt("暂不设置")
                    .setCancelColor(R.color.black)
                    .setSureTxt("前往设置")
                    .setOnClickSureListener {
                        CircleNavigator.startCircleHelperActivityForResult(activity, "发圈助手", HomeApiConstant.URL_SEND_CIRCLE,
                                type, CircleConstant.BUTLER_REQUEST_CODE_FOR_CIRCLE)
                    }
        }
    }

    private fun copyComment(item: CircleArticle, position: Int) {
        if (UserUtils.isLogin() && !TextUtils.isEmpty(UserUtils.getRelationId())) {
            if (item.comment?.contains(CircleConstant.TKL_MATCHER)
                    && TextUtils.isEmpty(item?.finalComment)
                    && !TextUtils.isEmpty(item?.product?.num_id)) {
                mCirclePresenter.getCircleComment(item.product.num_id, item.product.coupon_id, position)
            } else {
                try {
                    val content = if (StringUtils.isEmpty(item.finalComment)) item.comment else item.finalComment
                    ClipBoardManagerHelper.getInstance.writeToClipBoardContent(content)
                    ToastHelper.showToast("复制成功")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            //绑定渠道ID
            if (!::mBindRelationIdHelper.isInitialized) {
                mBindRelationIdHelper = BindReltaionIdHelper(activity)
            }
            mBindRelationIdHelper.bindRelationId()
        }
    }

    override fun getCircleCommentSuccess(response: CircleCommentResponse, position: Int) {
        if (position != mCopyCommentPos) return
        mCircleArticle?.let {
            it?.finalComment = it?.comment?.replace(CircleConstant.TKL_MATCHER, response.tkl)
            copyComment(it, position)
        }
    }

    private fun shareGoods(item: CircleArticle) {
        if (UserUtils.isLogin() && !TextUtils.isEmpty(UserUtils.getRelationId())) {
            ClipBoardManagerHelper.getInstance.writeToClipBoardContent(item.content)
            var shopImgShareHelper = ShopImgShareHelper<String>(context as Activity)
            shopImgShareHelper.bindData(item.images)
            shopImgShareHelper.setShareContent(item.content)
            shopImgShareHelper.postProductShare()
        } else {
            //绑定渠道ID
            if (!::mBindRelationIdHelper.isInitialized) {
                mBindRelationIdHelper = BindReltaionIdHelper(activity)
            }
            mBindRelationIdHelper.bindRelationId()
        }
    }

    override fun initEvent() {
        mRvList.onScrollListener = RefreshRecycleView.OnScrollListener { recyclerView, _, dy, _, _ ->
            mTotalDy += dy
            // 纯粹累加 dy，最后得出的总和可能为负数，这样是不合理的，所以当 mTotalDy<0 时，将其置为 0
            mTotalDy = if (mTotalDy < 0) 0f else mTotalDy
            if (mTotalDy >= mListScrollControlDis) {
                mIvToTop.visibility = View.VISIBLE
            } else {
                mIvToTop.visibility = View.GONE
            }
        }
        mIvToTop.onClick {
            mRvList.scrollToTop()
        }
    }

    override fun articleListRespone(list: BaseListBean<CircleArticle>) {

        mRVWrapper.onResponse(list)
    }

    override fun onNetWorkFail(erroeCode: Int, error: String) {
        mRVWrapper.onFailure()
        ToastHelper.showToast("$error")
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: UserEvent?) {
        when (event?.type) {
            UserConstant.LOGIN_EVENT -> {
                LogUtils.error("登陆成功")
                mPage = 0
                mRvList.refresh(true)
            }
            UserConstant.LOGOUT_EVENT -> {
                LogUtils.error("退出登陆")
                mPage = 0
                mRvList.refresh(true)
            }

        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onBindRelationIdEvent(event: Event) {
        when (event.name) {
            HomeEventConstant.EVENT_BIND_RELEATION_ID_SUCCESS -> {
                val intent = event.data as? Intent
                intent?.let {
                    if (::mBindRelationIdHelper.isInitialized) {
                        mBindRelationIdHelper.onActivityResult(it)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        if (::mBindRelationIdHelper.isInitialized) {
            mBindRelationIdHelper.release()
        }
        super.onDestroy()
    }
}