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

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        val createAccBtn = findViewById<Button>(R.id.btn_To_create_acc)
        val logInButton = findViewById<Button>(R.id.btnLogin)
        val loginEmail = findViewById<EditText>(R.id.Login_Email)
        val loginPassword = findViewById<EditText>(R.id.Login_Password)

        logInButton.setOnClickListener{
            val email = loginEmail.text.toString()
            val password = loginPassword.text.toString()

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener{task ->
                    if(task.isSuccessful){
                        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, Dashboard::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Login fail: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        createAccBtn.setOnClickListener{
            val intent = Intent(this, CreateAccount::class.java )
            startActivity(intent)

        }
    }
}
