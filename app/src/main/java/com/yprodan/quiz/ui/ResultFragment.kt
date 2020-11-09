package com.yprodan.quiz.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yprodan.quiz.R
import com.yprodan.quiz.utils.Constants
import kotlinx.android.synthetic.main.fragment_result.*

/**
 * Shows the result
 */
class ResultFragment : Fragment() {

    private var score: Int? = null
    private var questionsNumber: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            score = it.getInt(Constants.CURRENT_QUESTIONS_NUMBER_TAG)
            questionsNumber = it.getInt(Constants.TOTAL_QUESTIONS_NUMBER_TAG)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textResultView.text =
            getString(
                R.string.userScore, score, questionsNumber
            )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_result, container, false)
    }
}