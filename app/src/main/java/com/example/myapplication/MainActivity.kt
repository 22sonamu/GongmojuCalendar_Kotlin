package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.DocumentsContract
import android.util.Log
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import com.example.myapplication.datas.JusikData
import com.example.myapplication.datas.RetorifitService
import com.example.myapplication.firstscreen.FirstFragment
import com.example.myapplication.secondscreen.SecondFragment
import com.example.myapplication.thirdscreen.ThirdFragment
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import org.apache.http.HttpHeaders
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponents
import org.springframework.web.util.UriComponentsBuilder
import org.w3c.dom.Document
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import javax.xml.parsers.DocumentBuilderFactory
import okhttp3.internal.http.HttpHeaders as HttpHeaders1
val a = mutableListOf<JusikData>()
class MainActivity : AppCompatActivity() {
    private val fl:FrameLayout by lazy{
        findViewById(R.id.fl_)
    }
    private val bn : BottomNavigationView by lazy{
        findViewById(R.id.bnv_main)
    }
    private lateinit var navController : NavController
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().add(fl.id, FirstFragment()).commit()

        bn.setOnNavigationItemSelectedListener {
            replaceFragment(
                when(it.itemId){
                    R.id.firstitem -> FirstFragment()
                    R.id.seconditem -> SecondFragment()
                    else -> ThirdFragment()
                }
            )
            true
        }

//        val factory = HttpComponentsClientHttpRequestFactory()
//        factory.setConnectTimeout(5000)
//        factory.setReadTimeout(5000)
//        val restTemplate = RestTemplate(factory)
//        val header = org.springframework.http.HttpHeaders()
//        val entity = HttpEntity<Map<String, Any>>(header)
//        val url = "http://localhost:8000/jusikname/노을"
//        val uri : UriComponents = UriComponentsBuilder.fromHttpUrl(url).build();
//        val resultMap : ResponseEntity<Map<*, *>>
//                = restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, Map::class.java)
//        val result  = HashMap<String, Any>()
//        result.put("statusCode", resultMap.getStatusCodeValue()); //http status code를 확인
//        result.put("header", resultMap.getHeaders()); //헤더 정보 확인
//        resultMap.body?.let { result.put("body", it) };
//        val mapper = ObjectMapper()
//        var jsonInString = ""
//        jsonInString = mapper.writeValueAsString(resultap.getBody());
//        Log.d("hello", jsonInString);




        val retrofit = Retrofit.Builder().baseUrl("http://10.0.2.2:8080/").addConverterFactory(GsonConverterFactory.create()).build();
        val service = retrofit.create(RetorifitService::class.java);
        service.getJusikPage("노을")?.enqueue(object : Callback<JusikData>{
            override fun onResponse(call: Call<JusikData>, response: Response<JusikData>) {
                if(response.isSuccessful){
                    // 정상적으로 통신이 성고된 경우
                    var result: JusikData? = response.body()

                    Log.d("YMC", "onResponse 성공: " + result?.toString());
                }else{
                    // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                    Log.d("YMC", "onResponse 실패")
                }
            }

            override fun onFailure(call: Call<JusikData>, t: Throwable) {
                // 통신 실패 (인터넷 끊킴, 예외 발생 등 시스템적인 이유)
                Log.d("YMC", "onFailure 에러: " + t.message.toString());
            }
        })

        service.getJusikPageall()?.enqueue(object : Callback<MutableList<JusikData>>{
            override fun onResponse(
                call: Call<MutableList<JusikData>>,
                response: Response<MutableList<JusikData>>
            ) {
                if(response.isSuccessful){
                    // 정상적으로 통신이 성고된 경우
                    var result: MutableList<JusikData>? = response.body()
                    //Log.d("YMC", "onResponse 성공: " + result?.toString());
                    if (result != null) {
                        a.addAll(result)
                    }
                }else{
                    // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                    Log.d("YMC", "onResponse 실패")
                }
            }

            override fun onFailure(call: Call<MutableList<JusikData>>, t: Throwable) {
                // 통신 실패 (인터넷 끊킴, 예외 발생 등 시스템적인 이유)
                Log.d("YMC", "onFailure 에러: " + t.message.toString());
            }


        })




    }

    private fun replaceFragment(fragment : Fragment) {
        supportFragmentManager.beginTransaction().replace(fl.id, fragment).commit()
    }
}