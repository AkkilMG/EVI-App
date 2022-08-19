package `in`.exposysdata.eviapp.settings

import `in`.exposysdata.eviapp.Admin
import `in`.exposysdata.eviapp.HomeActivity
import `in`.exposysdata.eviapp.R
import `in`.exposysdata.eviapp.SignActivity
import `in`.exposysdata.eviapp.admin.AdminHomeActivity
import `in`.exposysdata.eviapp.blog.mainpage.PolicyActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.SwitchCompat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView
import com.bumptech.glide.Glide
import java.lang.Exception

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)

        // Storing in Database
        val database = Firebase.database.reference

        supportActionBar?.hide()

        // Read from the database
        if (Firebase.auth.currentUser != null) {
            val user = Firebase.auth.currentUser
            val userId = user?.uid.toString()
            val admin = Firebase.database.getReference("users/${userId}/admin")

            val editProfile = findViewById<AppCompatButton>(R.id.EditProfile)

            val back = findViewById<ImageView>(R.id.settings_back)
            val policy = findViewById<LinearLayout>(R.id.PolicyPage)
            val logout = findViewById<LinearLayout>(R.id.Logout)

            val nightMode = findViewById<SwitchCompat>(R.id.NightModeSwitch)
            val notification = findViewById<SwitchCompat>(R.id.NotificationSwitch)

            // User Info
            val profile = findViewById<ImageView>(R.id.settings_profile)
            val fullname = findViewById<TextView>(R.id.settings_fullname)
            val email = findViewById<TextView>(R.id.settings_email)


            admin.addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.value.toString().toBoolean()) {
                        startActivity(Intent(this@SettingsActivity, AdminHomeActivity::class.java))
                        finish()
                    } else {

                        val users = Firebase.database.getReference("users/${userId}")

                        users.addValueEventListener(object :
                            ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()) {

                                    if(user?.photoUrl != null) {
                                        Glide.with(this@SettingsActivity)
                                            .load(user.photoUrl.toString())
                                            .into(profile)
                                    } else {
                                        profile.setImageResource(R.drawable.ic_circle_contact_boy)
                                    }

                                    val fullname_db = snapshot.child("fullName").value.toString()
                                    val email_db = snapshot.child("email").value.toString()

                                    Log.e("fullname", fullname_db)
                                    Log.e("email", email_db)

                                    fullname.text = fullname_db
                                    email.text = email_db


                                } else {
                                    Toast.makeText(this@SettingsActivity, "Can't Access", Toast.LENGTH_SHORT).show()
                                }

                            }

                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(this@SettingsActivity, "Error getting data", Toast.LENGTH_LONG).show()
                            }

                        })

                        back.setOnClickListener {
                            startActivity(Intent(this@SettingsActivity, HomeActivity::class.java))
                            finish()
                        }

                        editProfile.setOnClickListener {
                            startActivity(Intent(this@SettingsActivity, EditProfileActivity::class.java))
                        }

                        policy.setOnClickListener {
                            startActivity(Intent(this@SettingsActivity, PolicyActivity::class.java))
                        }

                        logout.setOnClickListener {
                            Firebase.auth.signOut()
                            startActivity(Intent(this@SettingsActivity, SignActivity::class.java))
                            finish()
                        }

                        // Night Mode
                        var checkNight = nightMode.isChecked
                        if (checkNight) {
                            //Toast.makeText(this@SettingsActivity, "Night Mode On", Toast.LENGTH_SHORT).show()
                            //nightMode.isChecked = true
                        } else {
                            //Toast.makeText(this@SettingsActivity, "Night Mode Off", Toast.LENGTH_SHORT).show()
                            //nightMode.isChecked = false
                        }
                        var checkNotification = notification.isChecked
                        if (checkNotification) {
                            //Toast.makeText(this@SettingsActivity, "Notification On", Toast.LENGTH_SHORT).show()
                            //notification.setChecked(true)
                        } else {
                            //Toast.makeText(this@SettingsActivity, "Notification Off", Toast.LENGTH_SHORT).show()
                            //notification.isChecked = false
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@SettingsActivity, "Error getting data", Toast.LENGTH_LONG).show()
                }
            })

        } else {
            startActivity(Intent(this, SignActivity::class.java))
            finish()
        }

    }
}