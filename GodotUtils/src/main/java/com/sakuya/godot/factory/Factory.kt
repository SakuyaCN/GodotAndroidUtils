package com.sakuya.godot.factory

import android.app.Activity
import android.app.Application
import java.lang.ref.WeakReference

object Factory {

    val ALBUM_CODE = 1001

    var application:Application ?= null
    var activity: WeakReference<Activity>?= null

    val basicsFactory = BasicsFactory()
    var tapSdkFactory = TapSdkFactory()

    var isTapInit = false

    var emitInterface:EmitInterface ?= null

    interface EmitInterface{
        fun onEmitListener(name:String,signalArgs: Any?)
        fun onEmitOtherListener(name:String,code:Int,msg:String)
    }
}