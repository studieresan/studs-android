package com.studieresan.studs.happenings

import HappeningsQuery
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.studieresan.studs.R
import com.studieresan.studs.happenings.viewmodels.HappeningsViewModel


class MapsFragment : Fragment() {

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

        var centerLocation: LatLng? = null

        val viewModel = ViewModelProviders.of(requireActivity()).get(HappeningsViewModel::class.java)

        viewModel.happenings.observe(viewLifecycleOwner, Observer<List<HappeningsQuery.Happening>> { happenings ->
            happenings.map { happening ->
                if (happening.location?.geometry?.coordinates != null && happening.location?.geometry?.coordinates[0] != null && happening.location?.geometry?.coordinates[1] != null) {
                    val coordinates = LatLng(happening.location.geometry.coordinates[0]!!.toDouble(), happening.location.geometry.coordinates[1]!!.toDouble())
                    googleMap.addMarker(MarkerOptions().position(coordinates).title(happening.title))
                    if (centerLocation == null) {
                        centerLocation = coordinates
                    }
                }
            }
        })

        val cameraPosition = CameraPosition.Builder()
                .target(if (centerLocation != null) centerLocation else LatLng(59.3, 18.0) )
                .zoom(12f)
                .build()

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

    }
}