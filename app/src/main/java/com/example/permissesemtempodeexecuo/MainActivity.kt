package com.example.permissesemtempodeexecuo

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.permissesemtempodeexecuo.ui.theme.PermissõesEmTempoDeExecuçãoTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : ComponentActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var localizacaoText by mutableStateOf("")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setContent {
            Tela(this)
        }
    }

    private fun getLastLocation() {
        if (ContextCompat.checkSelfPermission(
                baseContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let {
                        val latitude = it.latitude
                        val longitude = it.longitude
                        Log.d("Localização", "Lat: $latitude, Long: $longitude")
                        // aqui você pode usar a localização como quiser
                        localizacaoText = "Lat: $latitude\n Long: $longitude"
                    } ?: run {
                        Log.d("Localização", "Localização é nula, talvez o GPS esteja desligado")
                        localizacaoText = "Localização é nula, talvez o GPS esteja desligado"
                    }
                }
        }
    }

    @SuppressLint("NewApi")
    @Composable
    fun Tela(activity: MainActivity) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = localizacaoText,
                fontSize = 36.sp
            )
            Button(
                modifier = Modifier.padding(top = 16.dp),
                onClick = {

                    when {
                        ContextCompat.checkSelfPermission(
                            baseContext,
                            android.Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED -> {
                            // You can use the API that requires the permission.
                            getLastLocation()
                        }

                        ActivityCompat.shouldShowRequestPermissionRationale(
                            activity, Manifest.permission.ACCESS_FINE_LOCATION
                        ) -> {
                            // In an educational UI, explain to the user why your app requires this
                            // permission for a specific feature to behave as expected, and what
                            // features are disabled if it's declined. In this UI, include a
                            // "cancel" or "no thanks" button that lets the user continue
                            // using your app without granting the permission.
                            Toast.makeText(
                                activity,
                                "Permissão negada automaticante",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> {
                            // You can directly ask for the permission.
                            requestPermissions(
                                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                1302
                            )
                        }
                    }


                }) {
                Text("Pegar localização")
            }

        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        deviceId: Int
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId)
        if (requestCode == 1302) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissão concedida
                getLastLocation()
            } else {
                // Permissão negada
                Toast.makeText(
                    this,
                    "Permissão negada. Não é possível acessar a localização.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}



