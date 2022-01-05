package com.sakuya.godot

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import com.sakuya.godot.activitys.WebActivity
import com.sakuya.godot.factory.Factory
import com.sakuya.godot.factory.TapSdkFactory
import com.taptap.sdk.TapLoginHelper
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.GodotPlugin
import org.godotengine.godot.plugin.SignalInfo
import java.lang.reflect.Method

class GodotMain(godot: Godot) :GodotPlugin(godot),Factory.EmitInterface {

    var context = activity

    private val methodMap = hashMapOf<String,String>()
    private val signalMap = mutableSetOf<SignalInfo>()

    init {
        methodMap.putAll(Factory.basicsFactory.getPluginMethods())
        methodMap.putAll(Factory.tapSdkFactory.getPluginMethods())
        signalMap.addAll(Factory.basicsFactory.getPluginSignals())
        signalMap.addAll(Factory.tapSdkFactory.getPluginSignals())
        Factory.emitInterface = this
    }

    override fun onMainCreate(activity: Activity?): View? {
        Factory.godotPlugin = this
        Log.e(pluginName,"加载成功")
        return super.onMainCreate(activity)
    }

    override fun getPluginName(): String {
        return "GodotUtils"
    }

    override fun getPluginMethods(): MutableList<String> {
        return mutableListOf("getMethods","showToast","openWebView","tapInit")
    }

    override fun getPluginSignals(): MutableSet<SignalInfo> {
        return signalMap
    }

    fun getMethods(method:String){
        val c = Class.forName(methodMap[method])
        val obj: Any = c.newInstance()
        val m1: Method = c.getDeclaredMethod(method)
        m1.isAccessible = true
        m1.invoke(obj)
    }

    @SuppressLint("WrongConstant")
    fun showToast(msg:String, type:Int){
        runOnUiThread {
            Toast.makeText(context!!,msg,0).show()
        }
    }

    fun openWebView(url:String){
        runOnUiThread {
            activity?.let {
                it.startActivity(Intent(it,WebActivity::class.java).putExtra("url",url))
            }
        }
    }

    fun tapInit(c_id:String,c_token:String){
        Factory.tapSdkFactory.create(activity!!,c_id,c_token)
    }

    override fun onEmitListener(name: String,signalArgs: Any?) {
        emitSignal(name,signalArgs)
    }
}