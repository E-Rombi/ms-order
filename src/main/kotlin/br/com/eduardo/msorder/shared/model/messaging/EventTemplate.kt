package br.com.eduardo.msorder.shared.model.messaging

import java.time.Instant

class EventTemplate<T>(
    val source: String,
    val timestamp: Instant,
    val tId: String,
    val data: T
)