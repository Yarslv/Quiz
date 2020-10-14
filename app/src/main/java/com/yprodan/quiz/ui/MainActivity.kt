package com.yprodan.quiz.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.yprodan.quiz.R
import com.yprodan.quiz.utils.AnswersController
import com.yprodan.quiz.utils.Constants
import com.yprodan.quiz.utils.TextController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //a flag that will show that it is time to show the result
    private var isNextResult = false

    //tints the text, points to questions
    private lateinit var controllerOfText: TextController

    //contains the correct answers, all answers, checks the user's answers
    private lateinit var controllerOfAnswers: AnswersController

    //links to all the buttons used by the application(answer buttons, next button)
    private lateinit var arrButton: Array<Button>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        controllerOfText =
            TextController(
                application.assets.open(getString(R.string.textForTest1)).bufferedReader().use {
                    it.readText()
                })
        controllerOfAnswers = AnswersController(
            application.assets.open(getString(R.string.answers1)).bufferedReader().use {
                it.readText()
            })

        initTextView()
        initButton()
        updateLabelsOnButtons()
    }

    private fun initTextView() {
        controllerOfText.markTheQuestion(controllerOfAnswers.currentQuestion)
        textView.text = controllerOfText.getUpadateText()
    }

    private fun initButton() {
        arrButton = arrayOf(
            answer_button_1, answer_button_2,
            answer_button_3, answer_button_4,
            next_button
        )
    }

    private fun updateLabelsOnButtons() {
        for (i in 0..3) {
            arrButton[i].text =
                (controllerOfAnswers.getSetAnswer(controllerOfAnswers.currentQuestion)[i])
        }
    }

    private fun updateLabelsOnButtons(label: String) {
        for (i in 0..4) { // 4 because need to replace all the labels on the buttons
            arrButton[i].text = label
        }
    }

    // todo replace this method with individual click listeners
    fun onClick(view: View) {
        val button: Button = findViewById(view.id)

        if (controllerOfAnswers.isEnd()) {
            if (isNextResult) {
                getResult()
            }
            isNextResult = true
            if (button != next_button) // edge case problem: if not check paste in the text "next"
                controllerOfText.markTheAnswer(
                    controllerOfAnswers.currentQuestion, button.text.toString(),
                    controllerOfAnswers.checkAnswer(button.text.toString())
                )
            updateLabelsOnButtons(getString(R.string.finishTestButtonText)) // todo set answer buttons visibility gone
        } else {
            when (button) {
                answer_button_1, answer_button_2, answer_button_3, answer_button_4 -> {
                    controllerOfText.markTheAnswer(
                        controllerOfAnswers.currentQuestion, button.text.toString(),
                        controllerOfAnswers.checkAnswer(button.text.toString())
                    )
                }
            }
            controllerOfAnswers.increasePointer()
            controllerOfText.markTheQuestion(controllerOfAnswers.currentQuestion)
            updateLabelsOnButtons()
        }
        textView.text = controllerOfText.getUpadateText()

    }

    /**
     * launches an activity that will show the number of correct answers
     */
    private fun getResult() {
        val intent =
            Intent(this, ResultActivity::class.java)
        intent.putExtra(
            Constants.TOTAL_QUESTIONS_NUMBER_TAG,
            controllerOfAnswers.getTheBestPossibleTestScore()
        )
        //the flags will not let go back
        // todo use constants
        intent.putExtra("rating", controllerOfAnswers.getTotalCount())
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            .addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
            )
        startActivity(intent)
    }
}