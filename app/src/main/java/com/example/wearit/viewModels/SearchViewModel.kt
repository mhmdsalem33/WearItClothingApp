package com.example.wearit.viewModels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wearit.data.ClothesData
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val firestore: FirebaseFirestore) :ViewModel() {

    private var _getSearchProductsData = MutableLiveData<List<ClothesData>>()
    var getSearchProductsData : LiveData<List<ClothesData>> = _getSearchProductsData
     fun searchInFirestore(searchText: String , context: Context) {
        firestore.collection("AllProducts")
            .orderBy("searchName")
            .startAt(searchText)
            .endAt("$searchText\uf8ff")
            .limit(10)
            .get()
            .addOnCompleteListener {
                val data = it.result.toObjects(ClothesData::class.java)
                _getSearchProductsData.value = data
            }
            .addOnFailureListener{
                Toast.makeText(context, ""+it.message, Toast.LENGTH_SHORT).show()
            }
    }
}