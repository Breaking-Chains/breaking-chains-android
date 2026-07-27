package com.breakingchains.app.data.model

import com.breakingchains.app.data.local.entity.CallRequestEntity

data class CallRequest(
    val id: String,
    val userId: String,
    val userName: String,
    val userEmail: String,
    val preferredDate: String,
    val preferredTime: String,
    val reasonNote: String,
    val status: String = "PENDING",
    val timestamp: Long = System.currentTimeMillis()
)

fun CallRequestEntity.toDomainModel(): CallRequest {
    return CallRequest(
        id = id,
        userId = userId,
        userName = userName,
        userEmail = userEmail,
        preferredDate = preferredDate,
        preferredTime = preferredTime,
        reasonNote = reasonNote,
        status = status,
        timestamp = timestamp
    )
}

fun CallRequest.toEntity(): CallRequestEntity {
    return CallRequestEntity(
        id = id,
        userId = userId,
        userName = userName,
        userEmail = userEmail,
        preferredDate = preferredDate,
        preferredTime = preferredTime,
        reasonNote = reasonNote,
        status = status,
        timestamp = timestamp
    )
}
