package com.yprodan.quiz.utils.model

import org.json.JSONArray

class AnswersToQuestions(val answerSet: JSONArray, val indexOfRightAnsw: Int) {

    fun checkForCorrectness(other: String): Boolean {
        return other.equals(answerSet[indexOfRightAnsw])
    }
}