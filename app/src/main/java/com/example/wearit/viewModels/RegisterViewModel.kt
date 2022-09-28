package com.example.wearit.viewModels


import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.util.*
import com.example.wearit.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth  :FirebaseAuth,
    private val firestore     :FirebaseFirestore
):ViewModel()
{
     fun saveUserInformation(user: User , context: Context){
        val userMap = HashMap<String , Any>()
            userMap["firstName"] = user.firstName
            userMap["lastName"]  = user.lastName
            userMap["email"]     = user.email
        firestore.collection("Users").document(firebaseAuth.currentUser!!.uid)
            .set(userMap)
            .addOnFailureListener { error ->
                Toast.makeText(context, error.message.toString(), Toast.LENGTH_SHORT).show()
            }
    }
}