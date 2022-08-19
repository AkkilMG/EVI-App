package `in`.exposysdata.eviapp

import `in`.exposysdata.eviapp.admin.AdminHomeActivity
import `in`.exposysdata.eviapp.retry.checkForInternet
import `in`.exposysdata.eviapp.settings.SettingsActivity
import android.Manifest
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var motionLayout: MotionLayout

    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser

    // permission functionality
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private var isReadPermissionGranted = false
    private var isLocationPermissionGranted = false

    // retry functionality
    private lateinit var mDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        // Initialize Firebase Auth
        auth = Firebase.auth

        // Storing in Database
        val database = Firebase.database.reference

        motionLayout=findViewById(R.id.motion_layout)

        motionLayout.startLayoutAnimation()

        motionLayout.setTransitionListener(object :MotionLayout.TransitionListener{
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {}

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {}

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                if (!checkForInternet(this@MainActivity)) {

                    // Pop up with check_internet.xml
                    mDialog = Dialog(this@MainActivity)

                    showPopup()
                }
                if (auth.currentUser != null) {
                    user = auth.currentUser!!
                    user.reload()
                    val check = user.isEmailVerified

                    if (check) {

                        // permission
                        /*permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permission ->

                            isReadPermissionGranted = permission[Manifest.permission.READ_EXTERNAL_STORAGE] ?: isReadPermissionGranted

                            isLocationPermissionGranted = permission[Manifest.permission.ACCESS_FINE_LOCATION] ?: isLocationPermissionGranted

                        }

                        requestPermission()*/

                        val user = Firebase.auth.currentUser
                        val userId = user?.uid.toString()
                        val ref = Firebase.database.getReference("users/${userId}/admin")
                        Log.e("Ref", "ref: ${ref.toString()}")
                        Log.e("UserId",userId)
                        ref.addValueEventListener(object :
                            ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                if (dataSnapshot.value.toString().toBoolean()) {
                                    // User is admin
                                    startActivity(Intent(this@MainActivity, AdminHomeActivity::class.java))
                                    finish()
                                } else {
                                    startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                                    finish()
                                }
                            }
                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(this@MainActivity, "Error getting data", Toast.LENGTH_LONG).show()
                            }
                        })

                    } else {

                        // User is not verified
                        startActivity(Intent(this@MainActivity, VerifyActivity::class.java))
                        finish()
                    }
                } else {

                    startActivity(Intent(this@MainActivity, SignActivity::class.java))
                    finish()
                }
            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {

            }

        })

    }

    /*private fun requestPermission() {

        isReadPermissionGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        isLocationPermissionGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val permissionRequest : MutableList<String> = ArrayList()

        if(!isReadPermissionGranted) {
            permissionRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if(!isLocationPermissionGranted) {
            permissionRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        if(permissionRequest.isNotEmpty()) {
            permissionLauncher.launch(permissionRequest.toTypedArray())
        }
    }*/

    fun showPopup() {
        mDialog.setContentView(R.layout.check_internet)
        val retry = mDialog.findViewById<AppCompatButton>(R.id.btnRetry)
        retry.setOnClickListener {
            if (checkForInternet(this@MainActivity)) {
                mDialog.dismiss()
                startActivity(Intent(this@MainActivity, MainActivity::class.java))
                finish()
            }
        }
        mDialog.show()
    }
}