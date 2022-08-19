package `in`.exposysdata.eviapp.admin

import `in`.exposysdata.eviapp.LoginActivity
import `in`.exposysdata.eviapp.R
import `in`.exposysdata.eviapp.settings.SettingsActivity
import `in`.exposysdata.eviapp.user.UserListActivity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.cardview.widget.CardView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AdminHomeActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home)

        supportActionBar?.hide()

        val auth = Firebase.auth
        val userList = findViewById<CardView>(R.id.userListPage)
        val logoutAdmin = findViewById<ImageView>(R.id.adminLogout)
        val applicantList = findViewById<CardView>(R.id.applicantList)
        val AdminWebsite = findViewById<ImageView>(R.id.AdminWebsite)
        //val blogList = findViewById<Button>(R.id.BlogPage)

        /*blogList.setOnClickListener {
            val intent = Intent(this, BlogListActivity::class.java)
            startActivity(intent)
        }*/

        if(auth.currentUser != null) {

            val user = auth.currentUser
            logoutAdmin.setOnClickListener {
                auth.signOut()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }

            AdminWebsite.setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://exposysdata.com/")))
            }

            userList.setOnClickListener {
                startActivity(Intent(this, UserListActivity::class.java))
            }

            applicantList.setOnClickListener {
                startActivity(Intent(this, ApplicantListActivity::class.java))
            }

        } else {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

    }
}