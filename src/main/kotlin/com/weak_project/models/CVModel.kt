package com.weak_project.models

import org.jetbrains.exposed.dao.LongIdTable
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

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
    ): CV? {
        return transaction {
            var query: Op<Boolean> = Op.FALSE

            fun makeBranch(column: Column<String>, field: String): Op<Boolean> {
                if (field.isEmpty())
                    return query

                return if (query == Op.FALSE) {
                    OrOp(query, column eq field)
                } else {
                    AndOp(query, column eq field)
                }
            }

            query = makeBranch(CVs.keySkills, skills)
            query = makeBranch(CVs.spokenLanguages, languages)
            query = makeBranch(CVs.country, theCountry)
            query = makeBranch(CVs.education, theEducation)

            println(query.toString())

            CVs.select { query }
            .map { CVs.toObject(it) }
            .firstOrNull()
        }
    }
}