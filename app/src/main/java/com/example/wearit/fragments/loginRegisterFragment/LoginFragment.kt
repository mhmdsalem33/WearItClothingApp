package com.example.wearit.fragments.loginRegisterFragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.ecommerceapp.util.Resource
import com.example.wearit.R
import com.example.wearit.activites.MainActivity
import com.example.wearit.databinding.FragmentLoginBinding
import com.example.wearit.viewModels.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding : FragmentLoginBinding
    private val loginMvvm : LoginViewModel by viewModels()

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            val email    = binding.emailLog.text.toString()
            val password = binding.passwordLog.text.toString()
            if (email.isEmpty())
            {
                binding.emailLog.apply {
                    requestFocus()
                    error = "Email can't be empty"
                }
            }
            else if (password.length < 6)
            {
                binding.passwordLog.apply {
                    requestFocus()
                    error = "Password can't be less 6 char" }
            }
               else
            {
                  loginMvvm.login(email, password)
            }
        }


        lifecycleScope.launchWhenStarted {
            loginMvvm.login.collect{
                when(it){
                    is Resource.Loading -> {binding.btnLogin.startAnimation()}
                    is Resource.Success -> {
                        binding.btnLogin.revertAnimation()
                        val intent = Intent(context , MainActivity::class.java)
                            startActivity(intent)
                            activity?.finish()
                    }
                    is Resource.Error -> {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                        binding.btnLogin.revertAnimation()
                    }
                    else -> Unit
                }
            }
        }
    }

}