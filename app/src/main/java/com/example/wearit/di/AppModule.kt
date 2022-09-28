package com.example.wearit.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.wearit.db.ClothesDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideFirestore()   = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideDatabase(app:Application)  : ClothesDatabase =
        Room.databaseBuilder(app , ClothesDatabase::class.java , "clothes.db").build()

}
