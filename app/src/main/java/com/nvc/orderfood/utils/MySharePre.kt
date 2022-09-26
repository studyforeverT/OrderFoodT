package com.nvc.orderfood.utils

import android.content.SharedPreferences
import com.google.gson.Gson
import com.nvc.orderfood.model.User

class MySharePre (private val sharedPreferences: SharedPreferences) {
    fun putString(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key,value)
        editor.apply()
    }
    fun getString(key: String): String? {
        return sharedPreferences.getString(key, "")
    }
    fun putBoolean(key: String, value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(key,value)
        editor.apply()
    }
     fun getBoolean(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    fun getUser(): User? {
        return try {
            Gson().fromJson(getString("user"), User::class.java)
        }catch (e : Exception){
            null
        }
    }

    fun removeKey(key : String){
        val editor = sharedPreferences.edit()
        editor.remove(key)
        editor.apply()
    }
}