package com.example.wearit.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.wearit.data.ClothesData

@Database(entities = [ClothesData::class] , version = 1)
abstract class ClothesDatabase  : RoomDatabase(){
    abstract fun clothesDao() : ClothesDao
}