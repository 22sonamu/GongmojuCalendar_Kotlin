package com.example.myapplication.datas

import com.google.gson.annotations.SerializedName


data class JusikData(
    @SerializedName("name")
    val name : String,

    @SerializedName("chungYakDay")
    val chungYakDay : String,

    @SerializedName("refundDay")
    val refundDay : String,

    @SerializedName("sangJangDay")
    val sangJangDay : String,

    @SerializedName("hopePrice")
    val hopePrice : String,

    @SerializedName("companys")
    val companys : String,

    @SerializedName("setPrice")
    val setPrice : String,

    @SerializedName("detailUrl")
    val detailUrl : String

    ){
    init {

    }
}
