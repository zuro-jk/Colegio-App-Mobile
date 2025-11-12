package com.jeyix.schooljeyix.data.di

import com.jeyix.schooljeyix.data.remote.feature.auth.api.AuthApi
import com.jeyix.schooljeyix.data.remote.feature.enrollment.api.EnrollmentApi
import com.jeyix.schooljeyix.data.remote.feature.parent.api.ParentApi
import com.jeyix.schooljeyix.data.remote.feature.student.api.StudentApi
import com.jeyix.schooljeyix.data.remote.feature.users.api.UserApi
import com.jeyix.schooljeyix.data.repository.auth.AuthRepositoryImpl
import com.jeyix.schooljeyix.data.repository.enrollment.EnrollmentRepositoryImpl
import com.jeyix.schooljeyix.data.repository.grade.GradeRepositoryImpl
import com.jeyix.schooljeyix.data.repository.parent.ParentRepositoryImpl
import com.jeyix.schooljeyix.data.repository.section.SectionRepositoryImpl
import com.jeyix.schooljeyix.data.repository.student.StudentRepositoryImpl
import com.jeyix.schooljeyix.data.repository.users.UserRepositoryImpl
import com.jeyix.schooljeyix.domain.usecase.auth.AuthRepository
import com.jeyix.schooljeyix.domain.usecase.enrollment.EnrollmentRepository
import com.jeyix.schooljeyix.domain.usecase.grade.GradeRepository
import com.jeyix.schooljeyix.domain.usecase.parent.ParentRepository
import com.jeyix.schooljeyix.domain.usecase.section.SectionRepository
import com.jeyix.schooljeyix.domain.usecase.student.StudentRepository
import com.jeyix.schooljeyix.domain.usecase.users.UserRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindEnrollmentRepository(
        enrollmentRepositoryImpl: EnrollmentRepositoryImpl
    ): EnrollmentRepository

    @Binds
    @Singleton
    abstract fun bindStudentRepository(
        studentRepositoryImpl: StudentRepositoryImpl
    ): StudentRepository

    @Binds
    @Singleton
    abstract fun bindParentRepository(
        parentRepositoryImpl: ParentRepositoryImpl
    ): ParentRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindGradeRepository(
        gradeRepositoryImpl: GradeRepositoryImpl
    ): GradeRepository

    @Binds
    @Singleton
    abstract fun bindSectionRepository(
        sectionRepositoryImpl: SectionRepositoryImpl
    ): SectionRepository
}