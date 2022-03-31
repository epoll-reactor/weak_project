package com.weak_project.repository

import java.security.MessageDigest
import org.jetbrains.exposed.dao.LongIdTable
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.*

data class User(
    val username: String,
    val password: String
)

object Users : LongIdTable("USERS") {
    val username = varchar("username", length = 50).uniqueIndex()
    val password = varchar("password", length = 64)

    fun toObject(row: ResultRow) =
        User(
            username = row[username],
            password = row[password]
        )
}

/**
 * Users database handler.
 */
object UserRepository {
    init {
        transaction {
            SchemaUtils.create(Users)
        }
    }

    /**
     * Try to register new user.
     *
     * @throws RuntimeException if user already registered.
     */
    fun register(uniqueUsername: String, rawPassword: String) {
        try {
            transaction {
                val hashedPassword = hashPassword(rawPassword)
                Users.insertAndGetId {
                    it[username] = uniqueUsername
                    it[password] = hashedPassword
                }
            }
        } catch (e: Exception) {
            throw RuntimeException("User $uniqueUsername already registered.")
        }
    }

    /**
     * Return user object if found, null otherwise.
     */
    fun login(username: String, password: String): User? {
        return transaction {
            val hashedPassword = hashPassword(password)
            Users
                .select { Users.username eq username and (Users.password eq hashedPassword) }
                .map { Users.toObject(it) }
                .firstOrNull()

        }
    }

    private fun hashPassword(input: String): String {
        val bytes = input.toByteArray()
        val hash = MessageDigest.getInstance("SHA-256")
        val digest = hash.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }
}