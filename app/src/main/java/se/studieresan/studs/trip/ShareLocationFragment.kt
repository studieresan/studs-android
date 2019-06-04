package se.studieresan.studs.trip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import se.studieresan.studs.R

class ShareLocationFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_share_location, container, false) ?: return null
        v.findViewById<Button>(R.id.btn_submit).setOnClickListener {
            dismiss()
        }
        return v
    }

    companion object {
        val TAG = "${ShareLocationFragment::class.java.simpleName}_tag"
        fun newInstance() = ShareLocationFragment()
    }
}
