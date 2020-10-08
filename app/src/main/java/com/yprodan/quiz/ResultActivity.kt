package com.yprodan.quiz

import kotlinx.android.synthetic.main.activity_result.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        val extras = intent.extras
        textResultView.text =
            getString(R.string.user_score, extras?.getInt("rating"), extras?.getInt("bestScore"))
    }
}