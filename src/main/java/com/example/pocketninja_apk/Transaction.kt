package com.example.pocketninja_apk

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Transaction : AppCompatActivity() {

    private lateinit var expenseAdapter: Adapter
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)

        // Initialize Firestore
        db = Firebase.firestore

        // Initialize button (keep your existing ID)
        findViewById<Button>(R.id.addExpenseBtn).setOnClickListener {
            startActivity(Intent(this, Expenses::class.java))
        }

        // Setup RecyclerView
        expenseAdapter = Adapter()
        findViewById<RecyclerView>(R.id.recyclerView).apply {
            adapter = expenseAdapter
            layoutManager = LinearLayoutManager(this@Transaction)
        }

        // Load data from Firestore
        loadExpensesFromFirestore()

        // Keep your existing navigation setup
        setupBottomNavigation()
    }

    private fun loadExpensesFromFirestore() {
        db.collection("expenses")
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    Toast.makeText(this, "Error loading expenses", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                val expenses = mutableListOf<Data.Expense>()
                snapshots?.forEach { document ->
                    document.toObject(Data.Expense::class.java).let {
                        expenses.add(it)
                    }
                }
                expenseAdapter.submitList(expenses)
            }
    }

    private fun setupBottomNavigation() {
        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dashboard -> startActivity(Intent(this, Dashboard::class.java))
                R.id.nav_wallet -> startActivity(Intent(this, Wallet::class.java))
                R.id.nav_transaction -> startActivity(Intent(this, Transaction::class.java))
                R.id.nav_budget -> startActivity(Intent(this, Budget::class.java))
                R.id.nav_more -> startActivity(Intent(this, Dashboard::class.java))
            }
            true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // No need to remove listeners with Firestore
    }
}