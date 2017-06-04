package ru.krivonozhko.artem.notelin.mvp.views

import com.arellomobile.mvp.MvpView
import ru.krivonozhko.artem.notelin.mvp.models.Note

interface NoteView : MvpView {
    fun showNote(note: Note)

    fun onNoteSaved()

    fun onNoteDeleted()

    fun showNoteInfoDialog(noteInfo: String)

    fun hideNoteInfoDialog()

    fun showNoteDeleteDialog()

    fun hideNoteDeleteDialog()
}
