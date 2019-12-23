package com.laka.ergou.mvp.advert.fragment

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.laka.androidlib.base.fragment.BaseFragment
import com.laka.androidlib.util.ApplicationUtils
import com.laka.androidlib.util.SPHelper
import com.laka.ergou.R
import com.laka.ergou.mvp.advert.utils.CountDownUtils
import com.laka.ergou.mvp.main.RouterNavigator
import com.laka.ergou.mvp.main.constant.HomeConstant
import com.laka.ergou.mvp.main.constant.HomeNavigatorConstant
import com.laka.ergou.mvp.main.view.activity.HomeActivity
import com.laka.ergou.mvp.shopping.center.constant.ShoppingCenterConstant
import com.laka.ergou.mvp.shopping.center.helper.SaveImageHelper
import com.laka.ergou.mvp.shopping.center.model.bean.AdvertBean
import com.laka.ergou.mvp.user.UserUtils.UserUtils

/**
 * @Author:summer
 * @Date:2019/1/15
 * @Description:
 */
class AdvertFragment : BaseFragment() {
    lateinit var countDownUtils: CountDownUtils
    var advertBean: AdvertBean? = null
    /** Fragment当前状态是否可见  */
    var isFragmentVisible = false
    var startSkipCountDown: TextView? = null
    var ivAdvert: ImageView? = null
    override fun setContentView(): Int {
        return R.layout.activity_advert
    }

    override fun initArgumentsData(arguments: Bundle?) {

    }

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
        startSkipCountDown = rootView?.findViewById(R.id.startSkipCountDown);
        ivAdvert = rootView?.findViewById(R.id.ivAdvert)
        var helper = SaveImageHelper(context)
        advertBean = SPHelper.getObject(ShoppingCenterConstant.SP_KEY_ADVERT, AdvertBean::class.java)
        var bitmap = helper.getImage(ShoppingCenterConstant.SP_KEY_ADVERT_IMG_NAME)
        bitmap?.let {
            ivAdvert?.setImageBitmap(it)
        }
        countDownUtils = CountDownUtils(startSkipCountDown!!, object : Runnable {
            override fun run() {
                goMain()
            }
        })
        countDownUtils.init()
    }

    override fun initData() {

    }

    override fun initEvent() {
        initClick()
    }

    private fun initClick() {
        startSkipCountDown?.setOnClickListener {
            goMain()
        }
        ivAdvert?.setOnClickListener {
            skipTypeActivity()
            goMain()
        }

    }


    private fun skipTypeActivity() {
        //1：邀请好友，2：其他消息，3：我的订单，4：商品详情，5：H5链接, 8: 商品专题,9:需登录的天猫h5相关页面
        advertBean?.let {

            countDownUtils.release()
            var paramsMap = it.scene_extra
            var target = RouterNavigator.bannerRouterReflectMap[it.scene_id].toString()
            paramsMap[HomeConstant.TITLE] = it.title
            paramsMap[HomeNavigatorConstant.ROUTER_VALUE] = it.scene_value
            if (!paramsMap.containsKey(HomeConstant.TOPIC_BIG_IMAGE_URL)) {
                paramsMap[HomeConstant.TOPIC_BIG_IMAGE_URL] = it.img_path
            }
            UserUtils.updateLocalUserInfo()
//            goMain()
            RouterNavigator.handleAppInternalNavigator(context, target, paramsMap)
//            finish()
//            LogUtils.debug("ONE Activity" + ApplicationUtils.isIsFormAdvert())
//            if (ApplicationUtils.isIsFormAdvert()) {
//                ApplicationUtils.setIsFormAdvert(false)
//                val cn = ComponentName(this, "com.laka.ergou.mvp.main.view.activity.HomeActivity")
//                val intent = Intent()
//                intent.component = cn
//                intent.action = "android.intent.action.VIEW"
//                startActivity(intent)
//            }
        }
    }

    fun goMain() {
        if (context != null && context is HomeActivity) {
            (context as HomeActivity).advertSwitch(true)
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (userVisibleHint) {
            isFragmentVisible = true
        } else {
            isFragmentVisible = false
        }
    }
}