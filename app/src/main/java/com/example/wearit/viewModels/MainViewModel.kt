package com.example.wearit.viewModels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wearit.data.ClothesData
import com.example.wearit.db.ClothesDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val db : ClothesDatabase) :ViewModel() {

    fun upsert(data: ClothesData) = viewModelScope.launch {
        db.clothesDao().upsert(data)
    }

    fun delete(data : ClothesData) = viewModelScope.launch {
        db.clothesDao().delete(data)
    }

    val readSavedData = db.clothesDao().readSavedClothes()

    suspend  fun deleteAll () = db.clothesDao().deleteAll()

}