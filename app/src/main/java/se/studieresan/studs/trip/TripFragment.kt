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
import com.google.android.gms.maps.model.PointOfInterest
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import se.studieresan.studs.R
import se.studieresan.studs.StudsActivity
import se.studieresan.studs.data.models.FeedItem

private const val PERMISSION_FINE_LOCATION = 1
private const val ERROR_MESSAGE_TEXT = "Location permission is required"

class TripFragment : Fragment(), OnMapReadyCallback, OnFeedItemClickedListener, GoogleMap.OnPoiClickListener {

    private lateinit var mapFragment: SupportMapFragment
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var feedRecyclerView: RecyclerView
    private lateinit var adapter: FeedAdapter

    private var map: GoogleMap? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_trip, container, false)
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        adapter = FeedAdapter(this)
        addDummyData()

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
            addItemDecoration(DividerItemDecoration(requireContext(), (layoutManager as LinearLayoutManager).orientation))
        }

        view.findViewById<FloatingActionButton>(R.id.fab_share).setOnClickListener { showShareFragment() }
        return view
    }

    private fun addDummyData() {
        val items = mutableListOf<FeedItem>()
        for (i in 0..20) {
            items.add(FeedItem(i, "Dummy $i", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent at vestibulum ligula. Duis vitae lectus lacus. Nullam semper ipsum neque, a lobortis nulla mattis nec. Ut elementum rhoncus risus eget eleifend. Donec viverra purus at nulla porta pulvinar. Quisque vulputate sapien quam, id posuere risus imperdiet eget. Duis lobortis velit eu luctus convallis.", "A location"))
        }
        adapter.submitList(items)
    }

    override fun onStart() {
        super.onStart()
        if (!hasLocationPermission()) {
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_FINE_LOCATION)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_FINE_LOCATION ->
                if (grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                    showDeviceLocation()
                } else {
                    showLocationPermissionRequired()
                }
        }
    }

    private fun showLocationPermissionRequired() {
        val snackBar = Snackbar.make((requireActivity() as StudsActivity).view, ERROR_MESSAGE_TEXT, Snackbar.LENGTH_LONG)
        snackBar.setAction("Prompt") { requestLocationPermission() }
        snackBar.show()
    }

    private fun hasLocationPermission() =
        ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

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
        Toast.makeText(requireContext(), "Clicked $feedItem", Toast.LENGTH_SHORT).show()
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap
        map?.setOnPoiClickListener(this)
        showDeviceLocation()
    }

    override fun onPoiClick(p0: PointOfInterest) {
        AlertDialog.Builder(requireContext())
            .setCancelable(true)
            .setTitle("Navigate")
            .setMessage("Would you like to navigate to ${p0.name}?")
            .setPositiveButton("Yes") { _, _ -> openNavigation(p0) }
            .setNegativeButton("Cancel") { d, _ -> d.dismiss() }
            .create()
            .show()
    }

    private fun openNavigation(p0: PointOfInterest) {
        val gmmIntentUri = Uri.parse("google.navigation:q=${p0.latLng.latitude},${p0.latLng.longitude}&mode=walking")
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
