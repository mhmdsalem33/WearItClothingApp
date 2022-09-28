package com.example.wearit.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wearit.activites.MainActivity
import com.example.wearit.data.CartData
import com.example.wearit.databinding.CartRowBinding
import com.example.wearit.viewModels.CartViewModel
import kotlin.math.roundToInt

class CartAdapter() :RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    private lateinit var cartMvvm     : CartViewModel

    lateinit var onDeleteITemFromCart : ((CartData) -> Unit)
    lateinit var onCartItemClick      : ((CartData) -> Unit)

    private var mealList = ArrayList<CartData>()
    fun setMeals(mealList :ArrayList<CartData>){
        this.mealList = mealList
        notifyDataSetChanged()
    }

    inner class ViewHolder (val binding : CartRowBinding) :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return  ViewHolder(CartRowBinding.inflate(LayoutInflater.from(parent.context) , parent , false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        cartMvvm = (holder.itemView.context as MainActivity).cartMvvm

        val data = mealList[position]
        holder.binding.apply {
               totalPrice.text   = data.totalPrice.toString()
               nameCart.text     = data.name
               countCart.text    = data.quantity.toString()
               size.text         = "size:"+data.itemSize
        }

        Glide.with(holder.itemView).load(data.imgUrl).into(holder.binding.imageCart)

        // TODO: 9/27/2022 handle click on itemView from img
        holder.binding.imageCart.setOnClickListener {
            onCartItemClick.invoke(data)
        }

        // TODO: 9/26/2022 Button +
        holder.binding.more.setOnClickListener {
                if (data.quantity < 10)
                {
                         data.quantity++

                    val  totalPrice   =  data.quantity * data.price
                    val  convertPrice = (totalPrice.times(100.0)).roundToInt() / 100.0
                         holder.binding.totalPrice.text = convertPrice.toString()
                         cartMvvm.updatePricesCart( data.quantity , convertPrice , data.id)
                }
        }  // TODO: 9/26/2022 Button -
        holder.binding.minus.setOnClickListener {
              if ( data.quantity > 1 )
              {
                      data.quantity--
                  val totalPrice   =  data.quantity * data.price
                  val convertPrice = (totalPrice.times(100.0)).roundToInt() / 100.0
                      holder.binding.totalPrice.text = convertPrice.toString()
                      cartMvvm.updatePricesCart( data.quantity , convertPrice , data.id)
              }
        }
        // TODO: 9/26/2022 handle delete item from cart fragment
        holder.binding.delete.setOnClickListener {
           onDeleteITemFromCart.invoke(data)
        }
    }

    override fun getItemCount(): Int {
       return  mealList.size
    }
}