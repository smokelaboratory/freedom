package com.smokelaboratoy.freedom

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var freedom: Freedom

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        freedom = freedom(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        ) { isGranted, status ->
            Toast.makeText(this, "Permission granted : $isGranted", Toast.LENGTH_LONG).show()
        }

        findViewById<View>(R.id.tv).setOnClickListener {
            freedom.request()
        }

    }
}