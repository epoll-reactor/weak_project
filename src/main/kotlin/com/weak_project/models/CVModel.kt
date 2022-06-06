package com.weak_project.models


import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.*


data class CV(
    val id: Int,
    val keySkills: String, // Comma-separated list.
    val spokenLanguages: String, // Comma-separated list.
    val country: String,
    val education: String,
    val ownerId: Int
)

object CVs : Table("CVS") {
    val id = integer("id").autoIncrement()
    val keySkills = varchar("keySkills", length = 1024)
    val spokenLanguages = varchar("spokenLanguages", length = 1024)
    val country = varchar("country", length = 64)
    val education = varchar("education", length = 64)
    val ownerId = integer("ownerId") references Users.id

    fun toObject(row: ResultRow) = CV(
        id = row[id],
        keySkills = row[keySkills],
        spokenLanguages = row[spokenLanguages],
        country = row[country],
        education = row[education],
        ownerId = row[ownerId]
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
        theEducation: String,
        ownerId__: Int
    ) {
        transaction {
            CVs.insert {
                it[keySkills] = skills
                it[spokenLanguages] = languages
                it[country] = theCountry
                it[education] = theEducation
                it[ownerId] = ownerId__
            }
        }
    }

    /**
     * Get the list of CV's. All parameters are optional and can be
     * used separately.
     */
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
                    cvsList.add(CVs.toObject(it))
                }
            }

            /* return */ cvsList
        }
    }

    fun update(
        skills_: String = "",
        languages_: String = "",
        theCountry_: String = "",
        theEducation_: String = "",
        ownerId_: Int
    ) {
        transaction {
            CVs
                .update({ CVs.ownerId eq ownerId_ }) {
                    fun updateIfNotEmpty(column: Column<String>, field: String) {
                        if (field.isNotEmpty())
                            it[column] = field
                    }

                    updateIfNotEmpty(CVs.keySkills, skills_)
                    updateIfNotEmpty(CVs.spokenLanguages, languages_)
                    updateIfNotEmpty(CVs.country, theCountry_)
                    updateIfNotEmpty(CVs.education, theEducation_)

                }
        }
    }

    fun get(CVId: Int): CV? {
        return transaction {
            CVs
                .select { CVs.id eq CVId }
                .map { CVs.toObject(it) }
                .firstOrNull()
        }
    }
}