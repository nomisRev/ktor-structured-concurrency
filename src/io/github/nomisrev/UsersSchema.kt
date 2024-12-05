package io.github.nomisrev

import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

@Serializable
data class ExposedUser(
    val name: String,
    val age: Int
)

class UserService(database: Database) {
    object Users : LongIdTable() {
        val name = varchar("name", length = 50)
        val age = integer("age")
    }

    suspend fun create(user: ExposedUser): Long =
        newSuspendedTransaction(Dispatchers.IO) {
            val inserted = Users.insert {
                it[name] = user.name
                it[age] = user.age
            }
            inserted[Users.id].value
        }
}
