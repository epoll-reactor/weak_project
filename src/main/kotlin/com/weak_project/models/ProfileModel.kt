package com.weak_project.models

import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

object ProfileModel {
    // Table created in UserModel.

    fun setupProfile(
        username: String,
        firstName: String,
        lastName: String,
        country: String,
        city: String,
        birthDate: String,
        gender: Int,
        phone: String
    ) {
        requireUserExists(username)

        transaction {
            Users
                .update({ Users.username eq username }) {
                    it[Users.firstName] = firstName
                    it[Users.lastName] = lastName
                    it[Users.country] = country
                    it[Users.city] = city
                    it[Users.birthDate] = birthDate
                    it[Users.gender] = gender
                    it[Users.phone] = phone
                }
        }
    }

    fun getByUsername(username: String): User? {
        return transaction {
            Users
                .select { Users.username eq username }
                .map { Users.toObject(it) }
                .firstOrNull()
        }
    }

    fun changePassword(username: String, newPassword: String) {
        transaction {
            Users
                .update({ Users.username eq username }) {
                    it[password] = newPassword
                }
        }
    }

    private fun requireUserExists(username: String) {
        transaction {
            Users
                .select { Users.username eq username }
                .map { Users.toObject(it) }
                .firstOrNull()

        } ?: throw RuntimeException("User $username not found.")
    }
}