package com.example.myapplication.datas

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RetorifitService {
    @GET("jusikname/{Name}")
    fun getJusikPage(@Path("Name") Name : String) : Call<JusikData>

    @GET("jusikname/all")
    fun getJusikPageall() : Call<ArrayList<JusikData>>

}