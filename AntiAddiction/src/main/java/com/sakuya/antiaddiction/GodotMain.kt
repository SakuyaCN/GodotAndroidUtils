package com.sakuya.antiaddiction

import android.util.Log
import com.google.gson.Gson
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.GodotPlugin
import org.godotengine.godot.plugin.SignalInfo
import com.tapsdk.antiaddictionui.AntiAddictionUIKit
import com.tapsdk.antiaddiction.config.AntiAddictionFunctionConfig
import com.tapsdk.antiaddiction.constants.Constants
import com.tapsdk.antiaddiction.entities.response.CheckPayResult
import com.tapsdk.antiaddictionui.Callback
import com.tapsdk.antiaddiction.entities.response.SubmitPayResult
import org.godotengine.godot.FullScreenGodotApp

class GodotMain(godot: Godot) : GodotPlugin(godot) {

    override fun getPluginName(): String {
        Log.e("GodotAntiaddiction","初始化成功")
        return "GodotAntiaddiction"
    }

    override fun getPluginMethods(): MutableList<String> {
        return mutableListOf("init","startup","logout","checkPayLimit","submitPayResult","gameTimer")
    }

    override fun getPluginSignals(): MutableSet<SignalInfo> {
        return mutableSetOf(SignalInfo("InitResult",Integer::class.java,String::class.java),
            SignalInfo("CheckPayLimitResult",Integer::class.java,String::class.java))
    }

    fun init(gameIdentifier :String,enablePaymentLimit:Boolean = true,enableOnLineTimeLimit:Boolean = true,showSwitchAccount:Boolean= true){
        // Android SDK 的各接口第一个参数是当前 Activity，以下不再说明
        val config = AntiAddictionFunctionConfig.Builder()
            .enablePaymentLimit(enablePaymentLimit) // 是否启用消费限制功能
            .enableOnLineTimeLimit(enableOnLineTimeLimit) // 是否启用时长限制功能
            .showSwitchAccount(showSwitchAccount) // 是否显示切换账号按钮
            .build()
        AntiAddictionUIKit.init(
            activity, gameIdentifier, config
        ) { code, extras ->
            // 根据 code 不同提示玩家不同信息，详见下面的说明
            emitSignal("InitResult",code, Gson().toJson(extras))
            if (code == Constants.ANTI_ADDICTION_CALLBACK_CODE.LOGIN_SUCCESS) {
                // 开始计时
                AntiAddictionUIKit.enterGame()
            }
        }
    }

    fun startup(userIdentifier :String,useTapLogin :Boolean,tapTapAccessToken:String = ""){
        AntiAddictionUIKit.startup(activity, useTapLogin, userIdentifier, tapTapAccessToken)
    }

    fun logout(){
        AntiAddictionUIKit.logout()
    }

    fun checkPayLimit(amount:Int){
        AntiAddictionUIKit.checkPayLimit(activity, amount.toLong(),
            object : Callback<CheckPayResult?> {
                override fun onSuccess(result: CheckPayResult?) {
                    if (!result!!.status) {
                        emitSignal("CheckPayLimitResult",Gson().toJson(result))
                    }else{
                        emitSignal("CheckPayLimitResult","")
                    }
                }

                override fun onError(p0: Throwable?) {
                    emitSignal("CheckPayLimitResult","")
                }

            }
        )
    }

    fun submitPayResult(amount:Int){
        AntiAddictionUIKit.submitPayResult(amount.toLong(),
            object : Callback<SubmitPayResult?>,
                com.tapsdk.antiaddiction.Callback<SubmitPayResult> {
                override fun onSuccess(p0: SubmitPayResult?) {

                }

                override fun onError(p0: Throwable?) {

                }

            }
        )
    }

    fun gameTimer(start:Boolean){
        if (start)
            AntiAddictionUIKit.enterGame()
        else
            AntiAddictionUIKit.leaveGame()
    }
}