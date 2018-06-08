package com.junleizg.services.smsreader

import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface API {
    /**
     * POST
     * http://admin.junleizg.com.cn/common/analyseSms
     * mobile=18900000&content=xxxxx&time=xxxxx
     */
    @FormUrlEncoded
    @POST("/common/analyseSms")
    fun postSMS(@Field("mobile")number: String, @Field("content")body: String,@Field("time")time: String): Observable<Any>
}