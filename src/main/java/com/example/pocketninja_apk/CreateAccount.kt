package com.example.pocketninja_apk

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class CreateAccount : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_account)

        auth = FirebaseAuth.getInstance()

        val createAccount = findViewById<Button>(R.id.createAccountBtn)
        val emailInput = findViewById<EditText>(R.id.emailInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)


        createAccount.setOnClickListener{
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()
            auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener{task->
                    if(task.isSuccessful){
                        Toast.makeText(this, "Registered successfully", Toast.LENGTH_SHORT).show()

                        startActivity(Intent(this, MainActivity:: class.java))
                        finish()
                    }else{
                        Toast.makeText(this,"Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }

                }
        }

        }
    }
