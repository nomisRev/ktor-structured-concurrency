package io.github.nomisrev

import io.ktor.http.HttpStatusCode
import io.ktor.resources.Resource
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.Routing
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

fun Routing.userRoutes(users: UserService) {
    post<User.Register> { registering ->
        call.launch { metrics(registering) }
        val id = users.insert(ExposedUser(registering.email, registering.age))
        call.respond(HttpStatusCode.OK, id)
    }
}
