package com.example.locationservice

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.example.locationservice.location.LocationService
import com.example.locationservice.ui.theme.LocationServiceTheme
import java.security.Permission

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PackageManager.PERMISSION_GRANTED
        )
        setContent {
            LocationServiceTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Button(onClick = {
                            Intent(applicationContext, LocationService::class.java).apply { action = LocationService.ACTION_START
                                startService(this)}

                        }) {
                            Text(text = "Start")
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(onClick = {
                            Intent(applicationContext, LocationService::class.java).apply { action = LocationService.ACTION_STOP
                                startService(this)}

                        }) {
                            Text(text = "Stop")
                        }
                    }
                }
            }
        }
    }
}
