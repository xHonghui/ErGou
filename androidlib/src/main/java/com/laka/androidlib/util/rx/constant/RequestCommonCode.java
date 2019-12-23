package com.laka.androidlib.util.rx.constant;

/**
 * @Author:summer
 * @Date:2019/1/15
 * @Description:
 */
public interface RequestCommonCode {
    int LK_OK = 0;  // 成功

    int LK_ERROR_REDIS_CONNECT_FATLED = 1; // 链接失败
    int LK_ERROR_MYSQL_CONNECT_FATLED = 2; // mysql 链接失败
    int LK_ERROR_MYSQL_QUERY_FATLED = 3;  // mysql 查询失败
    String LOADING_ERROR_MSG = "加载失败";

    int LK_NOT_LOGIN = 4;  // 用户未登录
    String USER_NOT_LOGIN_MSG = "用户未登录";

    int LK_WRONG_USER_TOKEN = 5;  // 错误的token
    String USER_TOKEN_INVALID_MSG = "用户登录信息过期，请重新登录";

    int LK_ERROR_SOURCH_EMPTRY = 6; // 搜索为空
    String EMPTRY_DATA_MSG = "数据为空";

    int LK_ERROR_JSON_FORMAT = 7; // json 解析异常
    //String DATA_FORMAT_EXCEPTION_MSG = "数据解析异常";
    String DATA_FORMAT_EXCEPTION_MSG = "数据解析异常";

    int LK_REQUEST_TIMEOUT = 8; // 请求超时
    String REQUEST_TIMEOUT_MSG = "请求超时";

    int LK_ERROR_OTHER = 9;  // 其他异常
    String REQUEST_FAIL = "请求失败";

    int LK_PRODUCT_DETAIL_NO_DATA = 99; //商品详情无数据或者商品已下架
    String PRODUCT_DETAIL_NO_DATA_ERROR = "此商品不存在或已下架";
}
