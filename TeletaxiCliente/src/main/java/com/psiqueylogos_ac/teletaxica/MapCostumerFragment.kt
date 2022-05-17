package com.psiqueylogos_ac.teletaxica

import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

class MapCostumerFragment : Fragment() {

    private var originMarker: Marker? = null
    private var destinationMarker: Marker? = null
    private var line: Polyline? = null
    lateinit var geocoder: Geocoder

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        val caracas = LatLng(10.5, -66.9)
        googleMap.addMarker(MarkerOptions().position(caracas).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(caracas))
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(16.0F))


        mapViewModel.originSelected.observe(this) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(it))
            //If exists remove it.
            originMarker?.remove()
            //Add new marker
            originMarker =
                googleMap.addMarker(MarkerOptions().position(it).title("Origen").draggable(true))

            //Fill order
            order.originLatLng = it


        }


        mapViewModel.destinationSelected.observe(this) { latLng ->
            //Move map to position
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
            //if exists remove it
            destinationMarker?.remove()

            //add marker
            destinationMarker = googleMap.addMarker(
                MarkerOptions().position(latLng).title("Destino").draggable(true)
            )

            line?.remove()
            line = googleMap.addPolyline(
                PolylineOptions()
                    .add(originMarker!!.position)
                    .add(destinationMarker!!.position)
            )

            //Fill order
            order.destinationLatLng = latLng

        }


        mapViewModel.position.observe(this) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(it))
        }


        googleMap.setOnMarkerDragListener(
            object : GoogleMap.OnMarkerDragListener {
                override fun onMarkerDrag(p0: Marker) {
                    line?.remove()

                    if (originMarker != null && destinationMarker != null) {
                        line = googleMap.addPolyline(
                            PolylineOptions()
                                .add(originMarker!!.position)
                                .add(destinationMarker!!.position)
                        )

                        order.originLatLng = originMarker!!.position
                        order.destinationLatLng = destinationMarker!!.position
                    }

                    when (p0) {
                        originMarker -> {
                            order.origin = geocoder
                                .getFromLocation(
                                    originMarker!!.position.latitude,
                                    originMarker!!.position.longitude,
                                    1
                                )
                                .first()
                                .getAddressLine(0)
                        }

                        destinationMarker -> {
                            order.destination = geocoder
                                .getFromLocation(
                                    destinationMarker!!.position.latitude,
                                    destinationMarker!!.position.longitude,
                                    1
                                )
                                .first()
                                .getAddressLine(0)
                        }
                    }
                }

                override fun onMarkerDragEnd(p0: Marker) {
                    //TODO("Not yet implemented")
                }

                override fun onMarkerDragStart(p0: Marker) {
                    //TODO("Not yet implemented")
                }
            }
        )


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map_costumer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        geocoder = Geocoder(this.context, java.util.Locale.getDefault())

    }
}