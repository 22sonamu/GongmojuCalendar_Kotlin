package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import com.example.myapplication.firstscreen.FirstFragment
import com.example.myapplication.secondscreen.SecondFragment
import com.example.myapplication.thirdscreen.ThirdFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

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




    }

    private fun replaceFragment(fragment : Fragment) {
        supportFragmentManager.beginTransaction().replace(fl.id, fragment).commit()
    }
}