package com.ajou.helptmanager

import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import java.io.File


class Application : Application() {
    init {
        instance = this
    }
    companion object{
        private var instance : Application? = null

        fun context() : Context {
            return instance!!.applicationContext
        }
    }
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, BuildConfig.NATIVE_API_KEY)
        val dexOutputDir: File = codeCacheDir
        dexOutputDir.setReadOnly()
//        Log.d("hash", Utility.getKeyHash(this))
    }
}