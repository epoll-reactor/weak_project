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

    fun getSome(maxCount: Int = 5): MutableList<Job> {
        return transaction {
            val jobsList = mutableListOf<Job>()

            Jobs.selectAll().forEach {
                if (jobsList.size <= maxCount) {
                    jobsList.add(Jobs.toObject(it))
                }
            }

            jobsList.shuffle()

            /* return */ jobsList
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

    fun update(
        roleName: String = "",
        description: String = "",
        companyName: String = "",
        country: String = "",
        keySkills: String = "",
        spokenLanguages: String = "",
        requiredEducation: String = "",
        ownerId: Int
    ) {
        transaction {
            Jobs
                .update ({ Jobs.ownerId eq ownerId }) {
                    fun updateIfNotEmpty(column: Column<String>, field: String) {
                        if (field.isNotEmpty())
                            it[column] = field
                    }

                    updateIfNotEmpty(Jobs.roleName, roleName)
                    updateIfNotEmpty(Jobs.description, description)
                    updateIfNotEmpty(Jobs.companyName, companyName)
                    updateIfNotEmpty(Jobs.country, country)
                    updateIfNotEmpty(Jobs.keySkills, keySkills)
                    updateIfNotEmpty(Jobs.spokenLanguages, spokenLanguages)
                    updateIfNotEmpty(Jobs.requiredEducation, requiredEducation)
                }
        }
    }

    fun getBy(
        roleName: String = "",
        description: String = "",
        companyName: String = "",
        country: String = "",
        keySkills: String = "",
        spokenLanguages: String = "",
        requiredEducation: String = ""
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

                condition = makeBranch(Jobs.roleName, roleName)
                condition = makeBranch(Jobs.description, description)
                condition = makeBranch(Jobs.companyName, companyName)
                condition = makeBranch(Jobs.country, country)
                condition = makeBranch(Jobs.keySkills, keySkills)
                condition = makeBranch(Jobs.spokenLanguages, spokenLanguages)
                condition = makeBranch(Jobs.requiredEducation, requiredEducation)

                if (condition) {
                    jobsList.add(Jobs.toObject(it))
                }
            }

            /* return */ jobsList
        }
    }
}