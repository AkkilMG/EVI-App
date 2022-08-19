package `in`.exposysdata.eviapp

import `in`.exposysdata.eviapp.admin.AdminHomeActivity
import `in`.exposysdata.eviapp.blog.mainpage.AboutActivity
import `in`.exposysdata.eviapp.databinding.ActivityHomeBinding
import `in`.exposysdata.eviapp.homeui.SliderAdapter
import `in`.exposysdata.eviapp.retry.checkForInternet
import `in`.exposysdata.eviapp.settings.SettingsActivity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.lang.Exception

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser

    // Nav
    private lateinit var toggle: ActionBarDrawerToggle

    // Home UI
    private var recyclerView4: RecyclerView? = null
    private var array = ArrayList<String>()

    // retry functionality
    private lateinit var mDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(1024, 1024)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        // database
        val database = Firebase.database.reference


        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        if (!checkForInternet(this@HomeActivity)) {

            // Pop up with check_internet.xml
            mDialog = Dialog(this@HomeActivity)
            showPopup()
        }

        if (auth.currentUser == null) {
            startActivity(Intent(this, SignActivity::class.java))
            finish()
        } else {
            user = auth.currentUser!!
            var check = user.isEmailVerified
            //Log.e("Error", "check: ${check.toString()}")

            val admin = Firebase.database.getReference("users").child(user.uid).child("admin")
            admin.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        val bol = p0.value.toString().toBoolean()
                        if (bol) {
                            startActivity(Intent(this@HomeActivity, AdminHomeActivity::class.java))
                            finish()
                        } else {
                            if (check) {

                                val userId = auth.uid.toString()

                                //actionBar?.setDisplayHomeAsUpEnabled(true)
                                //actionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM or ActionBar.DISPLAY_SHOW_HOME or ActionBar.DISPLAY_HOME_AS_UP
                                toggle = ActionBarDrawerToggle(this@HomeActivity, drawerLayout, R.string.open, R.string.close)

                                drawerLayout.addDrawerListener(toggle)
                                toggle.syncState()

                                supportActionBar?.setDisplayHomeAsUpEnabled(true)

                                // Home UI
                                val slider = Firebase.database.getReference("slider")
                                try {
                                    slider.addValueEventListener(object : ValueEventListener {
                                        override fun onCancelled(p0: DatabaseError) {
                                            //
                                        }

                                        override fun onDataChange(p0: DataSnapshot) {
                                            if (p0.exists()) {
                                                for (h in p0.children) {
                                                    //Log.e("h",h.value.toString())
                                                    array.add(h.value.toString())
                                                }
                                                //recyclerView4!!.adapter = SliderAdapter(this@HomeActivity, array)
                                                recyclerView4 = findViewById(R.id.recyclerview1)
                                                val mLayoutManager4 = LinearLayoutManager(
                                                    this@HomeActivity,
                                                    LinearLayoutManager.HORIZONTAL,
                                                    false
                                                )
                                                recyclerView4!!.layoutManager = mLayoutManager4
                                                recyclerView4!!.setHasFixedSize(true)
                                                //recyclerView4!!.isNestedScrollingEnabled = false
                                                recyclerView4!!.adapter =
                                                    SliderAdapter(this@HomeActivity, array)
                                            }
                                        }
                                    })
                                } catch (e: Exception) {

                                }

                                val recycler = findViewById<RecyclerView>(R.id.recyclerview1)

                                // On clicking recycler should start new activity
                                recycler.setRecyclerListener {
                                    startActivity(Intent(this@HomeActivity, InternshipActivity::class.java))
                                }

                                val InternshipHome = findViewById<ImageView>(R.id.InternshipHome)
                                val FundingHome = findViewById<ImageView>(R.id.FundingHome)
                                val MessageHome = findViewById<ImageView>(R.id.MessageHome)
                                val ReportHome = findViewById<ImageView>(R.id.ReportHome)

                                InternshipHome.setOnClickListener {
                                    startActivity(Intent(this@HomeActivity, InternshipActivity::class.java))
                                }

                                FundingHome.setOnClickListener {

                                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://exposysdata.com/fund.html")))
                                }

                                MessageHome.setOnClickListener {

                                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://exposysdata.com/Contact.html")))
                                }

                                ReportHome.setOnClickListener {

                                    // mail to developer
                                }

                                // Home UI Ends

                                // Admin or not
                                // https://console.firebase.google.com/u/0/project/evi-app-demo/database/evi-app-demo-default-rtdb/data/~2Fusers~2F-N8qoCvqTyUtMXu_GQIq~2Fadmin
                                // https://evi-app-demo-default-rtdb.firebaseio.com/admin/-N8r6ZK5nXed1tKm3jQm/admin
                                val ref = Firebase.database.getReference("users/${userId}")
                                Log.e("Ref", "ref: ${ref.toString()}")
                                Log.e("UserId", userId)
                                ref.addValueEventListener(object :
                                    ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            val user_profile = findViewById<ImageView>(R.id.user_profile)
                                            val user_name = findViewById<TextView>(R.id.user_name)
                                            val user_mail = findViewById<TextView>(R.id.user_mail)

                                            //
                                            val profile_image = findViewById<ImageView>(R.id.profile_image)
                                            val emailText = findViewById<TextView>(R.id.emailText)
                                            val nameText = findViewById<TextView>(R.id.textView2)

                                            if (user.photoUrl != null) {
                                                Glide.with(this@HomeActivity)
                                                    .load(user.photoUrl.toString())
                                                    .into(profile_image)
                                            } else {
                                                profile_image?.setImageResource(R.drawable.ic_circle_contact_boy)
                                            }

                                            //
                                            if (user.photoUrl != null) {
                                                Glide.with(this@HomeActivity)
                                                    .load(user.photoUrl.toString())
                                                    .into(user_profile)
                                            } else {
                                                user_profile?.setImageResource(R.drawable.ic_circle_contact_boy)
                                            }

                                            val db_user_name = dataSnapshot.child("fullName").value.toString()
                                            val db_user_mail = dataSnapshot.child("email").value.toString()

                                            //Log.e("User Info", "User: $user_name Email: $user_mail")
                                            user_name?.text = db_user_name
                                            user_mail?.text = db_user_mail
                                            nameText.text = db_user_name
                                            emailText.text = db_user_mail

                                            navView.setNavigationItemSelectedListener {

                                                when (it.itemId) {
                                                    R.id.nav_home -> {
                                                        drawerLayout.closeDrawer(GravityCompat.START)
                                                    }
                                                    R.id.nav_about -> {
                                                        startActivity(
                                                            Intent(
                                                                this@HomeActivity,
                                                                AboutActivity::class.java
                                                            )
                                                        )
                                                    }
                                                    R.id.nav_settings -> {
                                                        startActivity(
                                                            Intent(
                                                                this@HomeActivity,
                                                                SettingsActivity::class.java
                                                            )
                                                        )
                                                    }
                                                    R.id.nav_logout -> {
                                                        Firebase.auth.signOut()
                                                        startActivity(Intent(this@HomeActivity, SignActivity::class.java))
                                                        finish()
                                                    }
                                                    R.id.nav_message -> {
                                                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://exposysdata.com/Contact.html")))
                                                    }
                                                    R.id.nav_internship -> {
                                                        startActivity(
                                                            Intent(
                                                                this@HomeActivity,
                                                                InternshipActivity::class.java
                                                            )
                                                        )
                                                        finish()
                                                    }
                                                    R.id.nav_share -> {
                                                        //
                                                        val intent = Intent()
                                                        intent.action = Intent.ACTION_SEND
                                                        intent.putExtra(Intent.EXTRA_TEXT, "Check out this great app for Internship: https://")
                                                        intent.type = "text/plain"
                                                        startActivity(Intent.createChooser(intent, "Share to:"))
                                                    }
                                                }
                                                true
                                            }
                                        } else {
                                            startActivity(Intent(this@HomeActivity, SignActivity::class.java))
                                            finish()
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        startActivity(Intent(this@HomeActivity, SignActivity::class.java))
                                        finish()
                                    }
                                })

                            } else {
                                startActivity(Intent(this@HomeActivity, VerifyActivity::class.java))
                                finish()

                            }
                        }
                    }
                }
            })



            // Nav bar

            // Setting user_name and user_mail
            //user_name = findViewById(R.id.user_name)
            //user_mail = findViewById(R.id.user_mail)

            //user_name.text = user.displayName
            //user_mail.text = auth.currentUser!!.email


            /*binding.internAd.setOnClickListener{
                startActivity(Intent(this, ApplicationActivity::class.java))
                finish()
            }*/

        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun showPopup() {
        mDialog.setContentView(R.layout.check_internet)
        val retry = mDialog.findViewById<Button>(R.id.btnRetry)
        retry.setOnClickListener {
            if (checkForInternet(this@HomeActivity)) {
                mDialog.dismiss()
            }
        }
        mDialog.show()

    }

}

