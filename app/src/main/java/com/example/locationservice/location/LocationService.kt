package com.example.locationservice.location

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.locationservice.R
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class LocationService : Service() {
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: LocationClient

    val notification = NotificationCompat.Builder(this, "location")
        .setContentTitle("Tracking location ...")
        .setContentText("Location: null")
        .setSmallIcon(R.drawable.ic_launcher_background)
        .setOngoing(true)
    var notificationManager: NotificationManager? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        locationClient = LocationClientImpl(
            applicationContext, LocationServices.getFusedLocationProviderClient(applicationContext)
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    fun start() {


        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        locationClient.getLocationUpdates(10000L)
            .catch { it.printStackTrace() }
            .onEach { location ->
                val updatedNotification =
                    notification.setContentText("Location -> (${location.latitude}, ${location.longitude})")
                notificationManager?.notify(1, updatedNotification.build())
            }.launchIn(serviceScope)
    }

    fun stop() {
        stopForeground(STOP_FOREGROUND_DETACH)
        stopSelf()
        notificationManager?.cancel(null, 1)
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }
}