package deivid.pokedex.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.*
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import deivid.pokedex.R
import android.media.MediaPlayer

class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var locationCallback: LocationCallback

    var cont = 0

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pokeButton : Button = findViewById(R.id.pokemon)
        pokeButton.setOnClickListener{
            val i = Intent(this, PokemonActivity::class.java)
            startActivity(i)
        }

        if(tienePermisos()){
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            ubicacionActual()
        }
        else{
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), 42)
        }
    }

    private fun tienePermisos()= REQUIRED_PERMISSIONS_GPS.all{
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun ubicacionActual(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationClient.lastLocation.addOnSuccessListener {
                if (it != null) {
                    //imprimirUbicacion(it)
                } else {
                    Log.d("info", "No se pudo obtener la ubicación")
                }
            }

            val locationRequest = LocationRequest.create().apply {
                interval = 3000 //Cada 3 segundos
                //fastestInterval = 5000 //Cada 5 segundos
                smallestDisplacement = 100F //Cada 100 metros
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }

            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    //locationResult ?: return
                    for (location in locationResult.locations){
                        //imprimirUbicacion(location)
                        cont++
                        if(cont>1) {
                            pokeAlert()
                        }
                    }
                }
            }
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    companion object {
        private val REQUIRED_PERMISSIONS_GPS= arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun imprimirUbicacion(ubicacion: Location) {
        val texto : TextView = findViewById(R.id.distancia)
        texto.text = "latitud: " + ubicacion.latitude.toString() + " longitud: " + ubicacion.longitude.toString()
    }

    private fun pokeAlert() {
        //Alerta cuando salga un pokemon
        AlertDialog.Builder(this).apply {
            setTitle("¡Alerta Pokémon!")
            setMessage("Pokémon salvaje ha aparecido")
            setCancelable(false)
            setPositiveButton("Ok", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    var intent = Intent (this@MainActivity, PokemonActivity::class.java)
                    startActivity(intent)
                }
            })
            setNegativeButton("Cancel", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    if (dialog != null) {
                        dialog.cancel()
                    }
                }
            }).show()
        }
        vibrationEffect()
        soundEffect()
    }

    //Celular vibra cada que se genera una alerta
    private fun vibrationEffect(){
        var vibrator : Vibrator? = null
        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator?
        vibrator?.vibrate(1000)
        //Toast.makeText(this, "Vibracion", Toast.LENGTH_SHORT).show()
    }

    private fun soundEffect(){
        val sound = MediaPlayer.create(this, R.raw.pokemon_item_found)
        sound.start()

    }
}