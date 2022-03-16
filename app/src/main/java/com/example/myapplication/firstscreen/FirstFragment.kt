package com.example.myapplication.firstscreen

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.datas.HorizontalItemDecorator
import com.example.myapplication.datas.JusikData
import com.example.myapplication.datas.RecyclerViewAdapter
import com.example.myapplication.datas.VerticalItemDecorator
import kotlinx.android.synthetic.main.fragment_first.*

class FirstFragment : Fragment(R.layout.fragment_first){
    lateinit var mainActivity: MainActivity


    val mDatas = mutableListOf<JusikData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_first, null)
        val recyclerView : RecyclerView = view.findViewById(R.id.rv_firstscreen)
        val adapter1 = RecyclerViewAdapter(mainActivity)
        adapter1.datas = mDatas
        mDatas.apply {
            add(JusikData(name = "이름", chungYakDay= "청약일", sangJangDay = "상장일",companys = "회사들", setPrice = "확정공모가", hopePrice = "희망공모가", detailUrl ="url", refundDay ="환불일"))
            add(JusikData(name = "이름", chungYakDay = "청약일", sangJangDay = "상장일",companys = "회사들", setPrice = "확정공모가", hopePrice = "희망공모가", detailUrl ="url", refundDay ="환불일"))


        }

        recyclerView.adapter = adapter1
        recyclerView.addItemDecoration(VerticalItemDecorator(20))
        recyclerView.addItemDecoration(HorizontalItemDecorator(10))
        adapter1.notifyDataSetChanged()

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = context as MainActivity
    }

}