package com.yprodan.quiz.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yprodan.quiz.R
import com.yprodan.quiz.utils.Constants
import kotlinx.android.synthetic.main.result_fragment.*

/**
 * Shows the result
 */
class ResultFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            textResultView.text =
                getString(
                    R.string.userScore, it.getInt(Constants.CURRENT_QUESTIONS_NUMBER_TAG),
                    it.getInt(Constants.TOTAL_QUESTIONS_NUMBER_TAG)
                )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.result_fragment, container, false)
    }
}