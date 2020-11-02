package com.yprodan.quiz.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.yprodan.quiz.R
import com.yprodan.quiz.utils.Constants
import com.yprodan.quiz.utils.InformationReceiver

class MainActivity : AppCompatActivity(), InformationReceiver {

    private lateinit var controller: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        controller = Navigation.findNavController(this, R.id.nav_host_fragment_container)
        controller.navigate(R.id.itemFragment)
    }

    override fun transmitResultToActivity(score: Int, numberOfQuestions: Int) {
        val rating = Bundle()
        rating.let {
            it.putInt(Constants.CURRENT_QUESTIONS_NUMBER_TAG, score)
            it.putInt(Constants.TOTAL_QUESTIONS_NUMBER_TAG, numberOfQuestions)
        }
//        controller.popBackStack()
//        controller.popBackStack()
        controller.navigate(R.id.resultFragment, rating)
    }

    override fun transmitFileNameToActivity(fname: String) {
        Log.d(Constants.FILE_NAME_TAG, fname)
        val arg = Bundle()
        arg.let {
            it.putString(Constants.FILE_NAME_TAG, fname)
        }
        controller.navigate(R.id.mainFragment, arg)
    }

}

