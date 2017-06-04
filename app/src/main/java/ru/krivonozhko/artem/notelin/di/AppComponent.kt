package ru.krivonozhko.artem.notelin.di

import dagger.Component
import ru.krivonozhko.artem.notelin.mvp.presenters.MainPresenter
import ru.krivonozhko.artem.notelin.mvp.presenters.NotePresenter
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(NoteDaoModule::class))
interface AppComponent {
    fun inject(mainPresenter: MainPresenter)

    fun inject(notePresenter: NotePresenter)
}
