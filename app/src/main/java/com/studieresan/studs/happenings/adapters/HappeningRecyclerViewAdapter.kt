package com.studieresan.studs.happenings.adapters

import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.studieresan.studs.R
import java.time.OffsetDateTime

class HappeningRecyclerViewAdapter(
        private val values: List<HappeningsQuery.Happening>)
    : RecyclerView.Adapter<HappeningRecyclerViewAdapter.ViewHolder>() {

    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context)
                .inflate(R.layout.fragment_happening, parent, false)
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

        override fun toString(): String {
            return super.toString() + " '" + titleView.text + "'"
        }
    }
}