package com.example.pocketninja_apk

import android.os.Bundle
import android.content.Intent
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class Dashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        val userIcon = findViewById<ImageView>(R.id.userIcon)



        userIcon.setOnClickListener{

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

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