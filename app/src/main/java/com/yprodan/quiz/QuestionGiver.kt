package com.yprodan.quiz

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log

class QuestionGiver(text: String, allAnswer: String) {

    //text for the test
    private var text: SpannableString
    //a pointer to the question to be answered
    private var currentQuestion: Int
    //the number of correct answers
    private var count = 0
    //set of correct answers
    private var rightAnswer: Array<String>
    //a set of answers that will be written on the buttons
    private var setAnswers: Array<Array<String>>

    init {
        this.text = SpannableString(text)
        rightAnswer = allAnswer.split("\n").toTypedArray()[0].split("|").toTypedArray()
        currentQuestion = 0
        setAnswers = Array(rightAnswer.size) { Array(4) { "" } }
        for (i in 1..rightAnswer.size) {
            setAnswers[i - 1] =
                allAnswer.split("\n").toTypedArray()[i].split("|").toTypedArray()
        }
    }

    /**
     * Returns a set of items that will become labels on the buttons
     * @param number - the number of the question for which the answer options are returned
     */
    fun getSetAnswer(number: Int): Array<String> {
        if (number >= rightAnswer.size) {
            throw IndexOutOfBoundsException()
        }
        return setAnswers[number]
    }

    /**
     * Indicate in color the place in the text where you want to insert the word
     * @param number - the place where the answer will be inserted
     */
    fun pointToTheQuestion(number: Int): SpannableString {
        val index = text.indexOf("($number)", 0, true)
        text.setSpan(
            ForegroundColorSpan(Color.BLACK), index, index + "($number)_____".length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return text
    }

    /**
     * Highlights the inserted answer in red if it is incorrect, and in green if it is correct
     * @param number - the place where the answer will be inserted
     * @param answer - actually the answer itself
     */
    fun getChangedText(number: Int, answer: String): SpannableString {
        val index = text.indexOf("($number)", 0, true)
        if(index != -1) {
            text = SpannableString(text.toString().replace("($number)_____", answer))
            Log.d("index", "($number)")
            Log.d("index", index.toString())
            text.setSpan(
                when (answer) {
                    rightAnswer[number - 1] -> {
                        count++
                        ForegroundColorSpan(Color.GREEN)
                    }
                    else -> {
                        ForegroundColorSpan(Color.RED)
                    }
                }, index, index + answer.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        return text
    }

    fun getTheBestPossibleTestScore(): Int{
        return rightAnswer.size
    }

    fun getTotalCount(): Int{
        return count
    }

    fun increasePointer() {
        currentQuestion++
    }

    fun isEnd(): Boolean {
        return rightAnswer.size <= currentQuestion + 1
    }
}
