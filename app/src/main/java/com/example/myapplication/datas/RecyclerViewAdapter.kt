package com.example.myapplication.datas

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class RecyclerViewAdapter(private val context : Context) : RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {

    var datas = mutableListOf<JusikData>()

    inner class MyViewHolder(view : View): RecyclerView.ViewHolder(view) {
        private val name : TextView = itemView.findViewById(R.id.tv_fistscreen_name)
        private val chungyakday : TextView = itemView.findViewById(R.id.tv_fistscreen_chungyakday)
        private val sangjangday : TextView = itemView.findViewById(R.id.tv_fistscreen_sangjangday)
        fun bind(item : JusikData){
            name.text = item.Name
            chungyakday.text = item.ChungYakDay
            sangjangday.text = item.SangJangDay
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recyclerview_layout, parent, false)
        return MyViewHolder(view)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    override fun getItemCount(): Int = datas.size
}