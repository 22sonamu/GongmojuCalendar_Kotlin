package com.example.myapplication.firstscreen

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.datas.*
import kotlinx.android.synthetic.main.fragment_first.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.springframework.web.servlet.function.ServerResponse.async
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import kotlin.system.measureTimeMillis


class FirstFragment : Fragment(R.layout.fragment_first){
    lateinit var mainActivity: MainActivity


    val mDatas = mutableListOf<JusikData>()
    val today: String = LocalDate.now().toString() //TODO 오늘 날짜
    val parsed_today : String = parsetoday(today)
    var all_Jusik = mutableListOf<JusikData>()//TODO api 에서 주식 받아오기
    var all_Jusik_Count = 0
    val today_Jusik = mutableListOf<JusikData>() //TODO 오늘에 관련된 주식 고르기
    var answer  = mutableListOf<JusikData>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //////views///////
        val view = inflater.inflate(R.layout.fragment_first, null)
        val recyclerView : RecyclerView = view.findViewById(R.id.rv_firstscreen)




        this.getData { //TODO 콜백(1초)

            Log.d("1초 후 call back - complete", all_Jusik.toString())
            all_Jusik_Count = all_Jusik.size
            getTodayData("2022.02.28")
            Log.d("Today Jusik 목록", today_Jusik.toString())

            val adapter1 = RecyclerViewAdapter(mainActivity)
            adapter1.datas = mDatas
            mDatas.apply {
                for(i : Int in 0 until today_Jusik.size)
                    add(today_Jusik[i])

            }
            recyclerView.adapter = adapter1
            recyclerView.addItemDecoration(VerticalItemDecorator(20))
            recyclerView.addItemDecoration(HorizontalItemDecorator(10))
            adapter1.notifyDataSetChanged()
        }


        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = context as MainActivity
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
                        Log.d("hello", answer.toString())
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
        Handler().postDelayed({completion()}, 1000L)
    }

    fun getTodayData(today : String){
        for(i : Int in 0..all_Jusik_Count-1){
            val chungYakDay : String = all_Jusik[i].chungYakDay

            Log.d("getTodatData", chungYakDay)
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
            //TODO 가끔 청약일이 3일 이상인 공모주 날짜계산
            val chungYakDay1_todate : LocalDate  = LocalDate.parse(chungYakDay1.replace(".", "-"))
            val chungYakDay2_todate : LocalDate  = LocalDate.parse(chungYakDay2.replace(".", "-"))
            val today_todate : LocalDate  = LocalDate.parse(today.replace(".", "-"))
            val period = Period.between(chungYakDay1_todate, chungYakDay2_todate).toString().substring(1, 2)

            if(period.toInt() > 1){
                if(period.toInt() == 2){ //TODO 청약일이 3일
                    if(chungYakDay1_todate.plusDays(1) == today_todate){
                        today_Jusik.add(all_Jusik[i])
                    }
                }
                if(period.toInt() == 3){ //TODO 청약일이 4일
                    if(chungYakDay1_todate.plusDays(1) == today_todate || chungYakDay1_todate.plusDays(2) == today_todate){
                        today_Jusik.add(all_Jusik[i])
                    }
                }
            }

            //TODO 오늘 == 청약기간 or 오늘 == 상장일인 경우
            if(chungYakDay1 == today || chungYakDay2 == today || all_Jusik[i].sangJangDay.substring(1) == today){

                today_Jusik.add(all_Jusik[i]) //TODO 오늘의 주식 배열에 add
            }


        }

    }






}

    fun parsetoday (today : String) : String{//TODO 2022-03-16 --> 2022.03.16
        val answer = today.replace('-' , '.')
        return answer
    }




