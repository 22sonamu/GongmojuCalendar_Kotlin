package com.example.myapplication.datas

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RetorifitService {
    @GET("jusikname/{Name}") //TODO 주식 이름에 대한 정보
    fun getJusikPage(@Path("Name") Name : String) : Call<JusikData>

    @GET("jusikname/all") //TODO 등록된 주식 전부 가져오기
    fun getJusikPageall() : Call<MutableList<JusikData>>

}