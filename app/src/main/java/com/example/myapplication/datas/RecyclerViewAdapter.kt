package com.example.myapplication.datas

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.firstscreen.CustomDialog
import com.example.myapplication.firstscreen.FirstFragment


class RecyclerViewAdapter(private val context : Context) : RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {

    var datas = mutableListOf<JusikData>()

    inner class MyViewHolder(view : View): RecyclerView.ViewHolder(view) {
        private val name : TextView = itemView.findViewById(R.id.tv_fistscreen_name)
        private val chungyakday : TextView = itemView.findViewById(R.id.tv_fistscreen_chungyakday)
        private val sangjangday : TextView = itemView.findViewById(R.id.tv_fistscreen_sangjangday)
        fun bind(item : JusikData){
            name.text = item.name
            chungyakday.text = item.chungYakDay
            sangjangday.text = item.sangJangDay



        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recyclerview_layout, parent, false)
        return MyViewHolder(view)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(datas[position])

        holder.itemView.setOnClickListener {

            val dialog =CustomDialog(context)
            dialog.setDialog_jusikname(datas[position], context) //TODO 누른 주식 dialog 설정, Button에 startActivity 설정
            dialog.showDialog()


        }
    }

    override fun getItemCount(): Int = datas.size


}