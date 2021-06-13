package com.studieresan.studs.happenings.adapters

import HappeningDeleteMutation
import HappeningsQuery
import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.apollographql.apollo.api.toInput
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.studieresan.studs.R
import com.studieresan.studs.data.StudsPreferences
import com.studieresan.studs.graphql.apolloClient
import com.studieresan.studs.happenings.HappeningsListFragment
import com.studieresan.studs.happenings.viewmodels.HappeningsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.OffsetDateTime


class HappeningRecyclerViewAdapter(
        private val values: List<HappeningsQuery.Happening>,
        private val parentFragment: HappeningsListFragment)
    : RecyclerView.Adapter<HappeningRecyclerViewAdapter.ViewHolder>() {

    private var context: Context? = null
    private var viewModel: HappeningsViewModel?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context)
                .inflate(R.layout.fragment_happening, parent, false)

        viewModel = ViewModelProviders.of(parentFragment.requireActivity()).get(HappeningsViewModel::class.java)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val happenings = values
        val happening = happenings[position]
        val odt = OffsetDateTime.now()
        val zoneOffset = odt.offset
        val dateInMilli = happening.created?.atOffset(zoneOffset)?.toInstant()?.toEpochMilli()
        val displayDate = if (dateInMilli != null) DateUtils.formatDateTime(context, dateInMilli, DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_ABBREV_TIME or DateUtils.FORMAT_SHOW_WEEKDAY).capitalize() else ""
        holder.emojiView.text = happening.emoji
        holder.titleView.text = happening.title
        holder.descView.text = happening.description
        holder.timeView.text = displayDate
        holder.placeView.text = "@ ${happening.location?.properties?.name}"

        holder.cardView.setOnClickListener {

            if (!happening.location?.geometry?.coordinates.isNullOrEmpty() && happening.location?.geometry?.coordinates?.get(0) != null && happening.location?.geometry?.coordinates?.get(1) != null) {
                val coordinates = LatLng(happening.location.geometry.coordinates[1]!!.toDouble(), happening.location.geometry.coordinates[0]!!.toDouble())
                viewModel?.setMapCenter(coordinates)
            }
            parentFragment.setTab(0)

        }

        if (context != null && happening.host?.id == StudsPreferences.getID(context!!)) {
            holder.deleteBtn.isVisible = true
            holder.deleteBtn.setOnClickListener {
                MaterialAlertDialogBuilder(context!!)
                        .setTitle("Ta bort happening?")
                        .setNegativeButton("Avbryt") { dialog, which ->
                            dialog.dismiss()
                        }
                        .setPositiveButton("Ta bort") { dialog, which ->
                            CoroutineScope(Dispatchers.Main).launch {
                                val response = try {
                                    // lägg till nån loading feedback
                                    apolloClient(context!!).mutate(HappeningDeleteMutation(id = happening.id.toInput())).await()
                                    notifyItemRemoved(position)
                                    dialog.dismiss()
                                } catch (e: ApolloException) {
                                    null
                                }
                            }
                        }
                        .create()
                        .show()
                true
            }
        }

        // Set the image
        context?.let {

            Glide.with(it)
                    .load(happening.host?.info?.picture)
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.imageView)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val descView: TextView = view.findViewById((R.id.happening_desc))
        val emojiView: TextView = view.findViewById(R.id.happening_emoji)
        val imageView: ImageView = view.findViewById((R.id.happening_host))
        val placeView: TextView = view.findViewById(R.id.happening_place)
        val timeView: TextView = view.findViewById(R.id.happening_time)
        val titleView: TextView = view.findViewById(R.id.happening_title)
        val deleteBtn: TextView = view.findViewById(R.id.happening_delete)
        val cardView: LinearLayout = view.findViewById(R.id.happening_card)

        override fun toString(): String {
            return super.toString() + " '" + titleView.text + "'"
        }
    }
}