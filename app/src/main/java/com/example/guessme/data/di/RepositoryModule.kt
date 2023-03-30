package com.example.guessme.data.di

import com.example.guessme.data.repository.*
import com.example.guessme.domain.repository.*
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

    @Singleton
    @Binds
    abstract fun bindAddPersonRepository(
        addPersonRepositoryImpl: AddPersonRepositoryImpl
    ): AddPersonRepository

    @Singleton
    @Binds
    abstract fun bindPersonDetailRepository(
        personDetailRepositoryImpl: PersonDetailRepositoryImpl
    ): PersonDetailRepository

    @Singleton
    @Binds
    abstract fun bindPeopleListRepository(
        peopleListRepositoryImpl: PeopleListRepositoryImpl
    ): PeopleListRepository

    @Singleton
    @Binds
    abstract fun bindStartQuizRepository(
        startQuizRepositoryImpl: StartQuizRepositoryImpl
    ): StartQuizRepository

    @Singleton
    @Binds
    abstract fun bindScoringRepository(
        scoringRepositoryImpl: ScoringRepositoryImpl
    ): ScoringRepository
}