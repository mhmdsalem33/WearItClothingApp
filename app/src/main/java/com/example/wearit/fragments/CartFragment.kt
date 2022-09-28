package com.example.wearit.fragments

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wearit.R
import com.example.wearit.activites.OrderActivity
import com.example.wearit.adapters.CartAdapter
import com.example.wearit.data.CartData
import com.example.wearit.data.User
import com.example.wearit.databinding.FragmentCartBinding
import com.example.wearit.viewModels.CartViewModel
import java.io.Serializable
import kotlin.math.roundToInt


class CartFragment : Fragment() {

    private lateinit var binding     : FragmentCartBinding
    private lateinit var cartAdapter : CartAdapter
    private val cartMvvm             : CartViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // cartAdapter = CartAdapter()
        cartAdapter = CartAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater , container , false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCartRecView()
        getCartDataFromFireStore()
        onDeleteItemFromShoppingCart()
        onArrowBackClick()
        onItemCartClick()
        deleteAllItemsFromShoppingCart()

    }

    private fun deleteAllItemsFromShoppingCart() {
        binding.deleteAllItems.setOnClickListener {
            cartMvvm.getCartData.observe(viewLifecycleOwner){ data ->
                for (i in data)
                {
                    cartMvvm.deleteItemFromShoppingCart(i.id , requireContext())
                }
            }
        }
    }

    private fun onItemCartClick() {
        cartAdapter.onCartItemClick = { data ->
            val fragment =  DetailsFragment()
            val bundle   =  Bundle()
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

    private fun onArrowBackClick() {
        binding.arrowBack.setOnClickListener {
            findNavController().navigate(R.id.action_cartFragment_to_homeFragment)
        }
    }

    private fun onDeleteItemFromShoppingCart() {
      cartAdapter.onDeleteITemFromCart = { data ->
          cartMvvm.deleteItemFromShoppingCart(data.id , requireContext())
          getCartDataFromFireStore()
      }
    }

    private fun getCartDataFromFireStore() {
            cartMvvm.getCartDataFromFirestore()
            cartMvvm.getCartData.observe(viewLifecycleOwner){ data ->
                cartAdapter.setMeals(mealList = data as ArrayList<CartData>)
                calculateTotalCart(data)
                buyNow(data)
                if (data.isEmpty())
                {
                    binding.totalCartPrice.text = "00.0"
                    binding.subtotal.text       = "00.0"
                    binding.shipping.text       = "00.0"
                    binding.emptyCart.visibility        = View.VISIBLE
                    binding.cartInformation.visibility  = View.GONE
                }
                else
                {
                    binding.cartInformation.visibility  = View.VISIBLE
                    binding.emptyCart.visibility        = View.GONE

                }



            }
    }
    private fun calculateTotalCart(data: List<CartData>) {
        var totalPrice = 0.0
        for (i in data)
        {
                 totalPrice   += i.totalPrice
            val  convertPrice  =   (totalPrice.times(100.0)).roundToInt() / 100.0
                 binding.subtotal.text = convertPrice.toString()
                 binding.shipping.text = "10.00" // 10.00 shipping fee
            val  finalPrice = ((totalPrice + 10.00).times(100.0)).roundToInt() / 100.0
                 binding.totalCartPrice.text = (finalPrice).toString()
        }
    }

    private fun setupCartRecView() {
        binding.cartRec.apply {
            layoutManager = LinearLayoutManager(requireContext() , RecyclerView.VERTICAL  ,false)
            adapter       = cartAdapter
        }
    }

    private fun buyNow(data : List<CartData>) {
        binding.buyNow.setOnClickListener {
            // TODO: 9/27/2022 we will check if user didn't select size  he must select size to allow user to buy
            val check =   data.filter { it.itemSize == "" }
            if (check.isNotEmpty())
            {
                Toast.makeText(requireContext(), "Please select size for all items", Toast.LENGTH_LONG).show()
            }
            else
            {

                val intent = Intent(requireContext()  ,  OrderActivity::class.java)
                    intent.putExtra("Order"      ,  data as Serializable)
                startActivity(intent)

            }
        }
        }

    override fun onResume() {
        super.onResume()
        //بقفل الفراجمنت من ان الروتيشن
        getActivity()?.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    override fun onPause() {
        super.onPause()
        //هنا هيشغل الروتشن على باقى الفراجمنت
        getActivity()?.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
    }

}