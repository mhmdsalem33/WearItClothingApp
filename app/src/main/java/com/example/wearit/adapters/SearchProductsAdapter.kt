package com.example.wearit.adapters

import android.view.LayoutInflater

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wearit.data.ClothesData
import com.example.wearit.databinding.TopClothingRowBinding

class SearchProductsAdapter():RecyclerView.Adapter<SearchProductsAdapter.ViewHolder>() {

    lateinit var onItemClick      :((ClothesData) -> Unit)
    lateinit var onAddFavItem     :((ClothesData) -> Unit)
    lateinit var onAddItemToCart  :((ClothesData) -> Unit)

    private var searchList = ArrayList<ClothesData>()
    fun searchList(searchList :ArrayList<ClothesData>){
        this.searchList = searchList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding : TopClothingRowBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      return  ViewHolder(TopClothingRowBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = searchList[position]

       Glide.with(holder.itemView)
           .load(data.imgUrl)
           .into(holder.binding.imgTopClothing)

        holder.binding.apply {
            tvName.text =  data.name
            tvPrice.text = data.price.toString()
        }

        // TODO: 9/27/2022  handle click on search itemView
        holder.itemView.setOnClickListener {
              onItemClick.invoke(searchList[position])
        }

        // TODO: 9/27/2022 handle click on add favorite image
        holder.binding.icFavorite.setOnClickListener {
            onAddFavItem.invoke(data)
        }

        // TODO: 9/27/2022 handle click on add to cart image
        holder.binding.icAddToCart.setOnClickListener {
            onAddItemToCart.invoke(data)
        }
    }
    override fun getItemCount() = searchList.size
}