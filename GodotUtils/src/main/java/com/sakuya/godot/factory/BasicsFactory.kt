package com.sakuya.godotutils.factory

class BasicsFactory {

    private fun getAndroidPhoneIno(){

    }

    fun getPluginMethods():MutableList<String>{
        return mutableListOf(this::getAndroidPhoneIno.name)
    }
}