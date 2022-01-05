package com.sakuya.godot.factory

import com.sakuya.godot.GodotMain

object Factory {

    var godotPlugin:GodotMain ?= null

    val basicsFactory = BasicsFactory()
    var tapSdkFactory = TapSdkFactory()

    var emitInterface:EmitInterface ?= null

    interface EmitInterface{
        fun onEmitListener(name:String,signalArgs: Any?)
    }
}