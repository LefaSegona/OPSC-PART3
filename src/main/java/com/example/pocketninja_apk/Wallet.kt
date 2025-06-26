package com.example.pocketninja_apk

import com.example.pocketninja_apk.R
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView


class Wallet : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_wallet)


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.nav_dashboard -> {
                    // TODO: load home fragment or activity

                    val intent = Intent(this,Dashboard::class.java)
                    startActivity(intent)
                    return@setOnItemSelectedListener true
                }

                R.id.nav_wallet -> {
                    // TODO: load wallet fragment or activity
                    val intent = Intent(this,Wallet::class.java)
                    startActivity(intent)
                    return@setOnItemSelectedListener true
                }

                R.id.nav_transaction -> {
                    // TODO: load wallet fragment or activity
                    val intent = Intent(this,Transaction::class.java)
                    startActivity(intent)
                    return@setOnItemSelectedListener true
                }

                R.id.nav_budget -> {
                    // TODO: load wallet fragment or activity
                    val intent = Intent(this,Budget::class.java)
                    startActivity(intent)
                    return@setOnItemSelectedListener true
                }

                R.id.nav_more -> {
                    // TODO: load settings fragment or activity
                    val intent = Intent(this,Dashboard::class.java)
                    startActivity(intent)
                    return@setOnItemSelectedListener true
                }
            }
            false
        }


    }
}