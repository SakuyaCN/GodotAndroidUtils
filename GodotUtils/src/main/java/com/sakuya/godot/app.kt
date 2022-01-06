package com.sakuya.godot

import android.app.Application
import com.sakuya.godot.factory.Factory

class app: Application() {
    override fun onCreate() {
        super.onCreate()
        Factory.application = this
    }
}