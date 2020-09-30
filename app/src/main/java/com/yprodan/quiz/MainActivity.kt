package com.yprodan.quiz

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    var isNextResult = false
    var state = 1
    lateinit var textView: TextView
    lateinit var test: QuestionGiver
    lateinit var arrButton: Array<Button>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        test = QuestionGiver(application.assets.open("text1.txt").bufferedReader().use {
            it.readText()
        }, application.assets.open("answers1.txt").bufferedReader().use {
            it.readText()
        })

        textView = findViewById(R.id.textView)
        textView.text = test.getOrText(1)

        initButton()

        buttonSetter()
    }

    fun initButton(){
        arrButton = arrayOf(
            findViewById(R.id.answer_button_1), findViewById(R.id.answer_button_2),
            findViewById(R.id.answer_button_3), findViewById(R.id.answer_button_4),
            findViewById((R.id.next_button))
        )
    }

    fun buttonSetter() {
        for (i in 0..3) {
            arrButton[i].text = (test.getSetAnswer(state - 1)[i])
            Log.d("answw", test.getSetAnswer(4)[i].toString())
        }
    }

    fun onClick(view: View) {
        val button: Button = findViewById(view.id)
        if (!test.isEnd()) {
            when (view.id) {
                R.id.next_button -> {
                    state++
                    buttonSetter()
                    textView.text = test.getOrText(state)

                }
                else -> {
                    textView.text = test.getChangeText(state, button.text.toString())
                    state++
                    textView.text = test.getOrText(state)
                    buttonSetter()
                }
            }
        } else {
            if(isNextResult) {
                getResult(test.getTotalCount())
            }
            else if(view.id != R.id.next_button){
                textView.text = test.getChangeText(state, button.text.toString())
            }
            isNextResult = true
            for (i in 0..4) {
                arrButton[i].text = "finish"
            }
        }
        test.upUnswerCount()
    }

    fun getResult(totalCount: Int) {
        val intent =
            Intent(this, ResultActivity::class.java)
        intent.putExtra("rating", totalCount)
        startActivity(intent)

    }
}