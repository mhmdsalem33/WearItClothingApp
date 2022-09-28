package com.example.wearit.db

import androidx.room.*
import com.example.wearit.data.ClothesData
import kotlinx.coroutines.flow.Flow

@Dao
interface ClothesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(clothesData: ClothesData)

    @Delete
    suspend fun delete(clothesData: ClothesData)

    @Query("SELECT * FROM  clothesInformation")
     fun readSavedClothes()  : Flow<List<ClothesData>>

     @Query("DELETE FROM clothesInformation")
     suspend fun deleteAll()
}
