package com.example.wearit.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wearit.data.ClothesData
import com.example.wearit.databinding.TopClothingRowBinding

class ProductsAdapter() : RecyclerView.Adapter<ProductsAdapter.ViewHolder>(){

    // TODO: 9/27/2022 this adapter for topProducts fragment

    lateinit var onProductClick               :((ClothesData) -> Unit)
    lateinit var onFavoriteItemClickProducts  :((ClothesData) -> Unit)
    lateinit var onAddToCartClickProducts     :((ClothesData) -> Unit)

    class ViewHolder (val binding : TopClothingRowBinding) :RecyclerView.ViewHolder(binding.root)

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
       return ViewHolder(TopClothingRowBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = differ.currentList[position]
        Glide.with(holder.itemView)
            .load(data.imgUrl)
            .into(holder.binding.imgTopClothing)

        holder.binding.apply {
            tvName.text  = data.name
            tvPrice.text = "$${data.price}"
        }
        // TODO: 9/27/2022 handle click for itemView
        holder.itemView.setOnClickListener {
            onProductClick.invoke(data)
        }
        // TODO: 9/27/2022 handle click for favorite item
        holder.binding.icFavorite.setOnClickListener {
            onFavoriteItemClickProducts.invoke(data)
        }

        // TODO: 9/27/2022 handle click for add to cart item
        holder.binding.icAddToCart.setOnClickListener {
            onAddToCartClickProducts.invoke(data)
        }
        //todo all previous clicks will handle from top products fragment
    }

    override fun getItemCount() = differ.currentList.size

}