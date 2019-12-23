package com.laka.ergou.mvp.user.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.support.v4.view.ViewPager
import android.view.KeyEvent
import android.widget.ImageView
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.ViewSkeletonScreen
import com.laka.androidlib.base.activity.BaseActivity
import com.laka.androidlib.base.activity.BaseMvpActivity
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.ResourceUtils
import com.laka.androidlib.util.image.BitmapUtils
import com.laka.androidlib.util.screen.ScreenUtils
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.androidlib.widget.titlebar.TitleBarView
import com.laka.ergou.R
import com.laka.ergou.common.util.share.WxShareUtils
import com.laka.ergou.common.util.ui.CommonIndicator
import com.laka.ergou.mvp.advert.constant.AdvertConstant
import com.laka.ergou.mvp.shop.view.dialog.ShareDialog
import com.laka.ergou.mvp.user.constant.UserConstant
import com.laka.ergou.mvp.user.contract.IInvitationPlaybillConstract
import com.laka.ergou.mvp.user.model.bean.InvitationPagerBean
import com.laka.ergou.mvp.user.presenter.InvitationPlaybillPresenter
import com.laka.ergou.mvp.user.view.adapter.PlaybillPagerAdapter
import kotlinx.android.synthetic.main.activity_invitation_playbill.*


/**
 * @Author:summer
 * @Date:2019/4/23
 * @Description:邀请海报页面
 */
class InvitationPlaybillActivity : BaseMvpActivity<String>(), IInvitationPlaybillConstract.IInvitationPlaybillView {

    /**
     * 二维码在每个 pager 图片中的位置
     * */
//    private var mDataList = arrayListOf(InvitationPagerBean(R.drawable.playbill_03, 0.348, 0.360, 0.328, 0.204, 0.524, 0.596, R.color.color_main),
//            InvitationPagerBean(R.drawable.playbill_05, 0.381, 0.779, 0.240, 0.149, 0.0, 0.0, R.color.color_main),
//            InvitationPagerBean(R.drawable.playbill_07, 0.381, 0.789, 0.240, 0.149, 0.530, 0.946, R.color.white),
//            InvitationPagerBean(R.drawable.playbill_09, 0.381, 0.789, 0.240, 0.149, 0.530, 0.946, R.color.color_main),
//            InvitationPagerBean(R.drawable.playbill_11, 0.335, 0.731, 0.293, 0.182, 0.540, 0.614, R.color.color_main))
//
    private lateinit var mPagerAdapter: PlaybillPagerAdapter
    private var mDataList = ArrayList<String>()

    /**
     * share
     * */
    private lateinit var shareDialog: ShareDialog
    private var mScreenshotData = HashMap<Int, ByteArray>()
    private var defaultShareTitle = ResourceUtils.getString(R.string.share_invitation_title)
    private var defaultShareContent = ResourceUtils.getString(R.string.share_invitation_content)

    private lateinit var mPresenter: InvitationPlaybillPresenter
    private lateinit var mSkeleton: ViewSkeletonScreen
    private lateinit var mTitleBar: TitleBarView<*>
    private lateinit var mViewPager: ViewPager
    private lateinit var mRlSpot: CommonIndicator
    private var postId = -1
    private var isShareAddTime = false
    override fun setContentView(): Int {
        return R.layout.activity_invitation_playbill
    }

    override fun createPresenter(): IBasePresenter<*> {
        mPresenter = InvitationPlaybillPresenter()
        return mPresenter
    }

    override fun initIntent() {
        intent?.let {
            postId = it.getIntExtra(UserConstant.POST_ID, -1)
        }
    }

    override fun initViews() {
        mTitleBar = findViewById(R.id.title_bar)
        mViewPager = findViewById(R.id.view_pager_playbill)
        mRlSpot = findViewById(R.id.rl_spot)
        shareDialog = ShareDialog(this)
        shareDialog.enableQQShare(false)
                .enableQQZoneShare(false)
        mTitleBar.setRightIcon(R.drawable.seletor_nav_btn_share)
                .showDivider(false)
                .setLeftIcon(R.drawable.seletor_nav_btn_back)
                .setBackGroundColor(R.color.white)
                .setTitleTextColor(R.color.black)
                .setOnRightClickListener {
                    // 弹出分享框
                    createSharePost()
                }
                .setOnLeftClickListener {
                    val intent = Intent()
                    intent.putExtra(AdvertConstant.SHARE_ADD_TIME, isShareAddTime)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }

        //显示骨架框
        mSkeleton = Skeleton.bind(cl_root_view)
                .load(R.layout.activity_invitation_playbill_skeleton)
                .duration(1000)
                .color(R.color.shimmer_color)
                .angle(0)
                .show()
    }

    override fun initData() {
        mPresenter.onLoadSharePosterData(postId)
    }

    override fun initEvent() {
        shareDialog.setOnItemClickListener {
            when (it) {
                ShareDialog.WEIXIN_CLICK -> {
                    isShareAddTime = true
                    WxShareUtils.getInstance(this)
                            .shareImageAction(true, defaultShareTitle, defaultShareContent,
                                    mScreenshotData[mViewPager.currentItem])
                }
                ShareDialog.FRIEND_CIRCLE_CLICK -> {
                    isShareAddTime = true
                    WxShareUtils.getInstance(this)
                            .shareImageAction(false, defaultShareTitle, defaultShareContent,
                                    mScreenshotData[mViewPager.currentItem])
                }
            }
        }
        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                mRlSpot.setScrollData(position, positionOffset, positionOffsetPixels)
                LogUtils.info("invitationPlaybillActivity-------：position=$position,,,positionOffset=$positionOffset,,,positionOffsetPixels=$positionOffsetPixels")
            }

            override fun onPageSelected(position: Int) {

            }
        })
    }

    /**
     * description:截图View
     **/
    private fun createSharePost() {
        val position = mViewPager.currentItem
        val byteArray = mScreenshotData[position]
        if (byteArray != null && byteArray.isNotEmpty()) {
            mScreenshotData[position] = byteArray
            shareDialog.show()
            LogUtils.info("invitationPlaybillActivity-------集合中与存在图片数据")
        } else {
            val childView = mViewPager.getChildAt(mViewPager.currentItem)
            childView?.let {
                childView.buildDrawingCache()
                var bitmap = childView.drawingCache
                //微信分享图片有大小限制，太大会分享失败，所以这里做了一个压缩
                val array = BitmapUtils.compressBitmapToByte(bitmap, 50, true)
                if (array != null && array.isNotEmpty()) {
                    mScreenshotData[position] = array
                    shareDialog.show()
                    LogUtils.info("invitationPlaybillActivity-------截图成功")
                } else {
                    LogUtils.info("invitationPlaybillActivity-------截图失败")
                }
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val intent = Intent()
            intent.putExtra(AdvertConstant.SHARE_ADD_TIME, isShareAddTime)
            setResult(Activity.RESULT_OK, intent)
            finish()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        mScreenshotData.clear()
        super.onDestroy()
    }

    override fun showData(data: String) {

    }

    override fun showErrorMsg(msg: String?) {

    }

    override fun onLoadSharePosterDataSuccess(list: ArrayList<String>) {
        //隐藏骨架框
        if (::mSkeleton.isInitialized) {
            mSkeleton.hide()
        }
        mDataList.clear()
        mDataList.addAll(list)
        //初始化小点
        mRlSpot.initIndicator(mDataList.size)
        mPagerAdapter = PlaybillPagerAdapter(this, mDataList)
        mViewPager.offscreenPageLimit = mDataList.size
        mViewPager.adapter = mPagerAdapter
    }

    override fun onLoadSharePosterDataFail(msg: String) {
        ToastHelper.showToast(msg)
        Handler().postDelayed({
            finish()
        }, 500)
    }
}