package com.epilepto.dhyanapp.presentation.di

import android.app.Application

class DhyanApp:Application() {

    companion object{
        lateinit var appModule: AppModule
    }

    override fun onCreate() {
        super.onCreate()
        appModule = AppModuleImpl(this)
    }
}