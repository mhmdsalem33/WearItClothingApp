package com.example.wearit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.*
import com.example.wearit.R
import com.example.wearit.adapters.FavoriteAdapter
import com.example.wearit.databinding.FragmentFavoriteBinding
import com.example.wearit.viewModels.CartViewModel
import com.example.wearit.viewModels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class FavoriteFragment : Fragment() {


    private lateinit var binding : FragmentFavoriteBinding
    private val mainMvvm         : MainViewModel by viewModels()
    private val cartMvvm         : CartViewModel by viewModels()
    private lateinit var favoriteAdapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoriteAdapter = FavoriteAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteBinding.inflate(inflater , container , false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




            setupFavoriteRecView()
            getFavoriteSavedData()
            onItemFavoriteClick()
            itemTouchHelper()
            onAddToCartCLick()






    }

    private fun onAddToCartCLick() {
        favoriteAdapter.onAddToCartClick = { data ->
            cartMvvm.insertItemToShoppingCartToFirestore(data , requireContext())
        }
    }

    private fun itemTouchHelper() {
        val itemTouchHelper = object  :ItemTouchHelper.SimpleCallback(0 ,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT or
                    ItemTouchHelper.UP or ItemTouchHelper.DOWN){
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
               return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                val  currentPosition = favoriteAdapter.differ.currentList[position]
                     mainMvvm.delete(currentPosition)   //delete item from database
                Snackbar.make(binding.root , "Item Removed" , Snackbar.LENGTH_LONG).setAction(
                    "Restore" , View.OnClickListener {
                          mainMvvm.upsert(currentPosition)
                    }
                ).show()
            }
        }
       ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.favoriteRec)
    }

    private fun onItemFavoriteClick() {
        favoriteAdapter.onFavoriteItemClick = { data ->
            val fragment =  DetailsFragment()
            val bundle   =  Bundle()
                bundle.putString("id"            , data.id)
                bundle.putString("name"          , data.name)
                bundle.putString("mainImage"     , data.imgUrl)
                bundle.putString("secondImage"   , data.secondImage)
                bundle.putString("thirdImage"    , data.thirdImage)
                bundle.putDouble("price"         , data.price)
            fragment.arguments = bundle
            findNavController().navigate(R.id.detailsFragment , bundle)

        }
    }

    private fun getFavoriteSavedData() {
        lifecycleScope.launchWhenStarted {
            mainMvvm.readSavedData.collect{ savedData ->
                favoriteAdapter.differ.submitList(savedData)
                binding.favoriteCount.text = savedData.size.toString()
            }
        }

    }

    private fun setupFavoriteRecView() {
        binding.favoriteRec.apply {
            layoutManager = GridLayoutManager(context , 2 , RecyclerView.VERTICAL , false)
            adapter       = favoriteAdapter
        }

    }

}