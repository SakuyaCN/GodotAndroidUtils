package com.sakuya.godot.factory

import android.app.Activity
import com.google.gson.Gson
import com.sakuya.godot.SignCheck.getSignString
import com.sakuya.godot.entity.PhoneEntity
import com.sakuya.godot.utils.DeviceInfoUtils
import org.godotengine.godot.plugin.SignalInfo
import android.content.Intent

import android.os.Build
import android.provider.MediaStore
import com.sakuya.godot.factory.Factory.ALBUM_CODE


class BasicsFactory {

    private fun getAndroidPhoneInfo(activity: Activity){
        Factory.activity?.let {
            var result = Gson().toJson(PhoneEntity(
                signatures = getSignString(it.get()!!)?:"",
                versionCode = DeviceInfoUtils.getVersionCode(it.get()!!),
                versionName = DeviceInfoUtils.getVersionName(it.get()!!),
                deviceName = DeviceInfoUtils.deviceName,
                osName = DeviceInfoUtils.osName,
                screenWidth = DeviceInfoUtils.getScreenWidth(it.get()!!),
                screenHeight = DeviceInfoUtils.getScreenHeight(it.get()!!),
                screenDensity = DeviceInfoUtils.getScreenDensity(it.get()!!),
                screenDensityDpi = DeviceInfoUtils.getScreenDensityDpi(it.get()!!),
                packageName = DeviceInfoUtils.getPackageName(it.get()!!)
            ))
            Factory.emitInterface?.onEmitListener("AndroidPhoneInfo",result)
        }
    }

    private fun openAlbum(activity: Activity){
        val intent = if (Build.VERSION.SDK_INT >= 30) { // Android 11 (API level 30)
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        } else {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            Intent.createChooser(intent, null)
        }
        activity.startActivityForResult(intent, ALBUM_CODE)
    }

    fun getPluginMethods():HashMap<String,String>{
        return hashMapOf(
            this::getAndroidPhoneInfo.name to this.javaClass.name,
            this::openAlbum.name to this.javaClass.name
            )
    }

    fun getPluginSignals():MutableSet<SignalInfo> {
        return mutableSetOf(SignalInfo("AndroidPhoneInfo",String::class.java),
            SignalInfo("OnAlbumResult",String::class.java))
    }
}