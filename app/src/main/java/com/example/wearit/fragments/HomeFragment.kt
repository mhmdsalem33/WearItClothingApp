package com.example.wearit.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.graphics.Color
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wearit.R
import com.example.wearit.SpaceItemDecoration
import com.example.wearit.activites.StartActivity
import com.example.wearit.adapters.TopClothingAdapter
import com.example.wearit.data.ClothesData
import com.example.wearit.databinding.FragmentHomeBinding
import com.example.wearit.viewModels.CartViewModel
import com.example.wearit.viewModels.HomeViewModel
import com.example.wearit.viewModels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding

    private val homeMvvm : HomeViewModel by viewModels()
    private val mainMvvm : MainViewModel by viewModels()
    private val cartMvvm : CartViewModel by viewModels()
    private lateinit var topClothingAdapter: TopClothingAdapter


    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        topClothingAdapter = TopClothingAdapter()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater , container , false)
        return  binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClothingRecView()
        getTopClothingData()
        onTopClothingClick()
        onFavItemClick()
        onAddToCartClick()
        onLogoutClick()


        binding.search.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }



    }

    private fun onLogoutClick() {
        binding.logout.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Log out")
                    .setMessage("Are you want to logout?")
                    .setPositiveButton("Yes"){ dialog , _ ->
                            firebaseAuth.signOut()
                        val intent = Intent(requireContext() , StartActivity::class.java)
                            startActivity(intent)
                            activity?.finish()
                        dialog.dismiss()
                    }.setCancelable(false)
                    .setNegativeButton("No"){dialog  , _ -> dialog.dismiss() }
            val dialog = builder.create()
                dialog.show()
        }
    }


    private fun onAddToCartClick() {
        topClothingAdapter.onAddToCartClick = { data ->
           cartMvvm.insertItemToShoppingCartToFirestore(data , requireContext())
        }
    }

    private fun onFavItemClick() {
        topClothingAdapter.onFavItemClick = { data ->
              mainMvvm.upsert(data)
              Snackbar.make(requireView(), data.name + " is Saved" , Snackbar.LENGTH_SHORT)
                  .setBackgroundTint(resources.getColor(R.color.orange)).show()
        }
    }

    private fun onTopClothingClick() {
        topClothingAdapter.onTopClothingCLick = { data ->
             val fragment =  DetailsFragment()
             val bundle   =  Bundle()
                 bundle.putString("id" , data.id)
                 bundle.putString("name"          , data.name)
                 bundle.putString("mainImage"     , data.imgUrl)
                 bundle.putString("secondImage"   , data.secondImage)
                 bundle.putString("thirdImage"    , data.thirdImage)
                 bundle.putDouble("price"         , data.price)
             fragment.arguments = bundle
           findNavController().navigate(R.id.action_homeFragment_to_detailsFragment , bundle)
        }
    }

    private fun setupClothingRecView() {
        binding.topClothingRec.apply {
            layoutManager = GridLayoutManager(context , 2 , RecyclerView.VERTICAL , false)
            adapter       = topClothingAdapter
            addItemDecoration(SpaceItemDecoration())
        }
    }

    private fun getTopClothingData() {
        homeMvvm.getTopClothesData()
        lifecycleScope.launchWhenStarted {
            homeMvvm.getTopClothes.collect{ data ->
                if (data.isNotEmpty())
                {
                    topClothingAdapter.setClothingList(clothesList =  data as ArrayList<ClothesData>)
                }
            }
        }
    }


}