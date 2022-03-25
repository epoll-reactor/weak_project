package com.weak_project.repository

import org.jetbrains.exposed.dao.LongIdTable
import java.security.MessageDigest
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.*

object Users : LongIdTable() {
    val username = varchar("username", length = 50).uniqueIndex()
    val password = varchar("password", length = 64)
}

class UserRepository {
    init {
        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(Users)
        }
    }

    fun register(uniqueUsername: String, rawPassword: String) {
        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(Users)

            val hashedPassword = hashPassword(rawPassword)
            Users.insertAndGetId {
                it[username] = uniqueUsername
                it[password] = hashedPassword
            }

            Users.selectAll()
                .forEach {
                    println(it)
                }
        }
    }

    private fun hashPassword(input: String): String {
        val bytes = input.toByteArray()
        val hash = MessageDigest.getInstance("SHA-256")
        val digest = hash.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }
}