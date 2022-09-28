package com.example.wearit.fragments.loginRegisterFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ecommerceapp.util.Resource
import com.example.wearit.R
import com.example.wearit.data.User
import com.example.wearit.databinding.FragmentRegisterBinding
import com.example.wearit.viewModels.RegisterViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding : FragmentRegisterBinding
    private val registerMvvm     : RegisterViewModel by viewModels()


    @Inject
    lateinit var firestore: FirebaseFirestore

    @Inject
    lateinit var auth : FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater , container , false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkFieldsState()
        
    }
    private fun checkFieldsState() {
        binding.btnReg.setOnClickListener {
            val firstName   = binding.firstName.text.toString()
            val lastName    = binding.lastName.text.toString()
            val emailReg    = binding.emailReg.text.toString()
            val passwordReg = binding.passwordReg.text.toString()

            if (firstName.isEmpty())
            {
                binding.firstName.apply {
                    requestFocus()
                    error = "First Name can't be empty"
                }
            }
            else if (lastName.isEmpty())
            {
                binding.lastName.apply {
                    requestFocus()
                    error = "Last Name can't be empty"
                }
            }

            else if (emailReg.isEmpty()){
                binding.emailReg.apply {
                    requestFocus()
                    error = "Email can't be empty"
                }
            }
            else if (passwordReg.length < 6)
            {
                binding.passwordReg.apply {
                    requestFocus()
                    error   = "Password can't be less 6 char"
                }
            }
            else
            {
                createNewUser()
            }
        }
    }

    private fun createNewUser() {
        val user = User(
            binding.firstName.text.toString(),
            binding.lastName.text.toString(),
            binding.emailReg.text.toString())

        val email    = binding.emailReg.text.toString()
        val password = binding.passwordReg.text.toString()

        binding.btnReg.startAnimation()
        auth.createUserWithEmailAndPassword(email , password)
            .addOnSuccessListener {
             lifecycleScope.launchWhenStarted {
                 registerMvvm.saveUserInformation(user , requireContext())
                 delay(500)
                 binding.btnReg.revertAnimation()
                 findNavController().navigate(R.id.from_registerFragment_to_loginFragment)
             }
            }
            .addOnFailureListener {
                binding.btnReg.revertAnimation()
                Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
            }
    }

}