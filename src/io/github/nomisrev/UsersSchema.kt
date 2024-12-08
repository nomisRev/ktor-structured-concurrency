package io.github.nomisrev

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insertAndGetId
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

    suspend fun insert(user: ExposedUser): Long =
        newSuspendedTransaction(Dispatchers.IO) {
            Users.insertAndGetId {
                it[name] = user.name
                it[age] = user.age
            }.value
        }

    suspend fun process(email: String) {
        val users = newSuspendedTransaction(Dispatchers.IO) {
            Users.select(Users.name, Users.age)
                .where { Users.name.eq(email) }
                .map { ExposedUser(it[Users.name], it[Users.age]) }
        }
        if (users.isEmpty()) println("No users found $email")
        else users.forEach {
            delay(100)
            println("Processed $it")
        }
    }
}
