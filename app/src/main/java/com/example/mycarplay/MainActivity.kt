package com.example.mycarplay

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.mycarplay.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var selectedMenuId = R.id.menuMusicFeed
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        loadFragment(MusicFragment())
        initView()
    }

    private fun initView() {
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menuMusicFeed ->{
                    selectedMenuId = it.itemId
                    loadFragment(MusicFragment())
                }
                R.id.menuMap -> {
                    selectedMenuId = it.itemId
                    loadFragment(MapFragment())
                }
                R.id.menuPhone -> {
                    selectedMenuId = it.itemId
                    loadFragment(PhoneFragment())
                }
                R.id.menuHome -> {
                    selectedMenuId = it.itemId
                    loadFragment(HomeFragment())
                }
                else -> {
                    selectedMenuId = it.itemId
                    loadFragment(MusicFragment())
                }
            }
            true
        }
    }
    fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.flContainer, fragment).addToBackStack(
            null
        ).commit()
    }
}