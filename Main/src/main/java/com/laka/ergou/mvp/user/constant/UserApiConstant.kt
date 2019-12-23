package com.laka.ergou.mvp.user.constant

import com.laka.ergou.BuildConfig

/**
 * @Author:Rayman
 * @Date:2019/1/18
 * @Description:用户模块接口常量类
 */
object UserApiConstant {

    /**
     * description:与前端交互的常量配置
     **/
    const val JS_ACTION = "action"
    const val JS_ACTION_SHARE = "share"
    const val JS_ACTION_POSTER = "create_poster"
    const val JS_ACTION_WINNER = "share_winner"
    const val JS_ACTION_DETAIL = "goods_detail"
    const val JS_ACTION_MINE_COMMISSION = "mine_commission"
    const val JS_PARAMETERS = "parameters"
    const val JS_ITEM_ID = "item_id"

    /**
     * description:接口配置
     **/
    // const val SHOPPING_BASE_HOST = "http://ergou-api.lakatv.com"
    const val SHOPPING_BASE_HOST = BuildConfig.ERGOU_BASE_HOST

    // 获取用户信息
    const val API_GET_USER_INFO = "user/show"

    // 编辑用户信息
    const val API_EDIT_USER_INFO = "user/edit"

    // 获取sts token
    const val API_GET_STS_TOKEN = "user/get-sts-token"

    // 获取用户信息Banner
    const val API_GET_USER_BANNER_ADV = "system/banner"

    // 获取用户Robot列表
    const val API_USER_GET_ROBOT_LIST = "wxrobot/index"

    // 更换手机号
    const val API_CHANGE_USER_PHONE = "user/instead-phone"

    // 绑定淘宝账号
    const val API_BIND_TAO_BAO_ACCOUNT = "user/taobao-user/store"

    // 解除淘宝账号绑定
    const val API_UNBIND_TAO_BAO_ACCOUNT = "taobaouser/delete-taobao-user"

    // 绑定微信账号
    const val API_BIND_WE_CHAT_ACCOUNT = "user/wx-authorize"

    // 解绑微信号
    const val API_UNBIND_WE_CHAT_ACCOUNT = "user/wx-unbind"

    // 添加Robot
    const val USER_GET_ADD_ROBOT = ""

    // 绑定订单号
    const val USER_BIND_ORDER = "order/order-binding"

    // 获取联盟授权url
    const val API_GET_UNION_CODE_URL = "user/get-union-code-url"

    // 处理联盟授权code
    const val API_HANDLE_UNION_CODE_URL = "user/handle-union-code"

    // 分享海报
    const val API_GET_SHARE_POSTER_URL = "user/poster"

    //获取商品详情服务器地址
    const val API_GET_PRODUCT_DETAIL_SERVICE_URL = "taobaoke/product-detail-service"

    //其他奖励
    const val API_GET_OTHER_REWARD = "order/other-reward"

    /**
     * description:请求参数
     **/
    const val USER_TOKEN = "token"
    const val ORDER_ID = "order_id"
    const val USER_PHONE = "phone"
    const val VERIFY_CODE = "code"
    const val PARAM_PAGE = "page_no"
    const val PARAM_PAGE_SIZE = "page_size"
    const val PARAM_DEFAULT_PAGE_SIZE = 20
    const val VERIFY_CODE_TYPE = "type"
    const val TAOBAO_AVATART = "avatarUrl"
    const val TAOBAO_OPEN_ID = "openId"
    const val TAOBAO_OPEN_SID = "openSid"
    const val TAOBAO_ACCSEE_TOKEN = "topAccessToken"
    const val TAOBAO_AUTH_CODE = "topAuthCode"
    const val ALI_REAL_NAME = "alipay_realname"
    const val ALI_ACCOUNT = "alipay_username"
    const val JPUSH_REGISTER_ID: String = "jpush_id"
    val JPUSH_REGISTER_CHANNEL: String = "channel"


    /**
     * description:响应体数据常量
     **/
    const val IMAGE_PATH = "img_path"
    const val SCENE_ID = "scene_id"
    const val SCENE_VALUE = "scene_value"
    const val SCENE_EXTRA = "scene_extra"
}