package com.jcs.treinos.di

import com.jcs.treinos.data.firebase.FirebaseFirestoreSource
import com.jcs.treinos.data.firebase.FirestoreDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
interface DataSourceModule {

    @Singleton
    @Binds
    fun bindProductDataSource(dataSource: FirebaseFirestoreSource): FirestoreDataSource

}