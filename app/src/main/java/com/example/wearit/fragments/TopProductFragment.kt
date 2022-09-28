package com.example.wearit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wearit.R
import com.example.wearit.SpaceItemDecoration
import com.example.wearit.adapters.ProductsAdapter
import com.example.wearit.adapters.TopSellerAdapter
import com.example.wearit.databinding.FragmentTopProductBinding
import com.example.wearit.viewModels.CartViewModel
import com.example.wearit.viewModels.MainViewModel
import com.example.wearit.viewModels.TopProductsFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TopProductFragment : Fragment() {

    private lateinit var binding            : FragmentTopProductBinding
    private lateinit var topClothingAdapter : TopSellerAdapter
    private lateinit var productsAdapter    : ProductsAdapter
    private val topProductFragmentMvvm      : TopProductsFragmentViewModel by viewModels()
    private val mainMvvm                    : MainViewModel by viewModels()
    private val cartMvvm                    : CartViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        topClothingAdapter = TopSellerAdapter()
        productsAdapter    = ProductsAdapter()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTopProductBinding.inflate(inflater , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTopSellerRecView()
        getDataTopSeller()
        onTopSellerCLick()
        onAddToFavoriteClickOnTopSeller()
        onAddItemFromTopSellerToShoppingCartOnFirestore()


        setupProductsRecView()
        getDataProducts()
        onProductItemClick()
        onaAddToFavoriteClickOnProducts()
        onAddItemFromProductsToShoppingCartOnFireStore()

        onSearchIconClick()



    }

    private fun onSearchIconClick() {
       binding.search.setOnClickListener {
           findNavController().navigate(R.id.action_topProductFragment_to_searchFragment)
       }
    }

    private fun onAddItemFromProductsToShoppingCartOnFireStore() {
     productsAdapter.onAddToCartClickProducts = { data ->
                cartMvvm.insertItemToShoppingCartToFirestore(data , requireContext())
     }
    }

    private fun onaAddToFavoriteClickOnProducts() {
        productsAdapter.onFavoriteItemClickProducts = { data ->
            mainMvvm.upsert(data)
            Toast.makeText(requireContext(), "${data.name }added success to your favorites", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onAddItemFromTopSellerToShoppingCartOnFirestore() {
        topClothingAdapter.onAddToCartTopSeller = { data ->
            cartMvvm.insertItemToShoppingCartToFirestore(data , requireContext())
        }
    }

    private fun onAddToFavoriteClickOnTopSeller() {
        topClothingAdapter.onAddToFavorite = { data ->
            mainMvvm.upsert(data)
            Toast.makeText(requireContext(), "${data.name }added success to your favorites", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onTopSellerCLick() {
        topClothingAdapter.onTopSellsClick = { data ->
            val fragment = DetailsFragment()
            val bundle   = Bundle()
                bundle.putString("id" , data.id)
                bundle.putString("name"          , data.name)
                bundle.putString("mainImage"     , data.imgUrl)
                bundle.putString("secondImage"   , data.secondImage)
                bundle.putString("thirdImage"    , data.thirdImage)
                bundle.putDouble("price"         , data.price)
            fragment.arguments = bundle
            findNavController().navigate(R.id.detailsFragment , bundle)
        }
    }

    private fun onProductItemClick() {
        productsAdapter.onProductClick = { data ->
            val fragment = DetailsFragment()
            val bundle   = Bundle()
                bundle.putString("id" , data.id)
                bundle.putString("name"          , data.name)
                bundle.putString("mainImage"     , data.imgUrl)
                bundle.putString("secondImage"   , data.secondImage)
                bundle.putString("thirdImage"    , data.thirdImage)
                bundle.putDouble("price"         , data.price)
            fragment.arguments = bundle
            findNavController().navigate(R.id.detailsFragment , bundle)
        }
    }

    private fun getDataProducts() {
        topProductFragmentMvvm.getProductsFromFireStore()
        lifecycleScope.launchWhenStarted {
            topProductFragmentMvvm.getProducts.collect{ data ->
                productsAdapter.differ.submitList(data)
            }
        }
    }

    private fun setupProductsRecView() {
        binding.recProducts.apply {
            layoutManager = GridLayoutManager(context , 2 , RecyclerView.VERTICAL , false)
            adapter       = productsAdapter
            addItemDecoration(SpaceItemDecoration())
        }
    }


    private fun setupTopSellerRecView() {
        binding.topSelleRec.apply {
            layoutManager = LinearLayoutManager(context , RecyclerView.HORIZONTAL , false)
            adapter       = topClothingAdapter
        }
    }
    private fun getDataTopSeller() {
        lifecycleScope.launchWhenStarted {
            topProductFragmentMvvm.getTopSellsDataFromFireStore()
            topProductFragmentMvvm.getTopSellsProducts.collect{ data ->
                topClothingAdapter.differ.submitList(data)
            }
        }
    }




}