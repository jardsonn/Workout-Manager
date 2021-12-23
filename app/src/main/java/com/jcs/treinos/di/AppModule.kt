package com.jcs.treinos.di

import com.google.firebase.firestore.FirebaseFirestore
import com.jcs.treinos.data.firebase.FirebaseAuthSource
import com.jcs.treinos.data.firebase.FirebaseFirestoreSource
import com.jcs.treinos.data.firebase.FirestoreDataSource
import com.jcs.treinos.data.repositories.AuthRepository
import com.jcs.treinos.data.repositories.FirestoreRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAuthRepository(source: FirebaseAuthSource) = AuthRepository(source)

    @Provides
    @Singleton
    fun provideFirebaseAuthSource() = FirebaseAuthSource()

    @Provides
    @Singleton
    fun provideFirestoreRepository(source: FirebaseFirestoreSource) = FirestoreRepository(source)

//    @Provides
//    @Singleton
//    fun provideFirestoreSource(firestore: FirebaseFirestore) = FirebaseFirestoreSource(firestore)


    @Singleton
    @Provides
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()


}