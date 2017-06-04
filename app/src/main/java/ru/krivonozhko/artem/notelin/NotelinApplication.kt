package ru.krivonozhko.artem.notelin

import android.content.Context
import com.activeandroid.app.Application
import ru.krivonozhko.artem.notelin.di.AppComponent
import ru.krivonozhko.artem.notelin.di.DaggerAppComponent
import ru.krivonozhko.artem.notelin.di.NoteDaoModule
import ru.krivonozhko.artem.notelin.utils.initPrefs

class NotelinApplication : Application() {

    companion object {
        lateinit var graph: AppComponent
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()

        initPrefs(this)

        context = this
        graph = DaggerAppComponent.builder().noteDaoModule(NoteDaoModule()).build()
    }

}