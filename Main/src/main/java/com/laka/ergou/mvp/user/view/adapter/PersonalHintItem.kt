package com.laka.ergou.mvp.user.view.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.laka.androidlib.base.adapter.MultipleAdapterItem
import com.laka.androidlib.eventbus.EventBusManager
import com.laka.androidlib.widget.SelectorButton
import com.laka.ergou.R
import com.laka.ergou.mvp.login.LoginModuleNavigator
import com.laka.ergou.mvp.login.model.event.UserEvent
import com.laka.ergou.mvp.user.constant.UserConstant
import com.laka.ergou.mvp.user.model.bean.PersonalHintBean

/**
 * @Author:Rayman
 * @Date:2019/3/12
 * @Description:提示Hint
 */
class PersonalHintItem : MultipleAdapterItem<PersonalHintBean> {
    override fun convert(helper: BaseViewHolder?, item: PersonalHintBean?) {
        helper?.getView<SelectorButton>(R.id.btn_prevent_lost_order)
                ?.setOnClickListener {
                    //LoginModuleNavigator.startTaoBaoAuthorActivity(it.context)
                    EventBusManager.postEvent(UserEvent(UserConstant.TAOBAO_AUTHOR_EVENT))
                }
    }
}