package com.ismael.thecompose
import android.app.Application
import com.ismael.thecompose.data.AppContainer
import com.ismael.thecompose.data.DefaultAppContainer


class TheComposeApplication: Application(){
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}