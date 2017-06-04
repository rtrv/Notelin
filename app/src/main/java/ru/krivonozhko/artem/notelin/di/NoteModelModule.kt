package ru.krivonozhko.artem.notelin.di

import dagger.Module
import dagger.Provides
import ru.krivonozhko.artem.notelin.mvp.models.NoteDao
import javax.inject.Singleton

@Module
class NoteDaoModule {

    @Provides
    @Singleton
    fun provideNoteDao(): NoteDao = NoteDao()

}