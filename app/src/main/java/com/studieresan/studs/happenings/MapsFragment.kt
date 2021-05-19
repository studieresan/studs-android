package com.studieresan.studs.happenings

import HappeningsQuery
import android.os.Bundle
import android.text.format.DateUtils
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
import java.time.OffsetDateTime


class MapsFragment : Fragment() {

    private val callback = OnMapReadyCallback { googleMap ->

        // might remove this, does not seem to make a big difference?
        var cameraPosition = CameraPosition.Builder()
                .target(LatLng(59.3, 18.0))
                .zoom(12f)
                .build()
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

        var centerLocation: LatLng? = null

        val viewModel = ViewModelProviders.of(requireActivity()).get(HappeningsViewModel::class.java)

        viewModel.happenings.observe(viewLifecycleOwner, Observer<List<HappeningsQuery.Happening>> { happenings ->
            happenings.map { happening ->
                if (happening.location?.geometry?.coordinates != null && happening.location?.geometry?.coordinates[0] != null && happening.location?.geometry?.coordinates[1] != null) {

                    val coordinates = LatLng(happening.location.geometry.coordinates[1]!!.toDouble(), happening.location.geometry.coordinates[0]!!.toDouble())
                    val odt = OffsetDateTime.now()
                    val zoneOffset = odt.offset
                    val dateInMilli = happening.created?.atOffset(zoneOffset)?.toInstant()?.toEpochMilli()
                    val displayDate = if (dateInMilli != null) DateUtils.formatDateTime(context, dateInMilli, DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_ABBREV_TIME or DateUtils.FORMAT_SHOW_WEEKDAY or DateUtils.FORMAT_ABBREV_WEEKDAY).capitalize() else ""

                    googleMap
                            .addMarker(MarkerOptions().position(coordinates)
                                    .title("${happening.host?.firstName} ${happening.host?.lastName?.get(0)} ${happening.title?.decapitalize()} ${happening.emoji}")
                                    .snippet("${displayDate} @ ${happening.location?.properties?.name}"))

                    if (centerLocation == null) {
                        centerLocation = coordinates
                    }
                }
            }
            cameraPosition = CameraPosition.Builder()
                    .target(if (centerLocation != null) centerLocation else LatLng(59.3, 18.0))
                    .zoom(12f)
                    .build()

            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        })
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