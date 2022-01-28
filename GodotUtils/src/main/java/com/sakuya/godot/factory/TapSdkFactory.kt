package com.sakuya.godot.factory

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.sakuya.godot.utils.DeviceInfoUtils
import com.tapsdk.moment.TapMoment
import com.tapsdk.moment.TapMoment.TapMomentCallback
import org.godotengine.godot.plugin.SignalInfo
import com.taptap.sdk.*
import com.taptap.sdk.TapLoginHelper.TapLoginResultCallback
import com.taptap.sdk.net.Api
import com.tds.tapdb.sdk.TapDB

class TapSdkFactory {

    private val TAG: String = "TapSdk"

    fun create(activity:Activity,client_id:String,your_client_token:String,web_url:String = "y18canec.cloud.tds1.tapapis.cn"){
        Log.e("TapFactory","client_id :${client_id} token : $your_client_token")
        if(!Factory.isTapInit){
            TapLoginHelper.init(activity, client_id)
            TapMoment.init(activity, client_id)
            TapDB.init(activity, client_id, "taptap", DeviceInfoUtils.getVersionName(activity), true)
            TapLoginHelper.registerLoginCallback(loginCallback)
            TapMoment.setCallback { code, msg ->
                Factory.emitInterface?.onEmitOtherListener("OnTapMomentCallBack",code,msg)
            }
            Factory.isTapInit = true
        }else{
            activity.runOnUiThread { Toast.makeText(activity,"已初始化SDK",Toast.LENGTH_SHORT).show() }
        }
    }

    //TapTap登录
    private fun tapLogin(activity: Activity){
        if(!Factory.isTapInit){
            activity.runOnUiThread { Toast.makeText(activity,"未初始化SDK",Toast.LENGTH_SHORT).show() }
            return
        }
        TapLoginHelper.startTapLogin(activity, TapLoginHelper.SCOPE_PUBLIC_PROFILE)
    }

    //登出
    private fun tapLogout(activity: Activity){
        if(!Factory.isTapInit){
            activity.runOnUiThread { Toast.makeText(activity,"未初始化SDK",Toast.LENGTH_SHORT).show() }
            return
        }
        TapLoginHelper.logout()
    }

    //获取Token
    private fun tapAccessToken(activity: Activity){
        if(!Factory.isTapInit){
            activity.runOnUiThread { Toast.makeText(activity,"未初始化SDK",Toast.LENGTH_SHORT).show() }
            return
        }
        if (TapLoginHelper.getCurrentAccessToken() != null)
            Factory.emitInterface?.onEmitListener("OnTapLoginAccessToken",TapLoginHelper.getCurrentAccessToken().toJsonString())
        else
            Factory.emitInterface?.onEmitListener("OnTapLoginAccessToken","")
    }

    //TapTap篝火计划资格
    private fun getTestQualification(activity: Activity){
        if(!Factory.isTapInit){
            activity.runOnUiThread { Toast.makeText(activity,"未初始化SDK",Toast.LENGTH_SHORT).show() }
            return
        }
        TapLoginHelper.getTestQualification(object : Api.ApiCallback<Boolean>{
            override fun onSuccess(p0: Boolean?) {
                Factory.emitInterface?.onEmitListener("OnTestQualificationResult",p0)
            }

            override fun onError(p0: Throwable?) {
                Factory.emitInterface?.onEmitListener("OnTestQualificationResult",false)
            }

        })
    }

    //打开动态页面
    private fun tapOpenMoment(activity: Activity){
        if(!Factory.isTapInit){
            activity.runOnUiThread { Toast.makeText(activity,"未初始化SDK",Toast.LENGTH_SHORT).show() }
            return
        }
        TapMoment.open(TapMoment.ORIENTATION_SENSOR)
    }

    var loginCallback: TapLoginResultCallback = object : TapLoginResultCallback {
        override fun onLoginSuccess(token: AccessToken) {
            val profile: Profile = TapLoginHelper.getCurrentProfile()
            Factory.emitInterface?.onEmitListener("OnTapLoginResult",profile.toJsonString())
            Log.d(TAG, "TapTap authorization succeed :"+ profile.toJsonString())
        }

        override fun onLoginCancel() {
            Factory.emitInterface?.onEmitListener("OnTapLoginResult","")
            Log.d(TAG, "TapTap authorization cancelled")
        }

        override fun onLoginError(globalError: AccountGlobalError) {
            Factory.emitInterface?.onEmitListener("OnTapLoginResult","")
            Log.d(TAG, "TapTap authorization failed. cause: " + globalError.message)
        }
    }

    fun getPluginMethods():HashMap<String,String>{
        return hashMapOf(
            this::tapLogin.name to this.javaClass.name,
            this::tapLogout.name to this.javaClass.name,
            this::tapAccessToken.name to this.javaClass.name,
            this::tapOpenMoment.name to this.javaClass.name,
            this::getTestQualification.name to this.javaClass.name)
    }

    fun getPluginSignals():MutableSet<SignalInfo> {

        return mutableSetOf(
            SignalInfo("OnTestQualificationResult",Boolean::class.java),
            SignalInfo("OnTapLoginResult",String::class.java),
            SignalInfo("OnTapLoginAccessToken",String::class.java),
            SignalInfo("OnTapMomentCallBack",Integer::class.java,String::class.java))
    }
}