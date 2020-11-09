package com.yprodan.quiz.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
import com.yprodan.quiz.R
import com.yprodan.quiz.utils.Constants
import com.yprodan.quiz.utils.model.AnswersToQuestions
import com.yprodan.quiz.utils.TextController
import com.yprodan.quiz.utils.model.UserInfo
import kotlinx.android.synthetic.main.fragment_quiz.*
import org.json.JSONObject

/**
 * Shows text and buttons, shows the correct answer, counts the correct answers
 */
class QuizFragment : Fragment() {

    //tints the text, points to questions
    private lateinit var controllerOfText: TextController

    //links to all the buttons used by the application(answer buttons, next button)
    private lateinit var arrButton: Array<Button>

    //an array of answer blocks
    private lateinit var arrayOfAnswers: Array<AnswersToQuestions?>

    //user progress information (position, score)
    private lateinit var userInfo: UserInfo

    private lateinit var filename: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            filename = it?.getString(Constants.FILE_NAME_TAG).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_quiz, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        val json = JSONObject(
            requireActivity().application?.assets?.open(
                getString(
                    R.string.fullFileName,
                    Constants.FILE_FOLDER_NAME_TAG, filename, Constants.ABBREVIATION_TAG
                )
            )
                ?.bufferedReader().use {
                    it!!.readText()
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
                    controllerOfText.markTheQuestion(userInfo.getPositionInText())
                    updateLabelsOnButtons()
                } else if (userInfo.getPositionInArray() == arrayOfAnswers.size) {
                    prepareButtonsForFinish()
                }
                textView.text = controllerOfText.getUpadateText()
            }
        }

        next_button.setOnClickListener {
            userInfo.position++
            when {
                userInfo.getPositionInArray() == arrayOfAnswers.size -> {
                    prepareButtonsForFinish()
                }
                userInfo.getPositionInArray() < arrayOfAnswers.size -> {
                    updateLabelsOnButtons()
                    controllerOfText.markTheQuestion(userInfo.getPositionInText())
                    textView.text = controllerOfText.getUpadateText()
                }
                else -> {
                    runResultFragment()
                }
            }
        }
    }

    private fun runResultFragment() {
        val bundle = Bundle()
        bundle.let {
            it.putInt(Constants.CURRENT_QUESTIONS_NUMBER_TAG, userInfo.score)
            it.putInt(Constants.TOTAL_QUESTIONS_NUMBER_TAG, arrayOfAnswers.size)
        }
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_container)
            .navigate(R.id.action_quizFragment_to_resultFragment, bundle)
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
        next_button.text = getString(R.string.finishTestButtonText)
    }
}