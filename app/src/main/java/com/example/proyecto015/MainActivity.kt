package com.example.proyecto015
import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions

class MainActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener {
    //variable golbal para que toda la clase pueda usarla
//lateinit indica se será inicializada después, es importante
//inicializarla para que no falle después
    private lateinit var map:GoogleMap
    //codigo request code (código respuesta)
    companion object {
        const val REQUEST_CODE_LOCATION = 0
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//método createFragment()
        createFragment()
    }
    //este método creará el mapa
    private fun createFragment(){
        val mapFragment:SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)//aunque no falla ahora, pedira implemtar un OnMapReadyCallback
    }
    //se llamará cuando el mapa haya sido cargado
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
//        createMarker()//para establecer coordenadas por defecto
        createPolylines()
        map.setOnMyLocationButtonClickListener(this) //suscribete con esta clase, esta implemtado
        map.setOnMyLocationClickListener(this)
        enableLocation()
    }
    private fun createPolylines(){
//un objeto polylineOptions y se le añaden unas coordenadas
//esto gnerará un contorno en el mapa, punto tras punto(coordenadas forma punto).
        val polylineOptions = PolylineOptions()
            .add(LatLng(40.419173113350965,-3.705976009368897))
            .add(LatLng( 40.4150807746539, -3.706072568893432))
            .add(LatLng( 40.41517062907432, -3.7012016773223873))
            .add(LatLng( 40.41713105928677, -3.7037122249603267))
            .add(LatLng( 40.41926296230622,  -3.701287508010864))
            .add(LatLng( 40.419173113350965, -3.7048280239105225))
            .add(LatLng(40.419173113350965,-3.705976009368897))
//aquí se añade la inicialización en la Polyline
        val polyline = map.addPolyline(polylineOptions)
    }
    private fun createMarker() {
//uilizará un objeto LatLng para las coordenadas
        val coordinates = LatLng(39.980590814772846, -0.02840187586058776)//recibe dos datos: Latitud y Longitud
//crear un marker
        val marker = MarkerOptions().position(coordinates).title("Mi instituto IES CAMINAS")
//pasarle el objeto a map
        map.addMarker(marker)
//para que se vea más grande se realizará un zoom
//4000 ms = 4 segundos (duración)
//18f(float) es el tamaño del zoom
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(coordinates,18f),
            4000,
            null
        )
    }
    //importar el contexto this (en este caso)
//importar Manifest(android)
//comparar si el premiso está aeptado PackageManager.PERMISSION_GRANTED
    private fun isPermissionsGranted() = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
    private fun enableLocation(){
//Preguntar si map no ha sido inicializado
//salta este método por que no puede utilizar esta lógica
    if(!::map.isInitialized) return
    //Ha aceptado lo permisos
    if (isPermissionsGranted())
    {
    //si permiso aceptado
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        map.isMyLocationEnabled = true
    }
    else
    {
    //No tiene permiso
    //Pedirlo ahora
        requestLocationPermission()
    }
    }
    private fun requestLocationPermission() {
    //lógica para pedir a los usuarios que den permisos
    //la sctivity y el permiso que se quiere
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)) {
            Toast.makeText(this, "Ve a ajustes y acepta los permisos", Toast.LENGTH_SHORT).show()
        }
        else {
    //usar un array de permisos
    //recivirá un código cuaado se acepten los permisos  LOCATION_REQUEST_CODE
    //para saber el permiso que queremos
    //REQUEST_CODE_LOCATION se ha declarado en la parte superior
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_LOCATION)
        }
    }
    //capturar la respuesta si acepta los permisos el usuario
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    //empezar aquí la lógica par ver que el permiso ha sido aceptado
        when(requestCode){
    //ha aceptado nuestro permiso
            REQUEST_CODE_LOCATION -> if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                map.isMyLocationEnabled = true
            }else{
    //Lo ha rechazado y no acepta la ubicación en tiempo real
                Toast.makeText(this, "Para activar la localización ve a ajustes y acepta los permisos",
                    Toast.LENGTH_SHORT).show()
            }
    //
            else -> {}
        }
    }
    //cada vez que el usuario vuelva del background o se cierra la aplicación
    //que los permisos siguen activos
    override fun onResumeFragments() {
        super.onResumeFragments()
    //por si esta creado el mapa
        if (!::map.isInitialized) return
        if(!isPermissionsGranted()){
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            map.isMyLocationEnabled = false
            Toast.makeText(this, "Para activar la localización ve a ajustes y acepta los permisos", Toast.LENGTH_SHORT).show()
        }
    }
    //cada vez que pulse en localizar mi posicón ejecutará este método, devuelve un Boolean
    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(this, "Boton pulsado", Toast.LENGTH_SHORT).show()
    //con false devuelve la localización del usuario
        return false
    }
    //se llamará a este método cada vez que pulse el la señal azul
    override fun onMyLocationClick(p0: Location) {
        Toast.makeText(this, "Estás en ${p0.latitude}, ${p0.longitude}", Toast.LENGTH_SHORT).show()
    }
}