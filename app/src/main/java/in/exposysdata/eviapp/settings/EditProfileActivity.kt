package `in`.exposysdata.eviapp.settings

import `in`.exposysdata.eviapp.LoginActivity
import `in`.exposysdata.eviapp.R
import `in`.exposysdata.eviapp.SignActivity
import `in`.exposysdata.eviapp.admin.AdminHomeActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class EditProfileActivity : AppCompatActivity() {


    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        val back = findViewById<ImageView>(R.id.editProfileBack)
        val save = findViewById<AppCompatButton>(R.id.save)
        val profile = findViewById<ImageView>(R.id.editProfile)
        val fullName = findViewById<TextInputLayout>(R.id.editFullName)
        val email = findViewById<TextInputLayout>(R.id.editEmail)
        val phone = findViewById<TextInputLayout>(R.id.editPhone)
        val forgotPassword = findViewById<TextView>(R.id.forgotPassword)

        // Progress bar
        progressBar = findViewById(R.id.progressBar)


        // Action Bar
        supportActionBar?.hide()


        // Read from the database
        if (Firebase.auth.currentUser != null) {
            val auth = Firebase.auth
            val user = Firebase.auth.currentUser
            val userId = user?.uid.toString()
            val info = Firebase.database.getReference("users/${userId}")

            back.setOnClickListener {
                startActivity(Intent(this, SettingsActivity::class.java))
            }

            forgotPassword.setOnClickListener {
                val emailForDB = Firebase.database.getReference("users/${userId}/email")

                emailForDB.addValueEventListener(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            val emailAddress = dataSnapshot.value.toString()
                            Firebase.auth.sendPasswordResetEmail(emailAddress)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(this@EditProfileActivity, "A mail has sent to registered email. Please check your inbox.", Toast.LENGTH_LONG).show()
                                    }
                                }
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@EditProfileActivity, "Error getting data", Toast.LENGTH_SHORT).show()
                    }
                })
            }

            profile.setOnClickListener {
                intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(Intent.createChooser(intent, "Select Profile Picture"), 1)

                val profileImageRef = FirebaseStorage.getInstance().getReference("profileImages/${userId}.jpg")

                Toast.makeText(this, "Profile Image", Toast.LENGTH_LONG).show()
                // Storing in Firebase
                try {
                    profileImageRef.putFile(intent.data!!)
                        .addOnSuccessListener {
                            Toast.makeText(
                                this@EditProfileActivity,
                                "Profile Picture Uploaded",
                                Toast.LENGTH_LONG
                            ).show()
                            profileImageRef.downloadUrl.addOnSuccessListener {
                                val profileImageUrl = it
                                //info.child("profile").setValue(profileImageUrl)

                                Log.e("Profile Image", profileImageUrl.toString())
                                user?.photoUrl?.let {
                                    profile.setImageURI(profileImageUrl)
                                }

                            }
                        }
                } catch (e: Exception) {
                    Toast.makeText(this@EditProfileActivity, "$e", Toast.LENGTH_LONG).show()
                }
            }

            info.addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // User Exists
                        save.setOnClickListener {

                            progressBar.visibility = ProgressBar.VISIBLE
                            // Database Values
                            val db_fullName = dataSnapshot.child("fullName").value.toString()
                            val db_phone = dataSnapshot.child("phoneNumber").value.toString()
                            val db_email = dataSnapshot.child("email").value.toString()


                            // Comparing it with inserted
                            if(fullName.editText?.text.toString().isNotEmpty()) {
                                if (fullName.editText?.text.toString() == db_fullName) {
                                    fullName.error = "Fullname is same as previous"
                                } else {
                                    dataSnapshot.child("fullName").ref.setValue(fullName.editText?.text.toString())
                                }
                            }

                            if (phone.editText?.text.toString().isNotEmpty()) {
                                if (phone.editText?.text.toString() == db_phone) {
                                    phone.error = "Phone Number is same as previous"
                                } else {
                                    dataSnapshot.child("phoneNumber").ref.setValue(phone.editText?.text.toString())
                                }
                            }

                            if (email.editText?.text.toString().isNotEmpty()) {
                                if (email.editText?.text.toString() == db_email) {
                                    email.error = "Email is same as previous"
                                } else {
                                    user!!.updateEmail("user@example.com")
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                Toast.makeText(this@EditProfileActivity, "Email address is updated.", Toast.LENGTH_SHORT).show()
                                                user.sendEmailVerification()
                                            } else {
                                                Toast.makeText(this@EditProfileActivity, "Failed to update email!", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    dataSnapshot.child("email").ref.setValue(email.editText?.text.toString())

                                }
                            }

                                progressBar.visibility = ProgressBar.INVISIBLE

                                startActivity(Intent(this@EditProfileActivity, SettingsActivity::class.java))
                                finish()

                        }
                    } else {
                        startActivity(Intent(this@EditProfileActivity, LoginActivity::class.java))
                        finish()
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@EditProfileActivity, "Error getting data", Toast.LENGTH_LONG).show()
                }
                })
        } else {
            startActivity(Intent(this, SignActivity::class.java))
            finish()
        }

    }
}