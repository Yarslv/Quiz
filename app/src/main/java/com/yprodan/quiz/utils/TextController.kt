package com.yprodan.quiz.utils

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.CharacterStyle
import android.text.style.ForegroundColorSpan

/**
 * Word processing class:
 * - inserts answers
 * - paints the answers
 * - marks the question
 * - gives ready source text for insertion into the text field
 * @param text - the text on which the test will be performed
 */
class TextController(text: String) {

    //text for the test
    private var text = SpannableStringBuilder(text)

    /**
     * Indicates the answer entered by the user in green - if it is correct, red - if not
     */
    fun markTheAnswer(number: Int, answer: String, correctness: Boolean) {
        val index = text.indexOf("($number)", 0, true)
        if (index != -1) {
            text.replace(index, index + "($number)_____".length, answer)
            text.setSpan(
                when (correctness) {
                    true -> {
                        ForegroundColorSpan(Color.GREEN)
                    }
                    else -> {
                        ForegroundColorSpan(Color.RED)
                    }
                }, index, index + answer.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    /**
     * Highlights in black the place where the answer should be inserted
     */
    fun markTheQuestion(question_number: Int) {
        deletePreviousSpanOfQuestionIfExist(question_number - 1)
        val current_index = text.indexOf("($question_number)", 0, true)
        text.setSpan(
            ForegroundColorSpan(Color.BLACK),
            current_index,
            current_index + "($question_number)_____".length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    /**
     * Removes the previous selection in black if the answer was not inserted
     */
    private fun deletePreviousSpanOfQuestionIfExist(question_number: Int) {
        val oldIndex = text.indexOf("($question_number)", 0, true)
        if (oldIndex != -1) {
            text.removeSpan(
                text.getSpans(
                    oldIndex,
                    oldIndex + "($question_number)_____".length,
                    CharacterStyle::class.java
                )[0]
            )
        }
    }

    fun getUpadateText(): SpannableStringBuilder {
        return text
    }
}