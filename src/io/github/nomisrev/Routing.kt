package io.github.nomisrev

import io.ktor.http.HttpStatusCode
import io.ktor.resources.Resource
import io.ktor.server.application.Application
import io.ktor.server.response.respond
import io.ktor.server.routing.application
import io.ktor.server.resources.post
import io.ktor.server.routing.routing
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
@Resource("user")
class User {
    @Serializable
    @Resource("register")
    class Register(
        val parent: User = User(),
        val email: String,
        val age: Int
    )

    @Serializable
    @Resource("process")
    class Process(
        val parent: User = User(),
        val data: String
    )
}

fun Application.userRoutes(users: UserService) {
    routing {
        post<User.Register> { registering ->
            val id = users.create(ExposedUser(registering.email, registering.age))
            launch { delay(1000) } // Send email
            call.respond(HttpStatusCode.OK, id)
        }
        post<User.Process> { process ->
            application.launch {
                delay(3000) // Process data }
                call.respond(HttpStatusCode.Accepted)
            }
        }
    }
}
