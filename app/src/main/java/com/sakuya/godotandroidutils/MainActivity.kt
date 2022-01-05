package com.sakuya.godotandroidutils

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.sakuya.godot.factory.Factory
import java.lang.reflect.Constructor
import java.lang.reflect.Method

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.e("test", Factory.basicsFactory.getPluginMethods().toString())
        use("getAndroidPhoneIno")
    }

    private fun use(method:String){
        val c = Class.forName(Factory.basicsFactory.getPluginMethods()[method])
        val obj: Any = c.newInstance()
        val m1: Method = c.getDeclaredMethod(method)
        m1.isAccessible = true
        m1.invoke(obj)
    }
}