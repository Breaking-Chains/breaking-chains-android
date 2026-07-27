package com.breakingchains.app.di

import android.content.Context
import com.breakingchains.app.data.local.AppDatabase
import com.breakingchains.app.data.repository.AuthRepository
import com.breakingchains.app.data.repository.AuthRepositoryImpl
import com.breakingchains.app.data.repository.CallRequestRepository
import com.breakingchains.app.data.repository.CallRequestRepositoryImpl
import com.breakingchains.app.data.repository.TrackerRepository
import com.breakingchains.app.data.repository.TrackerRepositoryImpl
import com.google.firebase.firestore.FirebaseFirestore

interface AppContainer {
    val database: AppDatabase
    val authRepository: AuthRepository
    val trackerRepository: TrackerRepository
    val callRequestRepository: CallRequestRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {

    private val firestore: FirebaseFirestore? by lazy {
        runCatching { FirebaseFirestore.getInstance() }.getOrNull()
    }

    override val database: AppDatabase by lazy {
        AppDatabase.getInstance(context)
    }

    override val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(
            userDao = database.userDao(),
            firestore = firestore
        )
    }

    override val trackerRepository: TrackerRepository by lazy {
        TrackerRepositoryImpl(
            relapseLogDao = database.relapseLogDao(),
            userDao = database.userDao(),
            firestore = firestore
        )
    }

    override val callRequestRepository: CallRequestRepository by lazy {
        CallRequestRepositoryImpl(
            callRequestDao = database.callRequestDao(),
            firestore = firestore
        )
    }
}
