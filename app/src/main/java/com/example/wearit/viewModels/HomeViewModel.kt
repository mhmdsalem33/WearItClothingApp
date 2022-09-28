package com.example.wearit.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wearit.data.ClothesData
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor (private val firestore: FirebaseFirestore) : ViewModel(){

    private var _getTopClothes = MutableStateFlow<List<ClothesData>>(emptyList())
    var getTopClothes :StateFlow<List<ClothesData>> = _getTopClothes

    fun getTopClothesData() {
        firestore.collection("HomeClothes").addSnapshotListener{ data , errors ->
            if (errors !=  null)
            {
               Log.d("testApp" , errors.message.toString())
            }
            else
            {
              if (data != null)
              {
               viewModelScope.launch {
                   val topClothesList  = arrayListOf<ClothesData>()
                   val topClothesModel = data.toObjects(ClothesData::class.java)
                       topClothesList.addAll(topClothesModel)
                       _getTopClothes.emit(topClothesList)
               }
              }
            }
        }
    }
}