package `in`.exposysdata.eviapp.admin

import `in`.exposysdata.eviapp.R
import `in`.exposysdata.eviapp.admin.applicant.AdminListAdapter
import `in`.exposysdata.eviapp.user.ApplicantData
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class ApplicantListActivity : AppCompatActivity() {

    private lateinit var dbref : DatabaseReference
    private lateinit var applicantRecyclerView: RecyclerView
    private lateinit var applicantArrayList: ArrayList<ApplicantData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_applicant_list)

        val back = findViewById<ImageView>(R.id.applicantList_back)
        val applicantList = findViewById<RecyclerView>(R.id.applicantList)
        // val applicantOption = applicantList.findViewById<ImageView>(R.id.applicantOption)

        // ActionBar
        supportActionBar?.hide()

        applicantArrayList = arrayListOf<ApplicantData>()

        dbref = FirebaseDatabase.getInstance().getReference("applicant")
        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {

                    for (userSnapshot in snapshot.children) {

                        // Log.e("snapshot.children", snapshot.children.toString())
                        val applicant = userSnapshot.getValue(ApplicantData::class.java)
                        applicantArrayList.add(applicant!!)

                    }
                    applicantRecyclerView = findViewById(R.id.applicantList)
                    applicantRecyclerView.layoutManager = LinearLayoutManager(this@ApplicantListActivity)
                    applicantRecyclerView.setHasFixedSize(true)
                    applicantRecyclerView.adapter = AdminListAdapter(applicantArrayList)

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


        //applicantRecyclerView.getChildAdapterPosition(applicantRecyclerView.findViewHolderForAdapterPosition(0)!!.itemView)

        // Popup Menu in an recycler view
        /*applicantOption.setOnClickListener {
            // postion in recycler view
            //val position = applicantRecyclerView.getChildAdapterPosition(applicantRecyclerView.findViewHolderForAdapterPosition(0)!!.itemView)
            //applicantList[position].findViewById<ImageView>(R.id.applicantOption)
            val popupMenu = PopupMenu(this@ApplicantListActivity, it)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.EmailIt -> {

                        // Mail it
                        val position = applicantList.getChildAdapterPosition(it)
                        var type = false
                        Message(position, type)
                        true
                    }
                    R.id.WhatsAppIt -> {

                        // WhatsApp it
                        val position = applicantList.getChildAdapterPosition(it)
                        var type = true
                        Message(position, type)
                        true
                    }
                    else -> false
                }
            }

            popupMenu.inflate(R.menu.send)

            try {
                val fieldMPopup = PopupMenu::class.java.getDeclaredField("mPopup")
                fieldMPopup.isAccessible = true
                val mPopup = fieldMPopup.get(popupMenu)
                mPopup.javaClass
                    .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                    .invoke(mPopup, true)
            } catch (e: Exception) {
                // Log.e("error", e.message)
            } finally {
                popupMenu.show()
            }
        }*/
    }

    fun Message(position: Int,type: Boolean){
        val user = applicantArrayList[position]
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