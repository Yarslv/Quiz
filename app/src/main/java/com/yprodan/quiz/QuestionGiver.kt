package com.yprodan.quiz

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log

class QuestionGiver {

    var text: SpannableString
    var position: Int
    var count = 0
    var rightAnswer: Array<String>
    var setAnswers: Array<Array<String>>

    constructor(text: String, allAnswer: String) {
        this.text = SpannableString(text)
        rightAnswer = allAnswer.split("\n").toTypedArray().get(0).split("|").toTypedArray()
        position = 0
        var setAnswerss: Array<Array<String>> = Array(rightAnswer.size, { Array(4, { "" }) })

        for (i in 1..rightAnswer.size) {
            setAnswerss[i - 1] =
                allAnswer.split("\n").toTypedArray().get(i).split("|").toTypedArray()
        }
        setAnswers = setAnswerss
    }

    fun getSetAnswer(number: Int): Array<String> {
        if (number >= rightAnswer.size) {
            throw IndexOutOfBoundsException()
        }
        return setAnswers[number]
    }

    fun getOrText(index: Int): SpannableString {
        text.setSpan(
            ForegroundColorSpan(Color.RED), text.indexOf("($index)", 0, true), text.indexOf(
                "($index)", 0, true
            ) + "($index)_____".length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return text
    }

    fun getChangeText(index: Int, answer: String): SpannableString {
        val oldIndex = text.indexOf("($index)", 0, true)
        val newText = SpannableString(text.toString().replace("($index)_____", answer))
        Log.d("index", "($index)")
        Log.d("index", oldIndex.toString())
        newText.setSpan(
            when (answer) {
                rightAnswer[index - 1] -> {
                    count++;
                    ForegroundColorSpan(Color.GREEN)
                }
                else -> {
                    ForegroundColorSpan(Color.RED)
                }
            }, oldIndex, oldIndex + answer.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        text = newText

        return text
    }

    fun getTotalCount(): Int{
        return count
    }

    fun upUnswerCount() {
        position++
    }

    fun isEnd(): Boolean {
        Log.d("rightAns", rightAnswer.size.toString())
        Log.d("rightAns", position.toString())
        return rightAnswer.size <= position + 1
    }

}
