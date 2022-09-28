package com.example.wearit.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wearit.data.ClothesData
import com.example.wearit.databinding.TopClothingRowBinding
import com.example.wearit.databinding.TopSallerRowBinding
import com.example.wearit.viewModels.MainViewModel

class TopSellerAdapter() : RecyclerView.Adapter<TopSellerAdapter.ViewHolder>(){

// TODO: 9/27/2022 this adapter for topProducts fragment for Top Design

    lateinit var onTopSellsClick           :((ClothesData)  -> Unit)
    lateinit var onAddToFavorite           :((ClothesData)  -> Unit)
    lateinit var onAddToCartTopSeller      :((ClothesData)  -> Unit)

    class ViewHolder (val binding : TopSallerRowBinding) :RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object :DiffUtil.ItemCallback<ClothesData>(){
        override fun areItemsTheSame(oldItem: ClothesData, newItem: ClothesData): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: ClothesData, newItem: ClothesData): Boolean {
          return  oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this , diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return ViewHolder(TopSallerRowBinding.inflate(LayoutInflater.from(parent.context) , parent  , false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = differ.currentList[position]

        Glide.with(holder.itemView).load(data.imgUrl).into(holder.binding.imgTopClothing)

        holder.binding.apply {
            tvName.text  = data.name
            tvPrice.text = "$${data.price}"
        }

        // TODO: 9/27/2022 on click on itemView we will intent to fragment details to show data 
        holder.itemView.setOnClickListener {
            onTopSellsClick.invoke(data)
        }
        // TODO: 9/27/2022 we will add item to room database 
        holder.binding.icFavorite.setOnClickListener {
            onAddToFavorite.invoke(data)
        }
        // TODO: 9/27/2022 we will add item to shopping cart in our firestore database 
        holder.binding.addToCart.setOnClickListener { 
            onAddToCartTopSeller.invoke(data)
        }
        // TODO: 9/27/2022 all previous clicks will handle from topProduct fragment
    }
    override fun getItemCount() = differ.currentList.size
}