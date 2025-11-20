package com.jeyix.schooljeyix.data.di

import com.jeyix.schooljeyix.data.repository.auth.AuthRepositoryImpl
import com.jeyix.schooljeyix.data.repository.enrollment.EnrollmentRepositoryImpl
import com.jeyix.schooljeyix.data.repository.grade.GradeRepositoryImpl
import com.jeyix.schooljeyix.data.repository.notifications.NotificationRepositoryImpl
import com.jeyix.schooljeyix.data.repository.parent.ParentRepositoryImpl
import com.jeyix.schooljeyix.data.repository.section.SectionRepositoryImpl
import com.jeyix.schooljeyix.data.repository.student.StudentRepositoryImpl
import com.jeyix.schooljeyix.data.repository.users.UserRepositoryImpl
import com.jeyix.schooljeyix.domain.repository.AuthRepository
import com.jeyix.schooljeyix.domain.repository.EnrollmentRepository
import com.jeyix.schooljeyix.domain.repository.GradeRepository
import com.jeyix.schooljeyix.domain.repository.NotificationRepository
import com.jeyix.schooljeyix.domain.repository.ParentRepository
import com.jeyix.schooljeyix.domain.repository.SectionRepository
import com.jeyix.schooljeyix.domain.repository.StudentRepository
import com.jeyix.schooljeyix.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

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

    @Binds
    @Singleton
    abstract fun bindNotificationRepository(
        notificationRepositoryImpl: NotificationRepositoryImpl
    ): NotificationRepository
}