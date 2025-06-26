package com.example.pocketninja_apk

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Budget : AppCompatActivity() {

    // UI Components
    private lateinit var barChart: BarChart
    private lateinit var setBudgetButton: Button
    private lateinit var budgetInputContainer: LinearLayout
    private lateinit var congratsLayout: LinearLayout

    // Data
    private val db = Firebase.firestore
    private val budgets = mutableMapOf<String, Float>()
    private val expenses = mutableMapOf<String, Float>()
    private val categories = listOf("Food", "Transport", "Entertainment", "Utilities", "Shopping")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget)

        // Initialize views
        barChart = findViewById(R.id.barChart)
        setBudgetButton = findViewById(R.id.set_budget_button)
        budgetInputContainer = findViewById(R.id.budget_input_container)
        congratsLayout = findViewById(R.id.congratsLayout)

        // Setup chart
        setupChart()

        // Set button click listener
        setBudgetButton.setOnClickListener {
            toggleBudgetInputs()
        }

        // Load data
        loadExpenses()
        loadBudgets()

        // Setup bottom navigation
        setupBottomNavigation()
    }

    private fun setupChart() {
        barChart.apply {
            setDrawBarShadow(false)
            setDrawValueAboveBar(true)
            description.isEnabled = false
            legend.isEnabled = true
            setPinchZoom(true)

            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                granularity = 1f
                textColor = Color.WHITE
            }

            axisLeft.apply {
                setDrawGridLines(true)
                gridColor = Color.GRAY
                textColor = Color.WHITE
                axisMinimum = 0f
            }

            axisRight.isEnabled = false
        }
    }

    private fun toggleBudgetInputs() {
        if (budgetInputContainer.visibility == View.VISIBLE) {
            // Hide inputs and save
            budgetInputContainer.visibility = View.GONE
            saveBudgets()
            setBudgetButton.text = "Set Budgets"
        } else {
            // Show inputs
            budgetInputContainer.visibility = View.VISIBLE
            createBudgetInputs()
            setBudgetButton.text = "Save Budgets"
        }
    }

    private fun createBudgetInputs() {
        budgetInputContainer.removeAllViews()

        categories.forEach { category ->
            val inputView = LayoutInflater.from(this)
                .inflate(R.layout.item_budget_input, budgetInputContainer, false)

            val categoryName = inputView.findViewById<TextView>(R.id.category_name)
            val budgetInput = inputView.findViewById<EditText>(R.id.budget_amount)

            categoryName.text = category
            budgetInput.setText(budgets[category]?.toString() ?: "")

            budgetInputContainer.addView(inputView)
        }
    }

    private fun saveBudgets() {
        val updatedBudgets = mutableMapOf<String, Float>()

        for (i in 0 until budgetInputContainer.childCount) {
            val childView = budgetInputContainer.getChildAt(i)
            val category = childView.findViewById<TextView>(R.id.category_name).text.toString()
            val amountText = childView.findViewById<EditText>(R.id.budget_amount).text.toString()

            if (amountText.isNotEmpty()) {
                updatedBudgets[category] = amountText.toFloat()
            }
        }

        db.collection("budgets").document("user_budgets")
            .set(updatedBudgets)
            .addOnSuccessListener {
                budgets.clear()
                budgets.putAll(updatedBudgets)
                updateChart()
                Toast.makeText(this, "Budgets saved!", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadExpenses() {
        db.collection("expenses")
            .get()
            .addOnSuccessListener { documents ->
                expenses.clear()
                documents.forEach { doc ->
                    val expense = doc.toObject(Data.Expense::class.java)
                    expenses[expense.category] = (expenses[expense.category] ?: 0f) + expense.amount.toFloat()
                }
                updateChart()
            }
    }

    private fun loadBudgets() {
        db.collection("budgets").document("user_budgets")
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    budgets.clear()
                    document.data?.forEach { (category, amount) ->
                        budgets[category] = (amount as? Number)?.toFloat() ?: 0f
                    }
                    updateChart()
                }
            }
    }

    private fun updateChart() {
        val budgetEntries = mutableListOf<BarEntry>()
        val expenseEntries = mutableListOf<BarEntry>()
        var allWithinBudget = true

        categories.forEachIndexed { index, category ->
            val budget = budgets[category] ?: 0f
            val actual = expenses[category] ?: 0f

            budgetEntries.add(BarEntry(index.toFloat(), budget))
            expenseEntries.add(BarEntry(index.toFloat(), actual))

            if (budget > 0 && actual > budget) {
                allWithinBudget = false
            }
        }

        // Create datasets
        val budgetDataSet = BarDataSet(budgetEntries, "Budget").apply {
            color = Color.parseColor("#4CAF50")
            setDrawValues(true)
        }

        val expenseDataSet = BarDataSet(expenseEntries, "Actual").apply {
            color = Color.parseColor("#F44336")
            setDrawValues(true)
        }

        // Configure chart
        barChart.apply {
            data = BarData(budgetDataSet, expenseDataSet).apply {
                barWidth = 0.4f
                groupBars(0f, 0.4f, 0.02f)
                setValueTextColor(Color.WHITE)
            }
            xAxis.valueFormatter = IndexAxisValueFormatter(categories)
            invalidate()
            animateY(1000)
        }

        // Show achievement if budgets met
        if (allWithinBudget && budgets.isNotEmpty()) {
            congratsLayout.visibility = View.VISIBLE
            val anim = AnimationUtils.loadAnimation(this, R.anim.bounce)
            findViewById<ImageView>(R.id.congratsImage).startAnimation(anim)
        } else {
            congratsLayout.visibility = View.GONE
        }
    }

    private fun setupBottomNavigation() {
        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dashboard -> startActivity(Intent(this, Dashboard::class.java))
                R.id.nav_wallet -> startActivity(Intent(this, Wallet::class.java))
                R.id.nav_transaction -> startActivity(Intent(this, Transaction::class.java))
                R.id.nav_more -> startActivity(Intent(this, Dashboard::class.java))
            }
            true
        }
    }
}