package com.laka.ergou.mvp.shop.utils

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.pm.ResolveInfo
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.ergou.mvp.shop.view.dialog.ShareDialog
import java.util.*

/**
 * @Author:summer
 * @Date:2019/1/15
 * @Description: 分享工具类
 */
object ShareUtils {

    /**
     * 打开第三方app
     * */
    fun goToThirdParty(activity: Activity, targetPackageName: String) {
        //同AndroidManifest中主入口Activity一样
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        //得到一个PackageManager的对象
        val packageManager = activity.applicationContext.packageManager
        //获取到主入口的Activity集合
        val list = packageManager.queryIntentActivities(intent, 0)
        Collections.sort(list, ResolveInfo.DisplayNameComparator(packageManager))
        for (resolveInfo in list) {
            val packageName = resolveInfo.activityInfo.packageName
            val clazz = resolveInfo.activityInfo.name
            if (packageName.contains(targetPackageName)) {
                val componentName = ComponentName(packageName, clazz)
                val targetIntent = Intent()
                targetIntent.component = componentName
                targetIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                activity.startActivity(targetIntent)
                return
            }
        }
        ToastHelper.showCenterToast("找不到应用!")
    }


    fun showShareDialog(activity: Activity) {
        val shareDialog = ShareDialog(activity)
        shareDialog.setOnItemClickListener {
            when (it) {
                ShareDialog.WEIXIN_CLICK -> {
                    ShareUtils.goToThirdParty(activity, ShareDialog.WEIXIN_PACKAGE_NAME)
                }
                ShareDialog.QQ_KJ_CLICK -> {
                    ShareUtils.goToThirdParty(activity, ShareDialog.QQKJ_PACKAGE_NAME)
                }
                ShareDialog.QQ_CLICK -> {
                    ShareUtils.goToThirdParty(activity, ShareDialog.QQ_PACKAGE_NAME)
                }
                ShareDialog.FRIEND_CIRCLE_CLICK -> {
                    ShareUtils.goToThirdParty(activity, ShareDialog.FRIEND_CIRCLE_PACKAGE_NAME)
                }
            }
        }
        shareDialog.show()
    }
}