package com.example.wearit.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.wearit.R
import com.example.wearit.databinding.ActivityMainBinding
import com.example.wearit.viewModels.CartViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding :ActivityMainBinding
    //todo don't remove it!!
    val cartMvvm : CartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigation =  binding.bottomNavigation
        val findNavigation   =  Navigation.findNavController(this , R.id.fragmentContainerView)
        NavigationUI.setupWithNavController(bottomNavigation , findNavigation)
    }
}