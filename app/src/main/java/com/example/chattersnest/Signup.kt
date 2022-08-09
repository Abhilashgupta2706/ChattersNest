package com.example.chattersnest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Signup : AppCompatActivity() {

    private lateinit var etSignupFullName: EditText
    private lateinit var etSignupEmail: EditText
    private lateinit var etSignupPassword: EditText
    private lateinit var etSignupConfirmPassword: EditText
    private lateinit var btnSignup: Button
    private lateinit var tvSignupExistingUser: TextView

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()

        etSignupFullName = findViewById(R.id.etSignupFullName)
        etSignupEmail = findViewById(R.id.etSignupEmail)
        etSignupPassword = findViewById(R.id.etSignupPassword)
        etSignupConfirmPassword = findViewById(R.id.etSignupConfirmPassword)
        btnSignup = findViewById(R.id.btnSignup)
        tvSignupExistingUser = findViewById(R.id.tvSignupExistingUser)

        tvSignupExistingUser.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        btnSignup.setOnClickListener {
            val fullName = etSignupFullName.text.toString()
            val email = etSignupEmail.text.toString()
            val password = etSignupPassword.text.toString()
            val confirmPassword = etSignupConfirmPassword.text.toString()

            signup(fullName, email, password, confirmPassword)
        }
    }

    private fun signup(fullName: String, email: String, password: String, confirmPassword: String) {
        if (fullName.isEmpty() or email.isEmpty() or password.isEmpty()) {
            return Toast.makeText(
                this,
                "Make sure all details are filled",
                Toast.LENGTH_SHORT
            ).show()
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return Toast.makeText(this, "Please Enter valid Email ID", Toast.LENGTH_SHORT).show()
        }

        val passLength = 5
        if (password.length < passLength) {
            return Toast.makeText(
                this,
                "Password must be Greater then $passLength characters",
                Toast.LENGTH_SHORT
            ).show()
        }

        if (confirmPassword != password) {
            return Toast.makeText(this, "Password doesn't Match", Toast.LENGTH_SHORT).show()
        }

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    addUserToDatabase(fullName, email, mAuth.currentUser?.uid!!)
                    val intent = Intent(this@Signup, MainActivity::class.java)
                    finish()
                    startActivity(intent)

                    Toast.makeText(this, "Signing Successful", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show()
                }
            }

    }

    private fun addUserToDatabase(fullName: String, email: String, uid: String?) {
        mDbRef = FirebaseDatabase.getInstance().getReference()
        if (uid != null) {
            mDbRef.child("user").child(uid).setValue(User(fullName, email, uid))
        }

    }
}