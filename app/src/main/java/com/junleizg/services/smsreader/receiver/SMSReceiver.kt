package com.junleizg.services.smsreader.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Looper
import android.telephony.SmsMessage
import com.junleizg.services.smsreader.API
import com.junleizg.services.smsreader.MainActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class SMSReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        //pdus短信单位pdu
        //解析短信内容
        val pdus = intent.extras.get("pdus") as Array<Any>
        for (pdu in pdus) {
            //封装短信参数的对象
            val sms = SmsMessage.createFromPdu(pdu as ByteArray)
            val number = sms.originatingAddress
            val body = sms.messageBody
            //处理逻辑
            println("number = ${number}")
            println("body = ${body}")
//            context.startActivity(
//                    Intent(context, MainActivity::class.java).putExtra("body", body)
//                            .putExtra("number", number)
//                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            )
            if (body.isNotEmpty() && number.isNotEmpty()) {
                //tv_msg.text = "received msg:$number:$body"

                val retrofit = Retrofit.Builder().baseUrl("http://admin.junleizg.com.cn")
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build()
                retrofit.create(API::class.java)
                        .postSMS(number, body, System.currentTimeMillis().toString())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.from(Looper.getMainLooper()))
                        .subscribe(
                                {
                                    println("result = ${it}")
                                    //tv_msg.text = "received msg:$number:$body \n upload success"
                                },
                                {
                                    println("error = ${it.printStackTrace()}")
                                    //tv_msg.text = "received msg:$number:$body \n upload failed \n ${it.localizedMessage}"
                                }
                        )

            }
        }
    }
}