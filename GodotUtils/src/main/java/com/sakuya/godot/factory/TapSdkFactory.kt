package com.sakuya.godot.factory

import android.app.Activity
import android.util.Log
import com.tapsdk.bootstrap.TapBootstrap
import com.tds.common.models.TapRegionType
import com.tds.common.entities.TapConfig
import org.godotengine.godot.plugin.SignalInfo
import android.widget.Toast
import com.google.gson.Gson
import com.sakuya.godot.entity.TapEntity
import com.tapsdk.bootstrap.Callback

import com.tapsdk.bootstrap.exceptions.TapError

import com.tapsdk.bootstrap.account.TDSUser
import com.tapsdk.bootstrap.account.TDSUser.loginWithTapTap
import com.taptap.sdk.*
import java.lang.ref.WeakReference
import com.taptap.sdk.TapLoginHelper.TapLoginResultCallback


class TapSdkFactory {

    private val TAG: String = "TapSdk"
    private var activity:WeakReference<Activity> ?= null

    fun create(context: Activity,client_id:String,your_client_token:String){
        activity = WeakReference(context)
        TapLoginHelper.init(context, client_id)
        val config = LoginSdkConfig();
        config.regionType = RegionType.CN
        TapLoginHelper.init(context, client_id, config)
        TapLoginHelper.registerLoginCallback(loginCallback)
    }

    //TapTap登录
    private fun tapLogin(){
        if (activity == null){
            Log.e("TapFactory","未初始化TapSdk")
            return
        }
        TapLoginHelper.startTapLogin(activity!!.get(), TapLoginHelper.SCOPE_PUBLIC_PROFILE)
    }

    var loginCallback: TapLoginResultCallback = object : TapLoginResultCallback {
        override fun onLoginSuccess(token: AccessToken) {
            Log.d(TAG, "TapTap authorization succeed")
            // 开发者调用 TapLoginHelper.getCurrentProfile() 可以获得当前用户的一些基本信息，例如名称、头像。
            val profile: Profile = TapLoginHelper.getCurrentProfile()
        }

        override fun onLoginCancel() {
            Log.d(TAG, "TapTap authorization cancelled")
        }

        override fun onLoginError(globalError: AccountGlobalError) {
            Log.d(TAG, "TapTap authorization failed. cause: " + globalError.message)
        }
    }

    fun getPluginMethods():HashMap<String,String>{
        return hashMapOf(
            this::tapLogin.name to this.javaClass.name)
    }

    fun getPluginSignals():MutableSet<SignalInfo> {
        return mutableSetOf(SignalInfo("OnTapLoginResult",String::class.java))
    }
}