package com.yprodan.quiz

/**
 * Class for working with answers:
 * - contains the correct answers
 * - contains all the answers to place them on the buttons
 * - counts the correct answers of the user
 * - checks the answers from the user
 * - indicates the question to be answered
 * - determines when the end of the test
 * @param allAnswer - all answers for the test and correct answers
 */
class AnswersController(allAnswer: String) {

    //a pointer to the question to be answered
    private var currentQuestion: Int = 1

    //the number of correct answers
    private var count = 0

    //set of correct answers
    private var rightAnswer: Array<String> =
        allAnswer.split("\n").toTypedArray()[0].split("|").toTypedArray()

    //a set of answers that will be written on the buttons
    private var setAnswers: Array<Array<String>>

    init {
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
        if (number > rightAnswer.size) {
            throw IndexOutOfBoundsException()
        }
        return setAnswers[number - 1]
    }

    fun checkAnswer(answer: String): Boolean {
        if (answer == rightAnswer[currentQuestion - 1]) {
            count++
            return true
        }
        return false
    }

    fun getTheBestPossibleTestScore(): Int {
        return rightAnswer.size
    }

    fun getTotalCount(): Int {
        return count
    }

    fun increasePointer() {
        currentQuestion++
    }

    fun isEnd(): Boolean {
        return rightAnswer.size <= currentQuestion
    }

    fun getCurrentQuestion(): Int {
        return currentQuestion
    }
}