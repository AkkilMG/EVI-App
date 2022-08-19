package `in`.exposysdata.eviapp.settings

import `in`.exposysdata.eviapp.LoginActivity
import `in`.exposysdata.eviapp.R
import `in`.exposysdata.eviapp.SignActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ForgotActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot)

        // hides action bar
        supportActionBar?.hide()


        val auth = Firebase.auth
        val user = Firebase.auth.currentUser

        //
        val forgotEmail = findViewById<TextInputLayout>(R.id.forgotEmail)
        val requestPassword = findViewById<Button>(R.id.requestPassword)

            requestPassword.setOnClickListener {
                if (forgotEmail.editText?.text.toString().isEmpty()) {
                    forgotEmail.error = "Email is required"
                } else {
                    val mail = forgotEmail.editText?.text.toString()

                    auth.sendPasswordResetEmail(mail)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this@ForgotActivity, "A mail has sent to registered email. Please check your inbox.", Toast.LENGTH_LONG).show()

                                startActivity(Intent(this@ForgotActivity, LoginActivity::class.java))
                                finish()
                            }
                        }
                        .addOnFailureListener { task ->
                            Toast.makeText(this@ForgotActivity, "Error: ${task.message}", Toast.LENGTH_LONG).show()
                        }
                }
            }
    }
}
