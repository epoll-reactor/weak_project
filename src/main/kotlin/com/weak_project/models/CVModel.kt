package com.weak_project.models

import org.jetbrains.exposed.dao.LongIdTable
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.*

data class CV(
    val keySkills: String, // Comma-separated list.
    val spokenLanguages: String, // Comma-separated list.
    val country: String,
    val education: String
)

object CVs : LongIdTable("CVS") {
    val keySkills = varchar("keySkills", length = 1024)
    val spokenLanguages = varchar("spokenLanguages", length = 1024)
    val country = varchar("country", length = 64)
    val education = varchar("education", length = 64)

    fun toObject(row: ResultRow) = CV(
        keySkills = row[keySkills],
        spokenLanguages = row[spokenLanguages],
        country = row[country],
        education = row[education]
    )
}

/**
 * CV database handler.
 */
object CVModel {
    init {
        transaction {
            SchemaUtils.create(CVs)
        }
    }

    fun insert(
        skills: String,
        languages: String,
        theCountry: String,
        theEducation: String
    ) {
        transaction {
            CVs.insert {
                it[keySkills] = skills
                it[spokenLanguages] = languages
                it[country] = theCountry
                it[education] = theEducation
            }
        }
    }

    fun getBy(
        skills: String = "",
        languages: String = "",
        theCountry: String = "",
        theEducation: String = ""
    ): MutableList<CV> {
        return transaction {
            val cvsList = mutableListOf<CV>()

            CVs.selectAll().forEach {
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

                condition = makeBranch(CVs.keySkills, skills)
                condition = makeBranch(CVs.spokenLanguages, languages)
                condition = makeBranch(CVs.country, theCountry)
                condition = makeBranch(CVs.education, theEducation)

                if (condition) {
                    cvsList.add(CV(
                        keySkills = it[CVs.keySkills],
                        spokenLanguages = it[CVs.spokenLanguages],
                        country = it[CVs.country],
                        education = it[CVs.education]
                    ))
                }
            }

            /* return */ cvsList
        }
    }
}