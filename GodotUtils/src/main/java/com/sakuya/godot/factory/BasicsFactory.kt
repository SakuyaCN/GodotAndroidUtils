package com.sakuya.godot.factory

import com.google.gson.Gson
import com.sakuya.godot.SignCheck.getSignString
import com.sakuya.godot.entity.PhoneEntity
import com.sakuya.godot.utils.DeviceInfoUtils
import org.godotengine.godot.plugin.SignalInfo

class BasicsFactory {

    private fun getAndroidPhoneInfo(){
        Factory.godotPlugin?.let {
            var result = Gson().toJson(PhoneEntity(
                signatures = getSignString(it.context!!)?:"",
                versionCode = DeviceInfoUtils.getVersionCode(it.context!!),
                versionName = DeviceInfoUtils.getVersionName(it.context!!),
                deviceName = DeviceInfoUtils.deviceName,
                osName = DeviceInfoUtils.osName,
                screenWidth = DeviceInfoUtils.getScreenWidth(it.context!!),
                screenHeight = DeviceInfoUtils.getScreenHeight(it.context!!),
                screenDensity = DeviceInfoUtils.getScreenDensity(it.context!!),
                screenDensityDpi = DeviceInfoUtils.getScreenDensityDpi(it.context!!),
                packageName = DeviceInfoUtils.getPackageName(it.context!!)
            ))
            Factory.emitInterface?.onEmitListener("AndroidPhoneInfo",result)
        }
    }

    fun getPluginMethods():HashMap<String,String>{
        return hashMapOf(
            this::getAndroidPhoneInfo.name to this.javaClass.name)
    }

    fun getPluginSignals():MutableSet<SignalInfo> {
        return mutableSetOf(SignalInfo("AndroidPhoneInfo",String::class.java))
    }
}