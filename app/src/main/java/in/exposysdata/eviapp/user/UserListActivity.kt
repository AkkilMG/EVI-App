package `in`.exposysdata.eviapp.user

import `in`.exposysdata.eviapp.R
import `in`.exposysdata.eviapp.admin.AdminHomeActivity
import android.content.Intent
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.security.KeyStore


class UserListActivity : AppCompatActivity() {

    private lateinit var dbref : DatabaseReference
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userArrayList: ArrayList<UserData>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        val back = findViewById<ImageView>(R.id.userList_back)
        val userList = findViewById<RecyclerView>(R.id.userList)

        // ActionBar
        supportActionBar?.hide()

        userArrayList = arrayListOf<UserData>()

        dbref = FirebaseDatabase.getInstance().getReference("users")
        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {

                    for (userSnapshot in snapshot.children) {

                        // Log.e("snapshot.children", snapshot.children.toString())
                        val user = userSnapshot.getValue(UserData::class.java)
                        userArrayList.add(user!!)

                    }
                    userRecyclerView = findViewById(R.id.userList)
                    userRecyclerView.layoutManager = LinearLayoutManager(this@UserListActivity)
                    userRecyclerView.setHasFixedSize(true)
                    userRecyclerView.adapter = MyAdapter(userArrayList)
                }
            }

            override fun onCancelled(snapshot: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })

        back.setOnClickListener {
            startActivity(Intent(this, AdminHomeActivity::class.java))
            finish()
        }

        userList.setOnClickListener {

            // get position of clicked item
            val position = userList.getChildAdapterPosition(it)

            if (true) {
                var type = true
                Message(position, type)
            } else {
                var type = false
                Message(position, type)
            }

        }

    }

    fun Message(position: Int,type: Boolean){
        val user = userArrayList[position]
        val name = user.fullName
        val email = user.email
        val phone = user.phoneNumber

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        var message = "**ONLINE INTERNSHIP PROGRAM-WE HAVE LIMITED SEATS**\n\n" +
                    "Dear Aspirant\n\n" +
                    "We received your application for Internship. Based on Percentage criteria your profile has been shortlisted for a free Internship for one Month. After one month based on internship performance our team will take a further level of interview process.\n\n" +
                    "Only application fee Rs 500/-\n\n" +
                    "Fee Includes:\n" +
                    "1.Project Access\n" +
                    "2.Technical Support\n" +
                    "3.Resume Building\n" +
                    "4.E-Certificate\n" +
                    "5.It’s completely Flexible timings and Work from Home\n\n"+
                    "Apply Here\n\n" +
                    "Online application: \n" +
                    "http://exposysdata.in/registration.php\n\n" +
                    "Warm Regards\n" +
                    "Exposys Data Labs\n" +
                    "www.exposysdata.com\n" +
                    "www.exposysdata.in\n" +
                    "+917795207065"
        if (type) {
            // send message to WhatsApp to specific number
            intent.setPackage("com.whatsapp")
            intent.putExtra("address", phone)
            intent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(intent)
        } else {
            // send mail
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            intent.putExtra(Intent.EXTRA_SUBJECT, "Internship Application")
            intent.putExtra(Intent.EXTRA_TEXT, message)
            intent.setType("message/rfc822")
            startActivity(Intent.createChooser(intent, "Choose an Email client :"))

        }



    }
}