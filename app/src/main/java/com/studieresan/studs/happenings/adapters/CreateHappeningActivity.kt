package com.studieresan.studs.happenings.adapters

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.studieresan.studs.R


class CreateHappeningActivity : AppCompatActivity() {

    private var selectedEmoji: RadioButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_happening)

        selectedEmoji = findViewById<View>(R.id.create_emoji1) as RadioButton

        val radioGroup = findViewById<View>(R.id.create_rb) as RadioGroup
        radioGroup.setOnCheckedChangeListener { group, checkedId -> // checkedId is the RadioButton selected
            selectedEmoji!!.alpha = 0.5F
            selectedEmoji = findViewById<View>(checkedId) as RadioButton
            selectedEmoji!!.alpha = 1F
        }
    }


}