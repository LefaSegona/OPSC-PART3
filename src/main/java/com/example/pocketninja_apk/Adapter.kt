package com.example.pocketninja_apk

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.NumberFormat
import java.util.Locale

class Adapter : RecyclerView.Adapter<Adapter.ExpenseViewHolder>() {

    private val expenses = mutableListOf<Data.Expense>()
    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())

    fun submitList(newList: List<Data.Expense>) {
        expenses.clear()
        expenses.addAll(newList)
        notifyDataSetChanged()
    }

    inner class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryText: TextView = itemView.findViewById(R.id.categoryText)
        val descriptionText: TextView = itemView.findViewById(R.id.descriptionText)
        val amountText: TextView = itemView.findViewById(R.id.amountText)
        val dateText: TextView = itemView.findViewById(R.id.dateText)
        val imageView: ImageView = itemView.findViewById(R.id.expenseImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_food_expenses, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = expenses[position]

        // Set text values
        holder.categoryText.text = expense.category
        holder.descriptionText.text = expense.description
        holder.amountText.text = currencyFormat.format(expense.amount)
        holder.dateText.text = expense.date

        // Handle image loading with better memory management
        loadImage(expense.imageBase64, holder.imageView)
    }

    private fun loadImage(base64String: String, imageView: ImageView) {
        if (base64String.isEmpty()) {
            imageView.setImageResource(R.drawable.add_a_photo_24)
            return
        }

        try {
            val imageBytes = Base64.decode(base64String, Base64.DEFAULT)
            val options = BitmapFactory.Options().apply {
                inSampleSize = calculateInSampleSize(imageBytes)
            }
            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size, options)
            bitmap?.let {
                imageView.setImageBitmap(it)
            } ?: run {
                imageView.setImageResource(R.drawable.add_a_photo_24)
            }
        } catch (e: Exception) {
            imageView.setImageResource(R.drawable.add_a_photo_24)
        }
    }

    private fun calculateInSampleSize(imageBytes: ByteArray): Int {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size, options)

        val reqWidth = 200 // Target width in pixels
        val reqHeight = 200 // Target height in pixels
        val (width, height) = options.run { outWidth to outHeight }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2

            while (halfHeight / inSampleSize >= reqHeight &&
                halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    override fun getItemCount(): Int = expenses.size
}