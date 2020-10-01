package com.yprodan.quiz

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    //a flag that will show that it is time to show the result
    private var isNextResult = false

    //question number for answer
    private var state = 1

    //link to text box
    private lateinit var textView: TextView

    //reference to the class, which contains the text, answers, correct answers
    private lateinit var quest: QuestionGiver

    //links to all the buttons used by the application(answer buttons, next button)
    private lateinit var arrButton: Array<Button>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        quest =
            QuestionGiver(application.assets.open(getString(R.string.text1)).bufferedReader().use {
                it.readText()
            }, application.assets.open(getString(R.string.answer1)).bufferedReader().use {
                it.readText()
            })
        initTextView()
        initButton()
        updateLabelsOnButtons()
    }

    private fun initTextView() {
        textView = findViewById(R.id.textView)
        textView.text = quest.pointToTheQuestion(state)
    }

    private fun initButton() {
        arrButton = arrayOf(
            findViewById(R.id.answer_button_1), findViewById(R.id.answer_button_2),
            findViewById(R.id.answer_button_3), findViewById(R.id.answer_button_4),
            findViewById((R.id.next_button))
        )
    }

    private fun updateLabelsOnButtons() {
        for (i in 0..3) {
            arrButton[i].text = (quest.getSetAnswer(state - 1)[i])
//            Log.d("answw", test.getSetAnswer(4)[i].toString())
        }
    }

    fun onClick(view: View) {
        val button: Button = findViewById(view.id)
        if (!quest.isEnd()) {
            if (view.id != R.id.next_button) {
                textView.text = quest.getChangedText(state, button.text.toString())
            }
            state++
            textView.text = quest.pointToTheQuestion(state)
            updateLabelsOnButtons()
        } else {
            //allows to answer the last question and not go directly to the results page
            if (isNextResult) {
                getResult()
            } else if (view.id != R.id.next_button) {
                textView.text = quest.getChangedText(state, button.text.toString())
            }
            isNextResult = true
            for (i in 0..4) {
                arrButton[i].text = getString(R.string.text_finish)
            }
        }
        quest.increasePointer()
    }

    /**
     * launches an activity that will show the number of correct answers
     */
    private fun getResult() {
        val intent =
            Intent(this, ResultActivity::class.java)
        intent.putExtra("bestScore", quest.getTheBestPossibleTestScore())
        //the flags will not let go back
        intent.putExtra("rating", quest.getTotalCount()).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            .addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
            )
        startActivity(intent)
    }
}