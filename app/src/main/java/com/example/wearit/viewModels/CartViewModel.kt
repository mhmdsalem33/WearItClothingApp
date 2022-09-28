package com.example.wearit.viewModels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wearit.data.CartData
import com.example.wearit.data.ClothesData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class CartViewModel () : ViewModel() {

      private val firestore      = FirebaseFirestore.getInstance()
      private val firebaseAuth   = FirebaseAuth.getInstance()
      private var _getCartData   = MutableLiveData<List<CartData>>()
      var getCartData :LiveData<List<CartData>> = _getCartData

    fun getCartDataFromFirestore(){
        firestore.collection("cart").document(firebaseAuth.currentUser!!.uid).collection("myCart")
            .addSnapshotListener{data  , error ->
                if (error != null)
                {
                    Log.d("testApp" , error.message.toString())
                }
                else
                {
                 viewModelScope.launch {
                     if (data != null)
                     {
                         val cartList   = arrayListOf<CartData>()
                         val cartModel  = data.toObjects(CartData::class.java)
                             cartList.addAll(cartModel)
                         _getCartData.value = cartList
                     }
                 }
                 }
            }
    }

    fun updatePricesCart(quantity : Int  , totalPrice :Double , productId :String)
   {
       val   cartMap  = HashMap<String , Any>()
             cartMap["quantity"]       = quantity
             cartMap["totalPrice"]     = totalPrice

        firestore.collection("cart").document(firebaseAuth.currentUser!!.uid)
            .collection("myCart").document(productId)
            .update(cartMap)
            .addOnFailureListener {  error ->
                Log.d("testApp" , error.message.toString())
            }
   }


    fun insertItemToShoppingCartToFirestore(data: ClothesData, context : Context)
    {
        // TODO: 9/27/2022  Important thing maybe i can set data from parameter up to .set() down
        // TODO: 9/27/2022  but i need to equal totalPrice with price down because it's one item will be uploaded
        // TODO: 9/27/2022  and i'm don't need to equal totalPrice with price from adapter because performance
        // TODO: 9/27/2022  will not be nice NOTICE
        val  cartMap = HashMap<String , Any>()
             cartMap["id"]               = data.id
             cartMap["name"]             = data.name
             cartMap["imgUrl"]           = data.imgUrl
             cartMap["secondImage"]      = data.secondImage
             cartMap["thirdImage"]       = data.thirdImage
             cartMap["quantity"]         = data.quantity
             cartMap["price"]            = data.price
             cartMap["totalPrice"]       = data.price
             cartMap["itemSize"]         = data.itemSize

        firestore.collection("cart").document(firebaseAuth.currentUser!!.uid).collection("myCart")
            .document(data.id)
            .set(cartMap)
            .addOnSuccessListener {
                Toast.makeText(context, "item ${data.name} added to your cart success", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {  error ->
                Toast.makeText(context, error.message.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    fun deleteItemFromShoppingCart(id :String , context: Context)
    {
        firestore.collection("cart").document(firebaseAuth.currentUser!!.uid).collection("myCart")
            .document(id)
            .delete()
            .addOnFailureListener { error ->
                Toast.makeText(context, "failed to delete item from shopping cart"+error.message.toString(), Toast.LENGTH_SHORT).show()
            }
            .addOnSuccessListener {
              //  Toast.makeText(context, "Success to delete item from shopping cart", Toast.LENGTH_SHORT).show()
            }
    }

}