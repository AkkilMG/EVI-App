package `in`.exposysdata.eviapp.blog.mainpage

import `in`.exposysdata.eviapp.HomeActivity
import `in`.exposysdata.eviapp.R
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        // actionbar hide
        supportActionBar?.hide()

        val back = findViewById<ImageView>(R.id.about_back)
        val GitHub = findViewById<ImageView>(R.id.GitHub)
        val LinkedIn = findViewById<ImageView>(R.id.LinkedIn)
        val Telegram = findViewById<ImageView>(R.id.Telegram)
        val Instagram = findViewById<ImageView>(R.id.Instagram)

        back.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }


        // Connect me
        GitHub.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/HeimanPictures")))
        }

        LinkedIn.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/akkilmg")))
        }

        Telegram.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://telegram.me/HeimanCreatiin")))
        }

        Instagram.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/invites/contact/?i=whi70uuwzzey&utm_content=207by39")))
        }

    }
}