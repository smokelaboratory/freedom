package com.smokelaboratory.sample

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.smokelaboratory.freedom.Freedom
import com.smokelaboratory.freedom.rememberFreedomRequester

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val freedomRequest = rememberFreedomRequester()

            Freedom(
                freedomRequest,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                onPermissionResult = { isGranted, status ->
                    Log.e("TAG", status.toString())

                    Text(text = status.toString(), modifier = Modifier.clickable {
                    })

                },
                initialContent = {
                    //todo : make it global for requesting after denial
                    Text(text = "Test", modifier = Modifier.clickable {
                        freedomRequest.request()
                    })
                })
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}