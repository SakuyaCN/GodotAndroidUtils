package com.sakuya.godot.utils

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import java.util.*

//设备信息
object DeviceInfoUtils {

    fun getNetworkCountryIso(context: Context): String {
        return try {
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            tm.networkCountryIso
        } catch (e: Exception) {
            ""
        }
    }

    fun getSimCountryIso(context: Context): String {
        return try {
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            tm.simCountryIso
        } catch (e: Exception) {
            ""
        }
    }

    fun getNetworkOperator(context: Context): String {
        return try {
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            tm.networkOperator
        } catch (e: Exception) {
            ""
        }
    }

    fun getSimOperator(context: Context): String {
        return try {
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            tm.simOperator
        } catch (e: Exception) {
            ""
        }
    }

    val deviceName: String
        get() = Build.DEVICE
    val osName: String
        get() = Build.MODEL + "," + Build.VERSION.SDK_INT + "," + Build.VERSION.RELEASE

    fun getVersionName(context: Context): String {
        val packInfo = getPackageInfo(context)
        var name = ""
        if (packInfo != null) {
            name = packInfo.versionName
        }
        return name
    }

    fun getVersionCode(context: Context): Int {
        val packInfo = getPackageInfo(context)
        var code = 0
        if (packInfo != null) {
            code = packInfo.versionCode
        }
        return code
    }

    fun getPackageName(context: Context): String {
        return context.packageName
    }

    fun getPackageInfo(context: Context): PackageInfo? {
        val packageManager = context.packageManager
        var packInfo: PackageInfo? = null
        try {
            packInfo = packageManager.getPackageInfo(context.packageName, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return packInfo
    }

    fun getScreenWidth(context: Context): Int {
        val dm = context.resources.displayMetrics
        return dm.widthPixels
    }

    fun getScreenHeight(context: Context): Int {
        val dm = context.resources.displayMetrics
        return dm.heightPixels
    }

    fun getScreenDensity(context: Context): Float {
        val dm = context.resources.displayMetrics
        return dm.density
    }

    fun getScreenDensityDpi(context: Context): Float {
        val dm = context.resources.displayMetrics
        return dm.densityDpi.toFloat()
    }

    fun getScreenScaledDensity(context: Context): Float {
        val dm = context.resources.displayMetrics
        return dm.scaledDensity
    }

    fun getMetaData(context: Context?, key: String?): String? {
        var metaData: Bundle? = null
        var value: String? = null
        if (context == null || key == null) {
            return null
        }
        try {
            val ai = context.packageManager.getApplicationInfo(
                context.packageName, PackageManager.GET_META_DATA
            )
            if (null != ai) {
                metaData = ai.metaData
            }
            if (null != metaData) {
                value = metaData.getString(key)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return value
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    fun px2dip(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     * @param context
     * @param pxValue
     * @return
     */
    fun px2sp(context: Context, pxValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     * @param context
     * @param spValue
     * @return
     */
    fun sp2px(context: Context, spValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    /**.
     * 获取状态栏高度
     * @param context
     * @return
     */
    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    fun getNavBarHeight(context: Context): Int {
        var result = 0
        val resourceId =
            context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelOffset(resourceId)
        }
        return result
    }

    val timeZone: String
        get() {
            val timeZone = TimeZone.getDefault()
            return if (timeZone != null) {
                (timeZone.rawOffset / 1000).toString()
            } else ""
        }

    /**
     * 获取UserAgent,并将非法字符排除
     * @return
     */
    val userAgent: String
        get() {
            val ua = System.getProperties().getProperty("http.agent")
            var i = 0
            val length = ua.length
            while (i < length) {
                var c = ua[i]
                if (c <= '\u001f' || c >= '\u007f') {
                    c = ' '
                }
                i++
            }
            return ua
        }
}