package com.example.wearit.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clothesInformation")
data class ClothesData (
    @PrimaryKey
    val id          : String,
    val name        : String,
    val imgUrl      : String,
    val price       : Double,
    var secondImage : String,
    var thirdImage  : String    = "",
    val quantity    : Int       = 1 ,
    var totalPrice  : Double    = 0.00,
    var itemSize    : String    = ""
    )
{
    constructor() :this("" ,"" ,"",0.00 ,"" , "" ,1 ,0.00 , "")
}