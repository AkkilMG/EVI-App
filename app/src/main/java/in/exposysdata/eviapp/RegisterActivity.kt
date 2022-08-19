package `in`.exposysdata.eviapp

import `in`.exposysdata.eviapp.admin.AdminHomeActivity
import `in`.exposysdata.eviapp.databinding.ActivityRegisterBinding
import `in`.exposysdata.eviapp.retry.checkForInternet
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var til_fullname: TextInputLayout
    private lateinit var til_email: TextInputLayout
    private lateinit var til_password: TextInputLayout
    private lateinit var til_confirm_password: TextInputLayout
    private lateinit var til_phone:TextInputLayout

    //
    private lateinit var mail:String
    private lateinit var phone:String
    private lateinit var pass:String

    // Firebase instance variables
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser

    private lateinit var progressBar: ProgressBar

    // retry functionality
    private lateinit var mDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        //auth = FirebaseAuth.getInstance()
        auth = Firebase.auth

        // Assigning id to the views
        til_fullname = findViewById(R.id.til_fullname)
        til_email = findViewById(R.id.til_email)
        til_password = findViewById(R.id.til_password)
        til_confirm_password = findViewById(R.id.til_confirm_password)
        til_phone = findViewById(R.id.til_phone)

        // Progress bar
        progressBar = findViewById(R.id.progressBar)


        // hides action bar
        supportActionBar?.hide()

        if (!checkForInternet(this@RegisterActivity)) {

            // Pop up with check_internet.xml
            mDialog = Dialog(this@RegisterActivity)

            showPopup()
        }

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

        binding.btnRegister.setOnClickListener {
            if (til_fullname.editText?.text.toString().isEmpty()) {
                til_fullname.error = "Fullname is required"
            } else if (til_phone.editText?.text.toString().isEmpty()) {
                til_phone.error = "Phone Number is required"
            }  else if (til_email.editText?.text.toString().isEmpty()) {
                til_email.error = "Email is required"
            } else if (til_password.editText?.text.toString().isEmpty()) {
                til_password.error = "Password is required"
            } else if (til_confirm_password.editText?.text.toString().isEmpty()) {
                til_confirm_password.error = "Confirm password is required"
            } else if (til_password.editText?.text.toString() != til_confirm_password.editText?.text.toString()) {
                til_confirm_password.error = "Password does not match"
            } else {
                mail = til_email.editText?.text.toString()
                phone = til_phone.editText?.text.toString()
                pass = til_password.editText?.text.toString()
                val valid = isValidPassword(pass)
                if (valid) {
                    // DB
                   progressBar.visibility = ProgressBar.VISIBLE
                    auth.createUserWithEmailAndPassword(mail, pass)
                        .addOnCompleteListener(this) { task ->
                            if(task.isSuccessful) {

                                // Auth
                                user = auth.currentUser!!

                                // stores phone number

                                /*In Future*/

                                // Storing in Database
                                val database = Firebase.database.reference
                                val userId = auth.uid.toString()
                                val info = User(
                                    til_fullname.editText?.text.toString(),
                                    mail,
                                    phone,
                                    "false"
                                )

                                try {
                                    // store info in realtime database
                                    database.child("users").child(userId).setValue(info)
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                // Verification Link
                                                user.sendEmailVerification()

                                                val ref = Firebase.database.getReference("admin/${userId}/admin")

                                                ref.addValueEventListener(object :
                                                    ValueEventListener {
                                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                                        if (dataSnapshot.value.toString().toBoolean()) {
                                                            // User is admin
                                                            startActivity(Intent(this@RegisterActivity, AdminHomeActivity::class.java))
                                                            finish()
                                                        } else {
                                                            // User is not admin
                                                            startActivity(Intent(this@RegisterActivity, HomeActivity::class.java))
                                                            finish()
                                                        }
                                                    }
                                                    override fun onCancelled(error: DatabaseError) {
                                                        Toast.makeText(this@RegisterActivity, "Error getting data", Toast.LENGTH_LONG).show()
                                                    }
                                                })

                                            } else {
                                                Toast.makeText(
                                                    this,
                                                    "Error: ${task.exception?.message}",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                        }
                                } catch (e: FirebaseAuthUserCollisionException) {
                                    Log.e("Error", e.message!!)
                                }
                            } else if(task.exception is FirebaseAuthUserCollisionException) {
                                Toast.makeText(this, "User already exists", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(baseContext, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    progressBar.visibility = ProgressBar.INVISIBLE

                } else {
                    til_password.error = typeErrorPassword(pass)
                }
            }
        }

        binding.tvHaveAccount.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

    }

    fun showPopup() {
        mDialog.setContentView(R.layout.check_internet)
        val retry = mDialog.findViewById<AppCompatButton>(R.id.btnRetry)
        retry.setOnClickListener {
            if (checkForInternet(this@RegisterActivity)) {
                mDialog.dismiss()
                startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                finish()
            }
        }
        mDialog.show()
    }
}