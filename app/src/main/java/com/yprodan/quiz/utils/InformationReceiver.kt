package com.yprodan.quiz.utils

/**
 * Allows to transfer data to the main activity
 */
interface InformationReceiver {
    fun transmitToActivity(score: Int, numberOfQuestions: Int)
}