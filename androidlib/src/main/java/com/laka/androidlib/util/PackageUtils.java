package com.laka.androidlib.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

/**
 * @Author:Rayman
 * @Date:2019/2/22
 * @Description:Android包管理器
 */

public class PackageUtils {

    /**
     * description:国外热门App
     **/
    public static final String INSTAGRAM = "com.instagram.android";
    public static final String FACE_BOOK = "com.facebook.katana";
    public static final String MESSENGER = "com.facebook.orca";
    public static final String WHATS_APP = "com.whatsapp";
    public static final String GMAIL = "com.google.android.gm";
    public static final String GOOGLE_MAP = "com.google.android.apps.maps";
    public static final String ALLO = "com.google.android.apps.fireball";

    /**
     * description:国内热门App
     **/
    public static final String MEITUAN_WAIMAI = "com.sankuai.meituan.takeoutnew";
    public static final String E_LE_ME = "me.ele";
    public static final String MO_BAI = "com.mobike.mobikeapp";
    public static final String OFO = "so.ofo.labofo";
    public static final String JIN_RI_TOU_TIAO = "com.ss.android.article.news";
    public static final String SINA_WEI_BO = "com.sina.weibo";
    public static final String WANG_YI_XIN_WEN = "com.netease.newsreader.activity";
    public static final String KUAI_SHOU = "com.smile.gifmaker";
    public static final String ZHI_HU = "com.zhihu.android";
    public static final String HU_YA_ZHI_BO = "com.duowan.kiwi";
    public static final String YING_KE_ZHI_BO = "com.meelive.ingkee";
    public static final String MIAO_PAI = "com.yixia.videoeditor";
    public static final String MEI_TU_XIU_XIU = "com.mt.mtxx.mtxx";
    public static final String MEI_YAN_XIANG_JI = "com.meitu.meiyancamera";
    public static final String XIE_CHENG = "ctrip.android.view";
    public static final String MO_MO = "com.immomo.momo";
    public static final String YOU_KU = "com.youku.phone";
    public static final String AI_QI_YI = "com.qiyi.video";
    public static final String DI_DI = "com.sdu.didi.psnger";
    public static final String ZHI_FU_BAO = "com.eg.android.AlipayGphone";
    public static final String TAO_BAO = "com.taobao.taobao";
    public static final String JING_DONG = "com.jingdong.app.mall";
    public static final String DA_ZONG_DIAN_PING = "com.dianping.v1";
    public static final String JIAN_SHU = "com.jianshu.haruki";
    public static final String BAI_DU_DI_TU = "com.baidu.BaiduMap";
    public static final String GAO_DE_DI_TU = "com.autonavi.minimap";
    public static final String WEI_XIN = "com.tencent.mm";
    public static final String QQ = "com.tencent.mobileqq";

    public static boolean isAppInstalled(Context context, String uri) {
        PackageManager pm = context.getPackageManager();
        boolean installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_META_DATA);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }

    public static String getAppPackage() {
        @SuppressLint("ServiceCast")
        ActivityManager activityManager = (ActivityManager) ApplicationUtils.getApplication().getSystemService(Activity.ACTIVITY_SERVICE);
        return activityManager.getCurrentActivity().getPackageName();
    }
}
