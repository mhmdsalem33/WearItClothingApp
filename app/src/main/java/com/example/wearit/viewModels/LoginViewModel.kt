package com.example.wearit.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseAuth : FirebaseAuth) : ViewModel()
{

     private val _login = MutableSharedFlow<Resource<FirebaseUser>>()
     val login          = _login.asSharedFlow()

    fun login(email : String , password : String){
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { user ->
                   viewModelScope.launch {
                     user.user?.let {
                         _login.emit(Resource.Success(it))
                     }
                   }
                }
                .addOnFailureListener { error ->
                    viewModelScope.launch {
                       _login.emit(Resource.Error(error.message.toString()))
                    }
                }
    }
}