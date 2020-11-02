package com.yprodan.quiz.utils

/**
 * Allows to transfer data to the main activity
 */
interface InformationReceiver {
    fun transmitResultToActivity(score: Int, numberOfQuestions: Int)

    fun transmitFileNameToActivity(filename: String)
}