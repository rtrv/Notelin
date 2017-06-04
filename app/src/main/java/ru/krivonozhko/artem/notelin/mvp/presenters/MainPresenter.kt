package ru.krivonozhko.artem.notelin.mvp.presenters

import android.app.Activity
import android.content.Intent
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.krivonozhko.artem.notelin.NotelinApplication
import ru.krivonozhko.artem.notelin.bus.NoteDeleteAction
import ru.krivonozhko.artem.notelin.bus.NoteEditAction
import ru.krivonozhko.artem.notelin.mvp.models.Note
import ru.krivonozhko.artem.notelin.mvp.models.NoteDao
import ru.krivonozhko.artem.notelin.mvp.views.MainView
import ru.krivonozhko.artem.notelin.ui.activities.NoteActivity
import ru.krivonozhko.artem.notelin.utils.getNotesSortMethodName
import ru.krivonozhko.artem.notelin.utils.setNotesSortMethod
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.*
import javax.inject.Inject

@InjectViewState
class MainPresenter : MvpPresenter<MainView>() {
    enum class SortNotesBy : Comparator<Note> {
        DATE {
            override fun compare(lhs: Note, rhs: Note) = lhs.changeDate!!.compareTo(rhs.changeDate)
        },
        NAME {
            override fun compare(lhs: Note, rhs: Note) = lhs.title!!.compareTo(rhs.title!!)
        },
    }

    @Inject
    lateinit var mNoteDao: NoteDao
    lateinit var mNotesList: MutableList<Note>

    init {
        NotelinApplication.graph.inject(this)
        EventBus.getDefault().register(this)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadAllNotes()
    }

    fun loadAllNotes() {
        mNotesList = mNoteDao.loadAllNotes()
        Collections.sort(mNotesList, getCurrentSortMethod())
        viewState.onNotesLoaded(mNotesList)
    }

    fun deleteAllNotes() {
        mNoteDao.deleteAllNotes()
        mNotesList.removeAll(mNotesList)
        viewState.onAllNotesDeleted()
    }

    fun deleteNoteByPosition(position: Int) {
        val note = mNotesList[position];
        mNoteDao.deleteNote(note)
        mNotesList.remove(note)
        viewState.onNoteDeleted()
    }

    fun openNewNote(activity: Activity) {
        val newNote = mNoteDao.createNote()
        mNotesList.add(newNote)
        sortNotesBy(getCurrentSortMethod())
        openNote(activity, mNotesList.indexOf(newNote))
    }

    fun openNote(activity: Activity, position: Int) {
        val intent = Intent(activity, NoteActivity::class.java)
        intent.putExtra("note_position", position)
        intent.putExtra("note_id", mNotesList[position].id)
        activity.startActivity(intent)
    }

    fun search(query: String) {
        if (query.equals("")) {
            viewState.onSearchResult(mNotesList)
        } else {
            val searchResults = mNotesList.filter {
                it.title!!.startsWith(query, ignoreCase = true)
            }
            viewState.onSearchResult(searchResults)
        }
    }

    fun sortNotesBy(sortMethod: SortNotesBy) {
        mNotesList.sortWith(sortMethod)
        setNotesSortMethod(sortMethod.toString())
        viewState.updateView()
    }

    fun getCurrentSortMethod(): SortNotesBy {
        val defaultSortMethodName = SortNotesBy.DATE.toString()
        val currentSortMethodName = getNotesSortMethodName(defaultSortMethodName)
        return SortNotesBy.valueOf(currentSortMethodName)
    }

    @Subscribe
    fun onNoteEdit(action: NoteEditAction) {
        val notePosition = action.position
        mNotesList[notePosition] = mNoteDao.getNoteById(mNotesList[notePosition].id) //обновляем заметку по позиции
        sortNotesBy(getCurrentSortMethod())
    }

    @Subscribe
    fun onNoteDelete(action: NoteDeleteAction) {
        mNotesList.removeAt(action.position)
        viewState.updateView()
    }

    fun showNoteContextDialog(position: Int) {
        viewState.showNoteContextDialog(position)
    }

    fun hideNoteContextDialog() {
        viewState.hideNoteContextDialog()
    }

    fun showNoteDeleteDialog(position: Int) {
        viewState.showNoteDeleteDialog(position)
    }

    fun hideNoteDeleteDialog() {
        viewState.hideNoteDeleteDialog()
    }

    fun showNoteInfo(position: Int) {
        viewState.showNoteInfoDialog(mNotesList[position].getInfo())
    }

    fun hideNoteInfoDialog() {
        viewState.hideNoteInfoDialog()
    }
}
