package com.breakingchains.app.data.repository

import com.breakingchains.app.data.local.dao.CallRequestDao
import com.breakingchains.app.data.local.entity.CallRequestEntity
import com.breakingchains.app.data.model.CallRequest
import com.breakingchains.app.data.model.toDomainModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

interface CallRequestRepository {
    fun getCallRequestsForUser(userId: String): Flow<List<CallRequest>>
    fun getAllCallRequests(): Flow<List<CallRequest>>
    suspend fun scheduleCall(
        userId: String,
        userName: String,
        userEmail: String,
        preferredDate: String,
        preferredTime: String,
        note: String
    )
    suspend fun updateRequestStatus(requestId: String, status: String)
}

class CallRequestRepositoryImpl(
    private val callRequestDao: CallRequestDao,
    private val firestore: FirebaseFirestore? = null
) : CallRequestRepository {

    override fun getCallRequestsForUser(userId: String): Flow<List<CallRequest>> {
        return callRequestDao.getCallRequestsForUser(userId).map { list ->
            list.map { it.toDomainModel() }
        }
    }

    override fun getAllCallRequests(): Flow<List<CallRequest>> {
        return callRequestDao.getAllCallRequests().map { list ->
            list.map { it.toDomainModel() }
        }
    }

    override suspend fun scheduleCall(
        userId: String,
        userName: String,
        userEmail: String,
        preferredDate: String,
        preferredTime: String,
        note: String
    ) {
        val requestId = "req_${System.currentTimeMillis()}"
        val entity = CallRequestEntity(
            id = requestId,
            userId = userId,
            userName = userName,
            userEmail = userEmail,
            preferredDate = preferredDate,
            preferredTime = preferredTime,
            reasonNote = note,
            status = "PENDING",
            timestamp = System.currentTimeMillis()
        )

        // Offline-First: Insert into Room DB immediately
        callRequestDao.insertCallRequest(entity)

        // Background cloud sync to Firestore
        syncCallRequestToFirestoreInBackground(entity.toDomainModel())
    }

    override suspend fun updateRequestStatus(requestId: String, status: String) {
        callRequestDao.updateRequestStatus(requestId, status)
        if (firestore != null) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    firestore.collection("call_requests").document(requestId).update("status", status)
                } catch (_: Exception) { }
            }
        }
    }

    private fun syncCallRequestToFirestoreInBackground(request: CallRequest) {
        if (firestore == null) return
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val reqMap = mapOf(
                    "id" to request.id,
                    "userId" to request.userId,
                    "userName" to request.userName,
                    "userEmail" to request.userEmail,
                    "preferredDate" to request.preferredDate,
                    "preferredTime" to request.preferredTime,
                    "reasonNote" to request.reasonNote,
                    "status" to request.status,
                    "timestamp" to request.timestamp
                )
                firestore.collection("call_requests").document(request.id).set(reqMap)
            } catch (_: Exception) { }
        }
    }
}
