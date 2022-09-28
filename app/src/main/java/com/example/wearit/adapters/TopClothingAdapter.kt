package com.example.wearit.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wearit.data.ClothesData
import com.example.wearit.databinding.TopClothingRowBinding


class TopClothingAdapter() : RecyclerView.Adapter<TopClothingAdapter.ViewHolder>(){

    lateinit var onFavItemClick     :((ClothesData) -> Unit)
    lateinit var onTopClothingCLick :((ClothesData) -> Unit)
    lateinit var onAddToCartClick   :((ClothesData) -> Unit)

    class ViewHolder (val binding : TopClothingRowBinding) :RecyclerView.ViewHolder(binding.root)

    private var topClothingList = ArrayList<ClothesData>()
    fun setClothingList(clothesList :ArrayList<ClothesData>){
        this.topClothingList = clothesList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return ViewHolder(TopClothingRowBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data  = topClothingList[position]

        Glide.with(holder.itemView)
            .load(data.imgUrl)
            .into(holder.binding.imgTopClothing)

        holder.binding.apply {
            tvName.text  = data.name
            tvPrice.text = "$${data.price}"
        }

        holder.itemView.setOnClickListener {
            onTopClothingCLick.invoke(data)
        }

        holder.binding.icFavorite.setOnClickListener {
            onFavItemClick.invoke(data)
        }

        holder.binding.icAddToCart.setOnClickListener {
            onAddToCartClick.invoke(data)
        }
    }
    override fun getItemCount() =   topClothingList.size
}