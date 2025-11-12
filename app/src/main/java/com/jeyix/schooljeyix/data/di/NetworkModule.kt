package com.jeyix.schooljeyix.data.di

import android.content.Context
import com.jeyix.schooljeyix.data.local.datastore.UserPreferences
import com.jeyix.schooljeyix.data.remote.core.AuthInterceptor
import com.jeyix.schooljeyix.data.remote.core.UnauthorizedInterceptor
import com.jeyix.schooljeyix.data.remote.feature.auth.api.AuthApi
import com.jeyix.schooljeyix.data.remote.feature.enrollment.api.EnrollmentApi
import com.jeyix.schooljeyix.data.remote.feature.grade.api.GradeApi
import com.jeyix.schooljeyix.data.remote.feature.parent.api.ParentApi
import com.jeyix.schooljeyix.data.remote.feature.section.api.SectionApi
import com.jeyix.schooljeyix.data.remote.feature.student.api.StudentApi
import com.jeyix.schooljeyix.data.remote.feature.users.api.UserApi
import com.jeyix.schooljeyix.domain.usecase.auth.LogoutUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideAuthInterceptor(userPreferences: UserPreferences): AuthInterceptor {
        return AuthInterceptor(userPreferences)
    }

    @Provides
    @Singleton
    fun provideUnauthorizedInterceptor(
        @ApplicationContext context: Context,
        userPreferences: UserPreferences
    ): UnauthorizedInterceptor {
        return UnauthorizedInterceptor(context, userPreferences)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        unauthorizedInterceptor: UnauthorizedInterceptor
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(unauthorizedInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080/api/v1/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideEnrollmentApi(retrofit: Retrofit): EnrollmentApi = retrofit.create(EnrollmentApi::class.java)

    @Provides
    @Singleton
    fun provideStudentApi(retrofit: Retrofit): StudentApi = retrofit.create(StudentApi::class.java)

    @Provides
    @Singleton
    fun provideParentApi(retrofit: Retrofit): ParentApi = retrofit.create(ParentApi::class.java)

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi = retrofit.create(UserApi::class.java)

    @Provides
    @Singleton
    fun provideGradeApi(retrofit: Retrofit): GradeApi = retrofit.create(GradeApi::class.java)

    @Provides
    @Singleton
    fun provideSectionApi(retrofit: Retrofit): SectionApi = retrofit.create(SectionApi::class.java)
}