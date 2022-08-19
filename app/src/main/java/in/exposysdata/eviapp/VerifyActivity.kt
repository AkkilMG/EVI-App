package `in`.exposysdata.eviapp

import `in`.exposysdata.eviapp.admin.AdminHomeActivity
import `in`.exposysdata.eviapp.databinding.ActivityVerifyBinding
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class VerifyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerifyBinding

    // Firebase Auth
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Firebase init
        auth = Firebase.auth

        // database init
        val database = Firebase.database.reference

        // Get current user
        user = auth.currentUser!!

        binding.resendButton.setOnClickListener {
            if(auth.currentUser == null){
                startActivity(Intent(this, SignActivity::class.java))
                finish()
            } else {
                user.reload()
                var check = user.isEmailVerified
                if(check) {
                    // User is verified
                    val userId = auth.uid.toString()
                    // User is verified
                    val ref = Firebase.database.getReference("admin/${userId}/admin")

                    ref.addValueEventListener(object :
                        ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.value.toString().toBoolean()) {
                                // User is admin
                                startActivity(Intent(this@VerifyActivity, AdminHomeActivity::class.java))
                                finish()
                            } else {
                                // User is not admin
                                startActivity(Intent(this@VerifyActivity, HomeActivity::class.java))
                                finish()
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(this@VerifyActivity, "Error getting data", Toast.LENGTH_LONG).show()
                        }
                    })
                } else {
                    // User is not verified
                    user.sendEmailVerification()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Verification email sent! Please check your mail", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this, "Failed to send verification email", Toast.LENGTH_SHORT).show()
                            }
                        }

                }
            }
        }

    }
}