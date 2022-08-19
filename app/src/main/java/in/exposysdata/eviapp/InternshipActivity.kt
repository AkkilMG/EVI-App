package `in`.exposysdata.eviapp

import `in`.exposysdata.eviapp.databinding.ActivityInternshipBinding
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class InternshipActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInternshipBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInternshipBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        binding.btnToApplication.setOnClickListener {

            // Check Internet
            startActivity(Intent(this, ApplicationActivity::class.java))
            finish()
        }

    }
}