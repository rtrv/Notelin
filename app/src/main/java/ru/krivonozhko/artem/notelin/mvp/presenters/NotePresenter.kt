package ru.krivonozhko.artem.notelin.mvp.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.krivonozhko.artem.notelin.NotelinApplication
import ru.krivonozhko.artem.notelin.bus.NoteDeleteAction
import ru.krivonozhko.artem.notelin.bus.NoteEditAction
import ru.krivonozhko.artem.notelin.mvp.models.Note
import ru.krivonozhko.artem.notelin.mvp.models.NoteDao
import ru.krivonozhko.artem.notelin.mvp.views.NoteView
import org.greenrobot.eventbus.EventBus
import java.util.*
import javax.inject.Inject

@InjectViewState
class NotePresenter : MvpPresenter<NoteView>() {
    @Inject
    lateinit var mNoteDao: NoteDao
    lateinit var mNote: Note
    var mNotePosition: Int = -1

    init {
        NotelinApplication.graph.inject(this)
    }

    fun showNote(noteId: Long, notePosition: Int) {
        mNotePosition = notePosition
        mNote = mNoteDao.getNoteById(noteId)
        viewState.showNote(mNote)
    }

    fun saveNote(title: String, text: String) {
        mNote.title = title
        mNote.text = text
        mNote.changeDate = Date()
        mNoteDao.saveNote(mNote)
        EventBus.getDefault().post(NoteEditAction(mNotePosition))
        viewState.onNoteSaved()
    }

    fun deleteNote() {
        mNoteDao.deleteNote(mNote)
        EventBus.getDefault().post(NoteDeleteAction(mNotePosition))
        viewState.onNoteDeleted()
    }

    fun showNoteDeleteDialog() {
        viewState.showNoteDeleteDialog()
    }

    fun hideNoteDeleteDialog() {
        viewState.hideNoteDeleteDialog()
    }

    fun showNoteInfoDialog() {
        viewState.showNoteInfoDialog(mNote.getInfo())
    }

    fun hideNoteInfoDialog() {
        viewState.hideNoteInfoDialog()
    }
}
