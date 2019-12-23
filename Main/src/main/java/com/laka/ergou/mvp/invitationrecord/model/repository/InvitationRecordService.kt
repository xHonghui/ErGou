package com.laka.ergou.mvp.invitationrecord.model.repository

import com.laka.ergou.mvp.invitationrecord.constant.InvitationRecordConstant
import com.laka.ergou.mvp.invitationrecord.model.bean.InvitationRecordResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.QueryMap

/**
 * @Author:summer
 * @Date:2019/3/13
 * @Description:
 */
interface InvitationRecordService {

    @GET(InvitationRecordConstant.LOAD_INVITATION_RECORD_URL)
    fun onLoadInvitationRecoedData(@QueryMap params: HashMap<String, String>): Observable<InvitationRecordResponse>
}