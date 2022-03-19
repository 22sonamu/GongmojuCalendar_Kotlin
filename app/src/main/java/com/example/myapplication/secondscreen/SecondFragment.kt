package com.example.myapplication.secondscreen

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.datas.JusikData
import com.example.myapplication.datas.RetorifitService
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import kotlinx.android.synthetic.main.fragment_second.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.util.*
import kotlin.collections.HashMap

var all_Jusik = mutableListOf<JusikData>()//TODO api 에서 주식 받아오기
var hashMap = HashMap<String , LocalDate>() //TODO 날짜 : 주식명
var chungyakdays = mutableListOf<CalendarDay>()
var sangjangdays = mutableListOf<CalendarDay>()

class SecondFragment : Fragment(R.layout.fragment_second){


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val view = inflater.inflate(R.layout.fragment_second, null)
        val calendar = view.findViewById<MaterialCalendarView>(R.id.calendar)
        val cl = CalendarDay.from(2022, 3, 18)
        val c_cl = mutableListOf<CalendarDay>()
        c_cl.add(cl)

        Log.d("color", c_cl.toString())

        calendar.addDecorator(EventDecorator(c_cl, "#78EFAD"))


        this.getData {
            setDecorator()
            calendar.addDecorator(EventDecorator(sangjangdays, "#6699FF"))
            calendar.addDecorator(EventDecorator(chungyakdays, "#FF6666"))
            Log.d("happyhappy", hashMap.toString())

        }


        return view
    }



    fun setDecorator(){
        for(i : Int in 0..all_Jusik.size-1){

            val chungYakDay : String = all_Jusik[i].chungYakDay
            if(all_Jusik[i].sangJangDay != "-"){ //TODO 상장일이 없는 주식들은 점을 찍을 필요 없다.
                val sangJangDay : LocalDate = LocalDate.parse(all_Jusik[i].sangJangDay.substring(1).replace(".", "-")).minusMonths(1)
                val sanJangDay_toCal = localToCal(sangJangDay)
                hashMap.put(all_Jusik[i].name + "상장일", sangJangDay)
                sangjangdays.add(sanJangDay_toCal)}

            //TODO 보통 2일인 청약 "기간" 을 두개의 청약일로 변경(오늘에 해당하는지 확인하기 위함)
            //TODO 2022.01.20~01.21 --> 2022.01.20 , 2022.01.21
            val chungYakDay1 : String
            val chungYakDay2 : String
            if(chungYakDay.length > 16) {//TODO 중간에 년도가 바뀌는 경우(년도가 두번 들어가서 문자열의 길이가 길어짐)
                chungYakDay1 = chungYakDay.substring(0, 10)
                chungYakDay2 = chungYakDay.substring(11, 20)
            }
            else{
                chungYakDay1 = chungYakDay.substring(0, 10) //TODO 청약 첫번째 날
                chungYakDay2 = chungYakDay.substring(0, 5) + chungYakDay.substring(11)//TODO 청약 마지막 날
            }
            var chungYakDay1_todate : LocalDate = LocalDate.parse(chungYakDay1.replace(".", "-")).minusMonths(1)
            val chungYakDay2_todate : LocalDate = LocalDate.parse(chungYakDay2.replace(".", "-")).minusMonths(1)
            var i : Int =1
            while(chungYakDay1_todate <= chungYakDay2_todate){//TODO 청약기간에 모두 점을 찍어야 하므로
                hashMap.put(all_Jusik[i].name + i.toString(), chungYakDay1_todate)
                i+=1
                val a = localToCal(chungYakDay1_todate)
                chungyakdays.add(a)
                chungYakDay1_todate = chungYakDay1_todate.plusDays(1)
            }
        }
    }
    fun localToCal(date : LocalDate) : CalendarDay{ //TODO String --> CalendarDay
        Log.d("change", date.toString())
        val arr = date.toString().split("-")
        Log.d("change", arr.toString())
        val cal = CalendarDay.from(arr[0].toInt(), arr[1].toInt(), arr[2].toInt())
        Log.d("change", cal.toString())
        return cal
    }


    fun getData(completion : () -> Unit) { //TODO http://localhost:8080/jusikname/all 의 JusikData ArrayList를 받기 위함
        val retrofit = Retrofit.Builder().baseUrl("http://10.0.2.2:8080/")
            .addConverterFactory(GsonConverterFactory.create()).build();
        val service = retrofit.create(RetorifitService::class.java)
        service.getJusikPageall()?.enqueue(object : Callback<MutableList<JusikData>> {
            override fun onResponse(
                call: Call<MutableList<JusikData>>,
                response: Response<MutableList<JusikData>>
            ) {
                if (response.isSuccessful) {
                    //TODO 정상적으로 통신이 성공된 경우
                    val result: MutableList<JusikData>? = response.body()
                    if (result != null) {
                        //TODO result 를 all_Jusik에 담기
                        all_Jusik.addAll(result)

                    }
                    Log.d("YMC", "onResponse 성공: " + result?.toString());
                } else {
                    //TODO 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                    Log.d("YMC", "onResponse 실패")
                }
            }
            override fun onFailure(call: Call<MutableList<JusikData>>, t: Throwable) {
                //TODO 통신 실패 (인터넷 끊김, 예외 발생 등 시스템적인 이유)
                Log.d("YMC", "onFailure 에러: " + t.message.toString());
            }
        })
        //TODO override fun onResponse 가 항상 마지막에 실행됨 --> Jusik Data가 전역변수에 담기지 않는 문제 발생 --> 콜백 처리
        Handler().postDelayed({completion()}, 100L)
    }




}