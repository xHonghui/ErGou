package com.laka.ergou.mvp.share.model.bean

import com.laka.ergou.mvp.shop.model.bean.SmallImages
import com.laka.ergou.mvp.shop.model.bean.WechatShareBean
import java.io.Serializable

/**
 * @Author:summer
 * @Date:2019/5/27
 * @Description:
 */
class ShareResponse(val share: WechatShareBean = WechatShareBean(), val smallImages: SmallImages = SmallImages()) : Serializable