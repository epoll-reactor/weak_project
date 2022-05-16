package com.weak_project.models

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
    val from = integer("from")
    val to = integer("to")
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

            /* return */ dialogList
        }
    }
}