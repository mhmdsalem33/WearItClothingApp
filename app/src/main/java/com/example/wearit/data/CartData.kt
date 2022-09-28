package com.example.wearit.data

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

data class CartData (
     val id          : String,
     val name        : String,
     val imgUrl      : String,
     val price       : Double,
     val secondImage : String,
     val thirdImage  : String,
     var quantity    : Int = 0,
     val totalPrice  : Double,
     var itemSize    : String   = ""
):Serializable
 {

     constructor() :this("" ,"" ,"" ,0.00 , "" , "" ,0 ,0.00 ,"")


 }


