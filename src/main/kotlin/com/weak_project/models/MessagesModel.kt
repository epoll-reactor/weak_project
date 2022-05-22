package com.weak_project.models

import com.weak_project.models.CVs.references
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

data class Message(
    val text: String,
    val from: Int,
    val to: Int,
    val timestamp: Long
)

object Messages : Table("MESSAGES") {
    val id = integer("id").autoIncrement()
    val text = varchar("text", length = 8192)
    val from = integer("from") references Users.id
    val to = integer("to") references Users.id
    val timestamp = long("timestamp")

    fun toObject(row: ResultRow) = Message(
        text = row[text],
        from = row[from],
        to = row[to],
        timestamp = row[timestamp]
    )
}

object MessagesModel {
    init {
        transaction {
            SchemaUtils.create(Messages)
        }
    }

    fun insert(
        text_: String,
        from_: Int,
        to_: Int,
        timestamp_: Long
    ) {
        transaction {
            Messages.insert {
                it[text] = text_
                it[from] = from_
                it[to] = to_
                it[timestamp] = timestamp_
            }
        }
    }

    /**
     * Get list of dialogs where the id is participant.
     */
    fun getDialogList(id: Int): MutableList<Message> {
        return transaction {
            val dialogList = mutableListOf<Message>()

            Messages.selectAll().forEach { message ->
                if (message[Messages.from] == id) {
                    dialogList += Messages.toObject(message)
                }
            }

            dialogList.sortBy { it.timestamp }

            /* return */ dialogList.distinctBy { it.to }.toMutableList()
        }
    }

    /**
     * Get the dialog between two users itself.
     */
    fun getPrivateDialog(user1: Int, user2: Int): MutableList<Message> {
        return transaction {
            val dialogList = mutableListOf<Message>()

            Messages.selectAll().forEach { message ->
                if (((message[Messages.from] == user1) and (message[Messages.to] == user2)) or
                    ((message[Messages.from] == user2) and (message[Messages.to] == user1))) {
                    dialogList.add(Messages.toObject(message))
                }
            }

            dialogList.sortBy { it.timestamp }

            /* return */ dialogList
        }
    }
}