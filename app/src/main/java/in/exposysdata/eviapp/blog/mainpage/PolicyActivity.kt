package `in`.exposysdata.eviapp.blog.mainpage

import `in`.exposysdata.eviapp.HomeActivity
import `in`.exposysdata.eviapp.R
import `in`.exposysdata.eviapp.SignActivity
import `in`.exposysdata.eviapp.admin.AdminHomeActivity
import `in`.exposysdata.eviapp.settings.SettingsActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class PolicyActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_policy)

        val back = findViewById<ImageView>(R.id.policy_back)


        // Action Bar
        supportActionBar?.hide()

        // Read from the database
        if (Firebase.auth.currentUser != null) {
            val user = Firebase.auth.currentUser
            val userId = user?.uid.toString()

            val post = Firebase.database.getReference("/post/policy/body")

            post.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val body = snapshot.value.toString()
                    val policyBody = findViewById<TextView>(R.id.policyBody)

                    policyBody.text = body
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@PolicyActivity, "Error getting data", Toast.LENGTH_LONG).show()
                }

            })

            val Ifadmin = Firebase.database.getReference("users/${userId}/admin")

            back.setOnClickListener {

                Ifadmin.addValueEventListener(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (!dataSnapshot.value.toString().toBoolean()) {
                            // User is admin

                            startActivity(Intent(this@PolicyActivity, HomeActivity::class.java))
                            finish()

                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@PolicyActivity, "Error getting data", Toast.LENGTH_LONG).show()
                    }
                })
            }
        } else {
            startActivity(Intent(this, SignActivity::class.java))
            finish()
        }

    }
}