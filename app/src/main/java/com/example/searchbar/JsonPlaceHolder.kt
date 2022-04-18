package com.example.searchbar

import retrofit2.Call
import retrofit2.http.GET


interface JsonPlaceHolder {

    @GET("ships")
    fun getPosts () : Call<List<Posts>>
}