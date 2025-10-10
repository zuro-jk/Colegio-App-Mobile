package com.jeyix.schooljeyix.data.di

import com.jeyix.schooljeyix.data.remote.feature.auth.api.AuthApi
import com.jeyix.schooljeyix.data.remote.feature.enrollment.api.EnrollmentApi
import com.jeyix.schooljeyix.data.remote.feature.student.api.StudentApi
import com.jeyix.schooljeyix.data.repository.auth.AuthRepositoryImpl
import com.jeyix.schooljeyix.data.repository.enrollment.EnrollmentRepositoryImpl
import com.jeyix.schooljeyix.data.repository.student.StudentRepositoryImpl
import com.jeyix.schooljeyix.domain.usecase.auth.AuthRepository
import com.jeyix.schooljeyix.domain.usecase.enrollment.EnrollmentRepository
import com.jeyix.schooljeyix.domain.usecase.student.StudentRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(api: AuthApi): AuthRepository {
        return AuthRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideEnrollmentRepository(api: EnrollmentApi): EnrollmentRepository {
        return EnrollmentRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideStudentRepository(api: StudentApi): StudentRepository {
        return StudentRepositoryImpl(api)
    }
}