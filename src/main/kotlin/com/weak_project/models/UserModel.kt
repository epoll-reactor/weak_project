package com.weak_project.models

import java.security.MessageDigest
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.*
import java.io.File

data class User(
    val id: Int,
    val username: String,
    val password: String,
    var firstName: String,
    var lastName: String,
    var country: String,
    var city: String,
    var birthDate: String,
    var gender: Int,
    var phone: String,
    val employerOrEmployee: Int
)

fun isEmployee(property: Int): Boolean = property == 1
fun isEmployer(property: Int): Boolean = property == 2

object Users : Table("USERS") {
    val id = integer("id").autoIncrement()
    val username = varchar("username", length = 50).uniqueIndex()
    val password = varchar("password", length = 64)
    val firstName = varchar("firstName", length = 64).default("")
    val lastName = varchar("lastName", length = 64).default("")
    val country = varchar("country", length = 64).default("")
    val city = varchar("city", length = 64).default("")
    val birthDate = varchar("birthDate", length = 64).default("")
    val gender = integer("gender").default(1) // By standard ISO/IEC 5218.
    val phone = varchar("phone", length = 15).default("")
    val employerOrEmployee = integer("employerOrEmployee").default(0)
    val avatar = binary("avatar", 1024 * 1024).default(byteArrayOf()).nullable()

    fun toObject(row: ResultRow) = User(
        id = row[id],
        username = row[username],
        password = row[password],
        firstName = row[firstName],
        lastName = row[lastName],
        country = row[country],
        city = row[city],
        birthDate = row[birthDate],
        gender = row[gender],
        phone = row[phone],
        employerOrEmployee = row[employerOrEmployee]
    )
}

/**
 * Users database handler.
 */
object UserModel {
    init {
        transaction {
            SchemaUtils.create(Users)
        }
    }

    /**
     * Try to register new user.
     *
     * @throws RuntimeException if user already registered.
     * @return user id.
     */
    fun register(
        uniqueUsername: String,
        rawPassword: String,
        firstName_: String,
        lastName_: String,
        employerOrEmployee_: Int
    ): Int {
        return transaction {
            val hashedPassword = hashPassword(rawPassword)
            Users.insert {
                it[username] = uniqueUsername
                it[password] = hashedPassword
                it[firstName] = firstName_
                it[lastName] = lastName_
                it[employerOrEmployee] = employerOrEmployee_
            } get Users.id
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

    fun getById(id: Int): User {
        return transaction {
            Users
                .select { Users.id eq id }
                .map { Users.toObject(it) }
                .first()
        }
    }

    fun userExists(username: String): Boolean {
        val user = transaction {
            Users
                .select { Users.username eq username }
                .map { Users.toObject(it) }
                .firstOrNull()

        }
        return user != null
    }

    fun updateAvatar(username: String, path: String) {
        val bytes = File(path).readBytes()
        transaction {
            Users
                .update ({ Users.username eq username }) {
                    it[avatar] = bytes
                }
        }
    }

    fun getAvatar(username: String): ByteArray? {
        return transaction {
            Users
                .select { Users.username eq username }
                .map { it[Users.avatar] }
                .firstOrNull()
        }
    }

    fun hashPassword(input: String): String {
        val bytes = input.toByteArray()
        val hash = MessageDigest.getInstance("SHA-256")
        val digest = hash.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }
}