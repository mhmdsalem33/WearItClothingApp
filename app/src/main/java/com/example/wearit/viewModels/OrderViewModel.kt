package com.example.wearit.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.wearit.data.CartData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth     : FirebaseAuth
    ) : ViewModel() {
    fun uploadOrdersToFirestore(data : List<CartData>){
        for (i in data)
        {
            val orderMap = HashMap<String , Any>()
                orderMap["id"]          = i.id
                orderMap["name"]        = i.name
                orderMap["imgUrl"]      = i.imgUrl
                orderMap["itemSize"]    = i.itemSize
                orderMap["quantity"]    = i.quantity
                orderMap["secondImage"] = i.secondImage
                orderMap["thirdImage"]  = i.thirdImage
                orderMap["totalPrice"]  = i.totalPrice
            firestore.collection("Orders").document(auth.currentUser!!.uid)
                .collection("myOrders")
                .add(orderMap)
                .addOnSuccessListener {
                    Log.d("testApp" , "Success to proccess order")
                }
                .addOnFailureListener {
                    Log.d("testApp" , it.message.toString())
                }
        }
    }
}