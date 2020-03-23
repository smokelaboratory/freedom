package com.smokelaboratory.freedomdemo

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.smokelaboratory.freedom.Freedom
import com.smokelaboratory.freedom.R
import com.smokelaboratory.freedom.freedom

class MainActivity : AppCompatActivity() {

    private lateinit var freedom: Freedom

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        freedom = freedom(this) {

            permissions =
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            userResponse = { areAllGranted, permissionStatus ->
                //handle user response
                Log.e("TAG", areAllGranted.toString())
            }

            shouldShowSettingsPopup = true
            popupConfigurations {
                title = "Permission needed"
                negativeButtonText = "Not Now"
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        freedom.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        freedom.onActivityResult(requestCode, resultCode, data)
    }
}
