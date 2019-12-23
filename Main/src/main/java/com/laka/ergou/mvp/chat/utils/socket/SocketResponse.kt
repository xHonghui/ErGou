package com.laka.ergou.mvp.chat.utils.socket

import com.laka.ergou.mvp.chat.constant.ChatApiConstant

/**
 * @Author:Rayman
 * @Date:2019/2/18
 * @Description:【使用AES KEY加密的 4Bytes状态码 + 4Bytes ProtoBuf length + ProtoBuf 数据】 .
 */
class SocketResponse {

    var code = ChatApiConstant.SOCKET_RESPONSE_FAIL
    var dataLength = -1
    var errorMsg = ""
    var protoBufData = ByteArray(0)


}
