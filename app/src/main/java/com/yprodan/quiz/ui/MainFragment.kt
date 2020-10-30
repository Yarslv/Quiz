package com.yprodan.quiz.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.yprodan.quiz.R
import com.yprodan.quiz.utils.InformationReceiver
import com.yprodan.quiz.utils.model.AnswersToQuestions
import com.yprodan.quiz.utils.TextController
import com.yprodan.quiz.utils.model.UserInfo
import kotlinx.android.synthetic.main.fragment_main.view.*
import org.json.JSONObject

/**
 * Shows text and buttons, shows the correct answer, counts the correct answers
 */
class MainFragment : Fragment() {

    //gives access to visual elements
    private lateinit var content: View

    //tints the text, points to questions
    private lateinit var controllerOfText: TextController

    //links to all the buttons used by the application(answer buttons, next button)
    private lateinit var arrButton: Array<Button>

    //an array of answer blocks
    private lateinit var arrayOfAnswers: Array<AnswersToQuestions?>

    //user progress information (position, score)
    private lateinit var userInfo: UserInfo

    //link to the main activity
    private var activity: Activity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Activity) {
            activity = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        content = inflater.inflate(R.layout.fragment_main, container, false)
        getDataFromJSON()
        userInfo = UserInfo()
        initTextView()
        initButton()
        updateLabelsOnButtons()
        return content
    }

    /**
     * Reads the specified file, breaks it into text, a block of answers to questions,
     * a block of correct answers
     */
    private fun getDataFromJSON() {
        val json = JSONObject(
            activity?.application?.assets?.open(getString(R.string.textForTest1))
                ?.bufferedReader().use {
                    it?.readText()
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
        content.textView.text = controllerOfText.getUpadateText()
    }

    private fun initButton() {
        arrButton = arrayOf(
            content.answer_button_1, content.answer_button_2,
            content.answer_button_3, content.answer_button_4
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
                    arrayOfAnswers[userInfo.getPositionInArray()]
                        ?.checkForCorrectness(button.text.toString())!!
                )
                if (arrayOfAnswers[userInfo.getPositionInArray()]
                        ?.checkForCorrectness(button.text.toString())!!
                ) {
                    userInfo.score++
                }

                userInfo.position++

                if (userInfo.getPositionInArray() < arrayOfAnswers.size) {
                    Log.d("position", userInfo.getPositionInArray().toString())
                    controllerOfText.markTheQuestion(userInfo.getPositionInText())
                    updateLabelsOnButtons()
                } else if (userInfo.getPositionInArray() == arrayOfAnswers.size) {
                    prepareButtonsForFinish()
                }
                content.textView.text = controllerOfText.getUpadateText()
            }
        }

        content.next_button.setOnClickListener {
            userInfo.position++
            when {
                userInfo.getPositionInArray() == arrayOfAnswers.size -> {
                    prepareButtonsForFinish()
                }
                userInfo.getPositionInArray() < arrayOfAnswers.size -> {
                    updateLabelsOnButtons()
                    controllerOfText.markTheQuestion(userInfo.getPositionInText())
                    content.textView.text = controllerOfText.getUpadateText()
                }
                else -> (activity as InformationReceiver).transmitToActivity(
                    userInfo.score,
                    arrayOfAnswers.size
                )

            }

        }
    }

    private fun updateLabelsOnButtons() {
        for (i in 0..3) {
            arrButton[i].text =
                (arrayOfAnswers[userInfo.getPositionInArray()]?.answerSet?.get(i).toString())
        }
    }

    /**
     * Hides the answer buttons, and sets the special text for the next button
     */
    private fun prepareButtonsForFinish() {
        for (i in 0..3) {
            arrButton[i].visibility = View.INVISIBLE
        }
        content.next_button.text = getString(R.string.finishTestButtonText)
    }
}