package `in`.exposysdata.eviapp

import `in`.exposysdata.eviapp.admin.AdminHomeActivity
import `in`.exposysdata.eviapp.databinding.ActivityLoginBinding
import `in`.exposysdata.eviapp.settings.ForgotActivity
import android.content.Intent
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.collections.ArrayList


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    // Firebase instance variables
    private lateinit var auth: FirebaseAuth

    //
    private lateinit var userArrayList : ArrayList<Admin>

    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = Firebase.auth

        // info from login activity
        val til_email = findViewById<TextInputLayout>(R.id.til_email)
        val til_password = findViewById<TextInputLayout>(R.id.til_password)

        // Progress bar
        progressBar = findViewById(R.id.progressBar)

        // hides action bar
        supportActionBar?.hide()

        fun isValidPassword(password: String): Boolean {
            if (password.length < 8) return false
            if (password.filter { it.isDigit() }.firstOrNull() == null) return false
            if (password.filter { it.isLetter() }.filter { it.isUpperCase() }.firstOrNull() == null) return false
            if (password.filter { it.isLetter() }.filter { it.isLowerCase() }.firstOrNull() == null) return false
            if (password.filter { !it.isLetterOrDigit() }.firstOrNull() == null) return false

            return true
        }

        fun typeErrorPassword(password: String): String {
            if (password.length < 8) return "Your password length should be greater than 8 character."
            if (password.filter { it.isDigit() }.firstOrNull() == null) return "Missing atleast 1 digit parameter."
            if (password.filter { it.isLetter() }.filter { it.isUpperCase() }.firstOrNull() == null) return "Missing upper case parameter."
            if (password.filter { it.isLetter() }.filter { it.isLowerCase() }.firstOrNull() == null) return "Missing lower case parameter."
            if (password.filter { !it.isLetterOrDigit() }.firstOrNull() == null) return "Missing Both letter and number."
            return "All Fine!"
        }

        // database
        val database = Firebase.database.reference

        userArrayList = arrayListOf<Admin>()

        binding.btnLogin.setOnClickListener {
            if (til_email.editText?.text.toString().isEmpty()) {
                til_email.error = "Email/Username is required"
            } else if (til_password.editText?.text.toString().isEmpty()) {
                til_password.error = "Password is required"
            } else {
                val mail = til_email.editText?.text.toString()
                val pass: String = til_password.editText?.text.toString()

                progressBar.visibility = ProgressBar.VISIBLE
                auth.signInWithEmailAndPassword(mail,pass)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser!!

                            val userId = auth.uid.toString()
                            val ref = Firebase.database.getReference("admin/${userId}/admin")

                            ref.addValueEventListener(object :
                                ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    if (dataSnapshot.value.toString().toBoolean()) {
                                        // User is admin
                                        startActivity(Intent(this@LoginActivity, AdminHomeActivity::class.java))
                                        finish()
                                    } else {
                                        // User is not admin
                                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                                        finish()
                                    }
                                }
                                override fun onCancelled(error: DatabaseError) {
                                    // Failed
                                }
                            })


                        } else {
                            Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()

                        }
                    }

                progressBar.visibility = ProgressBar.INVISIBLE
            }
        }

        val tv_forgot_pw = findViewById<TextView>(R.id.tv_forgot_pw)
        tv_forgot_pw.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ForgotActivity::class.java))
        }

        //
        binding.tvHaventAccount.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }

    }
}