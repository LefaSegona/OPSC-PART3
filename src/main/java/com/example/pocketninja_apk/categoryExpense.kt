package com.example.pocketninja_apk

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.Toolbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Category_expenses : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var toolbar: Toolbar
    private lateinit var adapter: Adapter
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_expense)

        // Initialize views using findViewById()
        toolbar = findViewById(R.id.toolbar)
        recyclerView = findViewById(R.id.recyclerView)

        val category = intent.getStringExtra("CATEGORY") ?: run {
            Toast.makeText(this, "No category specified", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        setupToolbar(category)
        setupRecyclerView()
        loadCategoryExpenses(category)
    }

    private fun setupToolbar(category: String) {
        toolbar.title = "$category Expenses"
        toolbar.setNavigationOnClickListener { onBackPressed() }
        toolbar.setTitleTextColor(getColor(R.color.white))
    }

    private fun setupRecyclerView() {
        adapter = Adapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
    }

    private fun loadCategoryExpenses(category: String) {
        db.collection("expenses")
            .whereEqualTo("category", category)
            .addSnapshotListener { snapshots, error ->
                when {
                    error != null -> {
                        Toast.makeText(this, "Error loading expenses", Toast.LENGTH_SHORT).show()
                    }
                    snapshots != null -> {
                        val expenses = snapshots.toObjects(Data.Expense::class.java)
                        adapter.submitList(expenses)
                    }
                }
            }
    }
}