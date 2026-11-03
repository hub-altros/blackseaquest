package com.mathew.blackseaquest.model

enum class StationType { START, QUESTION, PENALTY, BONUS, FINISH }

data class AnswerOption(val text: String, val isCorrect: Boolean)

data class Station(
    val index: Int,
    val type: StationType,
    val title: String,
    val question: String = "",
    val options: List<AnswerOption> = listOf(),
    val explanation: String = "",
    val penaltyText: String = ""
)
