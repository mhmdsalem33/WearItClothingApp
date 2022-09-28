package com.example.wearit.Constants

import android.content.Context
import android.content.SharedPreferences

class Constants {
    var myshared                     : SharedPreferences? = null
    fun saveBackGroundSize(context: Context ,  size : String)
    {
        myshared    = context.getSharedPreferences("my_shared" , 0)
        val editor :SharedPreferences.Editor = myshared!!.edit()
            editor.putString("size" , size)
            editor.commit()
    }
}