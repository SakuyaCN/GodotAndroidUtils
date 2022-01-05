package com.sakuya.godot.entity

//设备信息
data class PhoneEntity(
    var signatures:String,
    var versionName:String,
    var versionCode:Int,
    var deviceName:String,
    var osName:String,
    var packageName:String,
    var screenWidth:Int,
    var screenHeight:Int,
    var screenDensity:Float,
    var screenDensityDpi:Float
)

//TapLogin
data class TapEntity(
    var userId :String,
    var avatar :String,
    var nickName :String,
)