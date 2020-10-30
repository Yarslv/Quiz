package com.yprodan.quiz.utils.model

/**
 * Class to save the user's position and his score
 */
class UserInfo() {
    var position = 0
    var score = 0

    fun getPositionInArray(): Int {
        return position
    }

    fun getPositionInText(): Int {
        return position + 1
    }
}