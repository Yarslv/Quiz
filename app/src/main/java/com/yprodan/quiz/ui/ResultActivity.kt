package com.yprodan.quiz.ui

import kotlinx.android.synthetic.main.activity_result.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yprodan.quiz.R
import com.yprodan.quiz.utils.Constants

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        val extras = intent.extras
        textResultView.text =
            getString(
                R.string.user_score, extras?.getInt(Constants.CURRENT_QUESTIONS_NUMBER_TAG),
                extras?.getInt(Constants.TOTAL_QUESTIONS_NUMBER_TAG)
            )
    }
}