package io.github.nomisrev

import kotlinx.coroutines.delay

suspend fun metrics(registering: User.Register) {
    delay(500)
    println("Tracking user... ${registering.email}")
}
