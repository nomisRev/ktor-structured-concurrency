package io.github.nomisrev

import io.ktor.server.application.Application
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

class EventService {
    fun events(): Flow<String> = flowOf("nomisRev", "vergauwen_simon")
}

fun Application.processing(service: EventService, users: UserService): Job =
    launch {
        service.events()
            .collect { event -> users.process(event) }
    }
