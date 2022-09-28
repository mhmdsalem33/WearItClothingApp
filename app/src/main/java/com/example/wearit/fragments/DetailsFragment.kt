package com.example.wearit.fragments

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.wearit.Constants.Constants
import com.example.wearit.R
import com.example.wearit.data.ClothesData
import com.example.wearit.databinding.FragmentDetailsBinding
import com.example.wearit.viewModels.CartViewModel
import com.example.wearit.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private lateinit var binding      :FragmentDetailsBinding
    private lateinit var name         :String
    private lateinit var mainImage    :String
    private lateinit var secondImage  :String
    private lateinit var thirdImage   :String
    private lateinit var id           :String
    private  var price                :Double ? = null
    private  var containerImage        = ""
    private val constants by  lazy  { Constants() }
    var myshared                     : SharedPreferences? = null


    private val mainMvvm : MainViewModel by viewModels()
    private val cartMvvm : CartViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailsBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
          myshared = requireContext().getSharedPreferences("my_shared" , 0)

            getDataByBundle()
            setInformationInViews()
            handleClicksOnSize()
            btnSaveData()
            handleBtnAddToShoppingCart()
            handleArrowBack()

    }

    private fun handleArrowBack() {
        binding.arrowBack.setOnClickListener {
            findNavController().navigate(R.id.action_detailsFragment_to_homeFragment)
        }
    }


    private fun handleBtnAddToShoppingCart() {
        binding.addToCart.setOnClickListener {
            myshared =  this.requireActivity().getSharedPreferences("my_shared" , 0)
            var size =  myshared!!.getString("size" , "")
            if (size == "")
            {
                Toast.makeText(requireContext(), "Sorry you must select size", Toast.LENGTH_SHORT).show()
            }
            else
            {
                val data  = ClothesData(id,name,mainImage, price!! , secondImage ,thirdImage , itemSize = size!!)
                cartMvvm.insertItemToShoppingCartToFirestore(data , requireContext())
            }
        }
    }

    private fun getDataByBundle() {
        val data = arguments
        if (data != null)
        {
            id          = data.getString("id").toString()
            name        = data.getString("name").toString()
            mainImage   = data.getString("mainImage").toString()
            secondImage = data.getString("secondImage").toString()
            thirdImage  = data.getString("thirdImage").toString()
            price       = data.getDouble("price")
        }
    }
    private fun setInformationInViews() {

           binding.tvName.text = name
           binding.price.text  = price.toString()
           containerImage      = mainImage

        Glide.with(requireContext()).load(containerImage).into(binding.mainImage)  // right image
        Glide.with(requireContext()).load(mainImage)     .into(binding.firstImage)  // first left image
        Glide.with(requireContext()).load(secondImage)   .into(binding.secondImage) // second left image
        Glide.with(requireContext()).load(thirdImage)    .into(binding.thirdImage)  // third left image

        binding.firstImage.setOnClickListener {
            containerImage = mainImage
            Glide.with(requireContext()).load(containerImage).into(binding.mainImage)
        }

        binding.secondImage.setOnClickListener {
         if (secondImage.isNotEmpty())
         {
             containerImage = secondImage
             Glide.with(requireContext()).load(containerImage).into(binding.mainImage)
         }
        }

        binding.thirdImage.setOnClickListener {
         if (thirdImage.isNotEmpty())
         {
             containerImage = thirdImage
             Glide.with(requireContext()).load(containerImage).into(binding.mainImage)
         }
        }
    }
    private fun handleClicksOnSize() {
        binding.linearSmall.setOnClickListener {
            constants.saveBackGroundSize(requireContext() , "S")
            setBackgroundForSize()
        }

        binding.linearMeduem.setOnClickListener {
            constants.saveBackGroundSize(requireContext() , "M")
            setBackgroundForSize()
        }

        binding.linearLarger.setOnClickListener {
            constants.saveBackGroundSize(requireContext() , "L")
            setBackgroundForSize()
        }

        binding.linearXlarge.setOnClickListener {
            constants.saveBackGroundSize(requireContext() , "Xl")
            setBackgroundForSize()

        }
    }


    fun setBackgroundForSize()
    {
        myshared =  this.requireActivity().getSharedPreferences("my_shared" , 0)
        var size =  myshared!!.getString("size" , "")

        if (size  == "S")
        {
            binding.linearSmall.setBackgroundResource(R.drawable.size_background)
            binding.tvSmall.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        }
        else
        {
            binding.linearSmall.setBackgroundResource(R.drawable.size_white_background)
            binding.tvSmall.setTextColor(ContextCompat.getColor(requireContext(), R.color.ramady))
        }
        if (size  == "M")
        {
            binding.linearMeduem.setBackgroundResource(R.drawable.size_background)
            binding.tvMedium.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        }
        else
        {
            binding.linearMeduem.setBackgroundResource(R.drawable.size_white_background)
            binding.tvMedium.setTextColor(ContextCompat.getColor(requireContext(), R.color.ramady))
        }
        if (size == "L" )
        {
            binding.linearLarger.setBackgroundResource(R.drawable.size_background)
            binding.tvL.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        }
        else
        {
            binding.linearLarger.setBackgroundResource(R.drawable.size_white_background)
            binding.tvL.setTextColor(ContextCompat.getColor(requireContext(), R.color.ramady))
        }
        if (size == "Xl")
        {
            binding.linearXlarge.setBackgroundResource(R.drawable.size_background)
            binding.tvXl.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        }
        else
        {
            binding.linearXlarge.setBackgroundResource(R.drawable.size_white_background)
            binding.tvXl.setTextColor(ContextCompat.getColor(requireContext(), R.color.ramady))
        }
    }


    private fun btnSaveData() {
        binding.favo.setOnClickListener {
              myshared =  this.requireActivity().getSharedPreferences("my_shared" , 0)
              var size  =  myshared!!.getString("size" , "")
            if (size == "")
            {
                Toast.makeText(requireContext(), "To Save $name  select size first", Toast.LENGTH_LONG).show()
            }
            else
            {
                val dataToSave = ClothesData(id,name,mainImage, price!! , secondImage ,thirdImage , itemSize = size!!)
                    mainMvvm.upsert(dataToSave)
                Toast.makeText(requireContext(),  "$name  Saved Success" , Toast.LENGTH_SHORT).show()
            }

        }
    }


    override fun onPause() {
        super.onPause()
        // TODO: 9/27/2022   if user intent to other fragment clear current size
        myshared?.edit()!!.clear().commit()
    }

}