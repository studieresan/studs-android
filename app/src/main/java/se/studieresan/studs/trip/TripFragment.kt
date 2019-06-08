package se.studieresan.studs.trip

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PointOfInterest
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import se.studieresan.studs.R
import se.studieresan.studs.StudsActivity
import se.studieresan.studs.data.StudsPreferences
import se.studieresan.studs.data.models.FeedItem
import timber.log.Timber

private const val PERMISSION_FINE_LOCATION = 1
private const val ERROR_MESSAGE_TEXT = "Location permission is required"

class TripFragment : Fragment(), OnMapReadyCallback, OnFeedItemClickedListener, GoogleMap.OnPoiClickListener,
    GoogleMap.OnMarkerClickListener {

    private lateinit var mapFragment: SupportMapFragment
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var feedRecyclerView: RecyclerView
    private lateinit var adapter: FeedAdapter

    private lateinit var listener: ValueEventListener
    private lateinit var reference: DatabaseReference

    private val userToMarkerMap = mutableMapOf<String, MarkerOptions>()
    private var map: GoogleMap? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_trip, container, false)
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        adapter = FeedAdapter(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        showDeviceLocation()

        bottomSheetBehavior = BottomSheetBehavior.from(view.findViewById(R.id.bottom_sheet_trip))
        bottomSheetBehavior.run {
            state = BottomSheetBehavior.STATE_COLLAPSED
        }
        view.findViewById<TextView>(R.id.tv_bottom_sheet_title).setOnClickListener {
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        feedRecyclerView = view.findViewById<RecyclerView>(R.id.rv_feed).apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@TripFragment.adapter
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    (layoutManager as LinearLayoutManager).orientation
                )
            )
        }
        setupDbListener()
        view.findViewById<FloatingActionButton>(R.id.fab_share).setOnClickListener { showShareFragment() }
        return view
    }

    private fun setupDbListener() {
        val postLifetime = 60 * 60 * 24 // Posts disappear after 24 hours
        val oneHourAgo = System.currentTimeMillis() / 1000 - postLifetime
        reference = FirebaseDatabase.getInstance().getReference("locations")
        listener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val feedItems = p0.children.map {
                    val feedItem = it.getValue(FeedItem::class.java)!!
                    feedItem.key = requireNotNull(it.key)
                    feedItem
                }.asReversed()
                adapter.submitList(feedItems)
                feedRecyclerView.scrollToPosition(0)
            }

            override fun onCancelled(p0: DatabaseError) {
                Timber.w(p0.toException())
            }
        }
        reference
            .orderByChild("timestamp")
            .startAt(oneHourAgo.toDouble())
            .addValueEventListener(listener)
    }

    override fun onStart() {
        super.onStart()
        if (!hasLocationPermission()) {
            requestLocationPermission()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        reference.removeEventListener(listener)
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_FINE_LOCATION
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_FINE_LOCATION ->
                if (grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) {
                    showDeviceLocation()
                } else {
                    showLocationPermissionRequired()
                }
        }
    }

    private fun showLocationPermissionRequired() {
        val snackBar =
            Snackbar.make((requireActivity() as StudsActivity).view, ERROR_MESSAGE_TEXT, Snackbar.LENGTH_LONG)
        snackBar.setAction("Prompt") { requestLocationPermission() }
        snackBar.show()
    }

    private fun hasLocationPermission() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    private fun showDeviceLocation() {
        if (hasLocationPermission()) {
            map?.run {
                isMyLocationEnabled = true
                uiSettings.isMapToolbarEnabled = true
                uiSettings.isMyLocationButtonEnabled = true
                fusedLocationClient.lastLocation.addOnSuccessListener {
                    val latLng = LatLng(it.latitude, it.longitude)
                    moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                }
            }
        } else {
            showLocationPermissionRequired()
        }
    }

    private fun showShareFragment() {
        with(fragmentManager!!) {
            val transaction = beginTransaction()
            val prev = findFragmentByTag(ShareLocationFragment.TAG)
            if (prev != null) {
                transaction.remove(prev)
            }
            transaction.addToBackStack(null)
            ShareLocationFragment.newInstance().show(transaction, ShareLocationFragment.TAG)
        }
    }

    override fun onFeedItemClicked(feedItem: FeedItem) {
        if (feedItem.includeLocation) {
            val alertDialogBuilder = AlertDialog.Builder(requireContext())
                .setMessage("Would you like to add this to the map?")
                .setNegativeButton("No") { d, _ -> d.dismiss() }
                .setPositiveButton("Yes") { dialog, _ ->
                    addMarker(feedItem)
                    dialog?.dismiss()
                }
            alertDialogBuilder.create().show()
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap
        map?.setOnPoiClickListener(this)
        map?.setOnMarkerClickListener(this)
        showDeviceLocation()
    }

    override fun onPoiClick(p0: PointOfInterest) {
        AlertDialog.Builder(requireContext())
            .setCancelable(true)
            .setTitle("Navigate")
            .setMessage("Would you like to navigate to ${p0.name}?")
            .setPositiveButton("Yes") { _, _ -> openNavigation(p0.latLng) }
            .setNegativeButton("Cancel") { d, _ -> d.dismiss() }
            .create()
            .show()
    }

    private fun addMarker(feedItem: FeedItem) {
        map?.run {
            if (!userToMarkerMap.containsKey(feedItem.key)) {
                val marker = MarkerOptions()
                    .position(LatLng(feedItem.lat, feedItem.lng))
                    .title(feedItem.user)
                    .snippet(feedItem.message)
                addMarker(marker).run {
                    tag = LatLng(feedItem.lat, feedItem.lng)
                }
                moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(feedItem.lat, feedItem.lng), 15f))
                userToMarkerMap[feedItem.key] = marker
            } else {
                Toast.makeText(requireContext(), "This marker already exists", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
            .setTitle(p0.title)
            .setMessage(p0.snippet)
            .setNegativeButton("Close") { d, _ -> d.dismiss() }

        if (p0.tag is LatLng) {
            alertDialogBuilder
                .setPositiveButton("Navigate to") { _, _ -> openNavigation(p0.tag as LatLng) }
        }
        alertDialogBuilder
            .create()
            .show()
        map?.animateCamera(CameraUpdateFactory.newLatLng(p0.tag as LatLng))
        return true
    }

    private fun openNavigation(latLng: LatLng) {
        val navigationPreference = StudsPreferences.getNavigationPreference(requireContext())
        val gmmIntentUri =
            Uri.parse("google.navigation:q=${latLng.latitude},${latLng.longitude}&mode=$navigationPreference")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
            setPackage("com.google.android.apps.maps")
        }

        if (mapIntent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(mapIntent)
        } else {
            Toast.makeText(requireContext(), getString(R.string.google_maps_unavailable), Toast.LENGTH_LONG).show()
        }
    }
}
