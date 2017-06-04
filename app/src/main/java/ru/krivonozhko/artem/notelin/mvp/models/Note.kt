package ru.krivonozhko.artem.notelin.mvp.models

import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table
import ru.krivonozhko.artem.notelin.utils.formatDate
import java.util.*

@Table(name = "Notes")
class Note : Model {
    @Column(name = "title")
    public var title: String? = null
    @Column(name = "text")
    public var text: String? = null
    @Column(name = "create_date")
    public var createDate: Date? = null
    @Column(name = "change_date")
    public var changeDate: Date? = null

    constructor(title: String, createDate: Date) {
        this.title = title
        this.createDate = createDate
        this.changeDate = createDate
    }

    constructor()

    fun getInfo(): String = "Название:\n$title\n" +
                            "Время создания:\n${formatDate(createDate)}\n" +
                            "Время изменения:\n${formatDate(changeDate)}"
}