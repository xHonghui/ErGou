package com.laka.ergou.mvp.chat.constant

/**
 * @Author:Rayman
 * @Date:2019/2/13
 * @Description:购小二模块API
 */
object ChatApiConstant {


    /**
     * description:Socket连接相关code
     **/
    // Socket数据处理成功
    const val SOCKET_RESPONSE_OK = 0
    // 解析数据错误
    const val SOCKET_RESPONSE_FAIL = Integer.MIN_VALUE
    // 服务器RSA解密失败
    const val IM_E_RSA_PRIVATE_DECRYPT_FAILED = 1
    // 协议protoBuf反序列化失败
    const val IM_E_PROTOCAL_SERIALIZE_FAILED = 2
    // 登录用户Token失效
    const val IM_E_INVALID_USER_TOKEN = 3
    // 用户不存在
    const val IM_E_USER_NOT_EXIST = 4
    // 发送空消息
    const val IM_E_EMPTY_MESSAGE = 5
    // 用户被禁言
    const val IM_E_USER_FORBIDDEN = 6
    // 无效的请求数据
    const val IM_E_INVALAD_REQUEST_DATA = 7
    // 请稍后再试
    const val IM_E_TRY_AGAIN_LATER = 8
    // 用户没有认证
    const val IM_E_USER_NOT_VERIFY = 9

    /**
     * description:Socket请求指令
     **/
    // 保持连接
    const val IM_REQ_KEEP_ALIVE = 0x10A0
    const val IM_RSP_KEEP_ALIVE = 0x10A1

    // 传输AES_KEY
    const val IM_REQ_AES_KEY = 0x10A2
    const val IM_RSP_AES_KEY = 0x10A3

    // AesKey回调
    const val IM_RSP_AES_KEY_FAILED = 0x10EE
    const val IM_RSP_SERIALIZE_FAILED = 0x10EF

    // 批量广播通知
    // 滚动广播通知
    const val IM_REQ_BROADCAST_MSG = 0x1000
    const val IM_RSP_BROADCAST_MSG = 0x1001

    // 获取滚动广播列表信息
    const val IM_REQ_GET_BROADCAST_MSG = 0x1016
    const val IM_RSP_GET_BROADCAST_MSG = 0x1017

    // 单播
    const val IM_REQ_UNICAST_SAY = 0x1002
    const val IM_RSP_UNICAST_SAY = 0x1003

    // 分配用户对象(获取机器人)
    const val IM_REQ_ASSIGN_TARGET = 0x1008
    const val IM_RSP_ASSIGN_TARGET = 0x1009

    // 私信功能
    // 发言
    const val IM_REQ_SEND_MESSAGE = 0x1010
    const val IM_RSP_SEND_MESSAGE = 0x1011

    // 获取用户信息
    const val IM_REQ_GET_USER_INFO = 0x1012
    const val IM_RSP_GET_USER_INFO = 0x1013

    // 获取用户离线消息
    const val IM_REQ_GET_OFFLINE_MESSAGE = 0x1014
    const val IM_RSP_GET_OFFLINE_MESSAGE = 0x1015

    // 获取新用户提示消息
    const val IM_REQ_GET_USERTIPS_MSG = 0x1018
    const val IM_RSP_GET_USERTIPS_MSG = 0x1019

    // 推送离线消息
    const val IM_REQ_PUSH_OFFLINE_MESSAGE = 0x103E
    const val IM_RSP_PUSH_OFFLINE_MESSAGE = 0x103F

    /**
     * description:Socket连接常量
     **/
    // 错误时间
    const val ERROR_RECONNECT = 1000
    const val ERROR_SERVER_DISCONNECT = 1001
    const val ERROR_READ_TIMEOUT = 1002
    const val ERROR_WRITE_TIMEOUT = 1003

    // 超时间隔
    const val CONNECT_TIMEOUT = 30000
    const val RECONNECT_TIME = 1000 * 3//10000
    const val READ_TIMEOUT = 90000
    const val WRITE_TIMEOUT = 10000

    /**
     * description:RSA公钥
     **/
    const val SOCKET_RSA_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDiSVR3BtCagDzdneC4TS+1WHVF\n" +
            "Uw6ViRm3j2JMmlbq/u/hYoDNHP0Oeaz8Z3NXtu1qf6L1sZFYCACze6fSKJiTEnzS\n" +
            "4abo/MiNl9GsT0gCgpnJBLfDWhscLIswNwEdczWnPWQ4RwIIihK82p6WoKcHLLhq\n" +
            "vXuoD7XI0skf6+bGKwIDAQAB"
}