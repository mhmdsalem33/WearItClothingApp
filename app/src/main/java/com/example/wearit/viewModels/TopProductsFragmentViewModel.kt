package com.example.wearit.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.wearit.data.ClothesData
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopProductsFragmentViewModel @Inject constructor(private val firestore: FirebaseFirestore) :ViewModel() {

    private var _getTopSellsProducts = MutableStateFlow<List<ClothesData>>(emptyList())
    var getTopSellsProducts : StateFlow<List<ClothesData>> = _getTopSellsProducts

    fun getTopSellsDataFromFireStore()
    {
        firestore.collection("TopClothes").addSnapshotListener{data , errors ->
            if (errors != null)
            {
                Log.d("testApp" , errors.message.toString() )
            }
            else
            {
               if (data != null)
               {

                   viewModelScope.launch {
                       val topSellsList  =    arrayListOf<ClothesData>()
                       val topSellsModel =    data.toObjects(ClothesData::class.java)
                           topSellsList.addAll(topSellsModel)
                       _getTopSellsProducts.emit(topSellsList)
                   }
               }
            }
        }
    }

    private var _getProducts = MutableStateFlow<List<ClothesData>>(emptyList())
    var getProducts : StateFlow<List<ClothesData>>  = _getProducts

    fun getProductsFromFireStore()
    {
        firestore.collection("AllProducts").addSnapshotListener{data , errors ->
            if (errors != null)
            {
                Log.d("testApp" , errors.message.toString() )
            }
            else
            {
                if (data != null)
                {
                    viewModelScope.launch {
                        val productsList  =    arrayListOf<ClothesData>()
                        val productsModel =    data.toObjects(ClothesData::class.java)
                            productsList.addAll(productsModel)
                         _getProducts.emit(productsList)
                    }
                }
            }
        }
    }
}