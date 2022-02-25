package com.rasika.offlinewordle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fragmentManagement = supportFragmentManager
        val currentFragment=supportFragmentManager
            .findFragmentById(R.id.fragment_container)
        if (currentFragment==null){
            val fragment = WordleGameFragment()
            fragmentManagement.beginTransaction().add(R.id.fragment_container,fragment).commit()
        }
    }
}