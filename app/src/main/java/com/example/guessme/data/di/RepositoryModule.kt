package com.example.guessme.data.di

import com.example.guessme.data.repository.LocalRepositoryImpl
import com.example.guessme.data.repository.LogInRepositoryImpl
import com.example.guessme.data.repository.SignUpRepositoryImpl
import com.example.guessme.domain.repository.LocalRepository
import com.example.guessme.domain.repository.LogInRepository
import com.example.guessme.domain.repository.SignUpRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindLogInRepository(
        logInRepositoryImpl: LogInRepositoryImpl
    ): LogInRepository

    @Singleton
    @Binds
    abstract fun bindSignUpRepository(
        signupRepositoryImpl: SignUpRepositoryImpl
    ): SignUpRepository

    @Singleton
    @Binds
    abstract fun bindLocalRepository(
        localRepositoryImpl: LocalRepositoryImpl
    ): LocalRepository
}