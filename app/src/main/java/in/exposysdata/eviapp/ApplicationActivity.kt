package `in`.exposysdata.eviapp

import `in`.exposysdata.eviapp.databinding.ActivityApplicationBinding
import `in`.exposysdata.eviapp.retry.checkForInternet
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList


class ApplicationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityApplicationBinding

    val items = arrayListOf<String>("Software Development", "Full Stack Developer", "Mean Stack Developer", "Web Development", "Data Science", "IOT", "App Developer", "Cyber Security", "HR", "Content Writer", "Process Associate", "Digital Marketing", "UI/UX Design", "Business Development", "Marketing", "Tele Caller", "Email Marketing", "SMS Marketing", "Photographer/Video Grapher", "Film Maker", "Digital Content Creator", "Social Media Promotor")

    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private lateinit var adapterItems : ArrayAdapter<String>

    // Firebase instance variables
    private lateinit var auth: FirebaseAuth

    // retry functionality
    private lateinit var mDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApplicationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = Firebase.auth

        supportActionBar?.hide()

        // Drop Box
        autoCompleteTextView = findViewById(R.id.ed_app_domain)

        adapterItems = ArrayAdapter(this, R.layout.list_domain, items)

        autoCompleteTextView.setAdapter(adapterItems)

        autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            val item = parent.getItemAtPosition(position).toString()
            // var id_domain = item
            //Toast.makeText(applicationContext, "Item"+item, Toast.LENGTH_SHORT).show()
        }
        //var id_domain = item

        //
        var id_full_name = findViewById<TextInputLayout>(R.id.app_full_name)
        var id_email = findViewById<TextInputLayout>(R.id.app_email)
        var id_phone = findViewById<TextInputLayout>(R.id.app_phone)

        if (!checkForInternet(this@ApplicationActivity)) {

            // Pop up with check_internet.xml
            mDialog = Dialog(this@ApplicationActivity)

            showPopup()
        }

        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }


        binding.btnSubmit.setOnClickListener {
            if(id_full_name.editText?.text.toString().isEmpty()) {
                id_full_name.error = "Full name is required."
            } else if(id_email.editText?.text.toString().isEmpty()) {
                id_email.error = "Email is required."
            } else if(id_phone.editText?.text.toString().isEmpty()) {
                id_phone.error = "Phone number is required."
            } /*else if(id_domain.editText?.text.toString().isEmpty()) {
                autoCompleteTextView.error = "Domain is required."
            }*/ else {


                try {
                    val userId = auth.uid.toString()
                    val database = Firebase.database.reference
                    // get id of item in autoCompleteTextView
                    val id_domain = autoCompleteTextView.text.toString()
                    // Date as per user Time Zone
                    val date = Calendar.getInstance().time
                    val dateFormat = android.text.format.DateFormat.format("dd-MM-yyyy hh:mm:ss", date) as String
                    val info = Applicant (
                        id_email.editText?.text.toString(),
                        id_full_name.editText?.text.toString(),
                        id_phone.editText?.text.toString(),
                        id_domain,
                        dateFormat
                    )

                    database.child("applicant").child(userId).setValue(info)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {

                                // startActivity(Intent(this, HomeActivity::class.java))
                                // finish()
                                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://exposysdata.in/registration.php")))
                                finish()

                            } else {
                                Toast.makeText(
                                    this,
                                    "Error: ${task.exception?.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                } catch (e: Exception) {
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }

            }
        }
    }

    fun showPopup() {
        mDialog.setContentView(R.layout.check_internet)
        val retry = mDialog.findViewById<AppCompatButton>(R.id.btnRetry)
        retry.setOnClickListener {
            if (checkForInternet(this@ApplicationActivity)) {
                mDialog.dismiss()
                startActivity(Intent(this@ApplicationActivity, MainActivity::class.java))
                finish()
            }
        }
        mDialog.show()
    }

}