package com.yprodan.quiz.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.yprodan.quiz.R
import com.yprodan.quiz.utils.Constants
import com.yprodan.quiz.utils.AnswersToQuestions
import com.yprodan.quiz.utils.TextController
import com.yprodan.quiz.utils.UserInfo
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    //tints the text, points to questions
    private lateinit var controllerOfText: TextController

    //links to all the buttons used by the application(answer buttons, next button)
    private lateinit var arrButton: Array<Button>

    //an array of answer blocks
    private lateinit var arrayOfAnswers: Array<AnswersToQuestions?>

    //user progress information (position, score)
    private lateinit var userInfo: UserInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getDataFromJSON()
        userInfo = UserInfo()
        initTextView()
        initButton()
        updateLabelsOnButtons()
    }

    /**
     * Reads the specified file, breaks it into text, a block of answers to questions,
     * a block of correct answers
     */
    private fun getDataFromJSON() {
        val json = JSONObject(application.assets.open(getString(R.string.textForTest1))
            .bufferedReader().use {
                it.readText()
            }
        )
        val allAnswers = json.getJSONObject(getString(R.string.answersForTest))
        val rightAnswers = json.getJSONArray(getString(R.string.rightAnswerOfTest))

        controllerOfText =
            TextController(
                json.getString(getString(R.string.text)).toString()
            )

        arrayOfAnswers = arrayOfNulls(allAnswers.length())
        for (i in 1..arrayOfAnswers.size) {
            arrayOfAnswers[i - 1] = AnswersToQuestions(
                allAnswers.getJSONArray((i).toString()),
                rightAnswers.getInt(i - 1)
            )
        }
    }

    private fun initTextView() {
        controllerOfText.markTheQuestion(userInfo.getPositionInText())
        textView.text = controllerOfText.getUpadateText()
    }

    private fun initButton() {
        arrButton = arrayOf(
            answer_button_1, answer_button_2,
            answer_button_3, answer_button_4
        )
        setKeyListener()
    }

    private fun setKeyListener() {
        for (i in 0..3) {
            arrButton[i].setOnClickListener {
                val button = it as Button
                controllerOfText.markTheAnswer(
                    userInfo.getPositionInText(),
                    button.text.toString(),
                    arrayOfAnswers.get(userInfo.getPositionInArray())
                        ?.checkForCorrectness(button.text.toString())!!
                )
                if (arrayOfAnswers.get(userInfo.getPositionInArray())
                        ?.checkForCorrectness(button.text.toString())!!
                )
                    userInfo.score++
                if (userInfo.getPositionInArray() < arrayOfAnswers.size - 1) {
                    userInfo.position++
                    controllerOfText.markTheQuestion(userInfo.getPositionInText())
                    updateLabelsOnButtons()
                } else {
                    prepareButtonsForFinish()
                }
                textView.text = controllerOfText.getUpadateText()
            }
        }

        next_button.setOnClickListener {
            when {
                userInfo.getPositionInArray() == arrayOfAnswers.size - 1 -> {
                    prepareButtonsForFinish()
                    userInfo.position++
                }
                userInfo.getPositionInArray() < arrayOfAnswers.size -> {
                    userInfo.position++
                    updateLabelsOnButtons()
                    controllerOfText.markTheQuestion(userInfo.getPositionInText())
                    textView.text = controllerOfText.getUpadateText()
                }
                else -> getResult()
            }
        }
    }

    private fun updateLabelsOnButtons() {
        for (i in 0..3) {
            arrButton[i].text =
                (arrayOfAnswers.get(userInfo.getPositionInArray())?.answerSet?.get(i).toString())
        }
    }

    /**
     * Hides the answer buttons, and sets the special text for the next button
     */
    private fun prepareButtonsForFinish() {
        for (i in 0..3) {
            arrButton[i].visibility = View.INVISIBLE
        }
        next_button.text = getString(R.string.finishTestButtonText)
    }


    /**
     * launches an activity that will show the number of correct answers
     */
    private fun getResult() {
        val intent =
            Intent(this, ResultActivity::class.java)
        intent.putExtra(
            Constants.TOTAL_QUESTIONS_NUMBER_TAG,
            arrayOfAnswers.size
        )
        //the flags will not let go back
        intent.putExtra(Constants.CURRENT_QUESTIONS_NUMBER_TAG, userInfo.score)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            .addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
            )
        startActivity(intent)
    }
}