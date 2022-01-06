package com.sakuya.godot

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import com.sakuya.godot.activitys.WebActivity
import com.sakuya.godot.factory.Factory
import com.sakuya.godot.factory.TapSdkFactory
import com.taptap.sdk.TapLoginHelper
import com.tds.tapdb.sdk.TapDB
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.GodotPlugin
import org.godotengine.godot.plugin.SignalInfo
import org.json.JSONObject
import java.lang.ref.WeakReference
import java.lang.reflect.Method
import com.google.gson.JsonParser

import com.google.gson.JsonObject
import com.sakuya.godot.factory.Factory.ALBUM_CODE
import com.sakuya.godot.utils.Utils
import org.slf4j.helpers.Util


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
        Factory.activity = WeakReference(activity)
        Log.e(pluginName,"加载成功")


        return super.onMainCreate(activity)
    }

    override fun getPluginName(): String {
        return "GodotUtils"
    }

    override fun getPluginMethods(): MutableList<String> {
        return mutableListOf("getMethods","showToast","openWebView","tapInit","tapTrackEvent")
    }

    override fun getPluginSignals(): MutableSet<SignalInfo> {
        return signalMap
    }

    fun getMethods(method:String){
        val c = Class.forName(methodMap[method])
        val obj: Any = c.newInstance()
        val m1: Method = c.getDeclaredMethod(method,Activity::class.java)
        m1.isAccessible = true
        m1.invoke(obj,activity)
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

    //自定义事件
    fun tapTrackEvent(event:String,json: String){
        if(!Factory.isTapInit){
            runOnUiThread { Toast.makeText(activity,"未初始化SDK",Toast.LENGTH_SHORT).show() }
            return
        }
        TapDB.trackEvent(event,JSONObject(json))
    }

    override fun onEmitListener(name: String,signalArgs: Any?) {
        emitSignal(name,signalArgs)
    }

    override fun onEmitOtherListener(name: String, code: Int, msg: String) {
        emitSignal(name,code,msg)
    }

    override fun onMainActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onMainActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            when(requestCode){
                ALBUM_CODE->{
                    val path = Utils.uriToFile(data!!.data, activity).absolutePath
                    emitSignal("AlbumResult",path)
                }
            }
        }
    }
}