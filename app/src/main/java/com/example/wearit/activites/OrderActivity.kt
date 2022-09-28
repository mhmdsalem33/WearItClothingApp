package com.example.wearit.activites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.example.wearit.data.CartData
import com.example.wearit.databinding.ActivityOrderBinding
import com.example.wearit.viewModels.OrderViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OrderActivity : AppCompatActivity() {

    private lateinit var binding : ActivityOrderBinding
    private val orderMvvm        : OrderViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val list : List<CartData>? =intent.getSerializableExtra("Order") as ArrayList<CartData>?
        if (list != null && list.isNotEmpty())
        {
            orderMvvm.uploadOrdersToFirestore(list)
        }



        binding.shop.setOnClickListener {
            val intent = Intent(this , MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finishAffinity()
        }

    }
}