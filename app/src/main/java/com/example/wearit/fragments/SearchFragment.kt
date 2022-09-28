package com.example.wearit.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wearit.R
import com.example.wearit.adapters.SearchProductsAdapter
import com.example.wearit.data.ClothesData
import com.example.wearit.databinding.FragmentSearchBinding
import com.example.wearit.viewModels.CartViewModel
import com.example.wearit.viewModels.MainViewModel
import com.example.wearit.viewModels.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList


@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var binding       : FragmentSearchBinding
    private val searchMvvm             : SearchViewModel by viewModels()
    private val mainMvvm               : MainViewModel   by viewModels()
    private val cartMvvm               : CartViewModel   by viewModels()
    private lateinit var searchAdapter : SearchProductsAdapter

    var search = "true"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchAdapter = SearchProductsAdapter()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecView()
        onSearchBox()
        getSearchData()
        onSearchItemClick()
        onBackPressedButton()
        onAddItemToSearchItemToFavorite()
        onAddItemSearchToShoppingCart()


    }



    private fun prepareRecView() {
        binding.searchRec.hasFixedSize()
        binding.searchRec.layoutManager = GridLayoutManager(context , 2 , RecyclerView.VERTICAL , false)
        binding.searchRec.adapter       = searchAdapter
    }

    private fun onSearchBox() {
        binding.searchBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val searchText  :String =  binding.searchBox.text.toString()
                searchMvvm.searchInFirestore(searchText.lowercase(Locale.getDefault()) , context!!)
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun getSearchData() {
        searchMvvm.getSearchProductsData.observe(viewLifecycleOwner){data ->
          searchAdapter.searchList(searchList = data as ArrayList<ClothesData>)
        }
    }

    private fun onSearchItemClick() {
        searchAdapter.onItemClick = { data ->
            val fragment =  DetailsFragment()
            val bundle   =  Bundle()
                bundle.putString("id"            , data.id)
                bundle.putString("name"          , data.name)
                bundle.putString("mainImage"     , data.imgUrl)
                bundle.putString("secondImage"   , data.secondImage)
                bundle.putString("thirdImage"    , data.thirdImage)
                bundle.putDouble("price"         , data.price)
            fragment.arguments = bundle
            findNavController().navigate(R.id.action_searchFragment_to_detailsFragment , bundle)
        }
    }

    private fun onBackPressedButton() {
        val callback = object : OnBackPressedCallback(true)
        {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_searchFragment_to_homeFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner , callback)

    }

    private fun onAddItemSearchToShoppingCart() {
        searchAdapter.onAddItemToCart = { data ->
            cartMvvm.insertItemToShoppingCartToFirestore(data , requireContext())
        }
    }

    private fun onAddItemToSearchItemToFavorite() {
        searchAdapter.onAddFavItem = { data ->
            mainMvvm.upsert(data)
            Toast.makeText(requireContext(), "${data.name} is saved success", Toast.LENGTH_SHORT).show()
        }
    }






    override fun onResume() {
        super.onResume()
        if (search == "true")
        {
            onBackPressedButton()
        }
    }

    override fun onPause() {
        super.onPause()
        search = "false"
    }


}