package com.example.wearit.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wearit.data.ClothesData
import com.example.wearit.databinding.FavoriteRowBinding
import com.example.wearit.databinding.TopClothingRowBinding
import com.squareup.picasso.Picasso

class FavoriteAdapter() :RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    lateinit var onFavoriteItemClick : ((ClothesData) -> Unit)
    lateinit var onAddToCartClick    : ((ClothesData) -> Unit)

    class ViewHolder (val binding : FavoriteRowBinding) :RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<ClothesData>(){
        override fun areItemsTheSame(oldItem: ClothesData, newItem: ClothesData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ClothesData, newItem: ClothesData): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this , diffUtil)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteAdapter.ViewHolder {
      return ViewHolder(FavoriteRowBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: FavoriteAdapter.ViewHolder, position: Int) {
        // هخلى السعر النهائى = سعر القطعة الواحدة عشان لما ارفعو على الكارت
        val data             = differ.currentList[position]
        Glide.with(holder.itemView).load(data.imgUrl).into(holder.binding.imgTopClothing)

        holder.binding.apply {
            tvName.text  = data.name
            tvPrice.text = "$${data.price}"
        }

        holder.itemView.setOnClickListener {
            onFavoriteItemClick.invoke(data)
        }
        holder.binding.icAddToCart.setOnClickListener {
            onAddToCartClick.invoke(data)
        }
    }
    override fun getItemCount() = differ.currentList.size
}