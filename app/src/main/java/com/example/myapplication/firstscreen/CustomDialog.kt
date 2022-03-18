package com.example.myapplication.firstscreen

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.myapplication.R
import com.example.myapplication.datas.JusikData
import org.thymeleaf.spring5.context.SpringContextUtils.getApplicationContext
import kotlin.coroutines.coroutineContext

class CustomDialog (context : Context){
    private val dialog = Dialog(context)
    private lateinit var onClickListener: OnDialogClickListener







    fun showDialog(){//TODO 다이얼로그 띄우기

        dialog.show()

    }
    //TODO JusikData 를 받아서 각 textview에 setText 하고 , context를 받아서 주식 상세 페이지 버튼에 startActivity를 통해 웹브라우저를 띄운다.
    fun setDialog_jusikname(jusik : JusikData , context: Context){

        dialog.setContentView(R.layout.dialog)
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        val tv_jusikname= dialog.findViewById<TextView>(R.id.tv_jusikname)
        val tv_sangjangday= dialog.findViewById<TextView>(R.id.tv_sangjangday)
        val tv_refundday = dialog.findViewById<TextView>(R.id.tv_refundday)
        val tv_chungyakday = dialog.findViewById<TextView>(R.id.tv_chunyakday)
        val tv_hopeprice = dialog.findViewById<TextView>(R.id.tv_hopeprice)
        val tv_setprice = dialog.findViewById<TextView>(R.id.tv_setprice)
        val tv_companys = dialog.findViewById<TextView>(R.id.tv_companys)
        val btn_detailurl = dialog.findViewById<Button>(R.id.btn_detailurl)
        tv_jusikname.setText(jusik.name)
        tv_sangjangday.setText(jusik.sangJangDay)
        tv_refundday.setText(jusik.refundDay)
        tv_chungyakday.setText(jusik.chungYakDay)
        tv_hopeprice.setText(jusik.hopePrice)
        tv_setprice.setText(jusik.setPrice)
        tv_companys.setText(jusik.companys)
        btn_detailurl.setOnClickListener{//TODO 상세정보 페이지로 이동

            dialog.dismiss()
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(jusik.detailUrl))
            startActivity(context, intent, null)
        }


    }

    interface OnDialogClickListener
    {
        fun onClicked(name: String)
    }

}