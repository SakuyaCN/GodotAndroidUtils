package com.sakuya.godot

import android.app.Activity
import android.util.Log
import android.view.View
import com.sakuya.godot.factory.Factory
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.GodotPlugin
import org.godotengine.godot.plugin.SignalInfo

class GodotMain(godot: Godot) :GodotPlugin(godot) {

    private val methodArray = mutableListOf<String>()

    override fun onMainCreate(activity: Activity?): View? {
        methodArray.addAll(Factory.basicsFactory.getPluginMethods())
        Log.e(pluginName,"加载成功")
        Log.e(pluginName,methodArray.toString())
        return super.onMainCreate(activity)
    }

    override fun getPluginName(): String {
        return "GodotUtils"
    }

    override fun getPluginMethods(): MutableList<String> {
        return mutableListOf("getMethods")
    }

    override fun getPluginSignals(): MutableSet<SignalInfo> {
        val signals: MutableSet<SignalInfo> = mutableSetOf()
        signals.add(SignalInfo("onSignStringResult", String::class.java))
        signals.add(SignalInfo("onAdResult", String::class.java))
        return signals
    }

    fun getMethods(){

    }

    fun getSignString(){
        var sign :String = activity?.let { SignCheck.getSignString(it) } ?: ""
        emitSignal("onSignStringResult",sign)
    }
}