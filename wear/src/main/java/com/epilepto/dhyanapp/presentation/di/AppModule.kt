package com.epilepto.dhyanapp.presentation.di

import android.content.Context
import com.epilepto.dhyanapp.presentation.data.repository.FirebaseSessionRepositoryImpl
import com.epilepto.dhyanapp.presentation.domain.repository.FirebaseSessionRepository

interface AppModule {
    val firebaseSessionRepository: FirebaseSessionRepository
}

class AppModuleImpl(
    private val appContext: Context
) : AppModule {
    override val firebaseSessionRepository by lazy {
        FirebaseSessionRepositoryImpl()
    }
}