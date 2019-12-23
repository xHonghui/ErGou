package com.laka.ergou.mvp.user.contract

import android.app.Activity
import com.laka.androidlib.mvp.IBaseLoadingView
import com.laka.androidlib.mvp.IBaseModel
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.net.response.BaseResponse
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.ergou.mvp.login.model.bean.UserBean
import com.laka.ergou.mvp.login.model.bean.UserInfoBean
import com.laka.ergou.mvp.user.model.bean.CommonData
import com.laka.ergou.mvp.user.model.bean.RelationResponse
import com.laka.ergou.mvp.user.model.bean.StsTokenBean
import com.laka.ergou.mvp.user.model.bean.UrlResponse
import io.reactivex.Observable
import org.json.JSONObject

/**
 * @Author:summer
 * @Date:2019/1/10
 * @Description: 用户基本信息修改契约类
 */
interface IUserInfoContract {

    interface IUserInfoView : IBaseLoadingView<CommonData> {
        fun onUploadUserAvatarSuccess(resultBean: CommonData)
        fun onGetStsTokenSuccess(resultBean: StsTokenBean)
        fun unbindTaoBaoAccountCallBack(isSuccess: Boolean)
        fun authorInvalid()
        fun onLogoutSuccess()
        fun operateWxAccount(isBind: Boolean, isSuccess: Boolean)
        fun onTaoBaoAuthorSuccess()
        fun getUnionCodeUrlSuccess(url: String)
        fun handleUnionCodeSuccess()
    }

    interface IUserInfoPresenter : IBasePresenter<IUserInfoView> {
        fun onUploadUserAvatar(avatar: String)
        fun onGetStsToken()
        fun onLogout(activity: Activity)
        fun unbindTaoBaoAccount()
        fun onLoadUserInfo(userId: String)
        fun bindWxAccount(wxAuthorizedCode: String)
        fun unbindWxAccount()
        fun onTaoBaoAuthor(activity: Activity)
        fun getUnionCodeUrl()
        fun handleUnionCode(code: String, state: String)
    }

    interface IUserInfoModel : IBaseModel<IUserInfoView> {
        fun onUploadUserAvatar(path:String, callback: ResponseCallBack<CommonData>)
        fun onGetStsToken(params: HashMap<String, String?>, callback: ResponseCallBack<StsTokenBean>)
        fun onLoadUserInfo(params: HashMap<String, String?>, callback: ResponseCallBack<UserBean>)
        fun unbindTaoBaoAccount(): Observable<JSONObject>
        fun updateUserOpenChatStatus(isOpenGouXiaoEr: Boolean): Observable<CommonData>
        fun bindWxAccount(wxAuthorizedCode: String): Observable<UserBean>
        fun unbindWxAccount(): Observable<JSONObject>
        fun getUnionCodeUrl(params: HashMap<String, String>, callback: ResponseCallBack<UrlResponse>)
        fun handleUnionCode(params: HashMap<String, String>, callback: ResponseCallBack<RelationResponse>)
    }
}