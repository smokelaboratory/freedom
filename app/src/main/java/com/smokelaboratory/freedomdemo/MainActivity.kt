package com.smokelaboratory.freedomdemo

import android.Manifest
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.smokelaboratory.freedom.Freedom
import com.smokelaboratory.freedom.R
import com.smokelaboratory.freedom.freedom

class MainActivity : AppCompatActivity() {

    private lateinit var freedom: Freedom

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        freedom = freedom(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            {}
        ) { isGranted, status ->
            Toast.makeText(this, "Permission granted : $isGranted", Toast.LENGTH_LONG).show()
        }

        findViewById<View>(R.id.tv).setOnClickListener {
            freedom.request()
        }

    }
}
