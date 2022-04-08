package com.weak_project.mvc.profile

import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import com.weak_project.mvc.user.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder

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
                    // birthdate
                    it[Users.gender] = gender
                    it[Users.phone] = phone
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