package com.example.wearit.activites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.wearit.databinding.ActivityStartBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StartActivity : AppCompatActivity() {

    private lateinit var binding : ActivityStartBinding


    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (firebaseAuth.currentUser != null)
        {
            val intent = Intent(this , MainActivity::class.java)
                startActivity(intent)
                finish()
        }

    }


}