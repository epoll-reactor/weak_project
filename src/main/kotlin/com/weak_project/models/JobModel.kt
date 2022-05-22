package com.weak_project.models

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

data class Job(
    val id: Int,
    val roleName: String,
    val description: String,
    val companyName: String,
    val country: String,
    val keySkills: String, // Comma-separated list.
    val spokenLanguages: String, // Comma-separated list.
    val requiredEducation: String,
    val ownerId: Int
)

object Jobs : Table("JOBS") {
    val id = integer("id").autoIncrement()
    val roleName = varchar("roleName", length = 256)
    val description = varchar("description", length = 8192)
    val companyName = varchar("companyName", length = 256)
    val country = varchar("country", length = 64)
    val keySkills = varchar("keySkills", length = 1024)
    val spokenLanguages = varchar("spokenLanguages", length = 1024)
    val requiredEducation = varchar("requiredEducation", length = 64)
    val ownerId = integer("ownerId") references Users.id

    fun toObject(row: ResultRow) = Job(
        id = row[id],
        roleName = row[roleName],
        description = row[description],
        companyName = row[companyName],
        country = row[country],
        keySkills = row[keySkills],
        spokenLanguages = row[spokenLanguages],
        requiredEducation = row[requiredEducation],
        ownerId = row[ownerId]
    )
}

/**
 * Job offers database handler.
 */
object JobModel {
    init {
        transaction {
            SchemaUtils.create(Jobs)
        }
    }

    fun insert(
        roleName_: String,
        description_: String,
        companyName_: String,
        country_: String,
        keySkills_: String,
        spokenLanguages_: String,
        requiredEducation_: String,
        ownerId_: Int
    ) {
        transaction {
            Jobs.insert {
                it[roleName] = roleName_
                it[description] = description_
                it[companyName] = companyName_
                it[country] = country_
                it[keySkills] = keySkills_
                it[spokenLanguages] = spokenLanguages_
                it[requiredEducation] = requiredEducation_
                it[ownerId] = ownerId_
            }
        }
    }

    fun get(jobId: Int): Job? {
        return transaction {
            Jobs
                .select { Jobs.id eq jobId }
                .map { Jobs.toObject(it) }
                .firstOrNull()
        }
    }

    fun getByOwnerId(ownerId: Int, maxCount: Int = 5): MutableList<Job> {
        return transaction {
            val jobsList = mutableListOf<Job>()

            Jobs.selectAll().forEach {
                if (jobsList.size <= maxCount && it[Jobs.ownerId] == ownerId) {
                    jobsList.add(Jobs.toObject(it))
                }
            }

            /* return */ jobsList
        }
    }

    fun getBy(
        roleName_: String,
        description_: String,
        companyName_: String,
        country_: String,
        keySkills_: String,
        spokenLanguages_: String,
        requiredEducation_: String
    ): MutableList<Job> {
        return transaction {
            val jobsList = mutableListOf<Job>()

            Jobs.selectAll().forEach {
                var condition = false

                fun makeBranch(column: Column<String>, field: String): Boolean {
                    if (field.isEmpty())
                        return condition

                    return if (!condition) {
                        condition or (it[column] == field)
                    } else {
                        condition and (it[column] == field)
                    }
                }

                condition = makeBranch(Jobs.roleName, roleName_)
                condition = makeBranch(Jobs.description, description_)
                condition = makeBranch(Jobs.companyName, companyName_)
                condition = makeBranch(Jobs.country, country_)
                condition = makeBranch(Jobs.keySkills, keySkills_)
                condition = makeBranch(Jobs.spokenLanguages, spokenLanguages_)
                condition = makeBranch(Jobs.requiredEducation, requiredEducation_)

                if (condition) {
                    jobsList.add(Jobs.toObject(it))
                }
            }

            /* return */ jobsList
        }
    }
}