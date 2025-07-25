package servicoop.comunic.redirectorllamadas

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import servicoop.comunic.redirectorllamadas.fragment.MqttFragment

class MainActivity : AppCompatActivity() {

    private val PERMISSION_REQUEST_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("MainActivity", "onCreate: Solicitando permisos.")
        requestPermissions()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MqttFragment())
                .commit()
            Log.d("MainActivity", "onCreate: MqttFragment añadido.")
        }
    }

    private fun requestPermissions() {
        val permissionsToRequest = mutableListOf<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
                Log.d("MainActivity", "requestPermissions: POST_NOTIFICATIONS no concedido, añadiendo a solicitud.")
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.READ_PHONE_STATE)
            Log.d("MainActivity", "requestPermissions: READ_PHONE_STATE no concedido, añadiendo a solicitud.")
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.READ_CONTACTS)
            Log.d("MainActivity", "requestPermissions: READ_CONTACTS no concedido, añadiendo a solicitud.")
        }

        // ¡Añadir este permiso!
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.READ_CALL_LOG)
            Log.d("MainActivity", "requestPermissions: READ_CALL_LOG no concedido, añadiendo a solicitud.")
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), PERMISSION_REQUEST_CODE)
            Log.d("MainActivity", "requestPermissions: Solicitando ${permissionsToRequest.size} permisos.")
        } else {
            Log.d("MainActivity", "requestPermissions: Todos los permisos ya concedidos.")
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d("MainActivity", "onRequestPermissionsResult: Request Code = $requestCode")

        if (requestCode == PERMISSION_REQUEST_CODE) {
            val deniedPermissions = permissions.filterIndexed { index, _ ->
                val permissionName = permissions[index]
                val isGranted = grantResults[index] == PackageManager.PERMISSION_GRANTED
                Log.d("MainActivity", "onRequestPermissionsResult: $permissionName Granted = $isGranted")
                !isGranted
            }

            if (deniedPermissions.isNotEmpty()) {
                Log.e("MainActivity", "onRequestPermissionsResult: Permisos denegados: ${deniedPermissions.joinToString()}")
                Toast.makeText(this, "Algunos permisos importantes fueron denegados. La aplicacion podria no funcionar correctamente.", Toast.LENGTH_LONG).show()
            } else {
                Log.d("MainActivity", "onRequestPermissionsResult: Todos los permisos necesarios concedidos.")
                Toast.makeText(this, "Todos los permisos necesarios concedidos.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}