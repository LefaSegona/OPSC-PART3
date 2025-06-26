package com.example.pocketninja_apk

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.pocketninja_apk.databinding.ActivityExpensesBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class Expenses : AppCompatActivity() {

    private lateinit var binding: ActivityExpensesBinding
    private var selectedImageUri: Uri? = null
    private lateinit var currentPhotoPath: String
    private val firestore = FirebaseFirestore.getInstance()

    // For gallery image selection
    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            binding.AddPictureImageView3.setImageURI(it)
        }
    }

    // For camera image capture
    private val takePictureLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            selectedImageUri = Uri.fromFile(File(currentPhotoPath))
            binding.AddPictureImageView3.setImageURI(selectedImageUri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityExpensesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Dropdown spinner setup
        val categories = listOf("Food", "Transport", "Shopping", "Entertainment", "Bills", "Other")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categoryPicker.adapter = adapter

        // Date picker
        binding.AddDateImageView.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, year, month, day ->
                    val date = Calendar.getInstance().apply { set(year, month, day) }.time
                    binding.DateEditText.text =
                        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Image selection options
        binding.AddPictureImageView3.setOnClickListener {
            showImagePickerOptions()
        }

        // Save button click handler
        binding.SaveButton.setOnClickListener {
            saveExpense()
        }
    }

    private fun showImagePickerOptions() {
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Add Photo")
        builder.setItems(options) { dialog, item ->
            when {
                options[item] == "Take Photo" -> dispatchTakePictureIntent()
                options[item] == "Choose from Gallery" -> pickImageLauncher.launch("image/*")
                options[item] == "Cancel" -> dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: Exception) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "${packageName}.fileprovider",
                        it
                    )
                    selectedImageUri = photoURI
                    takePictureLauncher.launch(photoURI)
                }
            }
        }
    }

    @Throws(Exception::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir("Pictures")
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    private fun saveExpense() {
        val category = binding.categoryPicker.selectedItem.toString()
        val description = binding.NotesEditText.text.toString().trim()
        val amountText = binding.AmountEditTextNumberDecimal.text.toString()
        val date = binding.DateEditText.text.toString().trim()

        // Input validation
        if (category.isEmpty() || description.isEmpty() || amountText.isEmpty() || date.isEmpty() || selectedImageUri == null) {
            Toast.makeText(this, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show()
            return
        }

        val amount = try {
            amountText.toDouble()
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "Invalid amount format", Toast.LENGTH_SHORT).show()
            return
        }

        val base64Image = uriToBase64(selectedImageUri!!) ?: run {
            Toast.makeText(this, "Failed to process image", Toast.LENGTH_SHORT).show()
            return
        }

        // Create expense object
        val expense = hashMapOf(
            "category" to category,
            "description" to description,
            "amount" to amount,
            "date" to date,
            "imageBase64" to base64Image,
            "createdAt" to Calendar.getInstance().time
        )

        // Save to Firestore
        firestore.collection("expenses")
            .add(expense)
            .addOnSuccessListener {
                Toast.makeText(this, "Expense saved successfully", Toast.LENGTH_SHORT).show()
                clearFields()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error saving: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uriToBase64(uri: Uri): String? {
        return try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                val bitmap = BitmapFactory.decodeStream(inputStream)
                val resizedBitmap = resizeBitmap(bitmap, 800)
                val stream = ByteArrayOutputStream()
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream)
                Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT)
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun resizeBitmap(bitmap: Bitmap, maxWidth: Int): Bitmap {
        val aspectRatio = bitmap.height.toFloat() / bitmap.width.toFloat()
        return Bitmap.createScaledBitmap(bitmap, maxWidth, (maxWidth * aspectRatio).toInt(), true)
    }

    private fun clearFields() {
        binding.NotesEditText.text.clear()
        binding.AmountEditTextNumberDecimal.text.clear()
        binding.DateEditText.text = ""
        binding.categoryPicker.setSelection(0)
        binding.AddPictureImageView3.setImageResource(android.R.color.transparent)
        selectedImageUri = null
    }
}